package com.wdiscute.starcatcher.blocks.plaque;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class PlaqueBlockRenderState extends BlockEntityRenderState
{
    ItemStack stack = ItemStack.EMPTY;
    Direction direction;
}
