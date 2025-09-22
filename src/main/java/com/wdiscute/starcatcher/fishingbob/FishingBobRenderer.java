package com.wdiscute.starcatcher.fishingbob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FishingBobRenderer extends EntityRenderer<FishingBobEntity>
{
    protected FishingBobModel<FishingBobEntity> model;

    public FishingBobRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.model = new FishingBobModel<>(context.bakeLayer(FishingBobModel.LAYER_LOCATION));
    }


    @Override
    public ResourceLocation getTextureLocation(FishingBobEntity fishingBobEntity)
    {
        return Starcatcher.rl("textures/entity/fishing/bob.png");
    }

    @Override
    public void render(FishingBobEntity fishingBobEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        poseStack.pushPose();

        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        this.model.setupAnim(fishingBobEntity, 0.0F, 0.0F, fishingBobEntity.tickCount + partialTicks, 0.0F, 0.0F);

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(fishingBobEntity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        //render string
        if(fishingBobEntity.getOwner() instanceof Player player)
        {
            poseStack.pushPose();

            float f = player.getAttackAnim(partialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            Vec3 vec3 = this.getPlayerHandPos(player, f1, partialTicks);
            Vec3 vec31 = fishingBobEntity.getPosition(partialTicks).add(0.0F, 0.25F, 0.0F);
            float f2 = (float) (vec3.x - vec31.x);
            float f3 = (float) (vec3.y - vec31.y);
            float f4 = (float) (vec3.z - vec31.z);
            VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose posestack$pose1 = poseStack.last();

            for (int j = 0; j <= 16; ++j)
            {
                stringVertex(f2, f3, f4, vertexconsumer1, posestack$pose1, fraction(j, 16), fraction(j + 1, 16));
            }

            poseStack.popPose();
        }
    }

    private static float fraction(int numerator, int denominator) {
        return (float)numerator / (float)denominator;
    }

    private Vec3 getPlayerHandPos(Player player, float p_340872_, float partialTick) {
        int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack itemstack = player.getMainHandItem();
        if (!itemstack.is(ModItems.STARCATCHER_FISHING_ROD)) {
            i = -i;
        }

        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double d4 = (double)960.0F / (double) this.entityRenderDispatcher.options.fov().get();
            Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float)i * 0.525F, -0.1F).scale(d4).yRot(p_340872_ * 0.5F).xRot(-p_340872_ * 0.7F);
            return player.getEyePosition(partialTick).add(vec3);
        } else {
            float f = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F);
            double d0 = Mth.sin(f);
            double d1 = Mth.cos(f);
            float f1 = player.getScale();
            double d2 = (double)i * 0.35 * (double)f1;
            double d3 = 0.8 * (double)f1;
            float f2 = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(partialTick).add(-d1 * d2 - d0 * d3, (double)f2 - 0.45 * (double)f1, -d0 * d2 + d1 * d3);
        }
    }


    private static void stringVertex(float x, float y, float z, VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction)
    {
        float f = x * stringFraction;
        float f1 = y * (stringFraction * stringFraction + stringFraction) * 0.5F + 0.25F;
        float f2 = z * stringFraction;
        float f3 = x * nextStringFraction - f;
        float f4 = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - f1;
        float f5 = z * nextStringFraction - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        consumer.addVertex(pose, f, f1, f2).setColor(-16777216).setNormal(pose, f3, f4, f5);
    }

}
