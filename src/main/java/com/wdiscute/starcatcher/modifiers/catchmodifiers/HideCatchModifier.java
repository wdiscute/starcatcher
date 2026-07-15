package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class HideCatchModifier extends AbstractCatchModifier
{
    float chance;

    public static final MapCodec<HideCatchModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("weight").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, HideCatchModifier::new));


    public HideCatchModifier(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public boolean shouldHideCatch(FishingBobEntity fbe)
    {
        return fbe.level().getRandom().nextFloat() < chance;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("hide_catch");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
