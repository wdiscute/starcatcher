package com.wdiscute.starcatcher.items;

import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties
{
    public static final FoodProperties BASIC_RAW_FISH = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.1f)
            //.usingConvertsTo(ModItems.FISH_BONES.get())
            .build();

}
