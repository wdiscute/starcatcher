package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class MushroomSweetSpotBehaviour extends AbstractSweetSpotBehaviour
{
    @Override
    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onAdd(instance, ass);
        ass.pos = Utils.r.nextInt(360);
    }

    @Override
    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onHit(instance, ass);
        if (instance.getModifiers().stream().noneMatch(o -> o.skipHitParticles(instance)))
            instance.addParticles(ass.pos, 30, ass.particleColor);
        instance.progress += ass.reward;
        if (ass.isFlip) instance.currentRotation *= -1;
        ass.pos = instance.getRandomFreePosition(ass.thickness);
        if (SCConfig.ENABLE_HIT_SOUNDS.get() && instance.getModifiers().stream().noneMatch(o -> o.skipMissSound(instance)))
            Minecraft.getInstance().player.playSound(SoundEvents.BIG_DRIPLEAF_TILT_DOWN, 0.3f, 1f);
        ass.alpha = 1;
    }
}
