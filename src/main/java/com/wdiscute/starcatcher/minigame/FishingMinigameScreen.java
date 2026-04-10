package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.network.FishingCompletedPayload;
import com.wdiscute.starcatcher.registry.minigamemodifiers.BaseMinigameModifier;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCKeymappings;
import com.wdiscute.starcatcher.registry.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.tackleskin.BaseTackleSkin;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FishingMinigameScreen extends Screen implements GuiEventListener
{
    public static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame/minigame.png");
    private static final ResourceLocation NETHER = Starcatcher.rl("textures/gui/minigame/nether.png");
    private static final ResourceLocation CAVE = Starcatcher.rl("textures/gui/minigame/cave.png");
    private static final ResourceLocation SURFACE = Starcatcher.rl("textures/gui/minigame/surface.png");
    public ResourceLocation tankTexture = SURFACE;

    public FishProperties.Difficulty difficulty;
    public FishProperties.Rarity rarity;

    public final ItemStack itemBeingFished;
    public final ItemStack bobber;
    public final ItemStack bait;
    public final ItemStack hook;
    public final ItemStack treasureIS;

    public final AbstractTackleSkin tackleSkin;

    public final InteractionHand handToSwing;

    public int hp;
    public int penalty;
    public float decay;

    public float kimbeMarkerPos = 0;
    public float kimbeMarkerAlpha = 0;
    public int kimbeMarkerColor = 0x00ff00;

    public int gracePeriod = 80;
    //todo do this smiley face :)
    public boolean minigameStarted;

    public float pointerSpeed;
    public float pointerBaseSpeed;

    public int tickCount = 0;
    public float pointerPos = 0;
    public int currentRotation = 1;
    public float partial;
    public float hitDelay;

    public float progress = 20;
    public int progressSmooth = 20;

    public boolean perfectCatch = true;
    public int consecutiveHits = 0;

    public boolean treasureActive;
    public int treasureProgress = 0;
    public int treasureProgressSmooth = 0;

    List<HitFakeParticle> hitParticles = new ArrayList<>();


    // Nikdo53 fields, these are mine dont steal them
    public final int holdingDelay = 6;
    public int holdingTicks = 0;
    protected boolean isHoldingKey = false;
    protected boolean isHoldingMouse = false;

    public float renderScale;
    public int xOffset = SCConfig.MINIGAME_X_OFFSET.getAsInt();
    public int yOffset = SCConfig.MINIGAME_Y_OFFSET.getAsInt();


    protected final List<ActiveSweetSpot> activeSweetSpots = new ArrayList<>();
    protected final List<ActiveSweetSpot> spotsToAdd = new ArrayList<>(); // delays the adding process to avoid concurrency exceptions

    protected final List<AbstractMinigameModifier> modifiers = new ArrayList<>();
    protected final List<AbstractMinigameModifier> modifiersToAdd = new ArrayList<>(); // delays the adding process to avoid concurrency exceptions

    public FishingMinigameScreen(FishProperties fp, ItemStack rod)
    {
        super(Component.empty());

        handToSwing = Minecraft.getInstance().player.getMainHandItem().is(SCTags.RODS) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        renderScale = SCConfig.MINIGAME_RENDER_SCALE.get().floatValue();
        hitDelay = SCConfig.HIT_DELAY.get().floatValue();

        this.difficulty = fp.dif();
        this.rarity = fp.rarity();

        //if override is not missingno (default) then use the override item set
        if (!fp.catchInfo().overrideMinigameWith().is(SCItems.MISSINGNO.getKey()))
            this.itemBeingFished = new ItemStack(fp.catchInfo().overrideMinigameWith());
        else
            this.itemBeingFished = new ItemStack(fp.catchInfo().fish());

        this.bobber = SCDataComponents.getOrDefault(rod, SCDataComponents.BOBBER, SingleStackContainer.empty()).stack();
        this.bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, SingleStackContainer.empty()).stack();
        this.hook = SCDataComponents.getOrDefault(rod, SCDataComponents.HOOK, SingleStackContainer.empty()).stack();

        this.treasureIS = fp.catchInfo().treasureIs();

        if (SCDataComponents.has(rod, SCDataComponents.TACKLE_SKIN))
        {
            ResourceLocation rl = SCDataComponents.get(rod, SCDataComponents.TACKLE_SKIN);

            Optional<Supplier<AbstractTackleSkin>> optional = Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.TACKLE_SKIN).getOptional(rl);
            if (optional.isPresent())
                this.tackleSkin = optional.get().get();
            else
                this.tackleSkin = new BaseTackleSkin();
        }
        else
        {
            this.tackleSkin = new BaseTackleSkin();
        }


        //tank texture change
        ClientLevel level = Minecraft.getInstance().level;
        ResourceKey<Level> dim = level.dimension();
        Player player = Minecraft.getInstance().player;
        if (player.getY() < 50 && dim.equals(Level.OVERWORLD))
            tankTexture = CAVE;

        if (dim.equals(Level.NETHER))
            tankTexture = NETHER;

        //base - a lot of these are now hitZone-based
        this.pointerSpeed = (float) (difficulty.speed() * SCConfig.POINTER_SPEED_MULTIPLIER.get());
        this.pointerBaseSpeed = (float) (difficulty.speed() * SCConfig.POINTER_SPEED_MULTIPLIER.get());
        this.penalty = (int) (difficulty.penalty() * SCConfig.PENALTY_MULTIPLIER.get());
        this.decay = (float) (difficulty.decay() * SCConfig.DECAY_RATE_MULTIPLIER.get());
        this.hp = (int) (difficulty.hp() * SCConfig.HP_RATE_MULTIPLIER.get());

        //add base modifier for kimbe before other modifiers so they can override kimbe if needed
        addModifier(new BaseMinigameModifier());

        //add every modifier from fp json which is registered
        for (Supplier<Supplier<AbstractMinigameModifier>> mod : fp.dif().modifiers())
        {
            addModifier(mod.get().get());
        }

        //add modifiers in armor/curios/rod
        modifiersToAdd.addAll(SCMinigameModifiers.getMinigameModifiers(player));

        //add every sweet spot from fp json which is registered
        for (FishProperties.SweetSpot ss : fp.dif().sweetSpots())
        {
            var newSweetSpot = new ActiveSweetSpot(this, ss, bobber, bait, hook);
            addSweetSpot(newSweetSpot);
        }
    }

    public List<ActiveSweetSpot> getActiveSweetSpots()
    {
        return activeSweetSpots;
    }

    public void addModifier(AbstractMinigameModifier mod)
    {
        if (!mod.removed) this.modifiersToAdd.add(mod);
    }

    public void addUniqueModifier(AbstractMinigameModifier mod)
    {
        //only adds if there's not a modifier of the same class already present
        if (modifiers.stream().noneMatch(m -> m.getClass() == mod.getClass()))
        {
            if (!mod.removed) this.modifiersToAdd.add(mod);
        }
    }

    public void addSweetSpot(ActiveSweetSpot ass)
    {
        if (!ass.removed) this.spotsToAdd.add(ass);
    }

    public int getRandomFreePosition(int sizeOfTheSweetspotToPlace)
    {
        int posBeingChecked = U.r.nextInt(360);

        //find the closest available pos
        for (int i = 0; i < 180; i++)
        {

            //check left and right
            for (int j = 1; j > -2; j -= 2)
            {

                int checkPos = clampPos(posBeingChecked + (i * j));

                if (activeSweetSpots.stream().noneMatch(s -> doDegreesOverlapWithLeeway(checkPos, s.pos, (s.thickness + sizeOfTheSweetspotToPlace) / 2)
                ))
                {
                    return checkPos;
                }
            }
        }

        LogUtils.getLogger().warn("Starcatcher's minigame couldn't find a non-overlapping free position! Consider not having so many active sweet spots");
        return U.r.nextInt(360);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTickNeo)
    {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTickNeo);

        final float partialTick = SCConfig.VANILLA_PARTIAL_TICK.get() ? partialTickNeo : PartialTickHelper.INSTANCE.getPartialTicks(minecraft.level);

        PoseStack poseStack = guiGraphics.pose();
        partial = partialTick;

        poseStack.pushPose();
        poseStack.translate(width >> 1, height >> 1, 0);

        poseStack.translate(xOffset, yOffset, 0);

        poseStack.scale(renderScale, renderScale, 1);

        poseStack.translate( -width >> 1, -height >> 1, 0);

        //render modifiers background
        modifiers.forEach(modifier -> modifier.renderBackground(guiGraphics, partialTick, width, height));

        if (treasureActive) renderTreasure(guiGraphics);

        //render tank background
        guiGraphics.blit(tankTexture, width / 2 - 42 - 100, height / 2 - 48, 85, 97, 0, 0, 85, 97, 85, 97);

        /*
        //test for the vignette shader
        ShaderUtils.blitWithShader(
                ModRenderTypes::getRendertypeGuiFadeShader,
                () -> ShaderUtils.setUpFadeShader(width, height, new Vec2(0, 0), new Vec2(0.01f, 0.02f), new Vec2(0.2f, 0.2f), new Vec2(0.5f, 0.7f), true),
                guiGraphics,
                tankTexture, width / 2 - 42 - 100, height / 2 - 48, 85, 97, 0, 0, 85, 97, 85, 97
        );*/

        //render wheel background
        guiGraphics.blit(TEXTURE, width / 2 - 32, height / 2 - 32, 64, 64, 0, 192, 64, 64, 256, 256);

        //render spacebar
        guiGraphics.blit(TEXTURE, width / 2 - 16, height / 2 + 40, 32, 16, isHoldingKey ? 48 : 0, 112, 32, 16, 256, 256);

        //render all sweet spots
        activeSweetSpots.forEach(ass -> renderSweetSpot(ass, guiGraphics, partialTick, poseStack));

        //render wheel second layer
        guiGraphics.blit(TEXTURE, width / 2 - 32, height / 2 - 32, 64, 64, 64, 192, 64, 64, 256, 256);

        //render pointer
        renderPointer(guiGraphics, poseStack, partialTick);

        //render kimbe marker
        renderKimbeMarker(guiGraphics);

        //silver thing on top
        guiGraphics.blit(TEXTURE, width / 2 - 16, height / 2 - 16, 32, 32, 208, 208, 32, 32, 256, 256);

        //fishing rod
        guiGraphics.blit(TEXTURE, width / 2 - 32 - 70, height / 2 - 24 - 57, 64, 48, 192, 0, 64, 48, 256, 256);

        int yoffset = progressSmooth == 0 ? 0 : (int) ((float) progressSmooth / (float) hp * 77);

        //fishing line
        guiGraphics.blit(
                TEXTURE, width / 2 - 6 - 102, height / 2 - 56 - 18,
                16, 112 - yoffset,
                176, (float) yoffset,
                16, 112 - yoffset,
                256, 256);

        //item being fished
        guiGraphics.renderItem(itemBeingFished, width / 2 - 8 - 100, height / 2 - 8 + 35 - yoffset);

        //render sweet spots foreground
        activeSweetSpots.forEach(sweetspot -> sweetspot.behaviour.renderForeground(guiGraphics, partialTick, width, height));

        //render modifiers foreground
        modifiers.forEach(modifier -> modifier.renderForeground(guiGraphics, partialTick, width, height));

        //render particles
        hitParticles.forEach(p -> p.render(guiGraphics, width, height));

        poseStack.popPose();
    }

    public void renderSweetSpot(ActiveSweetSpot ass, GuiGraphics guiGraphics, float partialTick, PoseStack poseStack)
    {
        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.pushPose();

        poseStack.translate(centerX, centerY, 0);

        poseStack.rotateAround(Axis.ZP.rotationDegrees(ass.pos + (partialTick * ass.movingRate) * ass.currentRotation), 0, 0, 0);

        boolean isDisabled = modifiers.stream().anyMatch(mod -> mod.disableSweetSpotRendering(ass));
        if (!isDisabled)
            ass.behaviour.render(guiGraphics, poseStack, partialTick);

        modifiers.forEach(mod -> mod.renderOnSweetSpot(guiGraphics, poseStack, ass, partialTick));

        poseStack.popPose();
    }

    public void renderTreasure(GuiGraphics guiGraphics)
    {
        //treasure bar
        guiGraphics.blit(
                TEXTURE, width / 2 - 158, height / 2 - 42 + (int) (64 - (64f * treasureProgressSmooth) / 100),
                5, 64 * treasureProgressSmooth / 100,
                141, 6 + 64 - (float) (64 * treasureProgressSmooth) / 100,
                5, 64 * treasureProgressSmooth / 100,
                256, 256);

        //treasure chest
        guiGraphics.blit(TEXTURE, width / 2 - 16 - 155, height / 2 - 48, 32, 96, 96, 0, 32, 96, 256, 256);

        //render treasure on top of bar
        guiGraphics.renderItem(treasureIS, width / 2 - 163, ((int) ((float) height / 2 - (64f * treasureProgressSmooth) / 100) + 15));

        //outline when treasure complete
        if (treasureProgress > 99)
            guiGraphics.blit(
                    TEXTURE, width / 2 - 16 - 155, height / 2 - 48,
                    32, 96, 64, 0, 32, 96, 256, 256);

        //todo related to above
//        if (tackleSkin.is(ModItems.COLORFUL_BOBBER_SMITHING_TEMPLATE) || tackleSkin.is(ModItems.PEARL_BOBBER_SMITHING_TEMPLATE))
//            RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public void renderKimbeMarker(GuiGraphics guiGraphics)
    {
        if(modifiers.stream().anyMatch(AbstractMinigameModifier::skipRenderingKimbeMarker)) return;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(kimbeMarkerPos)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(
                (float) U.intToRed(kimbeMarkerColor) / 255,
                (float) U.intToGreen(kimbeMarkerColor) / 255,
                (float) U.intToBlue(kimbeMarkerColor) / 255,
                kimbeMarkerAlpha);
        RenderSystem.enableBlend();

        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                64, 64, 128, 128, 64, 64, 256, 256);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    public void renderPointer(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick)
    {
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);

        poseStack.mulPose(Axis.ZP.rotationDegrees(pointerPos + ((pointerSpeed * partialTick) * currentRotation)));

        poseStack.translate(0, -16, 0);

        boolean isDisabled = modifiers.stream().anyMatch(AbstractMinigameModifier::disablePointerRendering);
        if (!isDisabled)
            renderPoseCentered(guiGraphics, TEXTURE, 64, 64, 128, 192, 256);

        poseStack.translate(0, 16, 0);

        modifiers.forEach(mod -> mod.renderOnPointer(guiGraphics, poseStack, partialTick));

        poseStack.popPose();
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int keyModifiers)
    {
        if (keyCode == SCKeymappings.MINIGAME_HIT.getKey().getValue())
        {
            isHoldingKey = false;
            holdingTicks = 0;
        }

        modifiers.forEach(mod -> mod.onKeyReleased(keyCode, scanCode, keyModifiers));

        return super.keyReleased(keyCode, scanCode, keyModifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        isHoldingMouse = false;
        holdingTicks = 0;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        inputPressed();
        isHoldingMouse = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int keyModifiers)
    {
        //closes when pressing E
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey))
        {
            if (SCConfig.ENABLE_VILLAGER_SOUND.get() && modifiers.stream().noneMatch(AbstractMinigameModifier::skipMissSound))
                Minecraft.getInstance().player.playSound(SoundEvents.VILLAGER_NO);
            this.onClose();
            return true;
        }

        //hit input
        if (SCKeymappings.MINIGAME_HIT.isActiveAndMatches(mouseKey))
        {
            if (!isHoldingKey) inputPressed();

            isHoldingKey = true;
        }

        this.modifiers.forEach(mod -> mod.onKeyPress(keyCode, scanCode, keyModifiers));

        return super.keyPressed(keyCode, scanCode, keyModifiers);
    }

    public void inputPressed()
    {
        if (gracePeriod > 0) gracePeriod = 0;

        Minecraft.getInstance().player.swing(handToSwing, true);

        boolean hitSomething = false;

        Vec3 pos = Minecraft.getInstance().player.position();
        ClientLevel level = Minecraft.getInstance().level;

        kimbeMarkerPos = getPointerPosPrecise();


        for (ActiveSweetSpot ass : activeSweetSpots)
        {
            if (doDegreesOverlapWithLeeway(getPointerPosPrecise(), ass.pos, ass.thickness / 2))
            {

                //check if each modifier allows the hit to register
                boolean isCanceled = false;
                for (AbstractMinigameModifier modifier : modifiers)
                {

                    if (modifier.onHit(ass))
                        isCanceled = true;
                }

                if (isCanceled) continue;

                hitSomething = true;
                ass.behaviour.onHit();
                break;
            }
        }


        if (!hitSomething)
        {
            this.modifiers.forEach(AbstractMinigameModifier::onMiss);

            consecutiveHits = 0;
            if(SCConfig.ENABLE_MISS_SOUND.get())
                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1, 1, false);
            progress -= penalty;
        }
    }

    public float getPointerPosPrecise()
    {
        float pointerPosPrecise = (pointerPos + ((pointerSpeed * partial) * currentRotation));

        pointerPosPrecise += hitDelay * pointerSpeed * currentRotation;
        return pointerPosPrecise;
    }

    public static boolean doDegreesOverlapWithLeeway(float degrees1, float degrees2, int leeway)
    {
        boolean b = Math.abs(degrees2 - degrees1) < (float) leeway;
        boolean b2 = Math.abs(degrees2 - degrees1) > 360 - ((float) leeway);
        return b || b2;
    }

    @Override
    public void tick()
    {
        if (isHoldingInput())
        { //mimics the keyboard behavior
            holdingTicks++;
            if (holdingTicks > holdingDelay) inputPressed();
        }

        //tick modifiers
        modifiers.forEach(AbstractMinigameModifier::tick);
        //tick behaviour
        activeSweetSpots.forEach(s -> s.behaviour.tick());

        //remove activeSweetSpots marked for removal
        activeSweetSpots.removeIf(s ->
        {
            if (s.removed) s.behaviour.onRemove();
            return s.removed;
        });

        //remove modifiers marked for removal
        modifiers.removeIf(m ->
        {
            if (m.removed) m.onRemove();
            return m.removed;
        });

        //add queued modifiers
        modifiers.addAll(modifiersToAdd);
        modifiersToAdd.forEach(o -> o.onAdd(this));
        modifiersToAdd.clear();

        //add queued sweetspots
        activeSweetSpots.addAll(spotsToAdd);
        //trigger onAdd of SweetSpotBehaviour
        spotsToAdd.forEach(o -> o.behaviour.onAdd(this, o));
        //trigger onSpotAdded of modifiers
        spotsToAdd.forEach(s -> modifiers.forEach(m -> m.onSpotAdded(s)));
        spotsToAdd.clear();

        //move pointer
        pointerPos += pointerSpeed * currentRotation;

        //decrease kimbe markers alpha
        kimbeMarkerAlpha -= 0.1f;

        if (pointerPos > 360) pointerPos -= 360;
        if (pointerPos < 0) pointerPos += 360;

        gracePeriod--;

        tickCount++;

        progressSmooth += (int) Math.signum(progress - progressSmooth);
        progressSmooth += (int) Math.signum(progress - progressSmooth);

        treasureProgressSmooth += (int) Math.signum(treasureProgress - treasureProgressSmooth);

        if (tickCount % 5 == 0 && gracePeriod < 0)
        {
            progress -= decay;
        }

        if (!isSettingsScreen())
        {

            if (progressSmooth < 0)
            {
                if (SCConfig.ENABLE_VILLAGER_SOUND.get())
                    Minecraft.getInstance().player.playSound(SoundEvents.VILLAGER_NO);
                this.onClose();
            }

            if (progressSmooth > hp)
            {
                //if completed treasure minigame, or is a perfect catch with the mossy hook
                boolean awardTreasure = treasureProgress > 100 || modifiers.stream().anyMatch(AbstractMinigameModifier::forceAwardTreasure);

                if (SCConfig.ENABLE_VILLAGER_SOUND.get())
                    Minecraft.getInstance().player.playSound(SoundEvents.VILLAGER_CELEBRATE);

                PacketDistributor.sendToServer(new FishingCompletedPayload(tickCount, awardTreasure, perfectCatch, consecutiveHits));
                this.onClose();
            }
        }

        hitParticles.removeIf(HitFakeParticle::tick);
    }

    @Override
    public void onClose()
    {
        modifiers.forEach(AbstractMinigameModifier::onRemove);

        PacketDistributor.sendToServer(new FishingCompletedPayload(-1, false, false, consecutiveHits));
        this.minecraft.popGuiLayer();
    }

    public void addParticles(float posInDegrees, int count, int color)
    {
        int xPos = (int) (30 * Math.cos(Math.toRadians(posInDegrees - 90)));
        int yPos = (int) (30 * Math.sin(Math.toRadians(posInDegrees - 90)));

        for (int i = 0; i < count; i++)
        {
            hitParticles.add(
                    new HitFakeParticle(
                            xPos,
                            yPos,
                            new Vector2d(U.r.nextFloat() * 2 - 1, U.r.nextFloat() * 2 - 1),
                            (float) U.intToRed(color) / 255,
                            (float) U.intToGreen(color) / 255,
                            (float) U.intToBlue(color) / 255,
                            1
                    ));
        }
    }

    /**
     * Renders a texture centered to the top left corner, to be moved with poseStack
     */
    public static void renderPoseCentered(GuiGraphics guiGraphics, ResourceLocation texture, int spriteSize)
    {
        guiGraphics.blit(
                texture, -spriteSize >> 1, -spriteSize >> 1,
                spriteSize, spriteSize, 0, 0, spriteSize, spriteSize, spriteSize, spriteSize);
    }

    public static void renderPoseCentered(GuiGraphics guiGraphics, ResourceLocation texture, int spriteWidth, int spriteHeight, int uOffset, int vOffset, int textureSize)
    {
        guiGraphics.blit(
                texture, -spriteWidth >> 1, -spriteHeight >> 1,
                spriteWidth, spriteHeight, uOffset, vOffset, spriteWidth, spriteHeight, textureSize, textureSize);
    }


    public boolean isHoldingInput()
    {
        return isHoldingMouse || isHoldingKey;
    }

    public static boolean hasDistantHorizons()
    {
        return ModList.get().isLoaded("distanthorizons");
    }

    public boolean isSettingsScreen()
    {
        return false;
    }

    public static int clampPos(int pos)
    {
        pos %= 360;
        if (pos < 0)
        {
            pos += 360;
        }
        return pos;
    }

    public List<AbstractMinigameModifier> getModifiers()
    {
        return modifiers;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    public void refreshSweetSpotsAlphas()
    {
        activeSweetSpots.forEach(s -> s.alpha = 1);
    }

    public void removeAllSweetSpots()
    {
        for (var dws : activeSweetSpots)
        {
            dws.removed = true;
        }
    }
}
