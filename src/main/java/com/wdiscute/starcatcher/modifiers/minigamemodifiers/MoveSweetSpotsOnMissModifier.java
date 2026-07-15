package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class MoveSweetSpotsOnMissModifier extends AbstractMinigameModifier
{

    public static final MapCodec<MoveSweetSpotsOnMissModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, MoveSweetSpotsOnMissModifier::new));

    public MoveSweetSpotsOnMissModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        instance.getActiveSweetSpots().forEach(o -> o.pos = instance.getRandomFreePosition(o.thickness));
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("move_sweetspots_on_miss");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
