package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.registry.SCMenuTypes;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.blocks.stand.StandBlockEntity;
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

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class StandMenu extends AbstractContainerMenu
{
    public final StandBlockEntity sbe;
    public final Level level;

    public StandMenu(int containerId, Inventory inv, BlockEntity blockEntity)
    {
        super(SCMenuTypes.STAND_MENU.get(), containerId);
        sbe = ((StandBlockEntity) blockEntity);
        level = inv.player.level();
    }

    public StandMenu(int containerId, Inventory inv, FriendlyByteBuf extraData)
    {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if (level.isClientSide()) return false;

        //six seven
        //¯\_(ツ)¯\_
        //
        //_/¯(ツ)_/¯


        if (sbe.tournament.status == Tournament.Status.SETUP)
        {
            //duration -
            if (id == 101)
            {
                if (sbe.tournament.settings.durationInTicks > 1200)
                {
                    sbe.tournament.settings.durationInTicks -= 1200;
                }
            }

            //duration --
            if (id == 102)
            {
                if (sbe.tournament.settings.durationInTicks > 12000)
                {
                    sbe.tournament.settings.durationInTicks -= 12000;
                }
            }

            //duration +
            if (id == 103)
            {
                sbe.tournament.settings.durationInTicks += 1200;
            }

            //duration ++
            if (id == 104)
            {
                sbe.tournament.settings.durationInTicks += 12000;
            }

            //start
            if (id == 68)
            {
                if (player.getUUID().equals(sbe.tournament.owner) && sbe.tournament.status.equals(Tournament.Status.SETUP))
                {
                    TournamentHandler.startTournament(player, sbe.tournament);
                }
            }

            //signup
            if (id == 67)
            {
                //if player has the items to signup and is not already signed up
                if (sbe.tournament.settings.canSignUp(player) && !sbe.tournament.playerScores.stream().anyMatch(t -> t.playerUUID.equals(player.getUUID())))
                {
                    //sign up player with empty score
                    sbe.tournament.playerScores.add(TournamentPlayerScore.empty(player.getUUID()));

                    List<SingleStackContainer> entryCost = sbe.tournament.settings.entryCost;

                    if (!entryCost.isEmpty())
                    {
                        for (SingleStackContainer ssc : entryCost)
                        {
                            Predicate<ItemStack> predicate = (is) -> is.is(ssc.create().getItem()) && is.getCount() >= ssc.create().getCount();

                            for (int i = 0; i < player.getInventory().getContainerSize(); ++i)
                            {
                                ItemStack is = player.getInventory().getItem(i);
                                if (predicate.test(is))
                                {
                                    is.shrink(ssc.create().getCount());
                                    break;
                                }
                            }

                        }
                    }
                }
            }
        }

        //cancel
        if (id == 69)
        {
            if (player.getUUID().equals(sbe.tournament.owner) && sbe.tournament.status.equals(Tournament.Status.ACTIVE))
            {
                TournamentHandler.cancelTournament(level, sbe.tournament);
            }
        }

        //wipe a finished/canceled tournament
        if (id == 53 && sbe.tournament.status.isDone())
        {
            Tournament tournamentOld = sbe.tournament;
            sbe.setUuid(UUID.randomUUID());
            sbe.tournament = null;
            sbe.tournament = sbe.makeOrGetTournament().setOwner(tournamentOld.owner);
        }



        sbe.sync();
        return super.clickMenuButton(player, id);
    }


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
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT)
        {
            // This is a TE slot so merge the stack into the playerScores inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
            {
                return ItemStack.EMPTY;
            }
        } else
        {
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0)
        {
            sourceSlot.set(ItemStack.EMPTY);
        } else
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
                player, SCBlocks.STAND.get());
    }
}
