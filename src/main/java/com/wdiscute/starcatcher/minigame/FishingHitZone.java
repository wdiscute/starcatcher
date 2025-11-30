package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.io.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public int removeDelay = 0;
    public int ticksAfterRemoved = 0;

    public boolean wasMissed = false;
    public int ticksAfterMissed = 0;
    public int redMissFlashLength = 10;
    public int lastColor = -1;

    public boolean isMoving = false;
    public float moveRate = 0;
    public int moveDirection = 1; //changes moving direction (-1 or 1)

    public int tickCount = 0;
    private boolean isBeingHoveredOver = false;

    BiConsumer<GuiGraphics, FishingHitZone> guiGraphicsConsumer;
    Consumer<FishingHitZone> onTickConsumer;
    Consumer<FishingHitZone> onHitConsumer;
    Consumer<FishingHitZone> onMissConsumer;
    Function<FishingHitZone, Boolean> preRemoveFunction; // cancels the return when false


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

        guiGraphicsConsumer.accept(guiGraphics, this);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.popPose();
    }

    public void tick() {
        final boolean wasHovering = isBeingHoveredOver;
        isBeingHoveredOver = isBeingHoveredOver();

        if (onTickConsumer != null) onTickConsumer.accept(this);
        tickCount++;

        if (forRemoval) {
            ticksAfterRemoved++;
            return;
        };

        if (isVanishing) vanishValue -= vanishingRate;
        if (removeOnVanish && vanishValue <= 0) forRemoval = true;

        if (isMoving) {
            pos -= (int) (moveRate * moveDirection);

            pos = getPos(); // normalize the value
        };

        if (wasMissed){
            ticksAfterMissed++;
            if (ticksAfterMissed > redMissFlashLength) {
                setColor(lastColor);
            }
        }

        if (wasHovering && !isBeingHoveredOver && missPenalty != 0) {
            onMiss();
        }
    }

    public void onMiss(){
        if (screen.gracePeriod > 0) return;

        wasMissed = true;
        ticksAfterMissed = 0;
        lastColor = color;
        setColor(HitZoneType.Presets.RED_COLOR);

        screen.completion -= missPenalty;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && missSound != null) player.playSound(missSound);

        screen.modifiers.forEach(modifier -> modifier.onMiss(this));

        if (onMissConsumer != null) onMissConsumer.accept(this);
    }

    public boolean preRemove() {
        if (preRemoveFunction != null) return preRemoveFunction.apply(this);

        return true;
    }

    public @Nullable FishingHitZone onRemove(){
        screen.removedZones++;

        if (shouldRecycle) {
           return this.copy();
        }

        return null;
    }

    public boolean isHitSuccess(float pointerPosPrecise) {
        if (!screen.isHoldingInput() && !forRemoval && FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, pos, forgiving)) {
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

    public boolean isBeingHoveredOver() {
        return isBeingHoveredOver(forgiving);
    }

    public boolean isBeingHoveredOver(int forgiving){
        return FishingMinigameScreen.isHitSuccesful(screen.getPointerPosPrecise(), getPos(), forgiving);
    }

    public int getPos() {
        return FishingMinigameScreen.normalizePos(pos);
    }

    public float getDistanceFromPointer(){
        float ret = Mth.abs(getPos() - screen.getPointerPosPrecise());
        if (ret > 360)  ret -= 360;
        if (ret < 0)  ret += 360;

        return ret;
    }

    public boolean shouldRemove(){
        boolean shouldRemove = forRemoval && removeDelay <= ticksAfterRemoved;
        if (shouldRemove) {
            return preRemove();
        }
        return false;
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

    public FishingHitZone setColor(int hex) {
        this.color = FastColor.ARGB32.color(255 , hex);

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

    public FishingHitZone setPenaltyAndReward(int missPenalty, int hitReward) {
        this.missPenalty = missPenalty;
        this.hitReward = hitReward;

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

    public FishingHitZone setRemoveDelay(int delay) {
        this.removeDelay = delay;

        return this;
    }

    public FishingHitZone setRendering(BiConsumer<GuiGraphics, FishingHitZone> guiGraphicsConsumer) {
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

    public FishingHitZone setPreRemoveFunction(Function<FishingHitZone, Boolean> preRemoveFunction) {
        this.preRemoveFunction = preRemoveFunction;
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

        if (original.lastColor != -1) copy.color = original.lastColor;


        copy.missPenalty = original.missPenalty;
        copy.hitReward = original.hitReward;
        copy.gracePeriod = original.gracePeriod;

        copy.isVanishing = original.isVanishing;
        copy.vanishingRate = original.vanishingRate;

        copy.removeOnVanish = original.removeOnVanish;
        copy.shouldRecycle = original.shouldRecycle;
        copy.canPlaceOver = original.canPlaceOver;
        copy.removeDelay = original.removeDelay;

        copy.redMissFlashLength = original.redMissFlashLength;

        copy.isMoving = original.isMoving;
        copy.moveRate = original.moveRate;
        copy.moveDirection = original.moveDirection;

        copy.guiGraphicsConsumer = original.guiGraphicsConsumer;
        copy.onTickConsumer = original.onTickConsumer;
        copy.onHitConsumer = original.onHitConsumer;
        copy.onMissConsumer = original.onMissConsumer;
        copy.preRemoveFunction = original.preRemoveFunction;

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
