package com.wdiscute.starcatcher.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.nikdo53.neobackports.utils.recipe.RecipeOutput;

import java.util.LinkedHashMap;
import java.util.Map;

public class TackleSkinSmithingRecipeBuilder
{
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final Map<String, Criterion> criteria = new LinkedHashMap<>();

    public TackleSkinSmithingRecipeBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category)
    {
        this.category = category;
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public static TackleSkinSmithingRecipeBuilder smithing(
            Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category
    )
    {
        return new TackleSkinSmithingRecipeBuilder(template, base, addition, category);
    }

    public TackleSkinSmithingRecipeBuilder unlocks(String key, Criterion criterion)
    {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(RecipeOutput recipeOutput, String recipeId)
    {
        this.save(recipeOutput, ResourceLocation.tryParse(recipeId));
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation recipeId)
    {
        this.ensureValid(recipeId);
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        TackleSkinSmithingRecipe tackleSkinSmithingRecipe = new TackleSkinSmithingRecipe(this.template, this.base, this.addition);
        recipeOutput.accept(recipeId, tackleSkinSmithingRecipe, advancement$builder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation location)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }
}