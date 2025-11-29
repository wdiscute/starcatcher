package com.wdiscute.starcatcher.minigame.modifiers;

import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.minigame.HitZoneType;

public class TntRainModifier extends AbstractFishingModifier{
    public TntRainModifier(FishingMinigameScreen screen, int length) {
        super(screen, length);
    }

    @Override
    public boolean tick() {
        if (tickCount % 20 == 0){
            HitZoneType.Presets.TNT.copy().buildAndAdd(screen);
        }

        return super.tick();
    }
}
