package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.AbstractMinigameModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class NormalSweetSpotBehaviour extends AbstractSweetSpotBehaviour
{
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
            Minecraft.getInstance().player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.3f, 1f);
        ass.alpha = 1;
    }
}
