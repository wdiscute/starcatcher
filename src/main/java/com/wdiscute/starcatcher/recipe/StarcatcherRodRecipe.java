package com.wdiscute.starcatcher.recipe;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record StarcatcherRodRecipe(Ingredient template,
                                   Ingredient rod,
                                   Ingredient material,
                                   ItemStack result,
                                   boolean addText,
                                   boolean keepStack,
                                   boolean applySkin)
        implements SmithingRecipe
{
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
    public boolean showNotification()
    {
        return true;
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
            AbstractTackleSkin tackleSkin = SCDataMaps.getOrDefault(input.template(), SCDataMaps.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.getValue(Starcatcher.BASE));
            SCDataComponents.set(resultRod, SCDataComponents.TACKLE_SKIN, tackleSkin);
        }

        //set netherite upgrade
        if (addText)
            SCDataComponents.set(resultRod, SCDataComponents.NETHERITE_UPGRADE, true);

        return resultRod;
    }

    @Override
    public ItemStack result()
    {
        ItemStack itemstack = new ItemStack(SCItems.ROD.get());
        if (addText)
            SCDataComponents.set(itemstack, SCDataComponents.NETHERITE_UPGRADE, true);
        return itemstack;
    }

    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer()
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
}
