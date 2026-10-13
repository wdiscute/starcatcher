package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.IncreaseChanceModifier;
import com.wdiscute.starcatcher.registry.SCRecipes;
import mezz.jei.forge.platform.RecipeHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.utils.recipe.CraftingRecipeNeo;
import net.nikdo53.neobackports.utils.recipe.RecipeSerializerNeo;
import net.nikdo53.neobackports.utils.recipe.holder.CraftingRecipeHolder;
import net.nikdo53.neobackports.utils.recipe.holder.RecipeHolder;
import net.nikdo53.neobackports.utils.recipe.input.CraftingInput;

import java.util.List;

public class TargetedBaitRecipe implements CraftingRecipeNeo
{
    final CraftingBookCategory category;
    final String group;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    final int extraChance;

    public TargetedBaitRecipe(NonNullList<Ingredient> ingredient, ItemStack result, int extraChance)
    {
        this.category = CraftingBookCategory.MISC;
        this.group = "bait";
        this.result = result;
        this.ingredients = ingredient;
        this.extraChance = extraChance;
    }

    public TargetedBaitRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredient, int extraChance)
    {
        this.category = category;
        this.group = group;
        this.result = result;
        this.ingredients = ingredient;
        this.extraChance = extraChance;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SCRecipes.TARGETED_BAIT.get();
    }

    @Override
    public String getGroup()
    {
        return group;
    }

    @Override
    public CraftingBookCategory category()
    {
        return CraftingBookCategory.MISC;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider)
    {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        NonNullList<Ingredient> list = NonNullList.create();

        list.addAll(ingredients);

        return list;
    }

    public boolean matches(CraftingInput input, Level level)
    {
        return input.size() == 2 && RecipeMatcher.findMatches(input.items(), this.ingredients) != null;
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries)
    {
        ItemStack result = this.result.copy();

        for (ItemStack item : input.items())
        {
            if (item.is(SCTags.HAS_TARGETED_BAIT))
            {
                ResourceLocation resourceLocation = registries.lookup(Starcatcher.FISH_REGISTRY_KEY)
                        .get()
                        .filterElements(o -> o.catchInfo().fish().toStack().is(item.getItem()))
                        .listElementIds().map(ResourceKey::location)
                        .findAny()
                        .orElse(Starcatcher.MISSINGNO);

                Modifier.addModifierToItem(result, new IncreaseChanceModifier(resourceLocation, extraChance, ""));
                break;
            }
        }

        return result;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= this.ingredients.size();
    }

    public static class Serializer implements RecipeSerializerNeo<TargetedBaitRecipe>
    {
        public static final MapCodec<TargetedBaitRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340779_ -> p_340779_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(p_301127_ -> p_301127_.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category),
                                ItemStack.CODEC.fieldOf("result").forGetter(p_301142_ -> p_301142_.result),
                                Ingredient.CODEC_NONEMPTY
                                        .listOf()
                                        .fieldOf("ingredients")
                                        .flatXmap(
                                                p_301021_ ->
                                                {
                                                    Ingredient[] aingredient = p_301021_.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
                                                    if (aingredient.length == 0)
                                                    {
                                                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                                                    }
                                                    else
                                                    {
                                                        return aingredient.length > 3 * 3
                                                                ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(3 * 3))
                                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                                    }
                                                },
                                                DataResult::success
                                        )
                                        .forGetter(p_300975_ -> p_300975_.ingredients),
                                Codec.INT.fieldOf("extra_chance").forGetter(o -> o.extraChance)
                        )
                        .apply(p_340779_, TargetedBaitRecipe::new)
        );

        public static final StreamCodec<TargetedBaitRecipe> STREAM_CODEC = StreamCodec.of(
                TargetedBaitRecipe.Serializer::toNetwork2, TargetedBaitRecipe.Serializer::fromNetwork
        );

        @Override
        public RecipeHolder<? extends Container, ? extends Recipe<? extends Container>> recipeHolderFactory(TargetedBaitRecipe targetedBaitRecipe, ResourceLocation resourceLocation)
        {
            return new CraftingRecipeHolder(targetedBaitRecipe, resourceLocation);
        }

        @Override
        public MapCodec<TargetedBaitRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec<TargetedBaitRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        private static TargetedBaitRecipe fromNetwork(FriendlyByteBuf buffer)
        {
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            int extraChance = buffer.readVarInt();
            return new TargetedBaitRecipe(s, craftingbookcategory, itemstack, nonnulllist, extraChance);
        }

        private static void toNetwork2(FriendlyByteBuf buffer, TargetedBaitRecipe recipe)
        {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients)
            {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeVarInt(recipe.extraChance);
        }
    }
}
