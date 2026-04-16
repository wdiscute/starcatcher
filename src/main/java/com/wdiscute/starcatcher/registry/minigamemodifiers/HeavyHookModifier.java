package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class HeavyHookModifier extends AbstractTimedModifier
{
    public static final MapCodec<HeavyHookModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength),
                    Codec.FLOAT.fieldOf("slowness").forGetter(mod -> mod.rate)
                    ).apply(instance, HeavyHookModifier::new));

    float rate;

    public HeavyHookModifier(float speedDividedByX)
    {
        super();
        this.rate = speedDividedByX;
    }

    public HeavyHookModifier(int length, float speedDividedByX)
    {
        super(length);
        this.rate = speedDividedByX;
    }


    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.SLOWER_MOVING_SWEET_SPOTS;
    }

    @Override
    public ActiveSweetSpot onSpotAdded(ActiveSweetSpot ass)
    {
        super.onSpotAdded(ass);
        ass.movingRate /= rate;
        return ass;
    }
}
