package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class RollPercentileModifier extends AbstractCatchModifier
{
    int rolls;

    public static final MapCodec<RollPercentileModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("rolls").forGetter(o -> o.rolls),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, RollPercentileModifier::new));


    public RollPercentileModifier(int rolls, String translationOverride)
    {
        super(translationOverride);
        this.rolls = rolls;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if (shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.roll_percentile.shift", rolls));
        else
            return List.of(Component.translatable("tooltip.modifier.starcatcher.roll_percentile"));
    }

    @Override
    public float modifyPercentile(FishingBobEntity fbe, float percentile)
    {
        float p = percentile;

        for (int i = 0; i < rolls; i++)
        {
            p = Math.max(fbe.getRandom().nextFloat() * 100, p);
        }


        return p;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("roll_percentile");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
