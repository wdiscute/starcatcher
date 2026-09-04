package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public interface SCRecipes
{
    DeferredRegisterTyped<RecipeSerializer<?>> REGISTRY =
            DeferredRegisterTyped.create(Registries.RECIPE_SERIALIZER, Starcatcher.MOD_ID);

     Supplier<RecipeSerializer<StarcatcherRodRecipe>> FISHING_ROD_SMITHING =
            REGISTRY.register("starcatcher_rod_smithing_recipe", StarcatcherRodRecipe.Serializer::new);

     Supplier<RecipeSerializer<BottledLetterRecipe>> BOTTLED_LETTER =
            REGISTRY.register("bottled_letter", BottledLetterRecipe.Serializer::new);

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
