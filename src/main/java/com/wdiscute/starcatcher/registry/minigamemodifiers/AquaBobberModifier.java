package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class AquaBobberModifier extends AbstractTimedModifier
{

    public static final MapCodec<AquaBobberModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(mod -> mod.numberOfSweetSpotsToAdd)
            ).apply(instance, AquaBobberModifier::new));

    int numberOfSweetSpotsToAdd;

    public AquaBobberModifier(int length, int numberOfSweetSpotsToAdd)
    {
        super(length);
        this.numberOfSweetSpotsToAdd = numberOfSweetSpotsToAdd;
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.ADD_AQUA_SWEET_SPOT;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);

        for (int i = 0; i < numberOfSweetSpotsToAdd; i++)
        {
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.AQUA));
        }
    }
}
