package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class CancelGoldenModifier extends AbstractCatchModifier
{
    public static final MapCodec<CancelGoldenModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, CancelGoldenModifier::new));

    public CancelGoldenModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean cancelGolden(FishingBobEntity fbe)
    {
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("cancel_golden");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
