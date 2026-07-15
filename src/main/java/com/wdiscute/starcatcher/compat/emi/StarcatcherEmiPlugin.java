package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipe;
import com.wdiscute.starcatcher.registry.SCDataEntries;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.fish.FishProperties;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
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

        //bonemealing farmland
        List<EmiIngredient> list = new ArrayList<>(SCDataEntries.FARMLAND_BONEMEAL_DROPS.get().stream().map(o -> EmiIngredient.of(Ingredient.of(o.first()))).toList());
        list.add(0, EmiIngredient.of(Ingredient.of(Items.BONE_MEAL)));
        list.add(0, EmiIngredient.of(Ingredient.of(Items.FARMLAND)));
        registry.addRecipe(new EmiInfoRecipe(list,
                List.of(
                        Component.translatable("emi.info.starcatcher.bonemeal_farmland")
                ),
                SellingBin.rl("/bonemealing_farmland")));

        //worms info
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiIngredient.of(SCTags.WORMS)),
                List.of(
                        Component.translatable("emi.info.starcatcher.worms.0"),
                        Component.translatable("emi.info.starcatcher.worms.1")
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

        //hooks, baits, bobbers
        registry.addRecipe(new EmiInfoRecipe(List.of(
                EmiIngredient.of(Ingredient.of(SCTags.HOOKS)),
                EmiIngredient.of(Ingredient.of(SCTags.BOBBERS)),
                EmiIngredient.of(Ingredient.of(SCTags.BAITS))),
                List.of(
                        Component.translatable("emi.info.starcatcher.attachments.0"),
                        Component.translatable("emi.info.starcatcher.attachments.1")
                ),
                SellingBin.rl("/attachments")));

        //fine bones info
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiIngredient.of(Ingredient.of(SCItems.FISH_BONES))),
                List.of(
                        Component.translatable("emi.info.starcatcher.fish_bones.0")
                ),
                SellingBin.rl("/fish_bones")));

        //pearl template info
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiIngredient.of(Ingredient.of(SCItems.PEARL_SMITHING_TEMPLATE))),
                List.of(
                        Component.translatable("emi.info.starcatcher.pearl_template.0")
                ),
                SellingBin.rl("/pearl_template")));

        //fisherman's hat
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiIngredient.of(Ingredient.of(SCTags.HATS))),
                List.of(
                        Component.translatable("emi.info.starcatcher.hat.0")
                ),
                SellingBin.rl("/hats")));

        Registry<FishProperties> fps = FishApi.getRegistry(Minecraft.getInstance().level);

        for (FishProperties fp : fps)
            registry.addRecipe(new StarcatcherEmiFPRecipe(fps.getKey(fp), fp));


        //add all smithing recipes
        List<RecipeHolder<SmithingRecipe>> smithingRecipes = registry.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING);
        smithingRecipes.stream()
                .filter(o -> o.value() instanceof StarcatcherRodRecipe)
                .map(o -> (StarcatcherRodRecipe) o.value())
                .forEach(recipe -> registry.addRecipe(new StarcatcherEmiSmithingRecipe(recipe)));

    }
}
