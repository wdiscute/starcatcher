package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.Nikdo53Modifier;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.data.network.SBFishingCompletedPayload;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Vector2d;

import java.util.*;

public class FishingMinigameScreen extends Screen implements GuiEventListener
{
    public static final ResourceLocation GOLD_OUTLINE = Starcatcher.rl("textures/gui/minigame/gold_outline.png");

    public ResourceLocation texture;
    public boolean renderBlur = true;

    public Difficulty difficulty;
    public Rarity rarity;

    public final ItemStack itemBeingFished;
    public final ItemStack treasureIS;

    public final AbstractTackleSkin tackleSkin;

    public final InteractionHand handToSwing;

    public int hp;
    public float penalty;
    public float decay;

    public float kimbeMarkerPos = 0;
    public float kimbeMarkerAlpha = 0;
    public int kimbeMarkerColor = 0x00ff00;

    public int gracePeriod = Integer.MAX_VALUE;

    public float handleSpeed;
    public float handleBaseSpeed;

    public int tickCount = 0;
    public float handlePos = 0;
    public int currentRotation = 1;
    public float partial;
    public float hitDelay;

    public float progress = 20;
    public float progressSmooth = 20;

    public boolean perfectCatch = true;
    public int consecutiveHits = 0;

    public boolean treasureActive;
    public int treasureProgress = 0;
    public int treasureProgressSmooth = 0;

    //used to store data that changes at runtime and is independent of each minigame, ie number of hits so far
    public final Map<ResourceLocation, Object> modifierData = new HashMap<>();

    List<HitFakeParticle> hitParticles = new ArrayList<>();


    // Nikdo53 fields, these are mine dont steal them
    public final int holdingDelay = 6;
    public int holdingTicks = 0;
    protected boolean isHoldingKey = false;
    protected boolean isHoldingMouse = false;

    protected final List<ActiveSweetSpot> activeSweetSpots = new ArrayList<>();
    protected final List<ActiveSweetSpot> spotsToAdd = new ArrayList<>(); // delays the adding process to avoid concurrency exceptions

    protected final List<AbstractMinigameModifier> modifiers = new ArrayList<>();
    protected final List<AbstractMinigameModifier> modifiersToAdd = new ArrayList<>(); // delays the adding process to avoid concurrency exceptions

    public FishingMinigameScreen(FishProperties fp, ItemStack treasure, List<AbstractMinigameModifier> extraModifiers, AbstractTackleSkin tackleSkin)
    {
        super(Component.empty());

        if(ModList.get().isLoaded("scauto")) throw new RuntimeException();

        handToSwing = Minecraft.getInstance().player.getMainHandItem().is(SCTags.RODS) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        texture = fp.textures();

        hitDelay = SCConfig.HIT_DELAY.get().floatValue();

        this.difficulty = fp.dif();
        this.rarity = fp.rarity();

        //set treasure being fished
        this.treasureIS = treasure;

        //set item being fished
        if (fp.catchInfo().overrideMinigameWith().isEmpty())
            this.itemBeingFished = fp.catchInfo().fish().toStack();
        else
            this.itemBeingFished = fp.catchInfo().overrideMinigameWith().toStack();

        this.tackleSkin = tackleSkin;

        //tank texture change
        Player player = Minecraft.getInstance().player;

        //rod - a lot of these are now hitZone-based
        this.handleSpeed = (float) (difficulty.speed() * player.getAttributeValue(SCAttributes.HANDLE_ROTATION_SPEED_MULTIPLIER) * SCConfig.HANDLE_SPEED_MULTIPLIER.get());
        this.handleBaseSpeed = (float) (difficulty.speed() * player.getAttributeValue(SCAttributes.HANDLE_ROTATION_SPEED_MULTIPLIER) * SCConfig.HANDLE_SPEED_MULTIPLIER.get());
        this.penalty = (int) (difficulty.penalty() * player.getAttributeValue(SCAttributes.PENALTY_MULTIPLIER) * SCConfig.PENALTY_MULTIPLIER.get());
        this.decay = (float) (difficulty.decay() * player.getAttributeValue(SCAttributes.BASE_DECAY_MULTIPLIER) * SCConfig.DECAY_RATE_MULTIPLIER.get());
        this.hp = (int) (difficulty.hp() * player.getAttributeValue(SCAttributes.REQUIRED_SCORE_MULTIPLIER) * SCConfig.HP_RATE_MULTIPLIER.get());

        //add default modifiers
        Modifier.getDefaultMinigameModifiers().forEach(this::addModifier);

        //add extra modifiers from constructor
        extraModifiers.forEach(this::addModifier);

        //add every modifier from fp json which is registered
        for (Modifier mod : fp.dif().modifiers())
        {
            if (mod instanceof AbstractMinigameModifier amm)
                addModifier(amm);
        }

        //add modifiers in armor/curios/rod
        modifiersToAdd.addAll(Modifier.getMinigameModifiers(player));

        tick();

        //add every sweet spot from fp Json which is registered
        for (Difficulty.SweetSpot ss : fp.dif().sweetSpots())
        {
            var newSweetSpot = new ActiveSweetSpot(this, ss);
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
        int posBeingChecked = Utils.r.nextInt(360);

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
        return Utils.r.nextInt(360);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTickNeo)
    {
        if (renderBlur)
            super.renderBackground(guiGraphics, mouseX, mouseY, partialTickNeo);

        final float partialTick = PartialTickHelper.INSTANCE.getPartialTicks(minecraft.level);

        PoseStack poseStack = guiGraphics.pose();
        partial = partialTick;

        poseStack.pushPose();
        poseStack.translate(width >> 1, height >> 1, 0);

        poseStack.translate(SCConfig.MINIGAME_X_OFFSET.get(), SCConfig.MINIGAME_Y_OFFSET.get(), 0);

        poseStack.scale(((float) SCConfig.MINIGAME_RENDER_SCALE.getAsDouble()), ((float) SCConfig.MINIGAME_RENDER_SCALE.getAsDouble()), 1);

        poseStack.translate(-width >> 1, -height >> 1, 0);

        //render modifiers background
        modifiers.forEach(modifier -> modifier.renderBackground(this, guiGraphics, partialTick, width, height));

        if (treasureActive) renderTreasure(guiGraphics);

        int centerX = width / 2;
        int centerY = height / 2;

        boolean shouldDarken = modifiers.stream().anyMatch(o -> o.shouldDarkenWheel(this));

        //render tank background
        guiGraphics.blit(texture, centerX - 44 - 106, centerY - 58,
                96, 112, 0, 112, 96, 112, 256, 256);

        //render golden border if legendary fish
        if (rarity.equals(Rarity.LEGENDARY))
            guiGraphics.blit(GOLD_OUTLINE,
                    width / 2 - 150, height / 2 - 58,
                    96, 112,
                    0, 0,
                    96, 112,
                    96, 112
            );

        //render wheel background
        if (shouldDarken) RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1);
        guiGraphics.blit(texture, centerX - 32, centerY - 32,
                64, 64, 96, 112, 64, 64, 256, 256);
        if (shouldDarken) RenderSystem.setShaderColor(1f, 1f, 1f, 1);


        //render spacebar
        Component displayName = SCKeymappings.MINIGAME_HIT.getKey().getDisplayName();
        String string = displayName.getString();

        guiGraphics.blit(texture, centerX - 24, centerY + 40 + (modifiers.stream().anyMatch(Nikdo53Modifier.class::isInstance) ? 10 : 0),
                48, 16,
                96, (isHoldingKey ? 16 : 0) + (string.length() < 3 ? 32 : 0),
                48, 16,
                256, 256);

        //render spacebar keybind
        renderCenteredString(guiGraphics, font, displayName,
                centerX, centerY + 42 + (modifiers.stream().anyMatch(Nikdo53Modifier.class::isInstance) ? 10 : 0) + (isHoldingKey ? 2 : 0), 0xff546170, false);

        //render all sweet spots
        activeSweetSpots.forEach(ass -> renderSweetSpot(ass, guiGraphics, partialTick, poseStack));

        //render wheel second layer
        if (shouldDarken) RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1);
        guiGraphics.blit(texture, centerX - 32, centerY - 32,
                64, 64, 160, 112, 64, 64, 256, 256);
        if (shouldDarken) RenderSystem.setShaderColor(1f, 1f, 1f, 1);

        //render pointer
        renderPointer(guiGraphics, poseStack, partialTick);

        //render kimbe marker
        renderKimbeMarker(guiGraphics);

        //silver thing on top
        guiGraphics.blit(texture, centerX - 16, centerY - 16,
                32, 32, 224, 128, 32, 32, 256, 256);

        boolean flip = modifiers.stream().anyMatch(o -> o.flipRodAndProgressDisplay(this));

        //fishing rod
        guiGraphics.blit(texture, centerX - 32 - 70, centerY - 24 - 57 + (flip ? 130 : 0),
                64, 48, 192, 0, 64, 48, 256, 256);

        progressSmooth += ((progress - progressSmooth) / 6) * partialTickNeo;

        float yoffset = progressSmooth == 0 ? 0 : (progressSmooth / (float) hp * 77);

        //fishing line
        if (flip)
        {
            guiGraphics.blit(
                    texture, centerX - 6 - 102, centerY - 46 + (int) yoffset,
                    16, (int) (112 - yoffset - 15),
                    176F, yoffset,
                    16, (int) (112 - yoffset - 15),
                    256, 256);
        }
        else
            guiGraphics.blit(
                    texture, centerX - 6 - 102, centerY - 56 - 24,
                    16, (int) (112 - yoffset + 8),
                    176F, yoffset,
                    16, (int) (112 - yoffset),
                    256, 256);

        //item being fished
        poseStack.pushPose();
        if (flip)
            poseStack.translate(0, -yoffset * -1 - 77, 0);
        else
            poseStack.translate(0, -yoffset, 0);
        guiGraphics.renderItem(itemBeingFished, centerX - 8 - 100, centerY - 8 + 35);
        poseStack.popPose();

        //render sweet spots foreground
        activeSweetSpots.forEach(ass -> ass.behaviour.renderForeground(guiGraphics, partialTick, width, height, this, ass));

        //render modifiers foreground
        modifiers.forEach(modifier -> modifier.renderForeground(this, guiGraphics, partialTick, width, height));

        //render particles
        hitParticles.forEach(p -> p.render(guiGraphics, width, height));

        poseStack.popPose();

        if (SCConfig.DEBUG_MINIGAME.get())
        {
            int yOffsetMod = 10;
            guiGraphics.drawString(this.font, "minigame modifiers: " + modifiers.size(), 10, yOffsetMod, 0xffffff00);

            for (AbstractMinigameModifier modifier : modifiers)
            {
                yOffsetMod = yOffsetMod + 10;
                guiGraphics.drawString(this.font, modifier.toString(), 10, yOffsetMod, 0xffffffff);
            }

            int yOffsetASS = 10;
            guiGraphics.drawString(this.font, "active sweet-spots: " + activeSweetSpots.size(), width / 2 + 90, yOffsetASS, 0xffffff00);

            for (ActiveSweetSpot ass : activeSweetSpots)
            {
                yOffsetASS += 10;
                guiGraphics.drawString(this.font, "texture: " + (
                        ass.texture.getPath().contains("/")
                                ? ass.texture.getPath().substring(ass.texture.getPath().lastIndexOf('/') + 1)
                                : ass.texture.getPath()
                ), width / 2 + 90, yOffsetASS, 0xffffffff);
                yOffsetASS += 10;
                guiGraphics.drawString(this.font, "rew: " + ass.reward + " / van: " + ass.vanishingRate + " / mov: " + ass.movingRate + " / pos: " + ass.pos, width / 2 + 90, yOffsetASS, 0xffffffff);
                yOffsetASS += 10;
                guiGraphics.drawString(this.font, "col: " + ass.particleColor + " / thick:" + ass.thickness + " / alpha: " + ass.alpha, width / 2 + 90, yOffsetASS, 0xffffffff);
                yOffsetASS += 10;
                guiGraphics.drawString(this.font, "behaviour: " + (
                        ass.behaviour.toString().contains(".")
                                ? ass.behaviour.toString().substring(ass.behaviour.toString().lastIndexOf('.') + 1)
                                : ass.behaviour.toString()
                ), width / 2 + 90, yOffsetASS, 0xffffffff);
                yOffsetASS += 10;
            }

            //fish
            guiGraphics.drawString(this.font, "fish: ", width / 2 - 100, height / 2 + 80, 0xffffff00);
            guiGraphics.drawString(this.font, "item: " + itemBeingFished, width / 2 - 100, height / 2 + 90, 0xffffffff);
            guiGraphics.drawString(this.font, "progress: " + progress + "/" + hp, width / 2 - 100, height / 2 + 100, 0xffffffff);
            guiGraphics.drawString(this.font, "rarity: " + rarity, width / 2 - 100, height / 2 + 110, 0xffffffff);

            //treasure
            guiGraphics.drawString(this.font, "treasure: " + (treasureActive ? "active" : "not active"), width / 2 - 100, height / 2 + 130, 0xffffff00);
            guiGraphics.drawString(this.font, "item: " + treasureIS.toString(), width / 2 - 100, height / 2 + 140, 0xffffffff);
            guiGraphics.drawString(this.font, "progress: " + treasureProgress + "/100", width / 2 - 100, height / 2 + 150, 0xffffffff);

            guiGraphics.drawString(this.font, "fp: ", 10, height - 100, 0xffffff00);
            guiGraphics.drawString(this.font, "penalty: " + penalty, 10, height - 90, 0xffffffff);
            guiGraphics.drawString(this.font, "handle speed: " + handleSpeed, 10, height - 80, 0xffffffff);
            guiGraphics.drawString(this.font, "handle pos: " + handlePos, 10, height - 70, 0xffffffff);
            guiGraphics.drawString(this.font, "decay: " + decay, 10, height - 60, 0xffffffff);
            guiGraphics.drawString(this.font, "perfect catch: " + perfectCatch, 10, height - 50, 0xffffffff);
            guiGraphics.drawString(this.font, "cons. hits: " + consecutiveHits, 10, height - 40, 0xffffffff);
            guiGraphics.drawString(this.font, "particles: " + hitParticles.size(), 10, height - 30, 0xffffffff);
            guiGraphics.drawString(this.font, "grace: " + gracePeriod, 10, height - 20, 0xffffffff);
            guiGraphics.drawString(this.font, "tick: " + tickCount, 10, height - 10, 0xffffffff);

            //rod attachments
//            guiGraphics.drawString(this.font, "attachments: ", 10, height - 50, 0xffffff00);
//            guiGraphics.drawString(this.font, "bobber: " + bobber, 10, height - 40, 0xffffffff);
//            guiGraphics.drawString(this.font, "bait: " + bait, 10, height - 30, 0xffffffff);
//            guiGraphics.drawString(this.font, "hook: " + hook, 10, height - 20, 0xffffffff);
        }
    }

    public void renderSweetSpot(ActiveSweetSpot ass, GuiGraphics guiGraphics, float partialTick, PoseStack poseStack)
    {
        float centerX = (float) width / 2;
        float centerY = (float) height / 2;

        poseStack.pushPose();

        poseStack.translate(centerX, centerY, 0);

        poseStack.rotateAround(Axis.ZP.rotationDegrees(ass.pos + (partialTick * ass.movingRate) * ass.currentRotation), 0, 0, 0);

        boolean isDisabled = modifiers.stream().anyMatch(mod -> mod.disableSweetSpotRendering(this, ass));
        if (!isDisabled)
            ass.behaviour.render(guiGraphics, poseStack, partialTick, this, ass);

        modifiers.forEach(mod -> mod.renderOnSweetSpot(this, guiGraphics, poseStack, ass, partialTick));

        poseStack.popPose();
    }

    public void renderTreasure(GuiGraphics guiGraphics)
    {
        int centerX = width / 2;
        int centerY = height / 2;


        int barSize = Math.min(64, (64 * treasureProgressSmooth) / 100);

        //treasure bar
        guiGraphics.blit(texture, centerX - 3 - 155, centerY + 22 - barSize,
                5, barSize,
                77, 6,
                5, barSize,
                256, 256);

        //treasure bar outline + chest at bottom
        guiGraphics.blit(texture, centerX - 16 - 155, centerY - 48,
                32, 96,
                32, 0,
                32, 96,
                256, 256);

        //render treasure on top of bar
        guiGraphics.renderItem(treasureIS,
                centerX - 163,
                centerY - barSize + 16);

        //outline when treasure complete
        if (treasureProgress > 99)
            guiGraphics.blit(texture, centerX - 16 - 155, centerY - 48,
                    32, 96,
                    0, 0,
                    32, 96,
                    256, 256);

    }

    public void renderKimbeMarker(GuiGraphics guiGraphics)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        float centerX = (float) width / 2;
        float centerY = (float) height / 2;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(kimbeMarkerPos)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(
                (float) Utils.intToRed(kimbeMarkerColor) / 255,
                (float) Utils.intToGreen(kimbeMarkerColor) / 255,
                (float) Utils.intToBlue(kimbeMarkerColor) / 255,
                kimbeMarkerAlpha);
        RenderSystem.enableBlend();

        guiGraphics.renderOutline((int) centerX, (int) centerY - 34, 2, 34, 0xffffffff);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    public void renderPointer(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick)
    {
        poseStack.pushPose();

        float centerX = (float) width / 2;
        float centerY = (float) height / 2;

        poseStack.translate(centerX, centerY, 0);

        poseStack.mulPose(Axis.ZP.rotationDegrees(handlePos + ((handleSpeed * partialTick) * currentRotation)));

        poseStack.translate(0, -16, 0);

        boolean isDisabled = modifiers.stream().anyMatch(o -> o.disablePointerRendering(this));
        if (!isDisabled)
            renderPoseCentered(guiGraphics, texture, 32, 64, 144, 0, 256);

        poseStack.translate(0, 16, 0);

        modifiers.forEach(mod -> mod.renderOnPointer(this, guiGraphics, poseStack, partialTick));

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

        modifiers.forEach(mod -> mod.onKeyReleased(this, keyCode, scanCode, keyModifiers));

        return super.keyReleased(keyCode, scanCode, keyModifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        modifiers.forEach(o -> o.mouseScrolled(this, mouseX, mouseY, scrollX, scrollY));
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
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
        //closes when pressing E unless keybind is set to E
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey) && !SCKeymappings.MINIGAME_HIT.getKey().equals(mouseKey))
        {
            //play miss sound
            if (SCConfig.ENABLE_TACKLE_SOUNDS.get() && modifiers.stream().anyMatch(o -> o.skipMissSound(this)))
                tackleSkin.onFailedMinigame(Minecraft.getInstance().player);
            this.onClose();
            return true;
        }

        //hit input
        if (SCKeymappings.MINIGAME_HIT.isActiveAndMatches(mouseKey))
        {
            if (!isHoldingKey) inputPressed();

            isHoldingKey = true;
        }

        this.modifiers.forEach(mod -> mod.onKeyPress(this, keyCode, scanCode, keyModifiers));

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


        for (ActiveSweetSpot ass : activeSweetSpots.reversed())
        {
            //if can hit spot
            if (ass.canHit && doDegreesOverlapWithLeeway(getPointerPosPrecise(), ass.pos, ass.thickness / 2)
            && modifiers.stream().allMatch(o -> o.canHitSpot(this, ass)))
            {
                //trigger modifiers on-hit, cancel if any returns true
                modifiers.forEach(o -> o.onHit(this, ass));

                consecutiveHits++;
                hitSomething = true;

                //trigger sweet-spot behaviour on hit
                ass.behaviour.onHit(this, ass);
                break;
            }
        }


        if (!hitSomething)
        {
            this.modifiers.forEach(o -> o.onMiss(this));

            perfectCatch = false;
            consecutiveHits = 0;

            if (SCConfig.ENABLE_MISS_SOUND.get() && modifiers.stream().noneMatch(o -> o.skipMissSound(this)))
                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1, 1, false);

            activeSweetSpots.forEach(ass -> ass.behaviour.onMiss(this, ass));

            progress -= penalty;
        }
    }

    public float getPointerPosPrecise()
    {
        float pointerPosPrecise = (handlePos + ((handleSpeed * partial) * currentRotation));

        pointerPosPrecise += hitDelay * handleSpeed * currentRotation;
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
        modifiers.forEach(o -> o.tick(this));
        //tick behaviour
        activeSweetSpots.forEach(ass -> ass.behaviour.tick(this, ass));

        //remove activeSweetSpots marked for removal
        activeSweetSpots.removeIf(ass ->
        {
            if (ass.removed) ass.behaviour.onRemove(this, ass);
            return ass.removed;
        });

        //remove modifiers marked for removal
        modifiers.removeIf(m ->
        {
            if (m.removed) m.onRemove(this);
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
        spotsToAdd.forEach(s -> modifiers.forEach(m -> m.onSpotAdded(this, s)));
        spotsToAdd.clear();

        //move pointer
        handlePos += handleSpeed * currentRotation;

        //decrease kimbe markers alpha
        kimbeMarkerAlpha -= 0.1f;

        if (handlePos > 360) handlePos -= 360;
        if (handlePos < 0) handlePos += 360;

        gracePeriod--;

        tickCount++;

        treasureProgressSmooth += (int) Math.signum(treasureProgress - treasureProgressSmooth);

        if (tickCount % 5 == 0 && gracePeriod < 0)
        {
            progress -= decay;
        }

        if (!isSettingsScreen())
        {

            if (progressSmooth < 0 && modifiers.stream().noneMatch(o -> o.preventLosingMinigame(this)))
            {
                if (SCConfig.ENABLE_TACKLE_SOUNDS.get())
                    tackleSkin.onFailedMinigame(Minecraft.getInstance().player);
                this.onClose();
            }

            if (progressSmooth > hp)
            {
                //if completed treasure minigame, or is a perfect catch with the mossy hook
                boolean awardTreasure = treasureProgress > 100 || modifiers.stream().anyMatch(o -> o.forceAwardTreasure(this));

                if (SCConfig.ENABLE_TACKLE_SOUNDS.get())
                    tackleSkin.onSuccessfulMinigame(Minecraft.getInstance().player);

                PacketDistributor.sendToServer(new SBFishingCompletedPayload(true, tickCount, awardTreasure, perfectCatch, consecutiveHits));
                this.onClose();
            }
        }

        hitParticles.removeIf(HitFakeParticle::tick);
    }

    @Override
    public void onClose()
    {
        modifiers.forEach(o -> o.onRemove(this));

        PacketDistributor.sendToServer(new SBFishingCompletedPayload(false, tickCount, false, false, consecutiveHits));
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
                            new Vector2d(Utils.r.nextFloat() * 2 - 1, Utils.r.nextFloat() * 2 - 1),
                            (float) Utils.intToRed(color) / 255,
                            (float) Utils.intToGreen(color) / 255,
                            (float) Utils.intToBlue(color) / 255,
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

    public void renderCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow)
    {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.drawString(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
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
        for (var ass : activeSweetSpots)
        {
            ass.removed = true;
        }
    }
}
