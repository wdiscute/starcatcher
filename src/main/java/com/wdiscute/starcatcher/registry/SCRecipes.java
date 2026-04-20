package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SCRecipes
{
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Starcatcher.MOD_ID);

    public static final Supplier<RecipeSerializer<TackleSkinSmithingRecipe>> TACKLE_SKIN_SMITHING =
            REGISTRY.register("smithing_tackle_skin", () -> TackleSkinSmithingRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<FishingRodSkinSmithingRecipe>> FISHING_ROD_SKIN_SMITHING =
            REGISTRY.register("smithing_rod_skin", () -> FishingRodSkinSmithingRecipe.SERIALIZER);

    public static final Supplier<RecipeSerializer<BottledLetterRecipe>> BOTTLED_LETTER =
            REGISTRY.register("bottled_letter", () -> BottledLetterRecipe.SERIALIZER);

    public static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
