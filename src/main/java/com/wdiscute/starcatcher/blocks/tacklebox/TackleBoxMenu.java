package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCMenuTypes;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TackleBoxMenu extends AbstractContainerMenu
{
    private final Container container;
    public final TackleBoxBlockEntity be;
    public static final int ROD_SLOT = 0;
    public static final int BOBBER_SLOT = 1;
    public static final int BAIT_SLOT = 2;
    public static final int HOOK_SLOT = 3;
    public static final int FISH_SLOT = 4;
    public static final int CONTAINER_SIZE = 19;

    public TackleBoxMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData)
    {
        this(containerId, playerInventory, new SimpleContainer(CONTAINER_SIZE), playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public TackleBoxMenu(int containerId, Inventory playerInventory, Container container, BlockEntity blockEntity)
    {
        super(SCMenuTypes.TACKLE_BOX.get(), containerId);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;
        this.be = ((TackleBoxBlockEntity) blockEntity);
        container.startOpen(playerInventory.player);


        this.addSlot(new TackleBoxRodSlot(this, container, ROD_SLOT, 134, 37));

        this.addSlot(new TackleBoxAttachmentSlot(this, SCTags.BOBBERS, container, BOBBER_SLOT, 158, 11, Starcatcher.rl("item/background/bobber_white")));
        this.addSlot(new TackleBoxAttachmentSlot(this, SCTags.BAITS, container, BAIT_SLOT, 158, 31, Starcatcher.rl("item/background/bait_white")));
        this.addSlot(new TackleBoxAttachmentSlot(this, SCTags.HOOKS, container, HOOK_SLOT, 158, 51, Starcatcher.rl("item/background/hook_white")));

        this.addSlot(new TackleBoxInfiniteStorageSlot(this, container, FISH_SLOT, 134, 55));

        for (int k = 0; k < 2; ++k)
            for (int l = 0; l < 7; ++l)
                this.addSlot(new TackleBoxStorageSlot(container, 5 + l + k * 7, l * 18 + 4, 37 + k * 18, be));


        for (int i1 = 0; i1 < 3; ++i1)
            for (int k1 = 0; k1 < 9; ++k1)
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));

        for (int j1 = 0; j1 < 9; ++j1)
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));

    }

    public boolean stillValid(Player player)
    {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index)
    {
        if (index == FISH_SLOT) be.updateFishSlot();

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

        return itemstack;
    }

    public void removed(Player player)
    {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public void update()
    {
        SCDataComponents.set(container.getItem(ROD_SLOT), SCDataComponents.BOBBER, new MaybeStack(container.getItem(BOBBER_SLOT)));
        SCDataComponents.set(container.getItem(ROD_SLOT), SCDataComponents.BAIT, new MaybeStack(container.getItem(BAIT_SLOT)));
        SCDataComponents.set(container.getItem(ROD_SLOT), SCDataComponents.HOOK, new MaybeStack(container.getItem(HOOK_SLOT)));

        container.setItem(BOBBER_SLOT, SCDataComponents.getOrDefault(container.getItem(ROD_SLOT), SCDataComponents.BOBBER, MaybeStack.EMPTY).stack());
        container.setItem(BAIT_SLOT, SCDataComponents.getOrDefault(container.getItem(ROD_SLOT), SCDataComponents.BAIT, MaybeStack.EMPTY).stack());
        container.setItem(HOOK_SLOT, SCDataComponents.getOrDefault(container.getItem(ROD_SLOT), SCDataComponents.HOOK, MaybeStack.EMPTY).stack());
    }

    public ItemStack getRod()
    {
        return container.getItem(ROD_SLOT);
    }

    public void onPlaceRod(ItemStack newStack)
    {
        container.setItem(BOBBER_SLOT, SCDataComponents.getOrDefault(newStack, SCDataComponents.BOBBER, MaybeStack.EMPTY).stack());
        container.setItem(BAIT_SLOT, SCDataComponents.getOrDefault(newStack, SCDataComponents.BAIT, MaybeStack.EMPTY).stack());
        container.setItem(HOOK_SLOT, SCDataComponents.getOrDefault(newStack, SCDataComponents.HOOK, MaybeStack.EMPTY).stack());
    }
}
