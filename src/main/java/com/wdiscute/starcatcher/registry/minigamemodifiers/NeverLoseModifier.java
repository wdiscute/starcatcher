package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class NeverLoseModifier extends AbstractMinigameModifier
{
    public static final MapCodec<NeverLoseModifier> CODEC = MapCodec.unit(NeverLoseModifier::new);

    public NeverLoseModifier()
    {
    }

    @Override
    public void tick()
    {
        super.tick();

        if (instance.progressSmooth < 5)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.SLIME_HURT);
            instance.progressSmooth += instance.hp / 5;
            instance.progress = (float) instance.hp / 5;
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
