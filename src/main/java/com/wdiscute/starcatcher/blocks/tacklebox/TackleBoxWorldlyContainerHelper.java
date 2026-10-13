package com.wdiscute.starcatcher.blocks.tacklebox;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface TackleBoxWorldlyContainerHelper extends WorldlyContainer
{
    TackleBoxContainer getTackleBoxContainer();

    @Override
    default int[] getSlotsForFace(Direction side)
    {
        return getTackleBoxContainer().getSlotsForFace(side);
    }

    @Override
    default boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction)
    {
        return getTackleBoxContainer().canPlaceItemThroughFace(index, itemStack, direction);
    }

    @Override
    default boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return getTackleBoxContainer().canTakeItemThroughFace(index, stack, direction);
    }

    @Override
    default boolean isEmpty()
    {
        return getTackleBoxContainer().isEmpty();
    }

    @Override
    default void clearContent()
    {
        getTackleBoxContainer().clearContent();
    }

    @Override
    default ItemStack getItem(int slot)
    {
        return getTackleBoxContainer().getItem(slot);
    }

    @Override
    default ItemStack removeItem(int slot, int amount)
    {
        return getTackleBoxContainer().removeItem(slot, amount);
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot)
    {
        return getTackleBoxContainer().removeItemNoUpdate(slot);
    }

    @Override
    default void setItem(int slot, ItemStack stack)
    {
        getTackleBoxContainer().setItem(slot, stack);
    }

    @Override
    default boolean stillValid(Player player)
    {
        return getTackleBoxContainer().stillValid(player);
    }

    @Override
    default int getContainerSize()
    {
        return getTackleBoxContainer().getContainerSize();
    }
}
