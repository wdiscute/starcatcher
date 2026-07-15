package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.world.food.FoodProperties;

public class SCFoodProperties
{
    public static final FoodProperties BASIC_RAW_FISH = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1f)
            .usingConvertsTo(SCItems.FISH_BONES.get())
            .alwaysEdible()
            .build();

    public static final FoodProperties BASIC_COOKED_FISH = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.7f)
            .usingConvertsTo(SCItems.FISH_BONES.get())
            .alwaysEdible()
            .build();

}
