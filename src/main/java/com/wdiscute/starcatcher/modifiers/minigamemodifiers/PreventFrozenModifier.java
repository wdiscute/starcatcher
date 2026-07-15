package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class PreventFrozenModifier extends AbstractMinigameModifier implements CancelFrozenEffect
{
    public static final MapCodec<PreventFrozenModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, PreventFrozenModifier::new));

    public PreventFrozenModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("prevent_frozen");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
