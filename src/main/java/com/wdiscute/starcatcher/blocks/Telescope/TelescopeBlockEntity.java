package com.wdiscute.starcatcher.blocks.Telescope;

import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TelescopeBlockEntity extends BlockEntity
{
    public TelescopeBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TELESCOPE.get(), pos, blockState);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
    }
}
