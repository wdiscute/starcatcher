package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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

public record StarcatcherRodRecipe(Ingredient template, Ingredient rod, Ingredient material, ItemStack result, boolean addText, boolean keepStack, boolean applySkin) implements SmithingRecipeNeo
{
    public boolean matches(SmithingRecipeInput input, Level level)
    {
        if (SCDataComponents.getOrDefault(input.base(), SCDataComponents.NETHERITE_UPGRADE, false) && addText)
            return false;

        return this.template.test(input.template())
               && this.rod.test(input.base())
               && this.material.test(input.addition());
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries)
    {
        return assembledNoRegistries(input);
    }

    public ItemStack assembledNoRegistries(SmithingRecipeInput input)
    {
        ItemStack resultRod;

        if (keepStack)
            resultRod = input.base().copy();
        else
        {
            resultRod = result.copy();
            CompoundTag compoundTag = resultRod.serializeNBT();
            resultRod.deserializeNBT(compoundTag);
        }

        //get data components already in the rod
        List<Modifier> modifiers = new ArrayList<>((SCDataMaps.getOrDefault(resultRod, SCDataMaps.ITEM_MODIFIERS, List.of())));
        //add default template modifiers from DataMap
        modifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.MODIFIERS, List.of()));
        //add data component modifiers from template itemstack
        modifiers.addAll(SCDataMaps.getOrDefault(input.template(), SCDataMaps.ITEM_MODIFIERS, List.of()));
        //set modifiers
        SCDataComponents.set(resultRod, SCDataComponents.MODIFIERS, modifiers);

        //set tackle skin
        if (applySkin)
        {
            AbstractTackleSkin tackleSkin = SCDataMaps.getOrDefault(input.template(), SCDataMaps.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.getValue(Starcatcher.BASE));
            SCDataComponents.set(resultRod, SCDataComponents.TACKLE_SKIN, tackleSkin);
        }

        //set netherite upgrade
        if (addText)
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
        return this.rod.test(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack)
    {
        return this.material.test(stack);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries)
    {
        ItemStack itemstack = new ItemStack(SCItems.ROD.get());
        if (addText)
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
        return Stream.of(this.template, this.rod, this.material).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer implements RecipeSerializerNeo<StarcatcherRodRecipe>
    {
        public static final MapCodec<StarcatcherRodRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                BackportCodecs.IngredientCodecs.CODEC.fieldOf("template").forGetter((o) -> o.template),
                BackportCodecs.IngredientCodecs.CODEC.fieldOf("base").forGetter((o) -> o.rod),
                BackportCodecs.IngredientCodecs.CODEC.fieldOf("addition").forGetter((o) -> o.material),
                BackportCodecs.ITEM_STACK_RECIPE.fieldOf("result").forGetter(o -> o.result),
                Codec.BOOL.fieldOf("add_text").forGetter(o -> o.addText),
                Codec.BOOL.fieldOf("keep_stack").forGetter(o -> o.keepStack),
                Codec.BOOL.fieldOf("apply_skin").forGetter(o -> o.applySkin)
        ).apply(instance, StarcatcherRodRecipe::new));

        public static final StreamCodec<StarcatcherRodRecipe> STREAM_CODEC = StreamCodec.of(
                StarcatcherRodRecipe.Serializer::toNetworkA, StarcatcherRodRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<StarcatcherRodRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<StarcatcherRodRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        @Override
        public RecipeHolder<? extends Container, ? extends Recipe<Container>> recipeHolderFactory(StarcatcherRodRecipe fishingRodSkinSmithingRecipe, ResourceLocation resourceLocation) {
            return new SmithingRecipeHolder(fishingRodSkinSmithingRecipe, resourceLocation);
        }

        private static StarcatcherRodRecipe fromNetwork(FriendlyByteBuf buffer)
        {
            Ingredient template = ByteBufCodecs.INGREDIENT.decode(buffer);
            Ingredient base = ByteBufCodecs.INGREDIENT.decode(buffer);
            Ingredient addition = ByteBufCodecs.INGREDIENT.decode(buffer);
            ItemStack result = ByteBufCodecs.ITEM_STACK.decode(buffer);
            boolean add_text = ByteBufCodecs.BOOL.decode(buffer);
            boolean keep_stack = ByteBufCodecs.BOOL.decode(buffer);
            boolean apply_skin = ByteBufCodecs.BOOL.decode(buffer);
            return new StarcatcherRodRecipe(template, base, addition, result, add_text, keep_stack, apply_skin);
        }

        private static void toNetworkA(FriendlyByteBuf buffer, StarcatcherRodRecipe recipe)
        {
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.template);
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.rod);
            ByteBufCodecs.INGREDIENT.encode(buffer, recipe.material);
            ByteBufCodecs.ITEM_STACK.encode(buffer, recipe.result);
        }
    }
}
