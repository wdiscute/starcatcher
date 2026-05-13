package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonObject;
import com.wdiscute.starcatcher.registry.SCRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TackleSkinSmithingRecipeBuilder
{
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final Map<String, InventoryChangeTrigger.TriggerInstance> criteria = new LinkedHashMap<>();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type = SCRecipes.TACKLE_SKIN_SMITHING.get();

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

    public TackleSkinSmithingRecipeBuilder unlocks(String key, InventoryChangeTrigger.TriggerInstance criterion)
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
        recipeConsumer.accept(new Result(location, this.type, this.template, this.base, this.addition, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }


    private void ensureValid(ResourceLocation location)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }

    public static record Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient template, Ingredient base, Ingredient addition, Advancement.Builder advancement, ResourceLocation advancementId) implements FinishedRecipe {
        public void serializeRecipeData(JsonObject p_266713_) {
            p_266713_.add("template", this.template.toJson());
            p_266713_.add("base", this.base.toJson());
            p_266713_.add("addition", this.addition.toJson());
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}