package com.wdiscute.starcatcher.bobberentity;

import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.tackleskin.BaseTackleSkin;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public class FishingBobRenderState extends EntityRenderState
{
    public Vec3 lineOriginOffset = Vec3.ZERO;
    AbstractTackleSkin skin = new BaseTackleSkin();
    float entityYaw = 0;
}
