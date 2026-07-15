package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCRecipes
{
    DeferredRegister<RecipeSerializer<?>> REGISTRY =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Starcatcher.MOD_ID);

     Supplier<RecipeSerializer<StarcatcherRodRecipe>> FISHING_ROD_SMITHING =
            REGISTRY.register("starcatcher_rod_smithing_recipe", StarcatcherRodRecipe.Serializer::new);

     Supplier<RecipeSerializer<BottledLetterRecipe>> BOTTLED_LETTER =
            REGISTRY.register("bottled_letter", BottledLetterRecipe.Serializer::new);

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
