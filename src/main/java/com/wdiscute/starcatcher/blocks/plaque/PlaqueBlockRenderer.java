package com.wdiscute.starcatcher.blocks.plaque;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockRenderState;
import com.wdiscute.starcatcher.blocks.display.DisplayBookModel;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlaqueBlockRenderer implements BlockEntityRenderer<PlaqueBlockEntity, PlaqueBlockRenderState>
{
    Map<Item, Float> rotation = new HashMap<>();
    Map<Item, Vec3> offsets = new HashMap<>();

    public PlaqueBlockRenderer(BlockEntityRendererProvider.Context context)
    {
        rotation.put(SCItems.CERBERAY.get(), 90f);

        offsets.put(SCItems.CERBERAY.get(), new Vec3(0.05, 0, 0));
        offsets.put(SCItems.MAGMA_FISH.get(), new Vec3(0.05, 0, 0));
        offsets.put(SCItems.PETALDRIFT_CARP.get(), new Vec3(0.08, 0, 0));
    }

    @Override
    public void extractRenderState(PlaqueBlockEntity be, PlaqueBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        state.stack = be.getImmutableItem() == null ? ItemStack.EMPTY : be.getImmutableItem();
        state.direction = be.getBlockState().getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.NORTH);
    }

    @Override
    public void submit(PlaqueBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.stack.is(SCTags.BUCKETABLE_FISHES))
        {
            ItemStack fish = state.stack;

            poseStack.pushPose();

            //block centering
            switch (state.direction)
            {
                case NORTH -> poseStack.translate(0.501f, -0.501f, 0.901f);
                case SOUTH -> poseStack.translate(0.501f, -0.501f, 0.101f);
                case EAST -> poseStack.translate(0.101f, -0.501f, 0.501f);
                default -> poseStack.translate(0.901f, -0.501f, 0.501f);
            }

            switch (state.direction)
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

            FishEntityRenderState ir = new FishEntityRenderState();
            ir.lightCoords = state.lightCoords;
            FishRenderer.renderFishFromItem(ir, fish, submitNodeCollector, poseStack);
            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(PlaqueBlockEntity blockEntity)
    {
        return new AABB(blockEntity.getBlockPos()).inflate(2);
    }

    @Override
    public PlaqueBlockRenderState createRenderState()
    {
        return new PlaqueBlockRenderState();
    }

    public record State(float openness, float pageFlip1, float pageFlip2) {
        public static State forAnimation(float progress, float pageFlip1, float pageFlip2, float openness) {
            return new State((Mth.sin(progress * 0.02F) * 0.1F + 1.25F) * openness, pageFlip1, pageFlip2);
        }
    }
}
