package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.nikdo53.neobackports.extensions.IngredientExtension;
import net.nikdo53.neobackports.extensions.ItemStackBackportExtension;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.BackportCodecs;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.io.utils.NeoForgeStreamCodecs;
import net.nikdo53.neobackports.utils.recipe.CraftingRecipeNeo;
import net.nikdo53.neobackports.utils.recipe.RecipeSerializerNeo;
import net.nikdo53.neobackports.utils.recipe.holder.CraftingRecipeHolder;
import net.nikdo53.neobackports.utils.recipe.holder.RecipeHolder;
import net.nikdo53.neobackports.utils.recipe.input.CraftingInput;

public class BottledLetterRecipe implements CraftingRecipeNeo
{
    final String group;
    final CraftingBookCategory category;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public BottledLetterRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients)
    {
        this.group = group;
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SCRecipes.BOTTLED_LETTER.get();
    }

    @Override
    public String getGroup()
    {
        return this.group;
    }


    @Override
    public CraftingBookCategory category()
    {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries)
    {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return this.ingredients;
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
            return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
        }
        else
        {
            return input.size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.get(0).test(input.getItem(0))
                    : input.stackedContents().canCraft(this, null);
        }
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries)
    {
        ItemStack is = this.result.copy();
        for (int i = 0; i < input.size(); i++)
        {
            if(SCDataComponents.has(input.getItem(i), SCDataComponents.MESSAGE))
            {
                SCDataComponents.set(is, SCDataComponents.MESSAGE, SCDataComponents.get(input.getItem(i), SCDataComponents.MESSAGE));
                break;
            }
        }
        return is;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= this.ingredients.size();
    }

    public static class Serializer implements RecipeSerializerNeo<BottledLetterRecipe>
    {
        private static final MapCodec<BottledLetterRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340779_ -> p_340779_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(p_301127_ -> p_301127_.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category),
                                BackportCodecs.ITEM_STACK_RECIPE.fieldOf("result").forGetter(p_301142_ -> p_301142_.result),
                                IngredientExtension.CODEC_NONEMPTY
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
                                        .forGetter(p_300975_ -> p_300975_.ingredients)
                        )
                        .apply(p_340779_, BottledLetterRecipe::new)
        );

        public static final StreamCodec<BottledLetterRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING, t -> t.group,
                NeoForgeStreamCodecs.enumCodec(CraftingBookCategory.class), t -> t.category,
                ItemStackBackportExtension.STREAM_CODEC, t -> t.result,
                IngredientExtension.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).map(list -> new NonNullList<>(list, null), n -> n.list), t -> t.ingredients,
                BottledLetterRecipe::new
        );

        @Override
        public MapCodec<BottledLetterRecipe> codec()
        {
            return CODEC;
        }

        @Override
        public StreamCodec< BottledLetterRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }

        @Override
        public RecipeHolder<? extends Container, ? extends Recipe<? extends Container>> recipeHolderFactory(BottledLetterRecipe bottledLetterRecipe, ResourceLocation resourceLocation) {
            return new CraftingRecipeHolder(bottledLetterRecipe, resourceLocation);
        }
    }
}
