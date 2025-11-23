package com.wdiscute.starcatcher.emi;

import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.networkandcodecs.DataComponents;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class StarcatcherEmiRecipe implements EmiRecipe
{

    private final ResourceLocation id;
    private final List<EmiStack> output;
    private final FishProperties fp;
    private final TrophyProperties tp;
    private static final List<EmiIngredient> INPUT = List.of(
            EmiIngredient.of(Ingredient.of(ModItems.GUIDE.get())),
            EmiIngredient.of(Ingredient.of(ModItems.ROD.get())));

    public StarcatcherEmiRecipe(ResourceLocation id, FishProperties fp)
    {
        this.output = List.of(EmiStack.of(fp.fish().value()));
        this.id = id;
        this.fp = fp;
        this.tp = null;
    }

    public StarcatcherEmiRecipe(ResourceLocation id, TrophyProperties tp)
    {
        this.output = List.of(EmiStack.of(tp.fp().fish().value()));
        this.id = id;
        this.fp = tp.fp();
        this.tp = tp;
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return StarcatcherEmiPlugin.STARCATCHER_CATEGORY;
    }

    @Override
    public ResourceLocation getId()
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
        return INPUT;
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
        widgets.addSlot(INPUT.get(0), 5, 2);
        widgets.addSlot(INPUT.get(1), 23, 2);

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 45, 2);

        ItemStack is = new ItemStack(fp.fish());

        if (!this.fp.customName().equals(FishProperties.DEFAULT.customName()))
            is.setHoverName(Component.translatable(fp.customName()));

        if (this.tp != null)
        {
            if (!this.tp.customName().equals(TrophyProperties.DEFAULT.customName()) && tp.trophyType().equals(TrophyProperties.TrophyType.TROPHY))
                is.setHoverName(Component.translatable(tp.customName()));

            DataComponents.setTrophyProperties(is, this.tp);
        }

        widgets.addSlot(EmiIngredient.of(Ingredient.of(is)), 73, 2).recipeContext(this);
    }
}
