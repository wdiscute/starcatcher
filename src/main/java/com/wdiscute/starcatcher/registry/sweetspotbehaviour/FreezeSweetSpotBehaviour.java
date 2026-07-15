package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.FreezeHandleModifier;

public class FreezeSweetSpotBehaviour extends NormalSweetSpotBehaviour
{
    @Override
    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onHit(instance, ass);
        for (AbstractMinigameModifier modifier : instance.getModifiers())
        {
            if(modifier instanceof FreezeHandleModifier fpwam)
            {
                fpwam.tickCount = 0;
                return;
            }
        }

        instance.addModifier(new FreezeHandleModifier(40, 10));
    }
}
