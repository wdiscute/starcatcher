package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.minigame.modifiers.FreezeModifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;

import java.util.function.BiConsumer;

public enum HitZoneType{
    EXTRA_WIDE,
    NORMAL,
    THIN,
    TREASURE,
    TNT,
    FREEZE,
    SLIDER,

    // For potential api uses
    CUSTOM_1,
    CUSTOM_2,
    CUSTOM_3,
    CUSTOM_4,
    CUSTOM_5,
    CUSTOM_6,
    CUSTOM_7,
    CUSTOM_8,
    CUSTOM_9,
    CUSTOM_10,;

    public static class Presets {
        private static final int SIZE_1 = 5;
        private static final int SIZE_2 = 7;
        private static final int SIZE_3 = 12;
        private static final int SIZE_4 = 17;

        public static final int GREEN_COLOR = 0x8bff6b;
        public static final int RED_COLOR = 0xf36770;

        public static final FishingHitZone EXTRA_LARGE = new FishingHitZone().setForgiving(SIZE_4)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 0, 0))
                .setColor(GREEN_COLOR);

        public static final FishingHitZone LARGE = new FishingHitZone().setForgiving(SIZE_3)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 16, 0))
                .setColor(GREEN_COLOR);

        public static final FishingHitZone MEDIUM = new FishingHitZone().setForgiving(SIZE_2)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 32, 0))
                .setType(HitZoneType.THIN)
                .setColor(0xa8bff6b);
        public static final FishingHitZone THIN = new FishingHitZone().setForgiving(SIZE_1)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 48, 0))
                .setType(HitZoneType.THIN)
                .setColor(GREEN_COLOR);

        public static final FishingHitZone TREASURE = new FishingHitZone().setForgiving(SIZE_2)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 64, 0))
                .setTreasure(10)
                .setType(HitZoneType.TREASURE)
                .setOnHitConsumer(zone -> {
                    if (zone.screen.treasureProgress > 100) zone.setRemoved(true).setRecycling(false);
                });

        //Custom hit zones
        public static final FishingHitZone TNT = new FishingHitZone().setForgiving(SIZE_3)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 0, -16))
                .setVanishing(false, 1, false)
                .setPenaltyAndReward(0, -20)
                .setRecycling(false)
                .setMoving(false, 0)
                .setPlaceOver(true)
                .setType(HitZoneType.TNT)
                .setHitSound(SoundEvents.GENERIC_EXPLODE.value());

        public static final FishingHitZone FREEZE = new FishingHitZone().setForgiving(SIZE_3)
                .setRendering(makeDisappearingRenderConsumer(FishingMinigameScreen.TEXTURE,  0, 0,16, -16, 100, 0xa8bff6b))
                .setPlaceOver(true)
                .setPenaltyAndReward(0, -5)
                .setRecycling(false)
                .setType(HitZoneType.FREEZE)
                .setHitSound(SoundEvents.PLAYER_HURT_FREEZE)
                .setRemoveDelay(20)
                .setOnHitConsumer(zone -> new FreezeModifier(zone.screen, 20).addModifier());

        public static final FishingHitZone EXTRA_WIDE = new FishingHitZone().setForgiving(30)
                .setRendering(makeWideRenderConsumer(FishingMinigameScreen.TEXTURE, 0, -64))
                .setPlaceOver(false)
                .setPenaltyAndReward(0, 8)
                .setRecycling(false)
                .setVanishing(true, 0.03f, true)
                .setType(HitZoneType.EXTRA_WIDE);

        public static final FishingHitZone SLIDER = new FishingHitZone().setForgiving(32)
                .setRendering(makeWideRenderConsumer(FishingMinigameScreen.TEXTURE, 0, -64))
                .setPlaceOver(false)
                .setPenaltyAndReward(4, 5)
                .setRecycling(true)
                .setColor(GREEN_COLOR)
                .setPreRemoveFunction(zone -> !zone.isBeingHoveredOver())
                .setOnTickConsumer(zone -> {
                    if (!zone.isBeingHoveredOver(zone.forgiving - 8)) {
                        zone.setColor(GREEN_COLOR);
                        return;
                    };

                    if (!zone.screen.isHoldingInput() && zone.screen.gracePeriod <= 0){
                        zone.screen.completion -= zone.missPenalty;
                        zone.setColor(RED_COLOR);
                    }

                    if (zone.screen.isHoldingInput()){
                        zone.screen.completion +=  zone.hitReward;
                        zone.setRemoved(true);
                        zone.setColor(0x97fffb);
                        zone.screen.holdingTicks = 0; // makes you not lose instantly when the zone ends
                    }
                })
                .setType(HitZoneType.SLIDER);


        public static BiConsumer<GuiGraphics, FishingHitZone> makeDefaultRenderConsumer(ResourceLocation texture, int uOffset, int vOffset) {
            final int spriteWidth = 16;
            final int spriteHeight = 16;

            return (guiGraphics, zone) -> guiGraphics.blit(
                    texture, -spriteWidth / 2, -spriteHeight / 2,
                    spriteWidth, spriteHeight, uOffset, 160 + vOffset, spriteWidth, spriteHeight, 256, 256);
        }

        public static BiConsumer<GuiGraphics, FishingHitZone> makeWideRenderConsumer(ResourceLocation texture, int uOffset, int vOffset) {
            final int spriteWidth = 32;
            final int spriteHeight = 16;

            return (guiGraphics, zone) -> guiGraphics.blit(
                    texture, -spriteWidth / 2,  -spriteHeight / 2,
                    spriteWidth, spriteHeight, uOffset, 160 + vOffset, spriteWidth, spriteHeight, 256, 256);
        }

        public static BiConsumer<GuiGraphics, FishingHitZone> makeDisappearingRenderConsumer(
                ResourceLocation texture, int uOffsetOver, int vOffsetOver, int uOffsetUnder, int vOffsetUnder, int appearDistance, int colorOver
        ) {
            final int spriteWidth = 16;
            final int spriteHeight = 16;
            final int color = FastColor.ARGB32.color(255, colorOver);

            return (guiGraphics, zone) -> {
                float distance = zone.getDistanceFromPointer();
                float alpha = Math.clamp(distance / appearDistance, 0, 1);

                guiGraphics.blit(
                        texture, -spriteWidth / 2, -spriteHeight / 2,
                        spriteWidth, spriteHeight, uOffsetUnder, 160 + vOffsetUnder, spriteWidth, spriteHeight, 256, 256);

                RenderSystem.setShaderColor(
                        (float) FastColor.ARGB32.red(color) / 255,
                        (float) FastColor.ARGB32.green(color) / 255,
                        (float) FastColor.ARGB32.blue(color) / 255, alpha);

                guiGraphics.blit(
                        texture, -spriteWidth / 2, -spriteHeight / 2,
                        spriteWidth, spriteHeight, uOffsetOver, 160 + vOffsetOver, spriteWidth, spriteHeight, 256, 256);

            };
        }

    }
}
