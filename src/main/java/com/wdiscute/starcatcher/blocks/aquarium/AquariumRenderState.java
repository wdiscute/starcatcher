package com.wdiscute.starcatcher.blocks.aquarium;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class AquariumRenderState extends BlockEntityRenderState
{
    ItemStack fish;
    public BlockPos fishTargetBP = BlockPos.ZERO;
    double partialHelper = 0;
    public double fishRotation = 0;
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public AquariumRenderState(ItemStack fish)
    {
        this.fish = fish;
    }
}
