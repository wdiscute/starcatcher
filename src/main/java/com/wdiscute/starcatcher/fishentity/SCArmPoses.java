package com.wdiscute.starcatcher.fishentity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public interface SCArmPoses
{
    EnumProxy<HumanoidModel.ArmPose> HOLD_ABOVE_HEAD = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            (IArmPoseTransformer) SCArmPoses::applyHoldAboveHead
    );

    static void applyHoldAboveHead(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm)
    {
        model.rightArm.xRot = -(float) Math.PI * 0.95F;
        model.rightArm.yRot = 0.0F;
        model.rightArm.zRot = 0.0F;

        model.leftArm.xRot = -(float) Math.PI * 0.95F;
        model.leftArm.yRot = 0.0F;
        model.leftArm.zRot = 0.0F;
    }
}
