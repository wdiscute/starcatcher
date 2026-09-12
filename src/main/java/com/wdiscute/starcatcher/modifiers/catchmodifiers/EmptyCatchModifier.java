package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class EmptyCatchModifier extends AbstractCatchModifier
{
    public static final MapCodec<EmptyCatchModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, EmptyCatchModifier::new));

    public EmptyCatchModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("empty_catch_modifier");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
