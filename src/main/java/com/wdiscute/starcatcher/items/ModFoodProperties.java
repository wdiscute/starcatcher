package com.wdiscute.starcatcher.items;

import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties
{
    public static final FoodProperties BASIC_RAW_FISH = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1f)
            .usingConvertsTo(ModItems.FISH_BONES.get())
            .build();

}
