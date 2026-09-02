package com.wdiscute.starcatcher.recipe;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;
import java.util.Map;

public class StarcatcherRodRecipeBuilder
{
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final boolean addText;
    private final boolean keepStack;
    private final boolean applySkin;
    private final RecipeCategory category;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public StarcatcherRodRecipeBuilder(
            Ingredient template, Ingredient rod, Ingredient material,
            RecipeCategory category, ItemStack result,
            boolean addText, boolean keepStack, boolean applySkin)
    {
        this.category = category;
        this.template = template;
        this.base = rod;
        this.addition = material;
        this.result = result;
        this.addText = addText;
        this.keepStack = keepStack;
        this.applySkin = applySkin;
    }

    public static StarcatcherRodRecipeBuilder tackleSkin(HolderLookup.Provider lookup, Ingredient template, Ingredient material)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(lookup.lookupOrThrow(Registries.ITEM).getOrThrow(SCTags.RODS)), material, RecipeCategory.TOOLS, SCItems.MISSINGNO.toStack(),
                false, true, true);
    }

    public static StarcatcherRodRecipeBuilder netheriteUpgrade(HolderLookup.Provider lookup, Ingredient template, Ingredient material)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(lookup.lookupOrThrow(Registries.ITEM).getOrThrow(SCTags.RODS)), material, RecipeCategory.TOOLS, SCItems.MISSINGNO.toStack(),
                true, true, false);
    }

    public static StarcatcherRodRecipeBuilder rodSkin(HolderLookup.Provider lookup, Ingredient template, Ingredient material, ItemStack result)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(lookup.lookupOrThrow(Registries.ITEM).getOrThrow(SCTags.RODS)), material, RecipeCategory.TOOLS, result,
                false, false, true);
    }

    public StarcatcherRodRecipeBuilder unlocks(String key, Criterion<?> criterion)
    {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(RecipeOutput recipeOutput, Identifier recipeId)
    {
        this.ensureValid(recipeId);
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        StarcatcherRodRecipe netheriteUpgradeSmithingRecipe = new StarcatcherRodRecipe(this.template, this.base, this.addition, result, addText, keepStack, applySkin);
        recipeOutput.accept(recipeId, netheriteUpgradeSmithingRecipe, advancement$builder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation location)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }
}