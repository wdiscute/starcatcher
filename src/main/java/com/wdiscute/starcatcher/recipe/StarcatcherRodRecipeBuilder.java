package com.wdiscute.starcatcher.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCRecipes;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.nikdo53.neobackports.NeoBackports;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

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
    private final Map<String, InventoryChangeTrigger.TriggerInstance> criteria = new LinkedHashMap<>();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type = SCRecipes.FISHING_ROD_SMITHING.get();

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

    public static StarcatcherRodRecipeBuilder tackleSkin(Ingredient template, Ingredient material)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(SCTags.RODS), material, RecipeCategory.TOOLS, new MaybeStack(SCItems.MISSINGNO.asItem()),
                false, true, true);
    }

    public static StarcatcherRodRecipeBuilder netheriteUpgrade(Ingredient template, Ingredient material)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(SCTags.RODS), material, RecipeCategory.TOOLS, new MaybeStack(SCItems.MISSINGNO.asItem()),
                true, true, false);
    }

    public static StarcatcherRodRecipeBuilder rodSkin(Ingredient template, Ingredient material, ItemStack result)
    {
        return new StarcatcherRodRecipeBuilder(template, Ingredient.of(SCTags.RODS), material, RecipeCategory.TOOLS, new MaybeStack(result),
                false, false, true);
    }


    public StarcatcherRodRecipeBuilder unlocks(String key, InventoryChangeTrigger.TriggerInstance criterion)
    {
        this.criteria.put(key, criterion);
        return this;
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, String location)
    {
        this.save(recipeConsumer, new ResourceLocation(location));
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location)
    {
        this.ensureValid(location);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        recipeConsumer.accept(new Result(location, this.type, this.template, this.base, this.addition, this.result, addText, keepStack, applySkin, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }


    private void ensureValid(ResourceLocation location)
    {
        if (this.criteria.isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }

    public record Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient template, Ingredient base,
                         Ingredient addition, MaybeStack result,
                         boolean addText,
                         boolean keepStack,
                         boolean applySkin,
                         Advancement.Builder advancement,
                         ResourceLocation advancementId) implements FinishedRecipe
    {
        public void serializeRecipeData(JsonObject main)
        {
            JsonElement encoded = StarcatcherRodRecipe.Serializer.CODEC.codec()
                    .encodeStart(JsonOps.INSTANCE, new StarcatcherRodRecipe(template, base, addition, result.toStack(), addText, keepStack, applySkin))
                    .getOrThrow(false, NeoBackports.LOGGER::error);

            encoded.getAsJsonObject().entrySet().forEach(entry -> main.add(entry.getKey(), entry.getValue()));
        }

        public ResourceLocation getId()
        {
            return this.id;
        }

        public RecipeSerializer<?> getType()
        {
            return this.type;
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