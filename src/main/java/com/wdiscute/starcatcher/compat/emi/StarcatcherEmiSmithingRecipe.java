package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipe;
import com.wdiscute.starcatcher.registry.SCItems;
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
import net.minecraft.world.item.crafting.SmithingRecipeInput;

import java.util.List;

public class StarcatcherEmiSmithingRecipe implements EmiRecipe
{
    protected final EmiIngredient template;
    protected final EmiIngredient input;
    protected final EmiIngredient base;
    protected final EmiIngredient material;
    protected final EmiStack output;

    public StarcatcherEmiSmithingRecipe(StarcatcherRodRecipe recipe)
    {
        this.template = EmiIngredient.of(recipe.template());
        this.input = EmiIngredient.of(recipe.rod());
        this.base = EmiIngredient.of(recipe.rod());
        this.material = EmiIngredient.of(recipe.material());

        ItemStack result = recipe.assembledwad(
                new SmithingRecipeInput(
                        recipe.template().getItems()[0],
                        SCItems.ROD.toStack(),
                        recipe.material().getItems()[0])
        );

        this.output = EmiStack.of(result);
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return VanillaEmiRecipeCategories.SMITHING;
    }

    @Override
    public ResourceLocation getId()
    {
        return Starcatcher.rl("/" + BuiltInRegistries.ITEM.getKey(template.getEmiStacks().getFirst().getItemStack().getItem()).getPath());
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
        widgets.addSlot(material, 36, 0);
        widgets.addSlot(output, 94, 0).recipeContext(this);
    }
}
