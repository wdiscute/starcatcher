package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCMenuTypes;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TackleBoxMenu extends AbstractContainerMenu
{
    private final TackleBoxContainer container;

    public TackleBoxMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData)
    {
        this(containerId, playerInventory, new TackleBoxContainer()
        {
            @Override
            public void setChanged()
            {

            }

            @Override
            public boolean stillValid(Player player)
            {
                return true;
            }
        });
    }

    public TackleBoxMenu(int containerId, Inventory playerInventory, TackleBoxContainer container)
    {
        super(SCMenuTypes.TACKLE_BOX.get(), containerId);
        this.container = container;
        container.startOpen(playerInventory.player);


        this.addSlot(new TackleBoxRodSlot(this, container, TackleBoxContainer.ROD_SLOT, 134, 37));

        this.addSlot(new TackleBoxAttachmentSlot(this, SCTags.BOBBERS, container, TackleBoxContainer.BOBBER_SLOT, 158, 11, Starcatcher.rl("item/background/bobber_white")));
        this.addSlot(new TackleBoxAttachmentSlot(this, SCTags.BAITS, container, TackleBoxContainer.BAIT_SLOT, 158, 31, Starcatcher.rl("item/background/bait_white")));
        this.addSlot(new TackleBoxAttachmentSlot(this, SCTags.HOOKS, container, TackleBoxContainer.HOOK_SLOT, 158, 51, Starcatcher.rl("item/background/hook_white")));

        this.addSlot(new TackleBoxInfiniteStorageSlot(container, TackleBoxContainer.FISH_SLOT, 134, 55));

        //add storage slots
        for (int k = 0; k < 2; ++k)
            for (int l = 0; l < 7; ++l)
                this.addSlot(new TackleBoxStorageSlot(container, 5 + l + k * 7, l * 18 + 4, 37 + k * 18));

        //add player inventory
        for (int i1 = 0; i1 < 3; ++i1)
            for (int k1 = 0; k1 < 9; ++k1)
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));

        //add player hotbar
        for (int j1 = 0; j1 < 9; ++j1)
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));

    }

    public boolean stillValid(Player player)
    {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index)
    {
        container.updateFishSlot();

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.container.getContainerSize())
            {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
                slot.setByPlayer(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        container.updateFishSlot();
        return itemstack;
    }

    public void removed(Player player)
    {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public void update()
    {
        SCDataComponents.set(container.getItem(TackleBoxContainer.ROD_SLOT), SCDataComponents.BOBBER, new MaybeStack(container.getItem(TackleBoxContainer.BOBBER_SLOT)));
        SCDataComponents.set(container.getItem(TackleBoxContainer.ROD_SLOT), SCDataComponents.BAIT, new MaybeStack(container.getItem(TackleBoxContainer.BAIT_SLOT)));
        SCDataComponents.set(container.getItem(TackleBoxContainer.ROD_SLOT), SCDataComponents.HOOK, new MaybeStack(container.getItem(TackleBoxContainer.HOOK_SLOT)));

        container.setItem(TackleBoxContainer.BOBBER_SLOT, SCDataComponents.getOrDefault(container.getItem(TackleBoxContainer.ROD_SLOT), SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack());
        container.setItem(TackleBoxContainer.BAIT_SLOT, SCDataComponents.getOrDefault(container.getItem(TackleBoxContainer.ROD_SLOT), SCDataComponents.BAIT, MaybeStack.EMPTY).toStack());
        container.setItem(TackleBoxContainer.HOOK_SLOT, SCDataComponents.getOrDefault(container.getItem(TackleBoxContainer.ROD_SLOT), SCDataComponents.HOOK, MaybeStack.EMPTY).toStack());
    }

    public ItemStack getRod()
    {
        return container.getItem(TackleBoxContainer.ROD_SLOT);
    }

    public void onPlaceRod(ItemStack newStack)
    {
        container.setItem(TackleBoxContainer.BOBBER_SLOT, SCDataComponents.getOrDefault(newStack, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack());
        container.setItem(TackleBoxContainer.BAIT_SLOT, SCDataComponents.getOrDefault(newStack, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack());
        container.setItem(TackleBoxContainer.HOOK_SLOT, SCDataComponents.getOrDefault(newStack, SCDataComponents.HOOK, MaybeStack.EMPTY).toStack());
    }
}
