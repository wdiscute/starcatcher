package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCRecipes;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.BackportCodecs;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.utils.recipe.RecipeSerializerNeo;
import net.nikdo53.neobackports.utils.recipe.SmithingRecipeNeo;
import net.nikdo53.neobackports.utils.recipe.holder.RecipeHolder;
import net.nikdo53.neobackports.utils.recipe.holder.SmithingRecipeHolder;
import net.nikdo53.neobackports.utils.recipe.input.SmithingRecipeInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record NetheriteUpgradeSmithingRecipe(Ingredient template, Ingredient base, Ingredient addition) implements SmithingRecipeNeo
{
    public boolean matches(SmithingRecipeInput input, Level level)
    {
        boolean b = this.template.test(input.template())
                && this.base.test(input.base())
                && this.addition.test(input.addition())
                && !SCDataComponents.getOrDefault(input.base(), SCDataComponents.NETHERITE_UPGRADE, false);
        return b;
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries)
    {
        ItemStack resultRod = input.base().copy();

        List<ResourceLocation> catchModifiers = new ArrayList<>(SCDataComponents.getOrDefault(input.base(), SCDataComponents.CATCH_MODIFIERS, List.of()));
        catchModifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.CATCH_MODIFIERS, List.of()));
        catchModifiers.addAll(SCDataMaps.getOrDefault(input.template(), SCDataMaps.CATCH_MODIFIERS, List.of()));

        List<ResourceLocation> minigameModifiers = new ArrayList<>(SCDataComponents.getOrDefault(input.base(), SCDataComponents.MINIGAME_MODIFIERS, List.of()));
        minigameModifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.MINIGAME_MODIFIERS, List.of()));
        minigameModifiers.addAll(SCDataMaps.getOrDefault(input.template(), SCDataMaps.MINIGAME_MODIFIERS, List.of()));

        ResourceLocation tackleSkin = SCTackleSkins.getTackleSkin(input.template());
        if (!tackleSkin.equals(SCTackleSkins.BASE_TACKLE_SKIN))
            SCDataComponents.set(resultRod, SCDataComponents.TACKLE_SKIN, tackleSkin);

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

    public static class Serializer implements RecipeSerializerNeo<NetheriteUpgradeSmithingRecipe>
    {
        public static final MapCodec<NetheriteUpgradeSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                BackportCodecs.IngredientCodecs.CODEC.fieldOf("template").forGetter((o) -> o.template),
                BackportCodecs.IngredientCodecs.CODEC.fieldOf("base").forGetter((o) -> o.base),
                BackportCodecs.IngredientCodecs.CODEC.fieldOf("addition").forGetter((o) -> o.addition)
        ).apply(instance, NetheriteUpgradeSmithingRecipe::new));

        public static final StreamCodec<NetheriteUpgradeSmithingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork1, Serializer::fromNetwork
        );

        @Override
        public MapCodec<NetheriteUpgradeSmithingRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<NetheriteUpgradeSmithingRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        @Override
        public RecipeHolder<? extends Container, ? extends Recipe<Container>> recipeHolderFactory(NetheriteUpgradeSmithingRecipe netheriteUpgradeSmithingRecipe, ResourceLocation resourceLocation) {
            return new SmithingRecipeHolder(netheriteUpgradeSmithingRecipe, resourceLocation);
        }

        private static NetheriteUpgradeSmithingRecipe fromNetwork(FriendlyByteBuf buffer)
        {
            Ingredient template = ByteBufCodecs.INGREDIENT.decode(buffer);
            Ingredient base = ByteBufCodecs.INGREDIENT.decode(buffer);
            Ingredient addition = ByteBufCodecs.INGREDIENT.decode(buffer);
            return new NetheriteUpgradeSmithingRecipe(template, base, addition);
        }

        private static void toNetwork1(FriendlyByteBuf buffer, NetheriteUpgradeSmithingRecipe recipe)
        {
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.template);
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.base);
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.addition);
        }
    }
}
