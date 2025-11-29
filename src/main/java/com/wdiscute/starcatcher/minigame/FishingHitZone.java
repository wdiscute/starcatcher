package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.io.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.function.Consumer;

public class FishingHitZone {
    public FishingMinigameScreen screen;

    public HitZoneType type = HitZoneType.NORMAL;
    public SoundEvent hitSound = SoundEvents.EXPERIENCE_ORB_PICKUP;
    public SoundEvent missSound = SoundEvents.VILLAGER_NO;

    public int pos;
    public int forgiving = 7;

    public int treasureProgress = 0;

    public int missPenalty = 0;
    public int hitReward = 10;
    public int gracePeriod = 0;

    public int color = -1;

    public boolean isVanishing = false;
    public float vanishValue = 1;
    public float vanishingRate = 0;

    public boolean removeOnVanish = false;
    public boolean forRemoval = false;
    public boolean shouldRecycle = true; // copies the zone to a different pos on remove
    public boolean canPlaceOver = false; // makes zone get ignored when checking for empty space

    public boolean isMoving = false;
    public float moveRate = 0;
    public int moveDirection = 1; //changes moving direction (-1 or 1)

    public int tickCount = 0;
    public boolean isBeingHoveredOver = false;

    TriConsumer<GuiGraphics, Integer, Integer> guiGraphicsConsumer;
    Consumer<FishingHitZone> onTickConsumer;
    Consumer<FishingHitZone> onHitConsumer;
    Consumer<FishingHitZone> onMissConsumer;

    // A builder made to mimic the old system
    public FishingHitZone setFromProperties(FishProperties properties, FishProperties.Difficulty difficulty, ItemStack hook, ItemStack bobber) {
        FishProperties.Rarity rarity = properties.rarity();

        if (difficulty.extras().isMoving()) {
            if (rarity.getId() < 3) {
                setMoving(true, 1);
                if (hook.is(ModItems.MOSSY_HOOK)) setMoving(true, 3);
            } else {
                if (rarity.equals(FishProperties.Rarity.EPIC))
                    setMoving(true, 6);
                if (rarity.equals(FishProperties.Rarity.LEGENDARY))
                    setMoving(true, 8);
                if (hook.is(ModItems.HEAVY_HOOK)) setMoving(true, 3);
            }

        }

        if (difficulty.extras().isVanishing()) {
            float vanishRate = 0.03f;

            if (rarity.equals(FishProperties.Rarity.EPIC)) vanishRate = 0.06f;
            if (rarity.equals(FishProperties.Rarity.LEGENDARY)) vanishRate = 0.1f;

            if (bobber.is(ModItems.CLEAR_BOBBER)) vanishRate /= 2;

            setVanishing(true, vanishRate, removeOnVanish);
        }

        if (hook.is(ModItems.STONE_HOOK)) {
            if (rarity == FishProperties.Rarity.COMMON) gracePeriod = 40;
            if (rarity == FishProperties.Rarity.UNCOMMON) gracePeriod = 20;
            if (rarity == FishProperties.Rarity.RARE) gracePeriod = 15;
            if (rarity == FishProperties.Rarity.EPIC) gracePeriod = 10;
            if (rarity == FishProperties.Rarity.LEGENDARY) gracePeriod = 5;
        }

        // A funny way to check if the TREASURE constant was used
        if (type == HitZoneType.TREASURE) {
            setTreasure(difficulty.treasure().hitReward());
        }

        return this;
    }

    public void render(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack, int width, int height) {
        if (vanishValue <= 0) return;

        final int wheelRadius = 32;
        final int wheelInset = 6;

        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        float red = FastColor.ARGB32.red(color) / 255f;
        float green = FastColor.ARGB32.green(color) / 255f;
        float blue = FastColor.ARGB32.blue(color) / 255f;
        float alpha = FastColor.ARGB32.alpha(color) / 255f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.translate(0, wheelInset - wheelRadius, 0);

        poseStack.rotateAround(Axis.ZP.rotationDegrees((float) pos - partialTick * moveDirection * moveRate), 0, -(wheelInset - wheelRadius), 0);

        RenderSystem.setShaderColor(red, green, blue, alpha * vanishValue);
        RenderSystem.enableBlend();

        guiGraphicsConsumer.accept(guiGraphics, width, height);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.popPose();
    }

    public static TriConsumer<GuiGraphics, Integer, Integer> makeDefaultRenderConsumer(ResourceLocation texture, int uOffset, int vOffset) {
        final int spriteWidth = 16;
        final int spriteHeight = 16;

        return (guiGraphics, width, height) -> guiGraphics.blit(
                texture, -spriteWidth / 2, -spriteHeight / 2,
                spriteWidth, spriteHeight, uOffset, 160 + vOffset, spriteWidth, spriteHeight, 256, 256);
    }

    public static TriConsumer<GuiGraphics, Integer, Integer> makeObeseRenderConsumer(ResourceLocation texture, int uOffset, int vOffset) {
        final int spriteWidth = 32;
        final int spriteHeight = 16;

        return (guiGraphics, width, height) -> guiGraphics.blit(
                texture, -spriteWidth / 2,  -spriteHeight / 2,
                spriteWidth, spriteHeight, uOffset, 160 + vOffset, spriteWidth, spriteHeight, 256, 256);
    }

    public void tick() {
        if (forRemoval) return;
        if (isVanishing) vanishValue -= vanishingRate;
        if (removeOnVanish && vanishValue <= 0) forRemoval = true;

        if (isMoving) {
            pos -= (int) (moveRate * moveDirection);

            if (pos > 360) pos -= 360;
            if (pos < 0) pos += 360;
        };

        final boolean wasHovering = isBeingHoveredOver;
        isBeingHoveredOver = FishingMinigameScreen.isHitSuccesful(screen.getPointerPosPrecise(), pos, forgiving);

        if (wasHovering && !isBeingHoveredOver && missPenalty != 0) {
            onMiss();
        }

        tickCount++;

        if (onTickConsumer != null) onTickConsumer.accept(this);
    }

    public void onMiss(){
        screen.completion -= missPenalty;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && missSound != null) player.playSound(missSound);

        if (onMissConsumer != null) onMissConsumer.accept(this);
    }

    public boolean isHitSuccess(float pointerPosPrecise) {
        if (!forRemoval && FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, pos, forgiving)) {
            forRemoval = true;
            if (onHitConsumer != null) onHitConsumer.accept(this);

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) player.playSound(hitSound);

            return true;
        }
        return false;
    }

    public boolean isTreasure() {
        return type == HitZoneType.TREASURE;
    }


    public FishingHitZone setRemoved(boolean removed) {
        forRemoval = removed;
        return this;
    }

    public FishingHitZone setVanishing(boolean isVanishing, float vanishingRate, boolean removeOnVanish) {
        this.isVanishing = isVanishing;
        this.vanishingRate = vanishingRate;
        this.removeOnVanish = removeOnVanish;

        return this;
    }

    public FishingHitZone setMoving(boolean isMoving, float movingRate) {
        this.isMoving = isMoving;
        this.moveRate = movingRate;

        return this;
    }

    public FishingHitZone setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;

        return this;
    }

    public FishingHitZone setPlaceOver(boolean canPlaceOver) {
        this.canPlaceOver = canPlaceOver;

        return this;
    }

    public FishingHitZone setColor(int red, int green, int blue, int alpha) {
        this.color = FastColor.ARGB32.color(alpha, red, green, blue);

        return this;
    }

    public FishingHitZone setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;

        return this;
    }

    public FishingHitZone setPenaltyAndReward(int penalty, int reward) {
        this.missPenalty = penalty;
        this.hitReward = reward;

        return this;
    }

    public FishingHitZone setForgiving(int forgiving) {
        this.forgiving = forgiving;

        return this;
    }

    public FishingHitZone setRecycling(boolean shouldRecycle) {
        this.shouldRecycle = shouldRecycle;

        return this;
    }

    public FishingHitZone setTreasure(int treasureProgress) {
        this.treasureProgress = treasureProgress;

        return this;
    }

    public FishingHitZone setType(HitZoneType type) {
        this.type = type;

        return this;
    }

    public FishingHitZone setHitSound(SoundEvent sound) {
        this.hitSound = sound;

        return this;
    }

    public FishingHitZone setMissSound(SoundEvent sound) {
        this.missSound = sound;

        return this;
    }

    public FishingHitZone setRendering(TriConsumer<GuiGraphics, Integer, Integer> guiGraphicsConsumer) {
        this.guiGraphicsConsumer = guiGraphicsConsumer;

        return this;
    }

    public FishingHitZone setOnHitConsumer(Consumer<FishingHitZone> onHitConsumer) {
        this.onHitConsumer = onHitConsumer;
        return this;
    }

    public FishingHitZone setOnTickConsumer(Consumer<FishingHitZone> onTickConsumer) {
        this.onTickConsumer = onTickConsumer;
        return this;
    }

    public FishingHitZone setOnMissConsumer(Consumer<FishingHitZone> onMissConsumer) {
        this.onMissConsumer = onMissConsumer;
        return this;
    }

    // it doesnt copy everything, just the parts that dont change
    public FishingHitZone copy() {
        FishingHitZone copy = new FishingHitZone();
        FishingHitZone original = this;

        copy.type = original.type;
        copy.hitSound = original.hitSound;
        copy.missSound = original.missSound;
        copy.forgiving = original.forgiving;

        copy.treasureProgress = original.treasureProgress;

        copy.color = original.color;

        copy.missPenalty = original.missPenalty;
        copy.hitReward = original.hitReward;
        copy.gracePeriod = original.gracePeriod;

        copy.isVanishing = original.isVanishing;
        copy.vanishingRate = original.vanishingRate;

        copy.removeOnVanish = original.removeOnVanish;
        copy.shouldRecycle = original.shouldRecycle;
        copy.canPlaceOver = original.canPlaceOver;

        copy.isMoving = original.isMoving;
        copy.moveRate = original.moveRate;
        copy.moveDirection = original.moveDirection;

        copy.guiGraphicsConsumer = original.guiGraphicsConsumer;
        copy.onTickConsumer = original.onTickConsumer;
        copy.onHitConsumer = original.onHitConsumer;
        copy.onMissConsumer = original.onMissConsumer;

        return copy;
    }

    public void buildAndAdd(FishingMinigameScreen screen, int pos, List<FishingHitZone> listToAdd) {
        this.screen = screen;
        this.pos = pos;
        listToAdd.add(this);
    }

    public void buildAndAdd(FishingMinigameScreen screen){
        this.screen = screen;
        this.pos = screen.getRandomFreePosition();
        screen.fishingHitZones.add(this);
    }
}
