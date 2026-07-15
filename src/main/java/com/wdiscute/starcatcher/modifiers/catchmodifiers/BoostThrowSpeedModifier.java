package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BoostThrowSpeedModifier extends AbstractCatchModifier
{
    final float multiplier;
    public static final MapCodec<BoostThrowSpeedModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(o -> o.multiplier),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BoostThrowSpeedModifier::new));

    public BoostThrowSpeedModifier(float multiplier, String translationOverride)
    {
        super(translationOverride);
        this.multiplier = multiplier;
    }

    @Override
    public Vec3 modifyThrowVec(FishingBobEntity fbe, Vec3 vec3)
    {
        return vec3.multiply(multiplier, multiplier, multiplier);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("boost_throw_speed");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
