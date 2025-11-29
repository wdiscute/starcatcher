package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.TrophyProperties;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarcatcherEmiRecipe implements EmiRecipe
{

    private final ResourceLocation id;
    private final List<EmiStack> output;
    private final FishProperties fp;
    private final TrophyProperties tp;
    private List<EmiIngredient> input = List.of(
            EmiIngredient.of(Ingredient.of(ModItems.GUIDE)),
            EmiIngredient.of(Ingredient.of(ModItems.ROD)));
    private final ItemStack is;

    public StarcatcherEmiRecipe(ResourceLocation id, FishProperties fp)
    {
        this.output = List.of(EmiStack.of(fp.fish().value()));
        this.id = id;
        this.fp = fp;
        this.tp = null;
        this.is = new ItemStack(fp.fish());
        if (!this.fp.customName().equals(FishProperties.DEFAULT.customName()))
            is.set(DataComponents.ITEM_NAME, Component.translatable(fp.customName()));

    }

    public StarcatcherEmiRecipe(ResourceLocation id, TrophyProperties tp)
    {
        this.output = List.of(EmiStack.of(tp.fp().fish().value()));
        this.id = id;
        this.fp = tp.fp();
        this.tp = tp;

        this.is = new ItemStack(fp.fish());

        if (!this.tp.customName().equals(TrophyProperties.DEFAULT.customName()) && tp.trophyType().equals(TrophyProperties.TrophyType.TROPHY))
            is.set(DataComponents.ITEM_NAME, Component.translatable(tp.customName()));

        is.set(ModDataComponents.TROPHY, this.tp);

    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return StarcatcherEmiPlugin.STARCATCHER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId()
    {
        return Starcatcher.rl("/" + id.getPath());
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return List.of();
    }

    @Override
    public List<EmiIngredient> getCatalysts()
    {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        return output;
    }

    @Override
    public int getDisplayWidth()
    {
        return 100;
    }

    @Override
    public int getDisplayHeight()
    {
        return 22;
    }


    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        widgets.addSlot(input.get(0), 5, 2);
        widgets.addSlot(input.get(1), 23, 2);

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 45, 2);

        widgets.addSlot(EmiIngredient.of(Ingredient.of(is)), 73, 2).recipeContext(this);
    }
}
