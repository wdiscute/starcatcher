package com.wdiscute.starcatcher.blocks.display;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DisplayBlockRenderer implements BlockEntityRenderer<DisplayBlockEntity, DisplayBlockRenderState>
{
    private final DisplayBookModel bookModel;
    public static final Identifier BOOK_TEXTURE = Starcatcher.rl("textures/entity/book.png");
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
        state.stack = be.getImmutableItem() == null ? ItemStack.EMPTY : be.getImmutableItem();

        //vanilla enchant table
        {
            state.flip = Mth.lerp(partialTicks, be.oFlip, be.flip);
            state.open = Mth.lerp(partialTicks, be.oOpen, be.open);
            state.time = be.time + partialTicks;
            float or = be.rot - be.oRot;

            while (or >= (float) Math.PI)
                or -= (float) (Math.PI * 2);

            while (or < (float) -Math.PI)
                or += (float) (Math.PI * 2);

            state.yRot = be.oRot + or * partialTicks;
        }

        state.fishRotating = be.fishRotating;
    }

    @Override
    public void submit(DisplayBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.stack.is(SCItems.GUIDE))
        {
            poseStack.pushPose();

            //vanilla enchant table
            {
                //todo 26 reimplement custom book movement
                poseStack.translate(0.5F, 0.9F, 0.5F);
                poseStack.translate(0.0F, 0.1F + Mth.sin(state.time * 0.1F) * 0.01F, 0.0F);
                float yRot = state.yRot;
                poseStack.mulPose(Axis.YP.rotation(-yRot));
                poseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));
                float ff1 = Mth.frac(state.flip + 0.25F) * 1.6F - 0.3F;
                float ff2 = Mth.frac(state.flip + 0.75F) * 1.6F - 0.3F;

                DisplayBookModel.State bookState = DisplayBookModel.State.forAnimation(state.time, Mth.clamp(ff1, 0.0F, 1.0F), Mth.clamp(ff2, 0.0F, 1.0F), state.open);
                submitNodeCollector.submitModel(
                        this.bookModel, bookState, poseStack, RenderTypes.entityCutout(BOOK_TEXTURE), state.lightCoords,
                        OverlayTexture.NO_OVERLAY, 0, state.breakProgress
                );
            }

            poseStack.popPose();
        }

        if (state.stack.is(SCTags.BUCKETABLE_FISHES))
        {
            ItemStack fish = state.stack;

            poseStack.pushPose();

            float scale = SCDataComponents.getOrDefault(
                    fish, SCDataComponents.CAUGHT_FISH_INFO,
                    new CaughtFishInfo(100, 100, 50, Rarity.COMMON)
            ).getScale();


            //block centering
            poseStack.translate(0.5f, 0.2f, 0.5f);

            //scaling + pivot adjusting
            poseStack.translate(0, 1.2f, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(180));
            poseStack.scale(scale, scale, scale);
            poseStack.translate(0, -1.2f, 0);


            if (state.fishRotating)
                poseStack.rotateAround(Axis.YN.rotation((float) ((float) Util.getMillis() / 10000 + Math.PI / 2)), 0, 0, 0);

            // Render model here
            FishEntityRenderState ir = new FishEntityRenderState();
            ir.lightCoords = state.lightCoords;
            ir.hasWarned = true;
            FishRenderer.renderFishFromItem(ir, fish, submitNodeCollector, poseStack);

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
}
