package com.wdiscute.starcatcher.compat.jei;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipe;
import com.wdiscute.starcatcher.registry.SCItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import org.jspecify.annotations.Nullable;

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
                .add(rodIs)
                .setStandardSlotBackground()
        ;

        builder.addInputSlot(25, 2)
                .add(VanillaTypes.ITEM_STACK, recipe.template.getValues().get(0).value().getDefaultInstance())
                .setStandardSlotBackground()
        ;

        builder.addInputSlot(45, 2)
                .add(VanillaTypes.ITEM_STACK, recipe.material.getValues().get(0).value().getDefaultInstance())
                .setStandardSlotBackground()
        ;

        builder.addOutputSlot(85, 2)
                .add(recipe.result)
                .setStandardSlotBackground()
        ;
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY)
    {
        StarcatcherJeiFPRecipe.ARROW.render(guiGraphics, 65, 2);
    }

    @Override
    public @Nullable Identifier getIdentifier(Recipe recipe)
    {
        return Starcatcher.rl(BuiltInRegistries.ITEM.getKey(recipe.template.getValues().get(0).value()).getPath());
    }

    public record Recipe(Ingredient template, Ingredient material, ItemStack result)
    {
        //convert StarcatcherRodRecipe into Recipe
        public static Recipe of(StarcatcherRodRecipe recipe)
        {
            ItemStack stack = recipe.assembledNoRegistries(new SmithingRecipeInput(
                    recipe.template().getValues().get(0).value().getDefaultInstance(),
                    SCItems.ROD.toStack(),
                    recipe.material().getValues().get(0).value().getDefaultInstance()));

            return new Recipe(recipe.template(), recipe.material(), stack);
        }

        public static final IRecipeType<Recipe> TYPE = IRecipeType.create(Starcatcher.rl("smithing"), Recipe.class);
    }
}
