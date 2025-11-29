package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.network.FishingCompletedPayload;
import com.wdiscute.starcatcher.items.ColorfulBobber;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.io.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class FishingMinigameScreen extends Screen implements GuiEventListener {
    private static final Random r = new Random();

    public static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame/minigame.png");
    private static final ResourceLocation NETHER = Starcatcher.rl("textures/gui/minigame/nether.png");
    private static final ResourceLocation CAVE = Starcatcher.rl("textures/gui/minigame/cave.png");
    private static final ResourceLocation SURFACE = Starcatcher.rl("textures/gui/minigame/surface.png");

    final FishProperties fp;
    final ItemStack itemBeingFished;
    final ItemStack bobber;
    final ItemStack bait;
    final ItemStack hook;
    final ItemStack treasureIS;

    final int penalty;
    final int decay;
    final boolean hasTreasure;
    final boolean changeRotation;
    final boolean isFlip;
    final boolean isVanishing;
    final boolean isMoving;

    float lastHitMarkerPos = 0;
    float kimbeColor = 0;

    int gracePeriod = 80;

    final InteractionHand hand;

    float pointerSpeed;
    int pointerPos = 0;
    int currentRotation = 1;
    float partial;
    float hitDelay;

    int completion = 20;
    int completionSmooth = 20;

    boolean perfectCatch = true;
    int consecutiveHits = 0;
    int removedZones = 0;
    int succeededZones = 0;

    boolean treasureActive;
    int treasureProgress = 0;
    int treasureProgressSmooth = 0;

    int tickCount = 0;
    List<HitFakeParticle> hitParticles = new ArrayList<>();

    int previousGuiScale;

    ResourceLocation tankTexture = SURFACE;

    // Nikdo53 values, these are mine dont steal them
    boolean isHoldingSpace = false;
    List<FishingHitZone> fishingHitZones = new ArrayList<>();
    List<Supplier<Boolean>> modifiers = new ArrayList<>(); // for making temporary modifiers

    public FishingMinigameScreen(FishProperties fp, ItemStack rod) {
        super(Component.empty());

        previousGuiScale = Minecraft.getInstance().options.guiScale().get();
        Minecraft.getInstance().options.guiScale().set(Config.MINIGAME_GUI_SCALE.get());

        hitDelay = Config.HIT_DELAY.get().floatValue();

        this.fp = fp;
        this.itemBeingFished = new ItemStack(fp.fish());
        this.bobber = rod.get(ModDataComponents.BOBBER).stack().copy();
        this.bait = rod.get(ModDataComponents.BAIT).stack().copy();
        this.hook = rod.get(ModDataComponents.HOOK).stack().copy();

        treasureIS = new ItemStack(BuiltInRegistries.ITEM.get(fp.dif().treasure().loot()));

        //tank texture change
        ResourceKey<Level> dim = Minecraft.getInstance().level.dimension();
        Player player = Minecraft.getInstance().player;
        if (player.getY() < 50 && dim.equals(Level.OVERWORLD))
            tankTexture = CAVE;

        if (dim.equals(Level.NETHER))
            tankTexture = NETHER;

        //assign difficulty, if using mossy_hook it should make common, uncommon and rare into a harder difficulty
        boolean commonUncommonRareFish = fp.rarity() == FishProperties.Rarity.COMMON ||
                fp.rarity() == FishProperties.Rarity.UNCOMMON ||
                fp.rarity() == FishProperties.Rarity.RARE;

        FishProperties.Difficulty difficulty = hook.is(ModItems.MOSSY_HOOK) &&
                (commonUncommonRareFish) ? FishProperties.Difficulty.MEDIUM_VANISHING_MOVING : fp.dif();

        //base - a lot of these are now hitZone-based
        this.pointerSpeed = 20;
        this.penalty = difficulty.penalty();
        this.decay = difficulty.decay();
        this.hasTreasure = difficulty.treasure().hasTreasure();
        this.changeRotation = difficulty.extras().isFlip() && !hook.is(ModItems.STABILIZING_HOOK);

        //extras
        this.isFlip = difficulty.extras().isFlip();
        this.isVanishing = difficulty.extras().isVanishing();
        this.isMoving = difficulty.extras().isMoving();

        FishingHitZone large = FishingHitZone.LARGE;
        FishingHitZone thin = FishingHitZone.THIN;

        //make sweet spots fatter if difficulty bobber is being used
        if (bobber.is(ModItems.STEADY_BOBBER)) {
            large = FishingHitZone.EXTRA_LARGE;
            thin = FishingHitZone.MEDIUM;
        }

        FishProperties.Difficulty.Markers markers = difficulty.markers();

        //TODO: This isn't hardcoded anymore, the jsons should reflect that
/*
        if (markers.first())
            large.copy().setFromProperties(fp, difficulty, hook, bobber).setPenaltyAndReward(0, difficulty.reward()).buildAndAdd(getRandomFreePosition(), fishingHitZones);
        if (markers.second())
            large.copy().setFromProperties(fp, difficulty, hook, bobber).setPenaltyAndReward(0, difficulty.reward()).buildAndAdd(getRandomFreePosition(), fishingHitZones);
        if (markers.firstThin())
            thin.copy().setFromProperties(fp, difficulty, hook, bobber).setPenaltyAndReward(0, difficulty.rewardThin()).buildAndAdd(getRandomFreePosition(), fishingHitZones);
        if (markers.secondThin())
            thin.copy().setFromProperties(fp, difficulty, hook, bobber).setPenaltyAndReward(0, difficulty.rewardThin()).buildAndAdd(getRandomFreePosition(), fishingHitZones);
*/

        FishingHitZone.OBESE.copy().buildAndAdd(getRandomFreePosition(), fishingHitZones);

        Supplier<Boolean> zoneSpawner = () -> {
            if (tickCount % 8 == 0){
                FishingHitZone.OBESE.copy().buildAndAdd(getRandomFreePosition(), fishingHitZones);
            }
            return getTotalMisses() >= 3;
        };

        modifiers.add(zoneSpawner);

        hand = Minecraft.getInstance().player.getMainHandItem().is(ModItems.ROD) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    private int getRandomFreePosition() {
        for (int i = 0; i < 100; i++) {
            int posBeingChecked = r.nextInt(360);

            List<FishingHitZone> list = new ArrayList<>(fishingHitZones);
            if (list.stream().anyMatch(zone -> !zone.canPlaceOver && (Math.abs(zone.pos - posBeingChecked) < 50 || Math.abs(zone.pos - posBeingChecked) > 310))) {
                continue;
            }

            return posBeingChecked;
        }

        return 0;
    }

    //region render

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        PoseStack poseStack = guiGraphics.pose();
        partial = partialTick;

        if (treasureActive)
            renderTreasure(guiGraphics);

        renderMainElements(guiGraphics, width, height, isHoldingSpace, tankTexture);

        //render all hit zones
        List<FishingHitZone> renderList = new ArrayList<>(fishingHitZones);
        renderList.forEach(zone -> zone.render(guiGraphics, partialTick, poseStack, width, height));

        //wheel second layer
        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32,
                64, 64, 64, 192, 64, 64, 256, 256);

        //POINTER
        renderPointer(guiGraphics, partialTick, poseStack);

        //KIMBE MARKER
        renderKimbeMarker(guiGraphics, poseStack);

        renderDecor(guiGraphics, width, height, completionSmooth, itemBeingFished);

        //particles
        for (HitFakeParticle instance : hitParticles) {
            renderParticle(guiGraphics, instance, poseStack, width, height);
        }


    }

    public static void renderParticle(GuiGraphics guiGraphics, HitFakeParticle instance, PoseStack poseStack, int width, int height) {
        poseStack.pushPose();
        poseStack.translate(instance.pos.x, instance.pos.y, 0);
        RenderSystem.setShaderColor(instance.r, instance.g, instance.b, instance.a);

        guiGraphics.blit(
                TEXTURE, width / 2 - 8, height / 2 - 8,
                16, 16, 80, 160, 16, 16, 256, 256);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.popPose();
    }

    public static void renderPosTreasure(GuiGraphics guiGraphics, PoseStack poseStack, int width, int height, int posTreasure) {
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(posTreasure)));
        poseStack.translate(-centerX, -centerY, 0);

        guiGraphics.blit(
                TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                16, 16, 64, 160, 16, 16, 256, 256);

        poseStack.popPose();
    }

    public static void renderHitPos(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack, int width, int height, int currentRotation, int moveRate, int difficultyBobberOffset, float hitPos, float hitPosVanishing, boolean isThin) {
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;


        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(hitPos - partialTick * currentRotation * moveRate)));
        poseStack.translate(-centerX, -centerY, 0);
        RenderSystem.setShaderColor(1, 1, 1, hitPosVanishing);
        RenderSystem.enableBlend();

        guiGraphics.blit(
                TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                16, 16, (isThin ? 48 : 16) - difficultyBobberOffset, 160, 16, 16, 256, 256);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.popPose();
    }

    public static void renderMainElements(GuiGraphics guiGraphics, int width, int height, boolean isHoldingSpace, ResourceLocation tankTexture) {
        //tank background
        guiGraphics.blit(
                tankTexture, width / 2 - 42 - 100, height / 2 - 48,
                85, 97, 0, 0, 85, 97, 85, 97);

        //wheel background
        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32,
                64, 64, 0, 192, 64, 64, 256, 256);

        //spacebar
        guiGraphics.blit(
                TEXTURE, width / 2 - 16, height / 2 + 40,
                32, 16, isHoldingSpace ? 48 : 0, 112, 32, 16, 256, 256);
    }

    public void renderTreasure(GuiGraphics guiGraphics) {
        renderTreasure(guiGraphics, width, height, treasureProgress, treasureProgressSmooth, treasureIS, bobber);
    }

    public static void renderTreasure(GuiGraphics guiGraphics, int width, int height, float treasureProgress, int treasureProgressSmooth, ItemStack treasureIS, ItemStack bobber) {
        //treasure bar
        guiGraphics.blit(
                TEXTURE, width / 2 - 158, height / 2 - 42 + (int) (64 - (64f * treasureProgressSmooth) / 100),
                5, 64 * treasureProgressSmooth / 100,
                141, 6 + 64 - (float) (64 * treasureProgressSmooth) / 100,
                5, 64 * treasureProgressSmooth / 100,
                256, 256);

        //treasure chest?
        guiGraphics.blit(
                TEXTURE, width / 2 - 16 - 155, height / 2 - 48,
                32, 96, 96, 0, 32, 96, 256, 256);

        //render treasure on top of bar
        guiGraphics.renderItem(treasureIS, width / 2 - 163, ((int) ((float) height / 2 - (64f * treasureProgressSmooth) / 100) + 15));


        int color = Tooltips.hueToRGBInt(Tooltips.hue);
        if (bobber.is(ModItems.GLITTER_BOBBER))
            RenderSystem.setShaderColor((float) FastColor.ARGB32.red(color) / 255, (float) FastColor.ARGB32.green(color) / 255, (float) FastColor.ARGB32.blue(color) / 255, 1);
        if (bobber.is(ModItems.COLORFUL_BOBBER)) color = bobber.get(ModDataComponents.BOBBER_COLOR).getColorAsInt();
        if (bobber.is(ModItems.COLORFUL_BOBBER))
            RenderSystem.setShaderColor((float) FastColor.ARGB32.red(color) / 255, (float) FastColor.ARGB32.green(color) / 255, (float) FastColor.ARGB32.blue(color) / 255, 1);

        //outline when treasure complete
        if (treasureProgress > 99)
            guiGraphics.blit(
                    TEXTURE, width / 2 - 16 - 155, height / 2 - 48,
                    32, 96, 64, 0, 32, 96, 256, 256);

        if (bobber.is(ModItems.COLORFUL_BOBBER) || bobber.is(ModItems.GLITTER_BOBBER))
            RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static void renderDecor(GuiGraphics guiGraphics, int width, int height, int completionSmooth, ItemStack itemBeingFished) {
        //silver thing on top
        guiGraphics.blit(
                TEXTURE, width / 2 - 16, height / 2 - 16,
                32, 32, 208, 208, 32, 32, 256, 256);

        //fishing rod
        guiGraphics.blit(
                TEXTURE, width / 2 - 32 - 70, height / 2 - 24 - 57,
                64, 48, 192, 0, 64, 48, 256, 256);

        //fishing line
        guiGraphics.blit(
                TEXTURE, width / 2 - 6 - 102, height / 2 - 56 - 18,
                16, 112 - completionSmooth,
                176, 0 + completionSmooth,
                16, 112 - completionSmooth,
                256, 256);

        //Twitch chat didn't force me to write this comment
        //Lies, kuko010 force me to write said comment
        //FISH
        guiGraphics.renderItem(itemBeingFished, width / 2 - 8 - 100, height / 2 - 8 + 35 - completionSmooth);
    }

    public void renderKimbeMarker(GuiGraphics guiGraphics, PoseStack poseStack) {
        renderKimbeMarker(guiGraphics, poseStack, width, height, kimbeColor, lastHitMarkerPos, bobber);
    }

    public static void renderKimbeMarker(GuiGraphics guiGraphics, PoseStack poseStack, int width, int height, float kimbeColor, float lastHitMarkerPos, ItemStack bobber) {
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(lastHitMarkerPos)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(1, 0, 0, kimbeColor);
        RenderSystem.enableBlend();

        //16 offset on y for texture centering
        if (!bobber.is(ModItems.KIMBE_BOBBER)) {
            guiGraphics.blit(
                    TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                    64, 64, 128, 128, 64, 64, 256, 256);
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    public void renderPointer(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack) {
        renderPointer(guiGraphics, partialTick, poseStack, width, height, isHoldingSpace, pointerPos, pointerSpeed, currentRotation);
    }

    public static void renderPointer(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack, int width, int height, boolean isHoldingSpace, int pointerPos, float speed, int currentRotation) {
        //TODO make it not use the partial ticks from rendering thread of whatever honestly its just nerd stuff that no one will care about
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);
        // if (isHoldingSpace) poseStack.scale(0.8f, 1f, 1f);

        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(pointerPos + ((speed * partialTick) * currentRotation))));
        poseStack.translate(-centerX, -centerY, 0);


        //16 offset on y for texture centering
        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                64, 64, 128, 192, 64, 64, 256, 256);

        poseStack.popPose();
    }

    //endregion

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Minecraft.getInstance().options.keyJump.getKey().getValue()) {
            isHoldingSpace = false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        //closes when pressing E
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }

        //spacebar input
        if (keyCode == Minecraft.getInstance().options.keyJump.getKey().getValue()) {
            isHoldingSpace = true;

            if (gracePeriod > 0) gracePeriod = 0;

            Minecraft.getInstance().player.swing(hand, true);

            AtomicBoolean hitSomething = new AtomicBoolean(false);

            Vec3 pos = Minecraft.getInstance().player.position();
            ClientLevel level = Minecraft.getInstance().level;

            float pointerPosPrecise = (pointerPos + ((pointerSpeed * partial) * currentRotation));

            pointerPosPrecise += hitDelay * pointerSpeed * currentRotation;

            lastHitMarkerPos = pointerPosPrecise;


            for (FishingHitZone zone : fishingHitZones){
                if (zone.isHitSuccess(lastHitMarkerPos)) {
                    onSuccessfulHit(zone);

                    hitSomething.set(true);
                }
            }


            if (hitSomething.get()) {
                consecutiveHits++;
                if ((hasTreasure && r.nextFloat() > 0.9 /*0.9*/ && completion < 60 && !treasureActive)
                        ||
                        (consecutiveHits == 3 && !treasureActive && hook.is(ModItems.SHINY_HOOK))) {
                    treasureActive = true;

                    FishingHitZone.TREASURE.copy().setFromProperties(fp, fp.dif(), hook, bobber).buildAndAdd(getRandomFreePosition(), fishingHitZones);

                    treasureProgress = 0;
                    treasureProgressSmooth = 0;
                }

                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1, false);
                if (changeRotation) currentRotation *= -1;

            } else {
                if (bobber.is(ModItems.KIMBE_BOBBER))
                    Minecraft.getInstance().player.playSound(SoundEvents.VILLAGER_NO, 1, 1);
                kimbeColor = 1;
                consecutiveHits = 0;
                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1, 1, false);
                completion -= penalty;
                perfectCatch = false;
            }

        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onSuccessfulHit(FishingHitZone zone) {
        addParticles(zone.pos, zone.type == HitZoneType.THIN ? 30 : 15, zone.isTreasure());

        succeededZones++;
        completion += zone.reward;
        gracePeriod += zone.gracePeriod;
        treasureProgress += zone.treasureProgress;

        if (treasureProgress > 100 && zone.isTreasure()) zone.setRecycling(false);

        if (zone.type == HitZoneType.FREEZE){
            final float previousPointerSpeed = pointerSpeed;
            final int currentTickCount = tickCount;

            pointerSpeed = 0;

            Supplier<Boolean> unfreeze = () -> {
                if (tickCount >= currentTickCount + 20){
                    this.pointerSpeed = previousPointerSpeed;
                    return true;
                }
                return false;
            };

            modifiers.add(unfreeze);
        }
    }


    public static boolean isHitSuccesful(float pointerPosPrecise, int hitPos, int forgiving) {
        if (hitPos == Integer.MIN_VALUE) return false;

        return Math.abs(hitPos - pointerPosPrecise) < forgiving || Math.abs(hitPos - pointerPosPrecise) > 360 - forgiving;
    }

    @Override
    public void tick() {
        pointerPos += (int) (pointerSpeed * currentRotation);

        kimbeColor -= 0.1f;

        List<FishingHitZone> toAdd = new ArrayList<>();

        fishingHitZones.removeIf(fishingHitZone -> {
            boolean forRemoval = fishingHitZone.forRemoval;
            if (forRemoval) removedZones++;

            if (fishingHitZone.shouldRecycle && forRemoval) {
                fishingHitZone.copy().buildAndAdd(getRandomFreePosition(), toAdd);
            }

            return forRemoval;
        });

        fishingHitZones.forEach(FishingHitZone::tick);

/*        if (tickCount % 40 == 0){
            FishingHitZone.FREEZE.copy().buildAndAdd(getRandomFreePosition(), toAdd);
        }*/

        fishingHitZones.addAll(toAdd);

        modifiers.removeIf(Supplier::get);

        if (pointerPos > 360) pointerPos -= 360;
        if (pointerPos < 0) pointerPos += 360;

        gracePeriod--;

        tickCount++;

        completionSmooth += (int) Math.signum(completion - completionSmooth);
        completionSmooth += (int) Math.signum(completion - completionSmooth);

        treasureProgressSmooth += (int) Math.signum(treasureProgress - treasureProgressSmooth);

        if (tickCount % 5 == 0 && gracePeriod < 0) {
            completion -= decay;
        }

        if (completionSmooth < 0) {
            this.onClose();
        }

        if (completionSmooth > 75) {
            //if completed treasure minigame, or is a perfect catch with the mossy hook
            boolean awardTreasure = treasureProgress > 100 || (perfectCatch && hook.is(ModItems.MOSSY_HOOK));

            PacketDistributor.sendToServer(new FishingCompletedPayload(tickCount, awardTreasure, perfectCatch, consecutiveHits));
            this.onClose();
        }

        hitParticles.removeIf(HitFakeParticle::tick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().options.guiScale().set(previousGuiScale);

        PacketDistributor.sendToServer(new FishingCompletedPayload(-1, false, false, consecutiveHits));
        this.minecraft.popGuiLayer();
    }

    private void addParticles(int posInDegrees, int count, boolean treasure) {
        int xPos = (int) (30 * Math.cos(Math.toRadians(posInDegrees - 90)));
        int yPos = (int) (30 * Math.sin(Math.toRadians(posInDegrees - 90)));

        for (int i = 0; i < count; i++) {
            if (bobber.is(ModItems.GLITTER_BOBBER)) {
                hitParticles.add(new HitFakeParticle(
                        xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        r.nextFloat(),
                        r.nextFloat(),
                        r.nextFloat(),
                        1
                ));
                continue;
            }

            if (bobber.is(ModItems.COLORFUL_BOBBER)) {
                ColorfulBobber.BobberColor color = bobber.get(ModDataComponents.BOBBER_COLOR);
                hitParticles.add(new HitFakeParticle(
                        xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        color.r(),
                        color.g(),
                        color.b(),
                        1
                ));
                continue;
            }

            if (treasure) {
                //red particles if treasure sweet spot was hit
                hitParticles.add(new HitFakeParticle(
                        xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        0.7f + r.nextFloat() / 3, 0.5f, 0.5f, 1
                ));
            } else {
                hitParticles.add(new HitFakeParticle(xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1)));
            }

        }
    }

    public int getTotalMisses(){
        return removedZones - succeededZones;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
