package com.wdiscute.starcatcher.compat.jei;

import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipe;
import com.wdiscute.starcatcher.registry.SCItems;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.common.platform.Services;
import mezz.jei.library.plugins.vanilla.anvil.SmithingCategoryExtension;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.nikdo53.neobackports.utils.recipe.input.SmithingRecipeInput;

import java.util.Arrays;
import java.util.List;

public class StarcatcherJeiSmithingCategoryExtension extends SmithingCategoryExtension<StarcatcherRodRecipe>
{
    public StarcatcherJeiSmithingCategoryExtension()
    {
        super(Services.PLATFORM.getRecipeHelper());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ingredientAcceptor.addItemStack(Arrays.stream(recipe.template().getItems()).findFirst().get());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ingredientAcceptor.addItemStack(Arrays.stream(recipe.rod().getItems()).findFirst().get());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ingredientAcceptor.addItemStack(Arrays.stream(recipe.material().getItems()).findFirst().get());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setOutput(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        Ingredient templateIngredient = recipe.template();
        Ingredient additionIngredient = recipe.material();

        List<ItemStack> templateStacks = Arrays.asList(templateIngredient.getItems());
        if (templateStacks.isEmpty())
        {
            templateStacks = List.of(ItemStack.EMPTY);
        }

        ItemStack addition = ItemStack.EMPTY;
        ItemStack[] additions = additionIngredient.getItems();
        if (additions.length > 0)
            addition = additions[0];

        for (ItemStack template : templateStacks)
        {
            SmithingRecipeInput recipeInput = new SmithingRecipeInput(template, SCItems.ROD.toStack(), addition);
            ItemStack output = recipe.assembledNoRegistries(recipeInput);
            ingredientAcceptor.addItemStack(output);
        }
    }
}

