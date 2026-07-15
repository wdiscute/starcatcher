package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class LeafSweetSpotBehaviour extends NormalSweetSpotBehaviour
{
    @Override
    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onHit(instance, ass);
        ass.removed = true;
        Minecraft.getInstance().player.playSound(SoundEvents.BIG_DRIPLEAF_BREAK, 1f, 1.6f);
    }
}
