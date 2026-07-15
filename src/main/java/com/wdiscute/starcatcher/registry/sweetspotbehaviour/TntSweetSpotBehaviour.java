package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class TntSweetSpotBehaviour extends NormalSweetSpotBehaviour
{
    @Override
    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onAdd(instance, ass);
        ass.shouldSudokuOnVanish = true;
    }

    @Override
    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onHit(instance, ass);
        if (SCConfig.ENABLE_HIT_SOUNDS.get())
            Minecraft.getInstance().player.playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.2f, 1f);
    }
}
