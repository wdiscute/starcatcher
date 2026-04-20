package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TackleSkinSmithingRecipe extends SimpleSmithingRecipe
{
    public static final MapCodec<TackleSkinSmithingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            Ingredient.CODEC.fieldOf("template").forGetter(o -> o.template),
                            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition)
                    )
                    .apply(i, TackleSkinSmithingRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, TackleSkinSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.addition,
            TackleSkinSmithingRecipe::new
    );
    public static final RecipeSerializer<TackleSkinSmithingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;

    public TackleSkinSmithingRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition)
    {
        super(commonInfo);
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    public ItemStack assemble(SmithingRecipeInput input)
    {
        ItemStack resultRod = input.base().transmuteCopy(input.base().getItem(), input.base().getCount());
        resultRod.applyComponents(input.base().getComponentsPatch());

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
        return Optional.of(this.template);
    }

    @Override
    public Ingredient baseIngredient()
    {
        return this.base;
    }

    @Override
    public Optional<Ingredient> additionIngredient()
    {
        return Optional.of(this.addition);
    }

    @Override
    public RecipeSerializer<TackleSkinSmithingRecipe> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo()
    {
        return PlacementInfo.create(List.of(this.template, this.base, this.addition));
    }

    @Override
    public List<RecipeDisplay> display()
    {
        return List.of(
                new SmithingRecipeDisplay(
                        this.template.display(),
                        this.base.display(),
                        this.addition.display(),
                        this.base.display(),
                        new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)
                )
        );
    }
}
