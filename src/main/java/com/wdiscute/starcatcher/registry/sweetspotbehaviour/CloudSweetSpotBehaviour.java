package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class CloudSweetSpotBehaviour extends NormalSweetSpotBehaviour
{

    @Override
    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        float pos = ass.pos;
        super.onHit(instance, ass);
        ass.pos = pos;
        Minecraft.getInstance().player.playSound(SoundEvents.BREEZE_IDLE_AIR, 0.8f, 0.6f);
    }

    @Override
    public void onMiss(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onMiss(instance, ass);
        Minecraft.getInstance().player.playSound(SoundEvents.BREEZE_LAND, 0.4f, 1f);
    }
}
