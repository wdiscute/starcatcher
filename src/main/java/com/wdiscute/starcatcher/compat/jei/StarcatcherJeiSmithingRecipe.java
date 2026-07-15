package com.wdiscute.starcatcher.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.sellingbin.jei.SellingBinJeiPlugin;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.recipe.FishingRodSkinSmithingRecipe;
import com.wdiscute.starcatcher.recipe.NetheriteUpgradeSmithingRecipe;
import com.wdiscute.starcatcher.recipe.TackleSkinSmithingRecipe;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.Treasure;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.ItemStackTagFix;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StarcatcherJeiSmithingRecipe extends AbstractRecipeCategory<StarcatcherJeiSmithingRecipe.Recipe>
{
    private static final ResourceLocation ICON = Starcatcher.rl("textures/gui/emi/emi_guide_icon.png");
    public ItemStack rodIs;

    public StarcatcherJeiSmithingRecipe(IGuiHelper guiHelper)
    {
        super(
                Recipe.TYPE,
                Component.translatable("jei.category.starcatcher.smithing"),
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
        ;

        builder.addInputSlot(25, 2)
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.template.getItems()[0])
        ;

        builder.addInputSlot(45, 2)
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.material.getItems()[0])
        ;

        builder.addOutputSlot(85, 2)
                .addItemStack(recipe.result)
        ;

    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        guiGraphics.blit(StarcatcherJeiPlugin.SLOT_BACKGROUND, 4, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 24, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 44, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 84, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(StarcatcherJeiPlugin.ARROW, 65, 2, 16, 16, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public ResourceLocation getRegistryName(Recipe recipe)
    {
        return Starcatcher.rl(BuiltInRegistries.ITEM.getKey(recipe.template.getItems()[0].getItem()).getPath());
    }

    public record Recipe(Ingredient template, Ingredient material, ItemStack result)
    {
        //netherite upgrade
        public static Recipe of(NetheriteUpgradeSmithingRecipe recipe)
        {
            ItemStack stack = SCItems.ROD.toStack();

            SCDataComponents.set(stack, SCDataComponents.NETHERITE_UPGRADE, true);

            return new Recipe(recipe.template(), recipe.addition(), stack);
        }

        //tackle skin
        public static Recipe of(TackleSkinSmithingRecipe recipe)
        {
            ItemStack stack = SCItems.ROD.toStack();
            SCDataComponents.set(stack, SCDataComponents.TACKLE_SKIN, SCTackleSkins.getTackleSkin(recipe.template().getItems()[0]));

            return new Recipe(recipe.template(), recipe.addition(), stack);
        }

        //rod skin
        public static Recipe of(FishingRodSkinSmithingRecipe recipe)
        {
            return new Recipe(recipe.template, recipe.addition, recipe.result);
        }

        public static final RecipeType<Recipe> TYPE = new RecipeType<>(Starcatcher.rl("smithing"), Recipe.class);
    }
}
