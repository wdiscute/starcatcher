package com.wdiscute.starcatcher.compat.jei;

import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipe;
import com.wdiscute.starcatcher.registry.SCItems;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.platform.Services;
import mezz.jei.library.plugins.vanilla.anvil.SmithingCategoryExtension;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

import java.util.List;
import java.util.Optional;

public class StarcatcherJeiSmithingCategoryExtension extends SmithingCategoryExtension<StarcatcherRodRecipe>
{
    public StarcatcherJeiSmithingCategoryExtension()
    {
        super(Services.PLATFORM.getRecipeHelper());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setTemplate(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ingredientAcceptor.add(recipe.templateIngredient().get());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setBase(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ingredientAcceptor.add(recipe.baseIngredient());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setAddition(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ingredientAcceptor.add(recipe.additionIngredient().get());
    }

    @Override
    public <T extends IIngredientAcceptor<T>> void setOutput(StarcatcherRodRecipe recipe, T ingredientAcceptor)
    {
        ContextMap contextMap = ingredientAcceptor.getContextMap();
        Optional<Ingredient> templateIngredient = Optional.of(recipe.template);
        Optional<Ingredient> additionIngredient = Optional.of(recipe.material);

        List<ItemStack> templateStacks = templateIngredient.map(i -> i.display().resolveForStacks(contextMap)).orElse(List.of(ItemStack.EMPTY));
        if (templateStacks.isEmpty())
            templateStacks = List.of(ItemStack.EMPTY);

        ItemStack addition = additionIngredient.map(i -> i.display().resolveForFirstStack(contextMap)).orElse(ItemStack.EMPTY);

        for (ItemStack template : templateStacks)
        {
            SmithingRecipeInput recipeInput = new SmithingRecipeInput(template, SCItems.ROD.toStack(), addition);
            ItemStack output = recipe.assembledNoRegistries(recipeInput);
            ingredientAcceptor.add(output);
        }
    }

    @Override
    public void onDisplayedIngredientsUpdate(
            StarcatcherRodRecipe recipe,
            IRecipeSlotDrawable templateSlot,
            IRecipeSlotDrawable baseSlot,
            IRecipeSlotDrawable additionSlot,
            IRecipeSlotDrawable outputSlot,
            IFocusGroup focuses)
    {
        super.onDisplayedIngredientsUpdate(recipe, templateSlot, baseSlot, additionSlot, outputSlot, focuses);

        List<IFocus<?>> outputFocuses = focuses.getFocuses(RecipeIngredientRole.OUTPUT).toList();
        if (outputFocuses.isEmpty())
        {
            ItemStack template = templateSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
            ItemStack base = baseSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
            ItemStack addition = additionSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);

            SmithingRecipeInput recipeInput = new SmithingRecipeInput(template, base, addition);
            ItemStack output = recipe.assemble(recipeInput);
            IIngredientAcceptor<?> iIngredientAcceptor = outputSlot.createDisplayOverrides();
            iIngredientAcceptor.add(output);
        }
        else
        {
            ItemStack output = outputSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
            ItemStack base = new ItemStack(output.getItem());
            ItemStack template = templateSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
            ItemStack addition = additionSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);

            IIngredientAcceptor<?> iIngredientAcceptor1 = baseSlot.createDisplayOverrides();
            iIngredientAcceptor1.add(base);

            SmithingRecipeInput recipeInput = new SmithingRecipeInput(template, base, addition);
            output = recipe.assemble(recipeInput);
            IIngredientAcceptor<?> iIngredientAcceptor = outputSlot.createDisplayOverrides();
            iIngredientAcceptor.add(output);
        }
    }
}

