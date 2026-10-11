package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.items.FishItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class HeldFishLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
{
    public HeldFishLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet models)
    {
        super(parent);
        FishRenderer.createMap(models);
    }

    @Override
    public void render(PoseStack ps, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing,
                       float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch)
    {
        for (InteractionHand hand : InteractionHand.values())
        {
            ItemStack stack = player.getItemInHand(hand);

            HumanoidArm side = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();

            ModelPart arm = side == HumanoidArm.RIGHT ? getParentModel().rightArm : getParentModel().leftArm;

            ps.pushPose();
            arm.translateAndRotate(ps);

            float scale = SCDataComponents.getOrDefault(
                    stack, SCDataComponents.CAUGHT_FISH_INFO,
                    CaughtFishInfo.AVERAGE
            ).getScale();

            //scaling + pivot adjusting
            ps.translate(0, 1, 0);
            ps.scale(scale, -scale, scale);
            ps.translate(0, -1, 0);

            ps.translate(0.4F, 0.35F, 0.0F);

            ps.translate((1 - scale) / 3, -scale / 4, 0.0F);
            ps.mulPose(Axis.YP.rotationDegrees(90));
            FishRenderer.renderFishFromItem(stack, buffer, ps, packedLight, OverlayTexture.NO_OVERLAY);
            ps.popPose();
        }
    }
}
