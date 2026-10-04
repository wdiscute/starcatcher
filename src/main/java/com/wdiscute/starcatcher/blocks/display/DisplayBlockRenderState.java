package com.wdiscute.starcatcher.blocks.display;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.item.ItemStack;

public class DisplayBlockRenderState extends BlockEntityRenderState
{
    boolean fishRotating = true;
    boolean hasBlockAbove = true;
    ItemStack stack = ItemStack.EMPTY;
    int time = 0;
    float partialTick = 0;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
}
