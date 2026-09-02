package com.wdiscute.starcatcher.bobentity;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class FishingBobRenderState extends EntityRenderState
{
    public Vec3 lineOriginOffset = Vec3.ZERO;
    EntityModel<FishingBobRenderState> skin = FishingBobRenderer.BOB_MODELS.get(Starcatcher.rl("base"));
    Identifier skinRL = Starcatcher.rl("base");
    float entityYaw = 0;
}
