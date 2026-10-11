package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.fishentity.SCArmPoses;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin
{
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void starcatcher$removeIdleBob(HumanoidRenderState state, CallbackInfo ci)
    {
        HumanoidModel.ArmPose pose = SCArmPoses.HOLD_ABOVE_HEAD.getValue();
        HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;

        if (state.rightArmPose == pose) SCArmPoses.applyHoldAboveHead(self, state, HumanoidArm.RIGHT);
        if (state.leftArmPose == pose) SCArmPoses.applyHoldAboveHead(self, state, HumanoidArm.LEFT);
    }
}
