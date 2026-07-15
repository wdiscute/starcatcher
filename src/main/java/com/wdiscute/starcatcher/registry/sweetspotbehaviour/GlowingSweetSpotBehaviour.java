package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.utils.Utils;

public class GlowingSweetSpotBehaviour extends NormalSweetSpotBehaviour
{
    @Override
    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onHit(instance, ass);
        Object data = instance.modifierData.computeIfAbsent(Starcatcher.rl("deep_dark"), o -> 0);

        if (data instanceof Integer i)
            instance.modifierData.put(Starcatcher.rl("deep_dark"), Math.clamp(i - 10, 0, 255));
    }
}
