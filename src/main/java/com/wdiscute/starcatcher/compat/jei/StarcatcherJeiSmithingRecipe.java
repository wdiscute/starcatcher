package com.wdiscute.starcatcher.compat.jei;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipe;
import com.wdiscute.starcatcher.registry.SCItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

public class StarcatcherJeiSmithingRecipe extends AbstractRecipeCategory<StarcatcherJeiSmithingRecipe.Recipe>
{
    public ItemStack rodIs;

    public StarcatcherJeiSmithingRecipe(IGuiHelper guiHelper)
    {
        super(
                Recipe.TYPE,
                Component.translatable("emi.category.starcatcher.smithing"),
                guiHelper.createDrawableItemLike(Items.SMITHING_TABLE),
                98 + 16,
                20
        );
        rodIs = SCItems.ROD.get().getDefaultInstance();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses)
    {
        builder.addInputSlot(5, 2)
                .addItemStack(rodIs)
                .setStandardSlotBackground()
        ;

        builder.addInputSlot(25, 2)
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.template.getItems()[0])
                .setStandardSlotBackground()
        ;

        builder.addInputSlot(45, 2)
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.material.getItems()[0])
                .setStandardSlotBackground()
        ;

        builder.addOutputSlot(85, 2)
                .addItemStack(recipe.result)
                .setStandardSlotBackground()
        ;
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        StarcatcherJeiFPRecipe.ARROW.render(guiGraphics, 65, 2);
    }

    @Override
    public ResourceLocation getRegistryName(Recipe recipe)
    {
        return Starcatcher.rl(BuiltInRegistries.ITEM.getKey(recipe.template.getItems()[0].getItem()).getPath());
    }

    public record Recipe(Ingredient template, Ingredient material, ItemStack result)
    {
        //convert StarcatcherRodRecipe into Recipe
        public static Recipe of(StarcatcherRodRecipe recipe)
        {
            ItemStack stack = recipe.assembledNoRegistries(new SmithingRecipeInput(
                    recipe.template().getItems()[0],
                    SCItems.ROD.toStack(),
                    recipe.material().getItems()[0]));

            return new Recipe(recipe.template(), recipe.material(), stack);
        }

        public static final RecipeType<Recipe> TYPE = new RecipeType<>(Starcatcher.rl("smithing"), Recipe.class);
    }
}
