package com.wdiscute.starcatcher.blocks.display;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.item.ItemStack;

public class DisplayBlockRenderState extends BlockEntityRenderState
{
    boolean fishRotating = true;
    ItemStack stack = ItemStack.EMPTY;

    //enchant table
    public float time;
    public float yRot;
    public float flip;
    public float open;
}
