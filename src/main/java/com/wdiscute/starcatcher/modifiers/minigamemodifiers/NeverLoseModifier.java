package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class NeverLoseModifier extends AbstractMinigameModifier
{
    public static final MapCodec<NeverLoseModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, NeverLoseModifier::new));

    public NeverLoseModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean preventLosingMinigame(FishingMinigameScreen fishingMinigameScreen)
    {
        fishingMinigameScreen.progress = fishingMinigameScreen.hp / 5f;
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("never_lose");
    }

    @Override
    public String toString()
    {
        return "[NeverLoseModifier@" + Integer.toHexString(hashCode()) + "]";
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
