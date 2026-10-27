package com.wdiscute.starcatcher.blocks.aquarium;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.display.DisplayBookModel;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.Tags;

public class AquariumRenderer implements BlockEntityRenderer<AquariumBlockEntity>
{
    ItemRenderer itemRenderer;
    final AquariumGroundModel groundModel;

    public AquariumRenderer(BlockEntityRendererProvider.Context context)
    {
        itemRenderer = context.getItemRenderer();
        FishRenderer.createMap(context.getModelSet());
        this.groundModel = new AquariumGroundModel(context.bakeLayer(AquariumGroundModel.LAYER_LOCATION));
    }

    private void moveFish(AquariumBlockEntity be)
    {
        //speed in blocks per second
        double fishSpeed = 1f;

        //shitty math
        long now = System.nanoTime();
        if (be.partialHelper == 0) be.partialHelper = now;
        double partial = (now - be.partialHelper) / 1000000000;
        partial = Math.clamp(partial, 0, 0.2);
        be.partialHelper = now;

        //move fish vertically right away
        double lerp = Mth.lerp(partial / 5, be.y, be.fishTarget.y);
        be.y = lerp * fishSpeed;

        //move fish towards destination bullshit
        Vec3 targetAngleVec = new Vec3(be.fishTarget.x - be.x, be.fishTarget.y - be.y, be.fishTarget.z - be.z);
        double targetAngle = Math.atan2(targetAngleVec.z, targetAngleVec.x);

        double delta2 = Math.abs(Math.atan2(
                Math.sin(be.fishRotation - targetAngle),
                Math.cos(be.fishRotation - targetAngle)));

        double closenessAngle = 1.0 - delta2 / Math.PI * 3;
        closenessAngle = Math.clamp(closenessAngle, 0, 1);

        double closenessDistance = 1 - 1.0 / Math.abs((be.fishTarget.x + be.fishTarget.z) - (be.x + be.z)) / 10;
        closenessDistance = Mth.clamp(closenessDistance, 0, 1);

        be.x += Math.cos(be.fishRotation) * fishSpeed * partial * closenessAngle * closenessDistance;
        be.z += Math.sin(be.fishRotation) * fishSpeed * partial * closenessAngle * closenessDistance;

        be.fishRotation += Math.atan2(Math.sin(targetAngle - be.fishRotation), Math.cos(targetAngle - be.fishRotation)) * partial * closenessDistance;
    }

    @Override
    public void render(AquariumBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        ItemStack fish = be.getFish();

        //render ground
        if (be.ground != null)
        {
            poseStack.pushPose();

            poseStack.translate(0.5f, -1.45f, 0.5f);

            ResourceLocation rl = be.ground;
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(rl));
            groundModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);

            poseStack.popPose();
        }

        //render decoration
        if (be.decoration != null)
        {
            poseStack.pushPose();
            float scale = be.decoration.scale();
            Vec3 offset = be.decoration.offset();

            //translate from offset
            poseStack.translate(offset.x, offset.y, offset.z);

            //scale
            poseStack.translate(0.5f, 0f, 0.5f);
            poseStack.scale(scale, scale, scale);
            poseStack.translate(-0.5f, 0f, -0.5f);

            //render bs
            Minecraft.getInstance().getBlockRenderer()
                    .renderSingleBlock(be.decoration.bs(), poseStack, buffer, packedLight, packedOverlay, ModelData.EMPTY, null);

            poseStack.popPose();
        }

        //render fish
        {
            if (!fish.isEmpty()) moveFish(be);

            poseStack.pushPose();

            //offset from be
            poseStack.translate(be.x, be.y, be.z);

            //block centering
            Vec3 offsetCenter = new Vec3(0.5f, -0.35f, 0.5f);
            poseStack.translate(offsetCenter.x, offsetCenter.y, offsetCenter.z);

            //scale
            poseStack.translate(0, 1, 0);
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(180));
            float scale = SCDataComponents.getOrDefault(fish, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.AVERAGE).getScale();
            poseStack.scale(scale, scale, scale);
            poseStack.translate(0, -1, 0);

            poseStack.rotateAround(Axis.YN.rotation((float) ((float) be.fishRotation + Math.PI / 2)), 0, 0, 0);

            //render fish model
            if (!be.getFish().isEmpty())
                FishRenderer.renderFishFromItem(fish, buffer, poseStack, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(AquariumBlockEntity aquariumBlockEntity)
    {
        BlockPos pos = aquariumBlockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }
}
