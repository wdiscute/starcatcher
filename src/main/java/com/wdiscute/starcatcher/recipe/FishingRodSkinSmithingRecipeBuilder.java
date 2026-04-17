package com.wdiscute.starcatcher.recipe;

import com.wdiscute.starcatcher.registry.SCRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.nikdo53.neobackports.utils.recipe.RecipeOutput;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FishingRodSkinSmithingRecipeBuilder
{
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final Item result;
    private final Map<String, InventoryChangeTrigger.TriggerInstance> criteria = new LinkedHashMap<>();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type = SCRecipes.FISHING_ROD_SKIN_SMITHING.get();

    public FishingRodSkinSmithingRecipeBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result)
    {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static FishingRodSkinSmithingRecipeBuilder smithing(
            Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result
    )
    {
        return new FishingRodSkinSmithingRecipeBuilder(template, base, addition, category, result);
    }

    public FishingRodSkinSmithingRecipeBuilder unlocks(String key, InventoryChangeTrigger.TriggerInstance criterion)
    {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, String location) {
        this.save(recipeConsumer, new ResourceLocation(location));
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location) {
        this.ensureValid(location);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        recipeConsumer.accept(new SmithingTransformRecipeBuilder.Result(location, this.type, this.template, this.base, this.addition, this.result, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }


    private void ensureValid(ResourceLocation location)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }
}