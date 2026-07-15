package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class NoGravityModifier extends AbstractCatchModifier
{
    public static final MapCodec<NoGravityModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, NoGravityModifier::new));

    public NoGravityModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean noGravity(FishingBobEntity fbe)
    {
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("no_gravity");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
