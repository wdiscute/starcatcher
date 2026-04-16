package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.function.Supplier;

public class BaseMinigameModifier extends AbstractMinigameModifier
{

    public static final MapCodec<AbstractMinigameModifier> CODEC = MapCodec.unit(BaseMinigameModifier::new);

    @Override
    public void onMiss()
    {
        super.onMiss();
        //kimbe marker
        instance.kimbeMarkerAlpha = 1;
        //You have to make the actual texture white before trying to recolor like this, dummy
        instance.kimbeMarkerColor = 0xff6767;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();

        //refresh all vanishes
        instance.refreshSweetSpotsAlphas();

        instance.perfectCatch = false;

        instance.consecutiveHits = 0;
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.BASE;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        instance.kimbeMarkerAlpha = 1;
        instance.kimbeMarkerColor = 0x2ce17d;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();

        instance.consecutiveHits++;

        if(U.r.nextFloat() > 0.98 && !instance.treasureActive && instance.treasureProgress == 0)
        {
            instance.addSweetSpot(new ActiveSweetSpot(instance, FishProperties.SweetSpot.TREASURE));
        }

        return super.onHit(ass);
    }
}
