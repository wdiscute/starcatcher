package com.wdiscute.starcatcher.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StandBlockItem extends BlockItem
{
    public StandBlockItem(Block block)
    {
        super(block, new Item.Properties());
    }
}
