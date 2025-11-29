package com.wdiscute.starcatcher.rod;

import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.registry.ModMenuTypes;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FishingRodMenu extends AbstractContainerMenu
{
    public final ItemStackHandler inventory = new ItemStackHandler(3)
    {
        @Override
        protected int getStackLimit(int slot, ItemStack stack)
        {
            return 64;
        }

    };

    public final ItemStack is;

    public FishingRodMenu(int containerId, Inventory inv, FriendlyByteBuf extraData)
    {
        this(containerId, inv, inv.player.getMainHandItem());
        System.out.println(extraData);
    }

    public FishingRodMenu(int containerId, Inventory inv, ItemStack itemStack)
    {
        super(ModMenuTypes.FISHING_ROD_MENU.get(), containerId);

        is = itemStack;

        //uuid inventory
        for (int i = 0; i < 3; ++i)
        {
            for (int l = 0; l < 9; ++l)
            {
                this.addSlot(new Slot(inv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
        //uuid hotbar
        for (int i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }

        inventory.setStackInSlot(0, is.get(ModDataComponents.BOBBER.get()).stack().copy());
        inventory.setStackInSlot(1, is.get(ModDataComponents.BAIT.get()).stack().copy());
        inventory.setStackInSlot(2, is.get(ModDataComponents.HOOK.get()).stack().copy());

        this.addSlot(new SlotItemHandler(inventory, 0, 50, 35)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.is(StarcatcherTags.BOBBERS);
            }
        });
        this.addSlot(new SlotItemHandler(inventory, 1, 80, 35)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return !stack.is(StarcatcherTags.HOOKS) && !stack.is(StarcatcherTags.BOBBERS);
            }
        });
        this.addSlot(new SlotItemHandler(inventory, 2, 110, 35)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.is(StarcatcherTags.HOOKS);
            }
        });
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
    {
        return super.moveItemStackTo(stack, startIndex, endIndex, reverseDirection);
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player)
    {
        if(slotId >= 0 && this.getSlot(slotId).getItem().equals(is)) return;

        if (clickType == ClickType.SWAP)
        {
            // When clickType is SWAP, the action is the hotbar number to swap it to.
            int hotbarSlotId = 2 + 3 * 9 + button;
            if (slotId == hotbarSlotId)
            {
                return;
            }
        }

        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);

        if (!player.level().isClientSide)
        {
            is.set(ModDataComponents.BOBBER.get(), new SingleStackContainer(inventory.getStackInSlot(0)));
            is.set(ModDataComponents.BAIT.get(), new SingleStackContainer(inventory.getStackInSlot(1)));
            is.set(ModDataComponents.HOOK.get(), new SingleStackContainer(inventory.getStackInSlot(2)));
        }

    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the uuid inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = uuid inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!


    public ItemStack quickMoveStack(Player playerIn, int pIndex)
    {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT)
        {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(
                    sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                            + TE_INVENTORY_SLOT_COUNT, false))
            {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        }
        else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT)
        {
            // This is a TE slot so merge the stack into the playerScores inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
            {
                return ItemStack.EMPTY;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0)
        {
            sourceSlot.set(ItemStack.EMPTY);
        }
        else
        {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return player.getMainHandItem().is(ModItems.ROD.get());
    }
}
