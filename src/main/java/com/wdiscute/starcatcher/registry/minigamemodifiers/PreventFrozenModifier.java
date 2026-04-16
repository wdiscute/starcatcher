package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class PreventFrozenModifier extends AbstractMinigameModifier implements CancelFrozenEffect
{
    public static final MapCodec<PreventFrozenModifier> CODEC = MapCodec.unit(PreventFrozenModifier::new);

    public PreventFrozenModifier()
    {
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder()
    {
        return SCMinigameModifiers.PREVENT_FROZEN;
    }
}
