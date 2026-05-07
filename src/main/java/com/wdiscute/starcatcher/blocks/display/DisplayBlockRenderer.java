package com.wdiscute.starcatcher.blocks.display;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.FishProperties;
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
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;

public class DisplayBlockRenderer implements BlockEntityRenderer<DisplayBlockEntity, DisplayBlockRenderState>
{
    private final DisplayBookModel bookModel;
    public static final SpriteId BOOK_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.defaultNamespaceApply("enchantment/enchanting_table_book");
    private final SpriteGetter sprites;

    public DisplayBlockRenderer(BlockEntityRendererProvider.Context context)
    {
        this.bookModel = new DisplayBookModel(context.bakeLayer(DisplayBookModel.LAYER_LOCATION));
        this.sprites = context.sprites();
    }

    @Override
    public void extractRenderState(DisplayBlockEntity be, DisplayBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        state.hasBlockAbove = !be.getLevel().getBlockState(be.getBlockPos().above()).isEmpty();
        state.stack = be.getItem() == null ? ItemStack.EMPTY : be.getItem();
        state.time = be.time;
        state.partialTick = partialTicks;
        state.flip = be.flip;
        state.oFlip = be.oFlip;
        state.flipT = be.flipT;
        state.flipA = be.flipA;
        state.open = be.open;
        state.oOpen = be.oOpen;
        state.rot = be.rot;
        state.oRot = be.oRot;
        state.tRot = be.tRot;
        state.fishRotating = be.fishRotating;
    }

    @Override
    public void submit(DisplayBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.stack.is(SCItems.GUIDE))
        {
            poseStack.pushPose();

            float ticks = (float) state.time + state.partialTick;
            float openPartial = Math.clamp(state.open + (state.partialTick * (0.1f * Math.signum(state.open - state.oOpen))), 0, 1);

            //move up slightly when open
            poseStack.translate(0.5F, 0.95F + 0.2f * (Math.clamp(openPartial * 4, 0, 1)), 0.5F);

            //float up and down
            poseStack.translate(0.0F, (0.1F + Mth.sin(ticks / 10 * 0.6F) * 0.03F) * openPartial, 0.0F);

            double rotation = state.rot + (state.rot - state.oRot) * state.partialTick;
            if (Math.abs(state.rot - state.oRot) > 3)
                rotation = state.rot;

            double x = Math.cos(rotation);
            double y = Math.sin(rotation);


            //move towards the player when open
            poseStack.translate(((x / 3) * openPartial) + ((-x / 5) * (1 - openPartial)), 0f, ((y / 3) * openPartial) + ((-y / 5) * (1 - openPartial)));


            float rotDiff = state.rot - state.oRot;

            while (rotDiff >= (float) Math.PI) rotDiff -= (float) (Math.PI * 2);
            while (rotDiff < (float) -Math.PI) rotDiff += (float) (Math.PI * 2);

            float f2 = state.oRot + rotDiff * state.partialTick;
            poseStack.mulPose(Axis.YP.rotation(-f2));

            //rotate to lay down when closed
            poseStack.mulPose(Axis.ZP.rotationDegrees(30.0F * (Math.clamp(openPartial * 2, 0, 1))));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F * (1 - Math.clamp(openPartial * 2, 0, 1))));

            float f3 = Mth.lerp(state.partialTick, state.oFlip, state.flip);
            float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
            float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
            float f6 = Mth.lerp(state.partialTick, state.oOpen, openPartial);

            State bookState = State.forAnimation(state.time, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), state.open);
            submitNodeCollector.submitModel(
                    this.bookModel, bookState, poseStack, state.lightCoords,
                    OverlayTexture.NO_OVERLAY, -1, BOOK_TEXTURE, this.sprites, 0, state.breakProgress
            );

            poseStack.popPose();
        }


        if (state.stack.is(SCTags.BUCKETABLE_FISHES))
        {
            ItemStack fish = state.stack;

            poseStack.pushPose();

            //block centering
            Vec3 offsetCenter = new Vec3(0.5f, state.hasBlockAbove ? 0.2f : 0.5f, 0.5f);
            poseStack.translate(offsetCenter.x, offsetCenter.y, offsetCenter.z);

            float scale = SCDataComponents.getOrDefault(
                    fish, SCDataComponents.CAUGHT_FISH_INFO,
                    new CaughtFishInfo(100, 100, 50, FishProperties.Rarity.COMMON, false)
            ).getScale();

            //scaling + pivot adjusting
            poseStack.translate(0, 1, 0);
            poseStack.scale(scale, -scale, scale);
            poseStack.translate(0, -1, 0);

            poseStack.translate(0, (-scale / 10) * (SCConfig.FISH_MAX_SCALE.getAsDouble() / 15), 0);

            if (state.fishRotating)
                poseStack.rotateAround(Axis.YN.rotation((float) ((float) Util.getMillis() / 10000 + Math.PI / 2)), 0, 0, 0);

            // Render model here

            FishRenderer.renderFishFromItem(new EntityRenderState(), fish, submitNodeCollector, poseStack);

            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(DisplayBlockEntity blockEntity)
    {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0F, pos.getY() + 1.5F, pos.getZ() + 1.0F);
    }

    @Override
    public DisplayBlockRenderState createRenderState()
    {
        return new DisplayBlockRenderState();
    }

    public record State(float openness, float pageFlip1, float pageFlip2) {
        public static State forAnimation(float progress, float pageFlip1, float pageFlip2, float openness) {
            return new State((Mth.sin(progress * 0.02F) * 0.1F + 1.25F) * openness, pageFlip1, pageFlip2);
        }
    }
}
