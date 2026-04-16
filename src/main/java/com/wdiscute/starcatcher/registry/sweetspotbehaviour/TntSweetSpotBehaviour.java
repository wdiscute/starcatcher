package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class TntSweetSpotBehaviour extends AbstractSweetSpotBehaviour
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
        if(ticksActive > 40) ass.removed = true;
    }

    @Override
    public void onHit()
    {
        super.onHit();
        ass.removed = true;
        instance.progress -= ass.reward;
        Minecraft.getInstance().player.playSound(SoundEvents.GENERIC_EXPLODE, 0.2f, 1f);
    }
}
