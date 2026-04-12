package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.io.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record NetheriteUpgradeSmithingRecipe(Ingredient template, Ingredient base, Ingredient addition) implements SmithingRecipe
{
    public boolean matches(SmithingRecipeInput input, Level level)
    {
        return this.template.test(input.template())
                && this.base.test(input.base())
                && this.addition.test(input.addition())
                && !SCDataComponents.getOrDefault(input.base(), SCDataComponents.NETHERITE_UPGRADE, false);
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries)
    {
        ItemStack resultRod = input.base().copy();

        List<ResourceLocation> catchModifiers = new ArrayList<>(SCDataComponents.getOrDefault(input.base(), SCDataComponents.MINIGAME_MODIFIERS, List.of()));
        catchModifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.MINIGAME_MODIFIERS, List.of()));

        List<ResourceLocation> minigameModifiers = new ArrayList<>(SCDataComponents.getOrDefault(input.base(), SCDataComponents.CATCH_MODIFIERS, List.of()));
        minigameModifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.CATCH_MODIFIERS, List.of()));

        SCDataComponents.set(resultRod, SCDataComponents.MINIGAME_MODIFIERS, minigameModifiers);
        SCDataComponents.set(resultRod, SCDataComponents.CATCH_MODIFIERS, catchModifiers);
        SCDataComponents.set(resultRod, SCDataComponents.NETHERITE_UPGRADE, true);
        return resultRod;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack)
    {
        return this.template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack)
    {
        return this.base.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack)
    {
        return this.addition.test(stack);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries)
    {
        ItemStack itemstack = new ItemStack(SCItems.ROD.get());
        SCDataComponents.set(itemstack, SCDataComponents.NETHERITE_UPGRADE, true);
        return itemstack;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SCRecipes.FISHING_ROD_SMITHING.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return RecipeType.SMITHING;
    }

    @Override
    public boolean isIncomplete()
    {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer implements RecipeSerializer<NetheriteUpgradeSmithingRecipe>
    {
        private static final MapCodec<NetheriteUpgradeSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Ingredient.CODEC.fieldOf("template").forGetter((o) -> o.template),
                Ingredient.CODEC.fieldOf("base").forGetter((o) -> o.base),
                Ingredient.CODEC.fieldOf("addition").forGetter((o) -> o.addition)
        ).apply(instance, NetheriteUpgradeSmithingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, NetheriteUpgradeSmithingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<NetheriteUpgradeSmithingRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NetheriteUpgradeSmithingRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        private static NetheriteUpgradeSmithingRecipe fromNetwork(RegistryFriendlyByteBuf buffer)
        {
            Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            return new NetheriteUpgradeSmithingRecipe(template, base, addition);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, NetheriteUpgradeSmithingRecipe recipe)
        {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition);
        }
    }
}
