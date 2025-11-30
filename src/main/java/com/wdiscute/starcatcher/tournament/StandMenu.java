package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.registry.ModMenuTypes;
import com.wdiscute.starcatcher.blocks.ModBlocks;
import com.wdiscute.starcatcher.blocks.StandBlockEntity;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Predicate;

public class StandMenu extends AbstractContainerMenu
{
    public final StandBlockEntity sbe;
    public final Level level;

    public StandMenu(int containerId, Inventory inv, BlockEntity blockEntity)
    {
        super(ModMenuTypes.STAND_MENU.get(), containerId);
        sbe = ((StandBlockEntity) blockEntity);
        level = inv.player.level();

        //player inventory
        for (int i = 0; i < 3; ++i)
        {
            for (int l = 0; l < 9; ++l)
            {
                this.addSlot(new Slot(inv, l + i * 9 + 9, 210 + l * 16, 131 + i * 16));
            }
        }

        //player hotbar
        for (int i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(inv, i, 210 + i * 16, 185));
        }

        if(!level.isClientSide)
        {
            Tournament tournament = TournamentHandler.getTournament(sbe.uuid);
            for (int i = 0; i < tournament.settings.entryCost.size(); i++)
            {
                sbe.entryCost.setStackInSlot(i, tournament.settings.entryCost.get(i).stack().copy());
            }
        }

//        if(!level.isClientSide)
//        {
//            for (int i = 0; i < sbe.tournament.settings.entryCost.size(); i++)
//            {
//                sbe.entryCost.insertItem(i, sbe.tournament.settings.entryCost.get(i).stack().copy(), false);
//            }
//        }

        for (int i = 0; i < 9; i++)
        {
            int slotid = i;
            this.addSlot(new SlotItemHandler(sbe.entryCost, slotid, 210 + slotid * 16, 99)
            {
                @Override
                public boolean mayPickup(Player playerIn)
                {
                    if(level.isClientSide) return false;

                    sbe.entryCost.setStackInSlot(slotid, ItemStack.EMPTY);
                    sbe.tournament.settings.entryCost = SingleStackContainer.fromItemStackHandler(sbe.entryCost);
                    return false;
                }

                @Override
                public boolean mayPlace(ItemStack stack)
                {
                    if(level.isClientSide) return false;

                    sbe.entryCost.setStackInSlot(slotid, stack.copy());
                    sbe.tournament.settings.entryCost = SingleStackContainer.fromItemStackHandler(sbe.entryCost);
                    return false;
                }
            });
        }
    }

    public StandMenu(int containerId, Inventory inv, FriendlyByteBuf extraData)
    {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        //six seven
        //¯\_(ツ)¯\_
        //
        //_/¯(ツ)_/¯
        if (id == 67)
        {
            //if player has the items to signup and is not already signed up
            if (sbe.tournament.settings.canSignUp(player) && !sbe.tournament.playerScores.containsKey(player.getUUID()))
            {

                System.out.println("signed up " + player.getName());
                //sign up player with empty score
                sbe.tournament.playerScores.put(player.getUUID(), TournamentPlayerScore.empty());

                List<SingleStackContainer> entryCost = sbe.tournament.settings.entryCost;

                if (!entryCost.isEmpty())
                {
                    for (SingleStackContainer ssc : entryCost)
                    {
                        Predicate<ItemStack> predicate = (is) -> is.is(ssc.stack().getItem()) && is.getCount() >= ssc.stack().getCount();

                        for (int i = 0; i < player.getInventory().getContainerSize(); ++i)
                        {
                            ItemStack is = player.getInventory().getItem(i);
                            if (predicate.test(is))
                            {
                                is.shrink(ssc.stack().getCount());
                                break;
                            }
                        }

                    }
                }


            }
        }

        //start/cancel tournament
        if (id == 69)
        {
            if (sbe.tournament.settings.duration > 0)
            {
                TournamentHandler.startTournament(sbe.tournament);
            }
        }


        return super.clickMenuButton(player, id);
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
    private static final int TE_INVENTORY_SLOT_COUNT = 0;  // must be the number of slots you have!


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
        return stillValid(
                ContainerLevelAccess.create(level, sbe.getBlockPos()),
                player, ModBlocks.STAND.get());
    }
}
