package com.wdiscute.starcatcher.bobberentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;

public class FishingBobRenderer extends EntityRenderer<FishingBobEntity, FishingBobRenderState>
{
    private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/fishing/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityCutoutCull(TEXTURE_LOCATION);

    public FishingBobRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    public boolean shouldRender(FishingBobEntity entity, Frustum culler, double camX, double camY, double camZ)
    {
        return super.shouldRender(entity, culler, camX, camY, camZ) && entity.getPlayerOwner() != null;
    }

    public void submit(FishingBobRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        poseStack.pushPose();
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(camera.orientation);
        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) ->
        {
            vertex(buffer, pose, state.lightCoords, 0.0F, 0, 0, 1);
            vertex(buffer, pose, state.lightCoords, 1.0F, 0, 1, 1);
            vertex(buffer, pose, state.lightCoords, 1.0F, 1, 1, 0);
            vertex(buffer, pose, state.lightCoords, 0.0F, 1, 0, 0);
        });
        poseStack.popPose();
        float xa = (float) state.lineOriginOffset.x;
        float ya = (float) state.lineOriginOffset.y;
        float za = (float) state.lineOriginOffset.z;
        float width = Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState.appropriateLineWidth;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) ->
        {
            for (int i = 0; i < 16; i++)
            {
                float a0 = fraction(i, 16);
                float a1 = fraction(i + 1, 16);
                stringVertex(xa, ya, za, buffer, pose, a0, a1, width);
                stringVertex(xa, ya, za, buffer, pose, a1, a0, width);
            }
        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public static HumanoidArm getHoldingArm(Player owner)
    {
        return owner.getMainHandItem().canPerformAction(net.neoforged.neoforge.common.ItemAbilities.FISHING_ROD_CAST) ? owner.getMainArm() : owner.getMainArm().getOpposite();
    }

    private Vec3 getPlayerHandPos(Player owner, float swing, float partialTicks)
    {
        int invert = getHoldingArm(owner) == HumanoidArm.RIGHT ? 1 : -1;
        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && owner == Minecraft.getInstance().player)
        {
            float fov = this.entityRenderDispatcher.options.fov().get().intValue();
            double viewBobbingScale = 960.0 / fov;
            Vec3 viewVec = this.entityRenderDispatcher
                    .camera
                    .getNearPlane(fov)
                    .getPointOnPlane(invert * 0.525F, -0.1F)
                    .scale(viewBobbingScale)
                    .yRot(swing * 0.5F)
                    .xRot(-swing * 0.7F);
            return owner.getEyePosition(partialTicks).add(viewVec);
        }
        else
        {
            float ownerYRot = Mth.lerp(partialTicks, owner.yBodyRotO, owner.yBodyRot) * (float) (Math.PI / 180.0);
            double sin = Mth.sin(ownerYRot);
            double cos = Mth.cos(ownerYRot);
            float playerScale = owner.getScale();
            double rightOffset = invert * 0.35 * playerScale;
            double forwardOffset = 0.8 * playerScale;
            float yOffset = owner.isCrouching() ? -0.1875F : 0.0F;
            return owner.getEyePosition(partialTicks)
                    .add(-cos * rightOffset - sin * forwardOffset, yOffset - 0.45 * playerScale, -sin * rightOffset + cos * forwardOffset);
        }
    }

    private static float fraction(int i, int steps)
    {
        return (float) i / steps;
    }

    private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int lightCoords, float x, int y, int u, int v)
    {
        builder.addVertex(pose, x - 0.5F, y - 0.5F, 0.0F)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightCoords)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private static void stringVertex(float xa, float ya, float za, VertexConsumer stringBuffer, PoseStack.Pose stringPose, float aa, float nexta, float width)
    {
        float x = xa * aa;
        float y = ya * (aa * aa + aa) * 0.5F + 0.25F;
        float z = za * aa;
        float nx = xa * nexta - x;
        float ny = ya * (nexta * nexta + nexta) * 0.5F + 0.25F - y;
        float nz = za * nexta - z;
        float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= length;
        ny /= length;
        nz /= length;
        stringBuffer.addVertex(stringPose, x, y, z).setColor(-16777216).setNormal(stringPose, nx, ny, nz).setLineWidth(width);
    }

    public FishingBobRenderState createRenderState()
    {
        return new FishingBobRenderState();
    }

    public void extractRenderState(FishingBobEntity entity, FishingBobRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
        Player owner = entity.getPlayerOwner();
        if (owner == null)
        {
            state.lineOriginOffset = Vec3.ZERO;
        }
        else
        {
            float swing = owner.getAttackAnim(partialTicks);
            float swing2 = Mth.sin(Mth.sqrt(swing) * (float) Math.PI);
            Vec3 playerPos = this.getPlayerHandPos(owner, swing2, partialTicks);
            Vec3 hookPos = entity.getPosition(partialTicks).add(0.0, 0.25, 0.0);
            state.lineOriginOffset = playerPos.subtract(hookPos);
        }

        state.skin = entity.level().registryAccess().getOrThrow(Starcatcher.TACKLE_SKIN).value().getValue(SCDataAttachments.get(entity, SCDataAttachments.TACKLE_SKIN)).get();
    }

    protected boolean affectedByCulling(FishingHook entity)
    {
        return false;
    }
}