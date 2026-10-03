package com.wdiscute.starcatcher.recipe;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class StarcatcherRodRecipeBuilder
{
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final MaybeStack result;
    private final boolean addText;
    private final boolean keepStack;
    private final boolean applySkin;
    private final RecipeCategory category;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();

    public StarcatcherRodRecipeBuilder(
            Ingredient template, Ingredient rod, Ingredient material,
            RecipeCategory category, MaybeStack result,
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

    public static StarcatcherRodRecipeBuilder tackleSkin(HolderGetter<Item> lookup, Ingredient template, Ingredient material)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(lookup.getOrThrow(SCTags.RODS)), material, RecipeCategory.TOOLS, new MaybeStack(SCItems.MISSINGNO),
                false, true, true);
    }

    public static StarcatcherRodRecipeBuilder netheriteUpgrade(HolderGetter<Item> lookup, Ingredient template, Ingredient material)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(lookup.getOrThrow(SCTags.RODS)), material, RecipeCategory.TOOLS, new MaybeStack(SCItems.MISSINGNO),
                true, true, false);
    }

    public static StarcatcherRodRecipeBuilder rodSkin(HolderGetter<Item> lookup, Ingredient template, Ingredient material, MaybeStack result)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(lookup.getOrThrow(SCTags.RODS)), material, RecipeCategory.TOOLS, result,
                false, false, true);
    }

    public StarcatcherRodRecipeBuilder unlocks(String key, Criterion<?> criterion)
    {
        this.advancementBuilder.unlockedBy(key, criterion);
        return this;
    }

    public void save(RecipeOutput recipeOutput, Identifier recipeId)
    {
        ResourceKey<Recipe<?>> recipeResourceKey = ResourceKey.create(Registries.RECIPE, recipeId);

        StarcatcherRodRecipe netheriteUpgradeSmithingRecipe = new StarcatcherRodRecipe(this.template, this.base, this.addition, result, addText, keepStack, applySkin);
        recipeOutput.accept(recipeResourceKey, netheriteUpgradeSmithingRecipe, advancementBuilder.build(recipeOutput, recipeResourceKey, this.category));
    }
}