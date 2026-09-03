package com.wdiscute.starcatcher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StarcatcherRodRecipe extends SimpleSmithingRecipe
{
    public StarcatcherRodRecipe(
            Ingredient template,
            Ingredient rod,
            Ingredient material,
            MaybeStack result,
            boolean addText,
            boolean keepStack,
            boolean applySkin)
    {
        super(new Recipe.CommonInfo(true));
        this.template = template;
        this.rod = rod;
        this.material = material;
        this.result = result;
        this.addText = addText;
        this.keepStack = keepStack;
        this.applySkin = applySkin;
    }

    public Ingredient template;
    public Ingredient rod;
    public Ingredient material;
    public MaybeStack result;
    public boolean addText;
    public boolean keepStack;
    public boolean applySkin;

    public static final StreamCodec<RegistryFriendlyByteBuf, StarcatcherRodRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.template,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.rod,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.material,
            MaybeStack.STREAM_CODEC,
            o -> o.result,
            ByteBufCodecs.BOOL,
            o -> o.addText,
            ByteBufCodecs.BOOL,
            o -> o.keepStack,
            ByteBufCodecs.BOOL,
            o -> o.applySkin,
            StarcatcherRodRecipe::new
    );

    public static final MapCodec<StarcatcherRodRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    Ingredient.CODEC.fieldOf("template").forGetter(o -> o.template),
                    Ingredient.CODEC.fieldOf("rod").forGetter(o -> o.rod),
                    Ingredient.CODEC.fieldOf("material").forGetter(o -> o.material),
                    MaybeStack.CODEC.fieldOf("result").forGetter(o -> o.result),
                    Codec.BOOL.fieldOf("add_text").forGetter(o -> o.addText),
                    Codec.BOOL.fieldOf("keep_stack").forGetter(o -> o.keepStack),
                    Codec.BOOL.fieldOf("apply_skin").forGetter(o -> o.applySkin)
            ).apply(i, StarcatcherRodRecipe::new)
    );

    public static final RecipeSerializer<StarcatcherRodRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public Optional<Ingredient> templateIngredient()
    {
        return Optional.of(template);
    }

    @Override
    public Ingredient baseIngredient()
    {
        return rod;
    }

    @Override
    public Optional<Ingredient> additionIngredient()
    {
        return Optional.of(material);
    }

    public boolean matches(SmithingRecipeInput input, Level level)
    {
        if (SCDataComponents.getOrDefault(input.base(), SCDataComponents.NETHERITE_UPGRADE, false) && addText)
            return false;

        return this.template.test(input.template())
               && this.rod.test(input.base())
               && this.material.test(input.addition());
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input)
    {
        return assembledNoRegistries(input);
    }

    @Override
    public String group()
    {
        return "";
    }

    public ItemStack assembledNoRegistries(SmithingRecipeInput input)
    {
        ItemStack resultRod;

        if (keepStack)
            resultRod = input.base().copy();
        else
        {
            resultRod = result.toStack();
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
            AbstractTackleSkin tackleSkin = SCDataMaps.getOrDefault(input.template(), SCDataMaps.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.getValue(Starcatcher.BASE));
            SCDataComponents.set(resultRod, SCDataComponents.TACKLE_SKIN, tackleSkin);
        }

        //set netherite upgrade
        if (addText)
            SCDataComponents.set(resultRod, SCDataComponents.NETHERITE_UPGRADE, true);

        return resultRod;
    }

    @Override
    public RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer()
    {
        return SCRecipes.FISHING_ROD_SMITHING.get();
    }

    @Override
    public RecipeType<SmithingRecipe> getType()
    {
        return RecipeType.SMITHING;
    }

    @Override
    public PlacementInfo placementInfo()
    {
        return PlacementInfo.createFromOptionals(List.of(templateIngredient(), Optional.of(rod), additionIngredient()));
    }

    @Override
    protected PlacementInfo createPlacementInfo()
    {
        return PlacementInfo.createFromOptionals(List.of(Optional.of(this.template), Optional.of(this.rod), Optional.of(this.material)));
    }
}
