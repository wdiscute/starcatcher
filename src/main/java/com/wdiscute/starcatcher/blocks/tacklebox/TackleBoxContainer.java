package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TackleBoxContainer implements WorldlyContainer
{
    public static final int ROD_SLOT = 0;
    public static final int BOBBER_SLOT = 1;
    public static final int BAIT_SLOT = 2;
    public static final int HOOK_SLOT = 3;
    public static final int FISH_SLOT = 4;

    public final Map<Integer, ItemStack> items;
    public final List<ItemStack> fishes;

    public TackleBoxContainer()
    {
        items = new HashMap<>();
        fishes = new ArrayList<>();
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction)
    {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return true;
    }

    @Override
    public int getContainerSize()
    {
        return 18;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : items.values())
            if (!itemstack.isEmpty())
                return false;

        for (ItemStack itemstack : fishes)
            if (!itemstack.isEmpty())
                return false;

        return true;
    }

    public void updateFishSlot()
    {
        //store & remove fish placed
        ItemStack itemInFishSlot = getItem(FISH_SLOT);

        //if empty fish slot, but there's fishes stored, refill
        if (itemInFishSlot.isEmpty() && !fishes.isEmpty())
        {
            setItem(FISH_SLOT, fishes.getFirst());
            fishes.removeFirst();
            itemInFishSlot = getItem(FISH_SLOT);
        }

        //cycle through the items in tackle box, if a fish is found, put it in the fish slot
        for (int i = 5; i < 19; i++)
        {
            if (getItem(i).is(SCTags.FISHABLE) && fishes.size() < SCConfig.MAX_TACKLE_BOX_FISH_STORAGE.get())
            {
                ItemStack fish = getItem(i);

                //if fish slot is not empty, move fish to fish stack
                if (!itemInFishSlot.isEmpty())
                    fishes.addFirst(itemInFishSlot);

                setItem(FISH_SLOT, fish);
                setItem(i, ItemStack.EMPTY);

                itemInFishSlot = fish;
            }
        }

        setChanged();
    }

    @Override
    public ItemStack getItem(int slot)
    {
        return items.getOrDefault(slot, ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItem(int index, int amount)
    {
        ItemStack stack = items.getOrDefault(index, ItemStack.EMPTY);

        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack result = stack.split(amount);

        if (!result.isEmpty())
            setChanged();

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        ItemStack itemStack = items.getOrDefault(slot, ItemStack.EMPTY);
        items.put(slot, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        items.put(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public void clearContent()
    {
        fishes.clear();
        items.clear();
    }
}
