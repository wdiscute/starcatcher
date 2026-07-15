package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;

public class KimbeMarkerModifier extends AbstractMinigameModifier
{
    public static final MapCodec<KimbeMarkerModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, KimbeMarkerModifier::new));

    public KimbeMarkerModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        instance.kimbeMarkerAlpha = 1;
        instance.kimbeMarkerColor = 0xff6767;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        instance.kimbeMarkerAlpha = 1;
        instance.kimbeMarkerColor = 0x2ce17d;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();
        return super.onHit(instance, ass);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("kimbe_marker");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[KimbeMarkerModifier@" + Integer.toHexString(hashCode()) + "]";
    }
}
