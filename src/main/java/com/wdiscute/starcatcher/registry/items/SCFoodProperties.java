package com.wdiscute.starcatcher.registry.items;

import net.minecraft.world.food.FoodProperties;

public class SCFoodProperties
{
    public static final FoodProperties BASIC_RAW_FISH = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.1f)
            .alwaysEat()
            .build();

    public static final FoodProperties BASIC_COOKED_FISH = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.7f)
            .alwaysEat()
            .build();

}
