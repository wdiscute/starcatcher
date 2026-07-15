package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;

public class FlipSweetSpotsOnMissModifier extends AbstractMinigameModifier
{

    final float chance;

    public static final MapCodec<FlipSweetSpotsOnMissModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, FlipSweetSpotsOnMissModifier::new));

    public FlipSweetSpotsOnMissModifier(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        for (ActiveSweetSpot ass : instance.getActiveSweetSpots())
        {
            if(Utils.r.nextFloat() < chance)
            {
                ass.currentRotation *= -1;
            }
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("flip_sweetspots_on_miss");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
