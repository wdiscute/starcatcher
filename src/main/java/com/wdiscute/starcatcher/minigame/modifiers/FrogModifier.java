package com.wdiscute.starcatcher.minigame.modifiers;

import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;

public class FrogModifier extends PermanentFishingModifier{
    public FrogModifier(FishingMinigameScreen screen) {
        super(screen);
    }

    @Override
    public boolean tick() {
        if (screen.modifiers.stream().anyMatch(modifier -> modifier instanceof FreezeModifier)){
            return false;
        }

        int tick = tickCount % 30;

        if (tick <= 14) {
            screen.pointerSpeed += 1f;
        } else
            screen.pointerSpeed -= 1f;

        if (screen.pointerSpeed < 0) screen.pointerSpeed = 0;

       // System.out.println("screen.pointerSpeed = " + screen.pointerSpeed);


        return super.tick();
    }
}
