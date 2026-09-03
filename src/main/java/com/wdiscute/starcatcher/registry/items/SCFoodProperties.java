package com.wdiscute.starcatcher.registry.items;

import net.minecraft.world.food.FoodProperties;

public class SCFoodProperties
{
    public static final FoodProperties BASIC_RAW_FISH = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1f)
            .alwaysEdible()
            .build();

    public static final FoodProperties BASIC_COOKED_FISH = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.7f)
            .alwaysEdible()
            .build();

}
