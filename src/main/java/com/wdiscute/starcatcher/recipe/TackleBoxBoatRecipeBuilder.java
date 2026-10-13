package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.wdiscute.starcatcher.registry.SCRecipes;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.nikdo53.neobackports.NeoBackports;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TackleBoxBoatRecipeBuilder implements RecipeBuilder
{
    private final RecipeCategory category;
    private String group;
    private final Item result;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public static TackleBoxBoatRecipeBuilder shapeless(ItemLike result, int count)
    {
        return new TackleBoxBoatRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, count));
    }

    public TackleBoxBoatRecipeBuilder(RecipeCategory p_250837_, ItemStack result)
    {
        this.category = p_250837_;
        this.result = result.getItem();
        this.group = "boat";
    }

    public TackleBoxBoatRecipeBuilder requires(Item item)
    {
        this.ingredients.add(Ingredient.of(item));
        return this;
    }

    public TackleBoxBoatRecipeBuilder requires(TagKey<Item> tag)
    {
        this.ingredients.add(Ingredient.of(tag));
        return this;
    }

    public TackleBoxBoatRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger)
    {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    public TackleBoxBoatRecipeBuilder group(@Nullable String groupName)
    {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult()
    {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location)
    {
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        recipeConsumer.accept(new Result(this.ingredients, this.result.getDefaultInstance(), location, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    public record Result(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation id,
                         Advancement.Builder advancement,
                         ResourceLocation advancementId) implements FinishedRecipe
    {
        public void serializeRecipeData(JsonObject main)
        {
            JsonElement encoded = TackleBoxBoatRecipe.Serializer.CODEC.codec()
                    .encodeStart(JsonOps.INSTANCE, new TackleBoxBoatRecipe(ingredients, result))
                    .getOrThrow(false, NeoBackports.LOGGER::error);

            encoded.getAsJsonObject().entrySet().forEach(entry -> main.add(entry.getKey(), entry.getValue()));
        }

        public ResourceLocation getId()
        {
            return this.id;
        }

        public RecipeSerializer<?> getType()
        {
            return SCRecipes.TACKLE_BOX_BOAT.get();
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
