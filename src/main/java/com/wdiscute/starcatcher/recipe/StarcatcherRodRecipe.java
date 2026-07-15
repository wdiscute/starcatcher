package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record StarcatcherRodRecipe(Ingredient template, Ingredient rod, Ingredient material, ItemStack result, boolean addText, boolean keepStack, boolean applySkin) implements SmithingRecipe
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
        return assembledwad(input);
    }

    public ItemStack assembledwad(SmithingRecipeInput input)
    {
        ItemStack resultRod;

        if (keepStack)
            resultRod = input.base().copy();
        else
        {
            resultRod = result.copy();
            resultRod.applyComponents(input.base().getComponentsPatch());
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
            AbstractTackleSkin tackleSkin = SCDataMaps.getOrDefault(input.template(), SCDataMaps.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.get(Starcatcher.BASE));
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

    public static class Serializer implements RecipeSerializer<StarcatcherRodRecipe>
    {
        private static final MapCodec<StarcatcherRodRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Ingredient.CODEC.fieldOf("template").forGetter((o) -> o.template),
                Ingredient.CODEC.fieldOf("rod").forGetter((o) -> o.rod),
                Ingredient.CODEC.fieldOf("material").forGetter((o) -> o.material),
                ItemStack.CODEC.fieldOf("result").forGetter((o) -> o.result),
                Codec.BOOL.fieldOf("add_netherite_text").forGetter((o) -> o.addText),
                Codec.BOOL.fieldOf("keep_stack").forGetter((o) -> o.keepStack),
                Codec.BOOL.fieldOf("apply_tackle_skin").forGetter((o) -> o.applySkin)
        ).apply(instance, StarcatcherRodRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, StarcatcherRodRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<StarcatcherRodRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StarcatcherRodRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        private static StarcatcherRodRecipe fromNetwork(RegistryFriendlyByteBuf buffer)
        {
            Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            boolean netheriteUpgrade = ByteBufCodecs.BOOL.decode(buffer);
            boolean keepStack = ByteBufCodecs.BOOL.decode(buffer);
            boolean applySkin = ByteBufCodecs.BOOL.decode(buffer);
            return new StarcatcherRodRecipe(template, base, addition, result, netheriteUpgrade, keepStack, applySkin);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, StarcatcherRodRecipe recipe)
        {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.rod);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.material);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            ByteBufCodecs.BOOL.encode(buffer, recipe.addText);
            ByteBufCodecs.BOOL.encode(buffer, recipe.keepStack);
            ByteBufCodecs.BOOL.encode(buffer, recipe.applySkin);
        }
    }
}
