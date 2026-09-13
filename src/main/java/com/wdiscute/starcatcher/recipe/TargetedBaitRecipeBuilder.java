package com.wdiscute.starcatcher.recipe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;

public class TargetedBaitRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private final Item result;
    private final ItemStack resultStack; // Neo: add stack result support
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    private int extraChance;

    public static TargetedBaitRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count, int extraChance)
    {
        return new TargetedBaitRecipeBuilder(category, new ItemStack(result, count), extraChance);
    }

    public TargetedBaitRecipeBuilder(RecipeCategory p_250837_, ItemStack result, int extraChance)
    {
        this.category = p_250837_;
        this.result = result.getItem();
        this.resultStack = result;
        this.extraChance = extraChance;
    }

    public TargetedBaitRecipeBuilder requires(Item item)
    {
        this.ingredients.add(Ingredient.of(item));
        return this;
    }

    public TargetedBaitRecipeBuilder requires(TagKey<Item> tag)
    {
        this.ingredients.add(Ingredient.of(tag));
        return this;
    }

    public TargetedBaitRecipeBuilder unlockedBy(String name, Criterion<?> criterion)
    {
        this.criteria.put(name, criterion);
        return this;
    }

    public TargetedBaitRecipeBuilder group(@Nullable String groupName)
    {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id)
    {
        this.ensureValid(id);
        Advancement.Builder advancement$builder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement$builder::addCriterion);

        TargetedBaitRecipe recipe = new TargetedBaitRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                this.resultStack,
                this.ingredients,
                extraChance
        );
        recipeOutput.accept(id, recipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation id)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }
}
