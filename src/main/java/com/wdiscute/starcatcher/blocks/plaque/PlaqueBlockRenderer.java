package com.wdiscute.starcatcher.blocks.plaque;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.blocks.display.DisplayBookModel;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class PlaqueBlockRenderer implements BlockEntityRenderer<PlaqueBlockEntity>
{
    ItemRenderer itemRenderer;

    Map<Item, Float> rotation = new HashMap<>();
    Map<Item, Vec3> offsets = new HashMap<>();

    public PlaqueBlockRenderer(BlockEntityRendererProvider.Context context)
    {
        itemRenderer = context.getItemRenderer();

        rotation.put(SCItems.CERBERAY.get(), 90f);

        offsets.put(SCItems.CERBERAY.get(), new Vec3(0.05, 0, 0));
        offsets.put(SCItems.MAGMA_FISH.get(), new Vec3(0.05, 0, 0));
        offsets.put(SCItems.PETALDRIFT_CARP.get(), new Vec3(0.08, 0, 0));
    }

    public void render(PlaqueBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        if (be.getImmutableItem().is(SCTags.BUCKETABLE_FISHES))
        {
            ItemStack fish = be.getImmutableItem();

            poseStack.pushPose();

            //block centering
            switch (be.getBlockState().getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.NORTH))
            {
                case NORTH -> poseStack.translate(0.501f, -0.501f, 0.901f);
                case SOUTH -> poseStack.translate(0.501f, -0.501f, 0.101f);
                case EAST -> poseStack.translate(0.101f, -0.501f, 0.501f);
                default -> poseStack.translate(0.901f, -0.501f, 0.501f);
            }

            switch (be.getBlockState().getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.NORTH))
            {
                case NORTH -> poseStack.mulPose(Axis.YP.rotation((float) Math.PI / 2));
                case SOUTH -> poseStack.mulPose(Axis.YP.rotation((float) Math.PI / 2));
                case EAST -> poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
                default -> poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
            }


            float scale = SCDataComponents.getOrDefault(
                    fish, SCDataComponents.CAUGHT_FISH_INFO,
                    CaughtFishInfo.AVERAGE
            ).getScale();

            //scaling + pivot adjusting
            poseStack.translate(0, 1, 0);

            Item item = fish.getItem();

            if (offsets.containsKey(item))
            {
                Vec3 offset = offsets.getOrDefault(item, Vec3.ZERO);
                poseStack.translate(offset.x, offset.y, offset.z);
            }

            if (rotation.containsKey(item))
                poseStack.mulPose(Axis.ZN.rotation((float) Math.toRadians(rotation.getOrDefault(item, 0f))));


            poseStack.scale(scale, -scale, scale);
            poseStack.translate(0, -1, 0);

            poseStack.translate(0, (-scale / 10) * (SCConfig.FISH_MAX_SCALE.getAsDouble() / 15), 0);

            FishRenderer.renderFishFromItem(itemRenderer, FishRenderer.map, fish, buffer, poseStack, packedLight, OverlayTexture.NO_OVERLAY, be.getLevel());

            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(PlaqueBlockEntity blockEntity)
    {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }
}
