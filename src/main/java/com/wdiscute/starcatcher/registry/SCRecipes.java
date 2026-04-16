package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCRecipes
{
    public static final DeferredRegisterTyped<RecipeSerializer<?>> REGISTRY =
            DeferredRegisterTyped.create(Registries.RECIPE_SERIALIZER, Starcatcher.MOD_ID);

    public static final  Supplier<RecipeSerializer<NetheriteUpgradeSmithingRecipe>> FISHING_ROD_SMITHING =
            REGISTRY.register("smithing_netherite_upgraded", NetheriteUpgradeSmithingRecipe.Serializer::new);

    public static final  Supplier<RecipeSerializer<TackleSkinSmithingRecipe>> TACKLE_SKIN_SMITHING =
            REGISTRY.register("smithing_tackle_skin", TackleSkinSmithingRecipe.Serializer::new);

    public static final  Supplier<RecipeSerializer<FishingRodSkinSmithingRecipe>> FISHING_ROD_SKIN_SMITHING =
            REGISTRY.register("smithing_rod_skin", FishingRodSkinSmithingRecipe.Serializer::new);

    public static final  Supplier<RecipeSerializer<BottledLetterRecipe>> BOTTLED_LETTER =
            REGISTRY.register("bottled_letter", BottledLetterRecipe.Serializer::new);

    public static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
