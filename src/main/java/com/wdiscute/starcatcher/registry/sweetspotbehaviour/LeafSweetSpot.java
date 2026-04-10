package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;

public class LeafSweetSpot extends NormalSweetSpotBehaviour
{
    @Override
    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onAdd(instance, ass);
        ass.shouldSudokuOnVanish = true;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(ticksActive > 20) ass.vanishingRate = 0.18f;
        if(ass.shouldSudokuOnVanish && ass.alpha <= 0)
            instance.addParticles(ass.pos, 10, ass.particleColor);
    }
}
