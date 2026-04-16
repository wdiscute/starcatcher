package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class SteadyBobberModifier extends AbstractMinigameModifier
{

    public static final MapCodec<SteadyBobberModifier> CODEC = MapCodec.unit(SteadyBobberModifier::new);

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.BIGGER_GREEN_SWEET_SPOTS;
    }

    @Override
    public ActiveSweetSpot onSpotAdded(ActiveSweetSpot ass)
    {
        super.onSpotAdded(ass);
        if(ass.baseSS.texturePath().equals(FishProperties.SweetSpot.NORMAL.texturePath()))
        {
            ass.texture = FishProperties.SweetSpot.NORMAL_STEADY.texturePath();
            ass.thickness = FishProperties.SweetSpot.NORMAL_STEADY.size();
        }

        if(ass.baseSS.texturePath().equals(FishProperties.SweetSpot.THIN.texturePath()))
        {
            ass.texture = FishProperties.SweetSpot.THIN_STEADY.texturePath();
            ass.thickness = FishProperties.SweetSpot.THIN_STEADY.size();
        }
        return ass;
    }
}
