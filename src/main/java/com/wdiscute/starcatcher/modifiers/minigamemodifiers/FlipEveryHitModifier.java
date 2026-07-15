package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class FlipEveryHitModifier extends AbstractMinigameModifier
{
    public static final MapCodec<FlipEveryHitModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, FlipEveryHitModifier::new));

    public FlipEveryHitModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        instance.currentRotation *= -1;
        instance.getActiveSweetSpots().forEach(o -> o.currentRotation *= -1);
        return super.onHit(instance, ass);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("flip_every_hit");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
