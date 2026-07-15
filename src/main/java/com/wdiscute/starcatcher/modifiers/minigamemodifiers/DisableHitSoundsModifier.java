package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class DisableHitSoundsModifier extends AbstractMinigameModifier
{

    public static final MapCodec<DisableHitSoundsModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, DisableHitSoundsModifier::new));

    public DisableHitSoundsModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean skipHitSound(FishingMinigameScreen instance)
    {
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("disable_hit_sounds");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
