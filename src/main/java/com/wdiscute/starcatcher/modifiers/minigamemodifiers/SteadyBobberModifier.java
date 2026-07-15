package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class SteadyBobberModifier extends AbstractMinigameModifier
{
    public static final MapCodec<SteadyBobberModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SteadyBobberModifier::new));

    public SteadyBobberModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public ActiveSweetSpot onSpotAdded(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onSpotAdded(instance, ass);
        if (ass.baseSS.texturePath().equals(Difficulty.SweetSpot.NORMAL.texturePath()))
        {
            ass.texture = Difficulty.SweetSpot.NORMAL_STEADY.texturePath();
            ass.thickness = Difficulty.SweetSpot.NORMAL_STEADY.size();
        }

        if (ass.baseSS.texturePath().equals(Difficulty.SweetSpot.THIN.texturePath()))
        {
            ass.texture = Difficulty.SweetSpot.THIN_STEADY.texturePath();
            ass.thickness = Difficulty.SweetSpot.THIN_STEADY.size();
        }
        return ass;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("bigger_green_sweetspots");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
