package com.wdiscute.starcatcher.fishentity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public interface SCArmPoses
{
    public static final HumanoidModel.ArmPose HOLD_ABOVE_HEAD =
            HumanoidModel.ArmPose.create(
                    "HOLD_ABOVE_HEAD",
                    false,
                    SCArmPoses::applyHoldAboveHead
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
