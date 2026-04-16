package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class BounceBackModifier extends AbstractMinigameModifier
{
    public static final MapCodec<BounceBackModifier> CODEC = MapCodec.unit(BounceBackModifier::new);

    private int bounceBackTime = 0;
    private boolean bounced = false;

    public BounceBackModifier()
    {
    }

    @Override
    public void onMiss()
    {
        if (bounceBackTime > 0)
        {
            instance.progress += instance.penalty;
        }

        if (instance.progress <= instance.penalty && !bounced)
        {
            instance.progress += instance.penalty;
            Minecraft.getInstance().player.playSound(SoundEvents.SLIME_HURT);
            bounceBackTime = instance.hp / 5;
            bounced = true;
        }

    }

    @Override
    public void tick()
    {
        super.tick();

        if (bounced)
            bounceBackTime--;

        if (bounceBackTime > 0)
        {
            instance.progress += 1;
            if (instance.progressSmooth < 5) instance.progressSmooth = 5;
        }

        if (bounceBackTime < 0) removed = true;

        if (instance.progressSmooth < 2 && !bounced)
        {
            bounced = true;
            Minecraft.getInstance().player.playSound(SoundEvents.SLIME_HURT);
            bounceBackTime = instance.hp / 5;
        }
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder()
    {
        return SCMinigameModifiers.BOUNCE_BACK;
    }

}
