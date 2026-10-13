package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.compat.QualityFoodCompat;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QualityFoodRollModifier extends AbstractCatchModifier implements QualityFoodModifier.Modify
{
    final float minChance;
    final float rollsToAdd;

    final boolean perfectOnly;
    final boolean goldenOnly;

    final float maxPercentile;

    public static final MapCodec<QualityFoodRollModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("add_min_chance", 0f).forGetter(o -> o.minChance),
                    Codec.FLOAT.optionalFieldOf("add_rolls", 0f).forGetter(o -> o.rollsToAdd),
                    Codec.BOOL.optionalFieldOf("only_for_perfect_catch", false).forGetter(o -> o.perfectOnly),
                    Codec.BOOL.optionalFieldOf("only_for_golden", false).forGetter(o -> o.goldenOnly),
                    Codec.FLOAT.optionalFieldOf("only_for_percentile_below", 100f).forGetter(o -> o.maxPercentile),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, QualityFoodRollModifier::new));

    public QualityFoodRollModifier(float minChance, float rollsToAdd, boolean perfectOnly, boolean goldenOnly, float belowPercent, String translationOverride)
    {
        super(translationOverride);

        this.minChance = minChance;
        this.rollsToAdd = rollsToAdd;

        this.perfectOnly = perfectOnly;
        this.goldenOnly = goldenOnly;

        this.maxPercentile = belowPercent;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("quality_food_roll");
    }

    @Override
    public boolean isEnabled()
    {
        return QualityFoodCompat.isLoaded();
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if(shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.quality_food_roll.shift", Starcatcher.FORMAT.format(rollsToAdd)));
        else
            return List.of(Component.translatable("tooltip.modifier.starcatcher.quality_food_roll"));
    }

    @Override
    public float addToRolls(FishingBobEntity fishingBobEntity, ItemStack itemStack, double currentRolls, boolean perfectCatch, boolean golden, float percentile)
    {
        if (this.perfectOnly && !perfectCatch)
            return 0;

        if (this.goldenOnly && !golden)
            return 0;

        if (this.maxPercentile < percentile)
            return 0;

        return rollsToAdd;
    }

    @Override
    public double addToMinChance(FishingBobEntity fishingBobEntity, ItemStack itemStack, double currentMinChance, boolean perfectCatch, boolean golden, float percentile)
    {
        if (this.perfectOnly && !perfectCatch)
            return 0;

        if (this.goldenOnly && !golden)
            return 0;

        if (this.maxPercentile < percentile)
            return 0;

        return minChance;
    }

    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[QFRollImpl@" + Integer.toHexString(hashCode()) + "] (chance: " + minChance
               + " / rolls: " + rollsToAdd
               + " / perfect: " + perfectOnly
               + " / golden: " + goldenOnly
               + " / percentile: " + maxPercentile
               + " )";
    }
}
