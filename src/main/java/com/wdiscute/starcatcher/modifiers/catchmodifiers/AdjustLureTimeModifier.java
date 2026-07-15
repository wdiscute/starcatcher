package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdjustLureTimeModifier extends AbstractCatchModifier
{
    final float minTicks;
    final float maxTicks;
    final float randomness;

    public static final MapCodec<AdjustLureTimeModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("min_ticks_multiplier").forGetter(o -> o.minTicks),
                    Codec.FLOAT.fieldOf("max_ticks_multiplier").forGetter(o -> o.maxTicks),
                    Codec.FLOAT.fieldOf("chance_every_tick_multiplier").forGetter(o -> o.randomness),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AdjustLureTimeModifier::new));

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("adjust_lure_time");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    public AdjustLureTimeModifier(float minTicks, float maxTicks, float randomness, String translationOverride)
    {
        super(translationOverride);
        this.minTicks = minTicks;
        this.maxTicks = maxTicks;
        this.randomness = randomness;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if (shift)
        {
            List<Component> list = new ArrayList<>();

            var format = new DecimalFormat("#.##");

            if (minTicks != 1)
                list.add(Component.translatable("tooltip.modifier.starcatcher.adjust_lure_time.shift.min_ticks",
                        format.format(minTicks * 100)));
            if (maxTicks != 1)
                list.add(Component.translatable("tooltip.modifier.starcatcher.adjust_lure_time.shift.max_ticks",
                        format.format(maxTicks * 100)));
            if (randomness != 1)
                list.add(Component.translatable("tooltip.modifier.starcatcher.adjust_lure_time.shift.random",
                        format.format(randomness * 100)));

            return list;
        }
        else
        {
            float total = (minTicks + maxTicks) / 2;

            MutableComponent lureTime = Component.translatable("tooltip.modifier.starcatcher.adjust_lure_time.base");

            if (total > 2f)
                return List.of(Component.translatable("tooltip.modifier.keyword.big_increase").append(lureTime));

            if (total > 1f)
                return List.of(Component.translatable("tooltip.modifier.keyword.increase").append(lureTime));

            if (total < 0.75f)
                return List.of(Component.translatable("tooltip.modifier.keyword.big_decrease").append(lureTime));

            return List.of(Component.translatable("tooltip.modifier.keyword.decrease").append(lureTime));
        }
    }

    @Override
    public int adjustMinTicksToFish(FishingBobEntity fbe, int minTicksToFish)
    {
        return (int) (minTicksToFish * minTicks);
    }

    @Override
    public int adjustMaxTicksToFish(FishingBobEntity fbe,int maxTicksToFish)
    {
        return (int) (maxTicksToFish * maxTicks);
    }

    @Override
    public float adjustChanceToFishEachTick(FishingBobEntity fbe,float chanceToFishEachTick)
    {
        return chanceToFishEachTick * randomness;
    }
}
