package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class FlipEveryHitModifier extends AbstractTimedModifier
{
    public static final MapCodec<FlipEveryHitModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength)
            ).apply(instance, FlipEveryHitModifier::new));

    public FlipEveryHitModifier(int length) {
        super(length);
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder()
    {
        return SCMinigameModifiers.FLIP_EVERY_HIT;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        instance.currentRotation *= -1;
        instance.getActiveSweetSpots().forEach(o -> o.currentRotation *= -1);
        return super.onHit(ass);
    }
}
