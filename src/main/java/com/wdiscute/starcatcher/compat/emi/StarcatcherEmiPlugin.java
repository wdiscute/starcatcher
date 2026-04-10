package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.registry.FishProperties;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

@EmiEntrypoint
public class StarcatcherEmiPlugin implements EmiPlugin
{
    public static final EmiStack MY_WORKSTATION = EmiStack.of(SCItems.ROD);
    public static final EmiRecipeCategory STARCATCHER_CATEGORY
            = new EmiRecipeCategory(
            Starcatcher.rl("fishing"),
            MY_WORKSTATION);

    public static void displayRecipes(ItemStack is)
    {
        EmiApi.displayRecipes(EmiIngredient.of(Ingredient.of(is)));
    }

    @Override
    public void register(EmiRegistry registry)
    {
        registry.addCategory(STARCATCHER_CATEGORY);
        registry.addWorkstation(STARCATCHER_CATEGORY, MY_WORKSTATION);

        //worms info
        registry.addRecipe(new EmiInfoRecipe(List.of(
                EmiIngredient.of(SCTags.WORMS)),
                List.of(
                        Component.translatable("emi.info.starcatcher.worms.0"),
                        Component.translatable("emi.info.starcatcher.worms.1"),
                        Component.translatable("emi.info.starcatcher.worms.2")
                ),
                SellingBin.rl("/worms")));

        //clams info
        registry.addRecipe(new EmiInfoRecipe(List.of(
                EmiIngredient.of(Ingredient.of(SCItems.PEARL.get())),
                EmiIngredient.of(Ingredient.of(SCBlocks.CLAM.get())),
                EmiIngredient.of(Ingredient.of(ItemTags.SAND))),
                List.of(
                        Component.translatable("emi.info.starcatcher.pearls.0"),
                        Component.translatable("emi.info.starcatcher.pearls.1")
                ),
                SellingBin.rl("/pearls")));

        Registry<FishProperties> fps = FishProperties.getRegistry(Minecraft.getInstance().level);

        for (FishProperties fp : fps)
            registry.addRecipe(new StarcatcherEmiFPRecipe(fps.getKey(fp), fp));

        //todo add netherrite recipe
        //todo add template recipes
    }
}
