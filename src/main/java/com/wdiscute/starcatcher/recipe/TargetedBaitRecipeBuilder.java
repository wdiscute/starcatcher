package com.wdiscute.starcatcher.recipe;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.wdiscute.starcatcher.registry.SCRecipes;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.nikdo53.neobackports.NeoBackports;

public class TargetedBaitRecipeBuilder
{
    private final RecipeCategory category;
    private final ItemStack result;
    private final int extraChance;
    private final ItemStack resultStack; // Neo: add stack result support
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Map<String, InventoryChangeTrigger.TriggerInstance> criteria = new LinkedHashMap<>();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public static TargetedBaitRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count, int extraChance)
    {
        return new TargetedBaitRecipeBuilder(category, new ItemStack(result, count), extraChance);
    }

    public TargetedBaitRecipeBuilder(RecipeCategory category, ItemStack result, int extraChance)
    {
        this.category = category;
        this.result = result;
        this.resultStack = result;
        this.extraChance = extraChance;
    }

    public TargetedBaitRecipeBuilder requires(Item item)
    {
        this.ingredients.add(Ingredient.of(item));
        return this;
    }

    public TargetedBaitRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
    {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    public TargetedBaitRecipeBuilder requires(TagKey<Item> tag)
    {
        this.ingredients.add(Ingredient.of(tag));
        return this;
    }

    public Item getResult()
    {
        return this.result.getItem();
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location)
    {
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        recipeConsumer.accept(new Result(this.ingredients, this.result, this.extraChance, location, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    public record Result(NonNullList<Ingredient> ingredients, ItemStack result, int chance, ResourceLocation id,
                         Advancement.Builder advancement,
                         ResourceLocation advancementId) implements FinishedRecipe
    {
        public void serializeRecipeData(JsonObject main)
        {
            JsonElement encoded = TargetedBaitRecipe.Serializer.CODEC.codec()
                    .encodeStart(JsonOps.INSTANCE, new TargetedBaitRecipe(ingredients, result, chance))
                    .getOrThrow(false, NeoBackports.LOGGER::error);

            encoded.getAsJsonObject().entrySet().forEach(entry -> main.add(entry.getKey(), entry.getValue()));
        }

        public ResourceLocation getId()
        {
            return this.id;
        }

        public RecipeSerializer<?> getType()
        {
            return SCRecipes.TARGETED_BAIT.get();
        }

        @Nullable
        public JsonObject serializeAdvancement()
        {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId()
        {
            return this.advancementId;
        }
    }
}
