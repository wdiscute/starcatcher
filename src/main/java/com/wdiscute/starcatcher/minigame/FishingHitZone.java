package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;
import org.joml.Quaternionf;

import java.util.List;
import java.util.function.Consumer;

public class FishingHitZone {
    private static final int SIZE_1 = 5;
    private static final int SIZE_2 = 7;
    private static final int SIZE_3 = 12;
    private static final int SIZE_4 = 17;

    public static final FishingHitZone EXTRA_LARGE = new FishingHitZone().setForgiving(SIZE_4).setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 0, 0));
    public static final FishingHitZone LARGE = new FishingHitZone().setForgiving(SIZE_3).setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 16, 0));
    public static final FishingHitZone MEDIUM = new FishingHitZone().setForgiving(SIZE_2).setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 32, 0));
    public static final FishingHitZone THIN = new FishingHitZone().setForgiving(SIZE_1).setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 48, 0));
    public static final FishingHitZone TREASURE = new FishingHitZone().setForgiving(SIZE_2)
            .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 64, 0))
            .setTreasure(10);

    //Custom hit zones
    public static final FishingHitZone TNT = new FishingHitZone().setForgiving(SIZE_3)
            .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 0, -16))
            .setVanishing(false, 1, false)
            .setPenaltyAndReward(0, -20)
            .setRecycling(false)
            .setMoving(false, 0)
            .setPlaceOver(true);

    int pos;
    int forgiving = SIZE_2;

    int treasureProgress = 0;

    int penalty = 0; //TODO: Add miss penalty
    int reward = 10;
    public int gracePeriod = 0;

    int color = FastColor.ARGB32.color(255, 255, 255, 255);

    boolean isVanishing = false;
    float vanishValue = 1;
    float vanishingRate = 0;

    boolean removeOnVanish = false;
    boolean forRemoval = false;
    boolean shouldRecycle = true; // copies the zone to a different pos on remove
    boolean canPlaceOver = false; // makes zone get ignored when checking for empty space

    boolean isMoving = false;
    float moveRate = 0;
    int moveDirection = 1; //changes moving direction (-1 or 1)

    TriConsumer<GuiGraphics, Integer, Integer> guiGraphicsConsumer;
    Consumer<FishingHitZone> onTickConsumer;
    Consumer<FishingHitZone> onHitConsumer;

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

            setVanishing(true, vanishRate, false);
        }

        if (hook.is(ModItems.STONE_HOOK)) {
            if (rarity == FishProperties.Rarity.COMMON) gracePeriod = 40;
            if (rarity == FishProperties.Rarity.UNCOMMON) gracePeriod = 20;
            if (rarity == FishProperties.Rarity.RARE) gracePeriod = 15;
            if (rarity == FishProperties.Rarity.EPIC) gracePeriod = 10;
            if (rarity == FishProperties.Rarity.LEGENDARY) gracePeriod = 5;
        }

        // A funny way to check if the TREASURE constant was used
        if (treasureProgress == 10) {
            setTreasure(difficulty.treasure().hitReward());
        }

        return this;
    }

    public void render(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack, int width, int height) {
        if (vanishValue <= 0) return;

        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        float red = FastColor.ARGB32.red(color) / 255f;
        float green = FastColor.ARGB32.green(color) / 255f;
        float blue = FastColor.ARGB32.blue(color) / 255f;
        float alpha = FastColor.ARGB32.alpha(color) / 255f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(pos - partialTick * moveDirection * moveRate)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(red, green, blue, alpha * vanishValue);
        RenderSystem.enableBlend();

        guiGraphicsConsumer.accept(guiGraphics, width, height);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.popPose();
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

        if (onTickConsumer != null) onTickConsumer.accept(this);
    }

    public boolean isHitSuccess(float pointerPosPrecise) {
        if (!forRemoval && FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, pos, forgiving)) {
            forRemoval = true;
            if (onTickConsumer != null) onHitConsumer.accept(this);
            return true;
        }
        return false;
    }

    public static TriConsumer<GuiGraphics, Integer, Integer> makeDefaultRenderConsumer(ResourceLocation texture, int uOffset, int vOffset) {
        return (guiGraphics, width, height) -> guiGraphics.blit(
                texture, width / 2 - 8, height / 2 - 8 - 25,
                16, 16, uOffset, 160 + vOffset, 16, 16, 256, 256);
    }

    public boolean isTreasure() {
        return treasureProgress > 0;
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
        this.penalty = penalty;
        this.reward = reward;

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

    public FishingHitZone setRendering(TriConsumer<GuiGraphics, Integer, Integer> guiGraphicsConsumer) {
        this.guiGraphicsConsumer = guiGraphicsConsumer;

        return this;
    }

    // it doesnt copy everything, just the parts that dont change
    public FishingHitZone copy() {
        FishingHitZone copy = new FishingHitZone();
        FishingHitZone original = this;
        copy.forgiving = original.forgiving;

        copy.treasureProgress = original.treasureProgress;

        copy.color = original.color;

        copy.penalty = original.penalty;
        copy.reward = original.reward;
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

        return copy;
    }

    public void buildAndAdd(int pos, List<FishingHitZone> listToAdd) {
        this.pos = pos;
        listToAdd.add(this);
    }
}
