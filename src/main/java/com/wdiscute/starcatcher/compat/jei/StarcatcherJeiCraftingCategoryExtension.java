package com.wdiscute.starcatcher.compat.jei;

import com.wdiscute.starcatcher.recipe.TackleBoxBoatRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class StarcatcherJeiCraftingCategoryExtension implements ICraftingCategoryExtension<TackleBoxBoatRecipe>
{
    @Override
    public void setRecipe(RecipeHolder<TackleBoxBoatRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses)
    {
        ICraftingCategoryExtension.super.setRecipe(recipeHolder, builder, craftingGridHelper, focuses);
    }

    @Override
    public List<SlotDisplay> getIngredients(RecipeHolder<TackleBoxBoatRecipe> recipeHolder)
    {
        RecipeDisplay display = getFirstDisplay(recipeHolder);
        if (display == null) {
            return List.of();
        }
        return getIngredients(display);
    }

    private static @Nullable RecipeDisplay getFirstDisplay(RecipeHolder<TackleBoxBoatRecipe> recipeHolder) {
        List<RecipeDisplay> displays = recipeHolder.value().display();
        if (displays.isEmpty()) {
            return null;
        }
        return displays.getFirst();
    }

    private static List<SlotDisplay> getIngredients(RecipeDisplay display) {
        if (display instanceof ShapedCraftingRecipeDisplay shapedCraftingRecipeDisplay) {
            return shapedCraftingRecipeDisplay.ingredients();
        } else if (display instanceof ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay) {
            return shapelessCraftingRecipeDisplay.ingredients();
        } else {
            return List.of();
        }
    }
}

