package com.wdiscute.starcatcher.blocks.tacklebox.boat;

import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.DyeColor;

public class TackleBoxBoatState extends BoatRenderState
{
    TackleBoxBoatEntity.Type type;
    DyeColor color;

    AnimationState openState;
    AnimationState closedState;
}
