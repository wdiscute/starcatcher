package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.IncreaseChanceModifier;
import com.wdiscute.starcatcher.registry.SCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class TargetedBaitRecipe extends NormalCraftingRecipe
{
    public static final MapCodec<TargetedBaitRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
                            Ingredient.CODEC.listOf(1, ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())
                                    .fieldOf("ingredients")
                                    .forGetter(o -> o.ingredients),
                            Codec.INT.fieldOf("extra_chance").forGetter(o -> o.extraChance),
                            RegistryOps.retrieveRegistryLookup(Starcatcher.FISH_REGISTRY_KEY).codec()
                                    .fieldOf("registry")
                                    .forGetter(o -> o.registryLookup)
                    )
                    .apply(i, TargetedBaitRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TargetedBaitRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            o -> o.bookInfo,
            ItemStackTemplate.STREAM_CODEC,
            o -> o.result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            o -> o.ingredients,
            ByteBufCodecs.INT,
            o -> o.extraChance,
            TargetedBaitRecipe::new
    );

    public static final RecipeSerializer<TargetedBaitRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients;
    private final boolean isSimple;
    private final int extraChance;
    HolderLookup.RegistryLookup<FishProperties> registryLookup;

    public TargetedBaitRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result,
                              List<Ingredient> ingredients, int extraChance, HolderLookup.RegistryLookup<FishProperties> registryLookup)
    {
        super(commonInfo, bookInfo);
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.extraChance = extraChance;
        this.registryLookup = registryLookup;
    }

    public TargetedBaitRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result,
                              List<Ingredient> ingredients, int extraChance)
    {
        super(commonInfo, bookInfo);
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.extraChance = extraChance;
        this.registryLookup = null;
    }

    @Override
    public RecipeSerializer<TargetedBaitRecipe> getSerializer()
    {
        return SERIALIZER;
    }

    public boolean matches(CraftingInput input, Level level)
    {
        if (input.ingredientCount() != this.ingredients.size())
        {
            return false;
        }
        else if (!isSimple)
        {
            var nonEmptyItems = new java.util.ArrayList<ItemStack>(input.ingredientCount());
            for (var item : input.items())
                if (!item.isEmpty())
                    nonEmptyItems.add(item);
            return net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
        }
        else
        {
            return input.size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.getFirst().test(input.getItem(0))
                    : input.stackedContents().canCraft(this, null);
        }
    }

    @Override
    public ItemStack assemble(CraftingInput input)
    {
        ItemStack result = this.result.create();

        if (registryLookup == null)
            return result;

        for (ItemStack item : input.items())
        {
            if (item.is(SCTags.HAS_TARGETED_BAIT))
            {

                Identifier resourceLocation = registryLookup
                        .filterElements(o -> o.catchInfo().fish().toStack().is(item.getItem()))
                        .listElementIds().map(ResourceKey::identifier)
                        .findAny()
                        .orElse(Starcatcher.MISSINGNO);

                Modifier.addModifierToItem(result, new IncreaseChanceModifier(resourceLocation, extraChance, ""));
                break;
            }
        }

        return result;
    }

    @Override
    protected PlacementInfo createPlacementInfo()
    {
        return PlacementInfo.create(this.ingredients);
    }
}
