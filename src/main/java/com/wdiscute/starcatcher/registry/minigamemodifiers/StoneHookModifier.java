package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class StoneHookModifier extends AbstractTimedModifier
{
    public static final MapCodec<StoneHookModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength)
            ).apply(instance, StoneHookModifier::new));

    public StoneHookModifier(int length) {
        super(length);
    }

    public StoneHookModifier() {
        super();
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.STOP_DECAY_ON_HIT;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        instance.gracePeriod = instance.rarity.getStoneHookGraceTicks();;

        return super.onHit(ass);
    }
}
