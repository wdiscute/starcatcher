package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.recipe.FishingRodSkinSmithingRecipe;
import com.wdiscute.starcatcher.recipe.NetheriteUpgradeSmithingRecipe;
import com.wdiscute.starcatcher.recipe.TackleSkinSmithingRecipe;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class StarcatcherEmiSmithingRecipe implements EmiRecipe
{
    protected final EmiIngredient template;
    protected final EmiIngredient input;
    protected final EmiIngredient base;
    protected final EmiIngredient addition;
    protected final EmiStack output;

    public StarcatcherEmiSmithingRecipe(NetheriteUpgradeSmithingRecipe recipe)
    {
        this.template = EmiIngredient.of(recipe.template());
        this.input = EmiIngredient.of(recipe.base());
        this.base = EmiIngredient.of(recipe.base());
        this.addition = EmiIngredient.of(recipe.addition());

        ItemStack stack = recipe.base().getItems()[0].copy();
        SCDataComponents.set(stack, SCDataComponents.NETHERITE_UPGRADE, true);

        ItemStack template = new ItemStack(recipe.template().getItems()[0].getItem());

        List<ResourceLocation> minigameRLs = SCDataMaps.getOrDefault(template, SCDataMaps.MINIGAME_MODIFIERS, List.of());
        List<ResourceLocation> catchRLs = SCDataMaps.getOrDefault(template, SCDataMaps.CATCH_MODIFIERS, List.of());

        SCDataComponents.set(stack, SCDataComponents.MINIGAME_MODIFIERS, minigameRLs);
        SCDataComponents.set(stack, SCDataComponents.CATCH_MODIFIERS, catchRLs);

        this.output = EmiStack.of(stack);
    }

    public StarcatcherEmiSmithingRecipe(TackleSkinSmithingRecipe recipe)
    {
        this.template = EmiIngredient.of(recipe.template());
        this.input = EmiIngredient.of(recipe.base());
        this.base = EmiIngredient.of(recipe.base());
        this.addition = EmiIngredient.of(recipe.addition());

        ItemStack resultRod = recipe.base().getItems()[0].copy();

        ItemStack template = new ItemStack(recipe.template().getItems()[0].getItem());

        List<ResourceLocation> minigameRLs = SCDataMaps.getOrDefault(template, SCDataMaps.MINIGAME_MODIFIERS, List.of());
        List<ResourceLocation> catchRLs = SCDataMaps.getOrDefault(template, SCDataMaps.CATCH_MODIFIERS, List.of());

        ResourceLocation tackleSkin = SCDataComponents.get(template, SCDataComponents.TACKLE_SKIN);

        if (tackleSkin != null)
            SCDataComponents.set(resultRod, SCDataComponents.TACKLE_SKIN, tackleSkin);

        SCDataComponents.set(resultRod, SCDataComponents.MINIGAME_MODIFIERS, minigameRLs);
        SCDataComponents.set(resultRod, SCDataComponents.CATCH_MODIFIERS, catchRLs);

        this.output = EmiStack.of(resultRod);
    }

    public StarcatcherEmiSmithingRecipe(FishingRodSkinSmithingRecipe recipe)
    {
        this.template = EmiIngredient.of(recipe.template);
        this.input = EmiIngredient.of(recipe.base);
        this.base = EmiIngredient.of(recipe.base);
        this.addition = EmiIngredient.of(recipe.addition);
        this.output = EmiStack.of(recipe.result.copy());
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return VanillaEmiRecipeCategories.SMITHING;
    }

    @Override
    public ResourceLocation getId()
    {
        return Starcatcher.rl("/" + BuiltInRegistries.ITEM.getKey(template.getEmiStacks().get(0).getItemStack().getItem()).getPath());
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return List.of(template, input);
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth()
    {
        return 112;
    }

    @Override
    public int getDisplayHeight()
    {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
        widgets.addSlot(template, 0, 0);
        widgets.addSlot(input, 18, 0);
        widgets.addSlot(addition, 36, 0);
        widgets.addSlot(output, 94, 0).recipeContext(this);
    }
}
