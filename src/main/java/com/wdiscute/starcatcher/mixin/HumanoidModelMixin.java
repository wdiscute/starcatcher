package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.fishentity.SCArmPoses;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity>
{
    @Shadow
    public HumanoidModel.ArmPose rightArmPose;

    @Shadow
    public HumanoidModel.ArmPose leftArmPose;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void starcatcher$removeIdleBob(T entity, float limbSwing, float limbSwingAmount,
                                           float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        HumanoidModel.ArmPose pose = SCArmPoses.HOLD_ABOVE_HEAD.getValue();
        HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;

        if (rightArmPose == pose) SCArmPoses.applyHoldAboveHead(self, entity, HumanoidArm.RIGHT);
        if (leftArmPose == pose) SCArmPoses.applyHoldAboveHead(self, entity, HumanoidArm.LEFT);
    }
}
