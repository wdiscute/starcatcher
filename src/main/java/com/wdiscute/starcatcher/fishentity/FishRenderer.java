package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishingbob.FishingBobEntity;
import com.wdiscute.starcatcher.fishingbob.FishingBobModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FishRenderer extends EntityRenderer<FishEntity>
{

    ItemRenderer itemRenderer;
    public FishRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(FishEntity fish)
    {
        return Starcatcher.rl("textures/entity/fishing/fish.png");
    }

    @Override
    public void render(FishEntity fish, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        poseStack.pushPose();

        poseStack.translate(0, 0.25f, 0);

        poseStack.mulPose(Axis.XP.rotationDegrees(fish.getXRot()));
        poseStack.mulPose(Axis.YP.rotationDegrees(90 - fish.getYRot()));

        poseStack.mulPose(Axis.ZP.rotationDegrees(45));

        if (!fish.isInWater()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }

        if(fish.is != null && !fish.is.isEmpty())
        {
            this.itemRenderer.renderStatic(fish.is, ItemDisplayContext.FIXED, packedLight,
                    OverlayTexture.NO_OVERLAY, poseStack, buffer, fish.level(), fish.getId());
        }

        poseStack.popPose();
    }

}
