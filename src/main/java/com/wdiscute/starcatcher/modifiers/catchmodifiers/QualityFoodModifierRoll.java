package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.minecraft.world.item.ItemStack;

public interface QualityFoodModifierRoll
{
    float addToRolls(FishingBobEntity fishingBobEntity, ItemStack itemStack, double currentRolls, boolean perfectCatch, boolean golden, float percentile);

    double addToMinChance(FishingBobEntity fishingBobEntity, ItemStack itemStack, double currentMinChance, boolean perfectCatch, boolean golden, float percentile);
}
