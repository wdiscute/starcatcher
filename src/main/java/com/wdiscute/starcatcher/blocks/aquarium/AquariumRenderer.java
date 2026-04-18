package com.wdiscute.starcatcher.blocks.aquarium;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class AquariumRenderer implements BlockEntityRenderer<AquariumBlockEntity, AquariumRenderState>
{

    ItemModelResolver itemRenderer;

    public AquariumRenderer(BlockEntityRendererProvider.Context context)
    {
        itemRenderer = context.itemModelResolver();
        FishRenderer.createMap(context.entityModelSet());
    }

    private void moveFish(AquariumRenderState be)
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
    public AABB getRenderBoundingBox(AquariumBlockEntity blockEntity)
    {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }

    @Override
    public AquariumRenderState createRenderState()
    {
        return new AquariumRenderState();
    }

    @Override
    public void submit(AquariumRenderState be, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {

        ItemStack fish = be.fish;

        if (!fish.isEmpty()) moveFish(be);

        poseStack.pushPose();

        Vec3 offsetCenter = new Vec3(0.5f, -0.35f, 0.5f);

        float scale = SCDataComponents.getOrDefault(
                fish, SCDataComponents.CAUGHT_FISH_INFO,
                new CaughtFishInfo(100, 100, 50, FishProperties.Rarity.COMMON, false)
        ).getScale();


        //offset from be
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null)
        {
            if (!player.getMainHandItem().is(Items.BUCKET) && !player.getOffhandItem().is(Items.BUCKET))
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

        // Render model
        if (!fish.isEmpty())
            FishRenderer.renderFishFromItem(be, fish, submitNodeCollector, poseStack);

        poseStack.popPose();

    }

    @Override
    public void extractRenderState(AquariumBlockEntity be, AquariumRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        state.fishTargetBP = be.fishTargetBP;
        state.fishRotation = be.fishRotation;
        state.fish = be.fish;
    }
}
