package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class MossyHookModifier extends AbstractMinigameModifier
{
    public static final MapCodec<MossyHookModifier> CODEC = MapCodec.unit(MossyHookModifier::new);

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.HARDER_WITH_TREASURE_ON_PERFECT;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(tickCount == 1 && instance.rarity != FishProperties.Rarity.LEGENDARY && instance.rarity != FishProperties.Rarity.EPIC)
        {
            instance.removeAllSweetSpots();
            instance.pointerBaseSpeed = 12;
            instance.pointerSpeed = 12;
            instance.penalty = 25;

            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.THIN_STEADY_MOSSY));
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.AQUA));
        }
    }

    //award treasure if perfect catch and mossy hook
    @Override
    public boolean forceAwardTreasure()
    {
        return instance.perfectCatch && instance.rarity != FishProperties.Rarity.LEGENDARY && instance.rarity != FishProperties.Rarity.EPIC;
    }
}


