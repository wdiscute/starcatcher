package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.ModMenuTypes;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.blocks.ModBlocks;
import com.wdiscute.starcatcher.blocks.StandBlock;
import com.wdiscute.starcatcher.blocks.StandBlockEntity;
import com.wdiscute.starcatcher.networkandcodecs.SingleStackContainer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class StandMenu extends AbstractContainerMenu
{

    public final StandBlockEntity sbe;
    public final Level level;

    public final ItemStackHandler inventory = new ItemStackHandler(99)
    {
        @Override
        protected int getStackLimit(int slot, ItemStack stack)
        {
            return 64;
        }

    };
    public StandMenu(int containerId, Inventory inv, FriendlyByteBuf extraData)
    {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));

//        //uuid inventory
//        for (int i = 0; i < 3; ++i)
//        {
//            for (int l = 0; l < 9; ++l)
//            {
//                this.addSlot(new Slot(inv, l + i * 9 + 9, 80 + l * 18, 10 + i * 18));
//            }
//        }
//
//        //uuid hotbar
//        for (int i = 0; i < 9; ++i)
//        {
//            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
//        }

        for (int i = 0; i < 3; ++i)
        {
            for (int l = 0; l < 3; ++l)
            {
                this.addSlot(new SlotItemHandler(inventory, i * 3 + l, 135 + l * 18, 67 + i * 18)
                {
                    @Override
                    public boolean mayPickup(Player playerIn)
                    {
                        return false;
                    }
                });
                inventory.setStackInSlot(i * 3 + l, new ItemStack(Items.DIAMOND));
            }
        }

    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        //six seven
        //¯\_(ツ)¯\_
        //
        //_/¯(ツ)_/¯
        if(id == 67)
        {
            //if player has the items to signup and is not already signed up
            if(sbe.tournament.settings.canSignUp(player) && !sbe.tournament.playerScores.containsKey(player.getUUID()))
            {
                System.out.println("signed up " + player.getName());
                //sign up player with empty score
                sbe.tournament.playerScores.put(player.getUUID(), TournamentPlayerScore.empty());
            }
        }
        return super.clickMenuButton(player, id);
    }

    public StandMenu(int containerId, Inventory inv, BlockEntity blockEntity)
    {
        super(ModMenuTypes.STAND_MENU.get(), containerId);
        sbe = ((StandBlockEntity) blockEntity);
        level = inv.player.level();
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
    private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must be the number of slots you have!


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
        return stillValid(ContainerLevelAccess.create(level, sbe.getBlockPos()),
                player, ModBlocks.STAND.get());
    }
}
