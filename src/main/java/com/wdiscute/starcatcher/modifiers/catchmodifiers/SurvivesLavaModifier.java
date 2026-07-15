package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class SurvivesLavaModifier extends AbstractCatchModifier
{
    public static final MapCodec<SurvivesLavaModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SurvivesLavaModifier::new));

    public SurvivesLavaModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean survivesLava(FishingBobEntity fbe)
    {
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("survives_lava");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
