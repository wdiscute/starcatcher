package com.wdiscute.starcatcher.fishentity;

import com.wdiscute.starcatcher.registry.items.FishItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;

public interface SCArmPoses
{
    static void register()
    {

    }

    HumanoidModel.ArmPose HOLD_ABOVE_HEAD =
            HumanoidModel.ArmPose.create(
                    "HOLD_ABOVE_HEAD",
                    false,
                    SCArmPoses::applyHoldAboveHead
            );

    enum ArmsUp
    {
        BOTH_ARMS,
        LEFT,
        RIGHT,
        BOTH_SPLIT
    }

    static ArmsUp getArmsUp(HumanoidArm mainArm, ItemStack main, ItemStack off)
    {
        if(main.getItem() instanceof FishItem && off.getItem() instanceof FishItem)
            return ArmsUp.BOTH_SPLIT;

        if (mainArm == HumanoidArm.RIGHT)
        {
            if (main.getItem() instanceof FishItem)
            {
                if (off.isEmpty())
                    return ArmsUp.BOTH_ARMS;

                return ArmsUp.RIGHT;
            }
            else
            {
                if (off.getItem() instanceof FishItem)
                {
                    if (main.isEmpty())
                        return ArmsUp.BOTH_ARMS;
                    else
                        return ArmsUp.LEFT;
                }
            }
        }
        else
        {
            if (main.getItem() instanceof FishItem)
            {
                if (off.isEmpty())
                    return ArmsUp.BOTH_ARMS;

                return ArmsUp.LEFT;
            }
            else
            {
                if (off.getItem() instanceof FishItem)
                {
                    if (main.isEmpty())
                        return ArmsUp.BOTH_ARMS;
                    else
                        return ArmsUp.RIGHT;
                }

            }
        }

        return ArmsUp.BOTH_SPLIT;
    }

    static void applyHoldAboveHead(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm)
    {
        switch (getArmsUp(entity.getMainArm(), entity.getMainHandItem(), entity.getOffhandItem()))
        {
            case BOTH_ARMS, BOTH_SPLIT ->
            {
                model.rightArm.xRot = -(float) Math.PI * 0.95F;
                model.rightArm.yRot = 0.0F;
                model.rightArm.zRot = 0.0F;

                model.leftArm.xRot = -(float) Math.PI * 0.95F;
                model.leftArm.yRot = 0.0F;
                model.leftArm.zRot = 0.0F;
            }

            case LEFT ->
            {
                model.leftArm.xRot = -(float) Math.PI * 0.95F;
                model.leftArm.yRot = 0.0F;
                model.leftArm.zRot = 0.0F;
            }

            case RIGHT ->
            {
                model.rightArm.xRot = -(float) Math.PI * 0.95F;
                model.rightArm.yRot = 0.0F;
                model.rightArm.zRot = 0.0F;
            }
        }
    }
}
