package com.wdiscute.starcatcher.blocks.aquarium;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class AquariumRenderer implements BlockEntityRenderer<AquariumBlockEntity>
{

    ItemRenderer itemRenderer;

    public AquariumRenderer(BlockEntityRendererProvider.Context context)
    {
        itemRenderer = context.getItemRenderer();
        FishRenderer.createMap(context.getModelSet());
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

        //follow player for testing
        //be.fishTarget = Minecraft.getInstance().player.position().subtract(be.getBlockPos().getX() + 0.5f, be.getBlockPos().getY() -0.5f, be.getBlockPos().getZ() + 0.5f);

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

        if (!fish.isEmpty()) moveFish(be);

        poseStack.pushPose();

        Vec3 offsetCenter = new Vec3(0.5f, -0.35f, 0.5f);

        float scale = SCDataComponents.getOrDefault(
                fish, SCDataComponents.CAUGHT_FISH_INFO,
                CaughtFishInfo.AVERAGE
        ).getScale();


        //offset from be
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null)
        {
            if (!player.getMainHandItem().is(Tags.Items.BUCKETS_EMPTY) && !player.getOffhandItem().is(Tags.Items.BUCKETS_EMPTY))
            {
                poseStack.translate(be.x, be.y, be.z);

            }
        }
        else
            poseStack.translate(be.x, be.y, be.z);

        //block centering
        poseStack.translate(offsetCenter.x, offsetCenter.y, offsetCenter.z);

        //scaling + pivot adjusting
        poseStack.translate(0, 1, 0);
        poseStack.scale(scale, -scale, scale);
        poseStack.translate(0, -1, 0);

        poseStack.rotateAround(Axis.YN.rotation((float) ((float) be.fishRotation + Math.PI / 2)), 0, 0, 0);

        // Render model here

        if (!be.getFish().isEmpty())
            FishRenderer.renderFishFromItem(itemRenderer, FishRenderer.map, fish, buffer, poseStack, packedLight, OverlayTexture.NO_OVERLAY, be.getLevel());

        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(AquariumBlockEntity aquariumBlockEntity)
    {
        BlockPos pos = aquariumBlockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }
}
