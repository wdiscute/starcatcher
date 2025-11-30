package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.tournament.StandMenu;
import com.wdiscute.starcatcher.tournament.Tournament;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StandBlockEntity extends BlockEntity implements MenuProvider
{
    public Tournament tournament;
    public UUID uuid;

    public final ItemStackHandler entryCost = new ItemStackHandler(9)
    {
        @Override
        protected int getStackLimit(int slot, ItemStack stack)
        {
            return 64;
        }

    };

    public StandBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.STAND.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        return new StandMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        if(uuid != null) tag.putUUID("tournament_uuid", uuid);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        if(tag.contains("tournament_uuid"))
            uuid = tag.getUUID("tournament_uuid");
        else
            uuid = UUID.randomUUID();
    }
}
