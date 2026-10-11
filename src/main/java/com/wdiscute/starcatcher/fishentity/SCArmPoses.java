package com.wdiscute.starcatcher.fishentity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public interface SCArmPoses
{
    EnumProxy<HumanoidModel.ArmPose> HOLD_ABOVE_HEAD = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false, // twoHanded
            false, // affectsOffhandPose
            (IArmPoseTransformer) SCArmPoses::applyHoldAboveHead
    );

    static void applyHoldAboveHead(HumanoidModel<?> model, HumanoidRenderState state, HumanoidArm arm)
    {
        model.rightArm.xRot = -(float) Math.PI * 0.95F;
        model.rightArm.yRot = 0.0F;
        model.rightArm.zRot = 0.0F;

        model.leftArm.xRot = -(float) Math.PI * 0.95F;
        model.leftArm.yRot = 0.0F;
        model.leftArm.zRot = 0.0F;
    }
}
