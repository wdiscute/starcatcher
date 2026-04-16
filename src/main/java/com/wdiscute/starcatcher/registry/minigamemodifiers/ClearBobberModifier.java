package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class ClearBobberModifier extends AbstractTimedModifier
{
    public static final MapCodec<ClearBobberModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength)
            ).apply(instance, ClearBobberModifier::new));

    public ClearBobberModifier(int length) {
        super(length);
    }

    public ClearBobberModifier() {
        super();
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.SLOWER_VANISHING;
    }

    @Override
    public ActiveSweetSpot onSpotAdded(ActiveSweetSpot ass)
    {
        super.onSpotAdded(ass);
        ass.vanishingRate /= 3;
        return ass;
    }
}
