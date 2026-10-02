package com.wdiscute.starcatcher.recipe;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TackleBoxBoatRecipeBuilder
{
    private final List<Ingredient> ingredients;
    private final MaybeStack result;
    private final Map<String, net.minecraft.advancements.triggers.Criterion<?>> criteria = new LinkedHashMap<>();

    public TackleBoxBoatRecipeBuilder(MaybeStack result)
    {
        this.ingredients = new ArrayList<>();
        this.result = result;
    }

    public static TackleBoxBoatRecipeBuilder shapeless(RecipeCategory cat, MaybeStack result)
    {
        return new TackleBoxBoatRecipeBuilder(result);
    }

    public static TackleBoxBoatRecipeBuilder shapeless(RecipeCategory cat, ItemLike result)
    {
        return new TackleBoxBoatRecipeBuilder(new MaybeStack(result.asItem()));
    }

    public TackleBoxBoatRecipeBuilder requires(Item item)
    {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public TackleBoxBoatRecipeBuilder requires(HolderLookup.RegistryLookup<Item> reg, TagKey<Item> tag)
    {
        ingredients.add(Ingredient.of(reg.getOrThrow(tag)));
        return this;
    }


    public TackleBoxBoatRecipeBuilder unlockedBy(String key, net.minecraft.advancements.triggers.Criterion<?> criterion)
    {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(RecipeOutput recipeOutput)
    {
        save(recipeOutput, result.identifier());
    }

    public void save(RecipeOutput recipeOutput, Identifier recipeId)
    {
        ResourceKey<Recipe<?>> recipeResourceKey = ResourceKey.create(Registries.RECIPE, recipeId);

        this.ensureValid(recipeId);
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", net.minecraft.advancements.triggers.RecipeUnlockedTrigger.unlocked(recipeResourceKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeResourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);

        TackleBoxBoatRecipe netheriteUpgradeSmithingRecipe = new TackleBoxBoatRecipe(
                new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "boat"), new ItemStackTemplate(result.toItem(), result.count(), result.patch()), ingredients);

        recipeOutput.accept(recipeResourceKey, netheriteUpgradeSmithingRecipe, advancement$builder.build(recipeId.withPrefix("recipes/" + RecipeCategory.TRANSPORTATION.getFolderName() + "/")));
    }

    private void ensureValid(Identifier location)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }
}
