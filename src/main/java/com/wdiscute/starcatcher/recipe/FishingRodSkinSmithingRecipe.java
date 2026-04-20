package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishingRodSkinSmithingRecipe extends SimpleSmithingRecipe
{
    public static final MapCodec<FishingRodSkinSmithingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            Ingredient.CODEC.optionalFieldOf("template").forGetter(o -> o.template),
                            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
                            Ingredient.CODEC.optionalFieldOf("addition").forGetter(o -> o.addition),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
                    )
                    .apply(i, FishingRodSkinSmithingRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FishingRodSkinSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
            o -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.base,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
            o -> o.addition,
            ItemStackTemplate.STREAM_CODEC,
            o -> o.result,
            FishingRodSkinSmithingRecipe::new
    );
    public static final RecipeSerializer<FishingRodSkinSmithingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final Optional<Ingredient> template;
    private final Ingredient base;
    private final Optional<Ingredient> addition;
    private final ItemStackTemplate result;

    public FishingRodSkinSmithingRecipe(
            Recipe.CommonInfo commonInfo, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, ItemStackTemplate result
    )
    {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public ItemStack assemble(SmithingRecipeInput input)
    {
        ItemStack resultRod = input.base().transmuteCopy(this.result.item().value(), this.result.count());
        resultRod.applyComponents(result.components());

        List<Identifier> catchModifiers = new ArrayList<>(SCDataComponents.getOrDefault(input.base(), SCDataComponents.CATCH_MODIFIERS, List.of()));
        catchModifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.CATCH_MODIFIERS, List.of()));
        catchModifiers.addAll(SCDataMaps.getOrDefault(input.template(), SCDataMaps.CATCH_MODIFIERS, List.of()));

        List<Identifier> minigameModifiers = new ArrayList<>(SCDataComponents.getOrDefault(input.base(), SCDataComponents.MINIGAME_MODIFIERS, List.of()));
        minigameModifiers.addAll(SCDataComponents.getOrDefault(input.template(), SCDataComponents.MINIGAME_MODIFIERS, List.of()));
        minigameModifiers.addAll(SCDataMaps.getOrDefault(input.template(), SCDataMaps.MINIGAME_MODIFIERS, List.of()));

        Identifier tackleSkin = SCTackleSkins.getTackleSkin(input.template());
        if (!tackleSkin.equals(SCTackleSkins.BASE_TACKLE_SKIN))
            SCDataComponents.set(resultRod, SCDataComponents.TACKLE_SKIN, tackleSkin);

        SCDataComponents.set(resultRod, SCDataComponents.MINIGAME_MODIFIERS, minigameModifiers);
        SCDataComponents.set(resultRod, SCDataComponents.CATCH_MODIFIERS, catchModifiers);
        return resultRod;
    }

    @Override
    public Optional<Ingredient> templateIngredient()
    {
        return this.template;
    }

    @Override
    public Ingredient baseIngredient()
    {
        return this.base;
    }

    @Override
    public Optional<Ingredient> additionIngredient()
    {
        return this.addition;
    }

    @Override
    public RecipeSerializer<FishingRodSkinSmithingRecipe> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo()
    {
        return PlacementInfo.createFromOptionals(List.of(this.template, Optional.of(this.base), this.addition));
    }

    @Override
    public List<RecipeDisplay> display()
    {
        return List.of(
                new SmithingRecipeDisplay(
                        Ingredient.optionalIngredientToDisplay(this.template),
                        this.base.display(),
                        Ingredient.optionalIngredientToDisplay(this.addition),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)
                )
        );
    }
}
