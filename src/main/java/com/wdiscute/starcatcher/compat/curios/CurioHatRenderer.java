package com.wdiscute.starcatcher.compat.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CurioHatRenderer implements ICurioRenderer
{


    @Override
    public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(
            ItemStack stack, SlotContext slotContext,
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
            int packedLight, S renderState, RenderLayerParent<S, M> renderLayerParent,
            EntityRendererProvider.Context context, float yRotation, float xRotation)
    {
        ICurioRenderer.super.render(stack, slotContext, poseStack, submitNodeCollector, packedLight, renderState, renderLayerParent, context, yRotation, xRotation);

        LivingEntity entity = slotContext.entity();
        poseStack.pushPose();

        float yOffset = entity.isCrouching() ? 25 : 0F;
        poseStack.translate(0.0, yOffset, 0.0); //sneak offset is off by like the tiniest bit.

        poseStack.mulPose(Axis.YP.rotationDegrees(yRotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRotation));

        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        poseStack.mulPose(Axis.YP.rotationDegrees(180f));

        poseStack.translate(0.0, 0.25, 0.0);
        poseStack.scale(0.62f, 0.62f, 0.62f);

        //todo 26
        //Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.HEAD, light,
        //        OverlayTexture.NO_OVERLAY, poseStack, bufferSource, Minecraft.getInstance().level, 0
        //);

        poseStack.popPose();
    }
}

