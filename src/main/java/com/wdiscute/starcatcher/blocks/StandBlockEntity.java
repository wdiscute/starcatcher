package com.wdiscute.starcatcher.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StandBlockEntity extends BlockEntity
{
    public StandBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.STAND.get(), pos, blockState);
    }



}
