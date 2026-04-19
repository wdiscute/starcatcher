package com.wdiscute.starcatcher.bobberentity;

import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.tackleskin.BaseTackleSkin;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class FishingBobRenderState extends EntityRenderState
{
    double entityYaw;
    AbstractTackleSkin skin = new BaseTackleSkin();

}
