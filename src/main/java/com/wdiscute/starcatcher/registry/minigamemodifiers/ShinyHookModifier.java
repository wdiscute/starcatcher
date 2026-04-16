package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class ShinyHookModifier extends AbstractMinigameModifier
{
    public static final MapCodec<ShinyHookModifier> CODEC = MapCodec.unit(ShinyHookModifier::new);

    private int hits = 0;

    @Override
    public void onMiss()
    {
        super.onMiss();
        hits = 0;
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.SPAWN_TREASURE_ON_THREE_HITS;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        hits++;

        if(hits == 3 && !instance.treasureActive && instance.treasureProgress == 0)
        {
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.TREASURE));
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.TREASURE));
            removed = true;
        }

        return super.onHit(ass);
    }
}
