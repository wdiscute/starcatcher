package com.wdiscute.starcatcher.minigame;

import com.wdiscute.starcatcher.minigame.modifiers.FreezeModifier;
import net.minecraft.sounds.SoundEvents;

import static com.wdiscute.starcatcher.minigame.FishingHitZone.*;

public enum HitZoneType{
    OBESE,
    NORMAL,
    THIN,
    TREASURE,
    TNT,
    FREEZE;

    public static class Presets {
        private static final int SIZE_1 = 5;
        private static final int SIZE_2 = 7;
        private static final int SIZE_3 = 12;
        private static final int SIZE_4 = 17;

        public static final FishingHitZone EXTRA_LARGE = new FishingHitZone().setForgiving(SIZE_4)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 0, 0));

        public static final FishingHitZone LARGE = new FishingHitZone().setForgiving(SIZE_3)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 16, 0));

        public static final FishingHitZone MEDIUM = new FishingHitZone().setForgiving(SIZE_2)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 32, 0))
                .setType(HitZoneType.THIN);
        public static final FishingHitZone THIN = new FishingHitZone().setForgiving(SIZE_1)
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 48, 0))
                .setType(HitZoneType.THIN);

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
                .setRendering(makeDefaultRenderConsumer(FishingMinigameScreen.TEXTURE, 16, -16))
                .setPlaceOver(true)
                .setPenaltyAndReward(0, -5)
                .setRecycling(false)
                .setType(HitZoneType.FREEZE)
                .setHitSound(SoundEvents.PLAYER_HURT_FREEZE)
                .setOnHitConsumer(zone -> new FreezeModifier(zone.screen, 20).addModifier());

        public static final FishingHitZone OBESE = new FishingHitZone().setForgiving(30)
                .setRendering(makeObeseRenderConsumer(FishingMinigameScreen.TEXTURE, 0, -64))
                .setPlaceOver(false)
                .setPenaltyAndReward(0, 8)
                .setRecycling(false)
                .setVanishing(true, 0.03f, true)
                .setType(HitZoneType.OBESE);

    }
}
