package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

public class BottledLetterRecipe extends NormalCraftingRecipe
{
    public static final MapCodec<BottledLetterRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
                            com.mojang.serialization.Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, ShapedRecipePattern.getMaxHeight() *
                                    ShapedRecipePattern.getMaxWidth())).fieldOf("ingredients").forGetter(o -> o.ingredients)
                    )
                    .apply(i, BottledLetterRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, BottledLetterRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            o -> o.bookInfo,
            ItemStackTemplate.STREAM_CODEC,
            o -> o.result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            o -> o.ingredients,
            BottledLetterRecipe::new
    );
    public static final RecipeSerializer<BottledLetterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients;
    private final boolean isSimple;

    public BottledLetterRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients)
    {
        super(commonInfo, bookInfo);
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public RecipeSerializer<BottledLetterRecipe> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo()
    {
        return PlacementInfo.create(this.ingredients);
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

    /**
     * {@return the result of this shapeless recipe or null if it is not static and needs ot be obtained by assembling it}
     */
    public @org.jspecify.annotations.Nullable ItemStackTemplate result()
    {
        return result;
    }

    public ItemStack assemble(CraftingInput input)
    {
        ItemStack itemStack = this.result.create();
        for (int i = 0; i < input.size(); i++)
        {
            if(SCDataComponents.has(input.getItem(i), SCDataComponents.MESSAGE))
            {
                SCDataComponents.set(itemStack, SCDataComponents.MESSAGE, SCDataComponents.get(input.getItem(i), SCDataComponents.MESSAGE));
                break;
            }
        }
        return itemStack;
    }

    @Override
    public List<RecipeDisplay> display()
    {
        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        this.ingredients.stream().map(Ingredient::display).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }
}
