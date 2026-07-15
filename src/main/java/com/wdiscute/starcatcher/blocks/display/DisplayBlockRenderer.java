package com.wdiscute.starcatcher.blocks.display;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.Util;
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

public class DisplayBlockRenderer implements BlockEntityRenderer<DisplayBlockEntity>
{
    private final DisplayBookModel bookModel;
    ItemRenderer itemRenderer;

    public DisplayBlockRenderer(BlockEntityRendererProvider.Context context)
    {
        this.bookModel = new DisplayBookModel(context.bakeLayer(DisplayBookModel.LAYER_LOCATION));
        itemRenderer = context.getItemRenderer();
    }

    public void render(DisplayBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        if (be.getItem().is(SCItems.GUIDE))
        {
            poseStack.pushPose();

            float ticks = (float) be.time + partialTick;
            float openPartial = Math.clamp(be.open + (partialTick * (0.1f * Math.signum(be.open - be.oOpen))), 0, 1);

            //move up slightly when open
            poseStack.translate(0.5F, 0.95F + 0.2f * (Math.clamp(openPartial * 4, 0, 1)), 0.5F);

            //float up and down
            poseStack.translate(0.0F, (0.1F + Mth.sin(ticks / 10 * 0.6F) * 0.03F) * openPartial, 0.0F);

            double rotation = be.rot + (be.rot - be.oRot) * partialTick;
            if (Math.abs(be.rot - be.oRot) > 3)
                rotation = be.rot;

            double x = Math.cos(rotation);
            double y = Math.sin(rotation);


            //move towards the player when open
            poseStack.translate(((x / 3) * openPartial) + ((-x / 5) * (1 - openPartial)), 0f, ((y / 3) * openPartial) + ((-y / 5) * (1 - openPartial)));


            float rotDiff = be.rot - be.oRot;

            while (rotDiff >= (float) Math.PI) rotDiff -= (float) (Math.PI * 2);
            while (rotDiff < (float) -Math.PI) rotDiff += (float) (Math.PI * 2);

            float f2 = be.oRot + rotDiff * partialTick;
            poseStack.mulPose(Axis.YP.rotation(-f2));

            //rotate to lay down when closed
            poseStack.mulPose(Axis.ZP.rotationDegrees(30.0F * (Math.clamp(openPartial * 2, 0, 1))));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F * (1 - Math.clamp(openPartial * 2, 0, 1))));

            float f3 = Mth.lerp(partialTick, be.oFlip, be.flip);
            float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
            float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
            float f6 = Mth.lerp(partialTick, be.oOpen, openPartial);

            this.bookModel.setupAnim(ticks, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);

            VertexConsumer vertexconsumer = buffer.getBuffer(bookModel.renderType(Starcatcher.rl("textures/entity/book.png")));
            this.bookModel.render(poseStack, vertexconsumer, packedLight, packedOverlay, -1);
            poseStack.popPose();
        }


        if (be.getItem().is(SCTags.BUCKETABLE_FISHES))
        {
            ItemStack fish = be.getItem();

            poseStack.pushPose();

            //block centering
            Vec3 offsetCenter = new Vec3(0.5f, be.getLevel().getBlockState(be.getBlockPos().above()).isEmpty() ? 0.2f : 0.5f, 0.5f);
            poseStack.translate(offsetCenter.x, offsetCenter.y, offsetCenter.z);

            float scale = SCDataComponents.getOrDefault(
                    fish, SCDataComponents.CAUGHT_FISH_INFO,
                    CaughtFishInfo.AVERAGE
            ).getScale();

            //scaling + pivot adjusting
            poseStack.translate(0, 1, 0);
            poseStack.scale(scale, -scale, scale);
            poseStack.translate(0, -1, 0);

            poseStack.translate(0, (-scale / 10) * (SCConfig.FISH_MAX_SCALE.getAsDouble() / 15), 0);

            if (be.fishRotating)
                poseStack.rotateAround(Axis.YN.rotation((float) ((float) Util.getMillis() / 10000 + Math.PI / 2)), 0, 0, 0);

            // Render model here

            FishRenderer.renderFishFromItem(itemRenderer, FishRenderer.map, fish, buffer, poseStack, packedLight, OverlayTexture.NO_OVERLAY, be.getLevel());

            poseStack.popPose();
        }

    }

    @Override
    public AABB getRenderBoundingBox(DisplayBlockEntity blockEntity)
    {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }
}
