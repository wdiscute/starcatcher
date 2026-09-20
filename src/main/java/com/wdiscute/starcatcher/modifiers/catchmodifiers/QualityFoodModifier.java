package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.compat.QualityFoodCompat;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QualityFoodModifier extends AbstractCatchModifier
{
    public static final MapCodec<QualityFoodModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, QualityFoodModifier::new));

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("quality_food");
    }

    @Override
    public ItemStack modifyBaseItem(FishingBobEntity fbe, Player player, ItemStack itemStack, boolean perfectCatch, boolean golden, float percentile)
    {
        //ignore if quality food is not installed
        if (!QualityFoodCompat.isLoaded())
            return itemStack;

        //get actual stack to modify (if it's a bucket, get the fish inside bucket)
        ItemStack is = itemStack;
        if (itemStack.is(SCItems.STARCAUGHT_BUCKET) || itemStack.is(SCItems.STARCAUGHT_LAVA_BUCKET))
            is = itemStack.getOrDefault(SCDataComponents.BUCKETED_FISH, new MaybeStack(itemStack)).toStack();

        RandomSource r = fbe.level().getRandom();
        List<Modify> rollModifiers = fbe.modifiers
                .stream()
                .filter(o -> o instanceof Modify)
                .map(o -> (Modify) o)
                .toList();

        float rolls = 0;
        double minChance = r.nextDouble();

        for (Modify rollModifier : rollModifiers)
            rolls += rollModifier.addToRolls(fbe, is, rolls, perfectCatch, golden, percentile);

        for (Modify rollModifier : rollModifiers)
            minChance += rollModifier.addToMinChance(fbe, is, minChance, perfectCatch, golden, percentile);

        int finalRolls = ((int) Math.floor(rolls)) + (r.nextFloat() < rolls % 1 ? 1 : 0);

        double chance = minChance;

        //roll chance finalRolls numbers of times
        for (int i = 0; i < finalRolls; i++)
            chance = Math.max(r.nextDouble(), chance);

        //quality food compat
        QualityFoodCompat.addQuality(is, player, chance);

        if (itemStack.is(SCItems.STARCAUGHT_BUCKET) || itemStack.is(SCItems.STARCAUGHT_LAVA_BUCKET))
        {
            itemStack.set(SCDataComponents.BUCKETED_FISH, new MaybeStack(is));
            return itemStack;
        }

        return is;
    }

    @Override
    public boolean isEnabled()
    {
        return QualityFoodCompat.isLoaded();
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    public QualityFoodModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public String toString()
    {
        return "[QualityFoodModifier@" + Integer.toHexString(hashCode()) + "]";
    }

    public interface Modify
    {
        float addToRolls(FishingBobEntity fishingBobEntity, ItemStack itemStack, double currentRolls, boolean perfectCatch, boolean golden, float percentile);

        double addToMinChance(FishingBobEntity fishingBobEntity, ItemStack itemStack, double currentMinChance, boolean perfectCatch, boolean golden, float percentile);
    }
}
