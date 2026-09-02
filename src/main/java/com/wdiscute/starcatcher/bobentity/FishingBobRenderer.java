package com.wdiscute.starcatcher.bobentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.tackles.*;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishingBobRenderer extends EntityRenderer<FishingBobEntity, FishingBobRenderState>
{
    public static final List<Identifier> skins = new ArrayList<>();

    public FishingBobRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        createMap(context.getModelSet());
    }

    public static final Map<Identifier, EntityModel<FishingBobRenderState>> BOB_MODELS = new HashMap<>();

    public static void createMap(EntityModelSet modelSet)
    {
        if (!BOB_MODELS.isEmpty()) return;

        BOB_MODELS.put(Starcatcher.rl("base"), new BaseModel(modelSet.bakeLayer(BaseModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("clear"), new ClearModel(modelSet.bakeLayer(ClearModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("colorful"), new ColorfulModel(modelSet.bakeLayer(ColorfulModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("frog"), new FrogModel(modelSet.bakeLayer(FrogModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("kimbe"), new KimbeModel(modelSet.bakeLayer(KimbeModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("king"), new KingModel(modelSet.bakeLayer(KingModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("pearl"), new PearlModel(modelSet.bakeLayer(PearlModel.LAYER_LOCATION)));
    }

    public boolean shouldRender(FishingBobEntity entity, Frustum culler, double camX, double camY, double camZ)
    {
        return super.shouldRender(entity, culler, camX, camY, camZ) && entity.getOwner() != null;
    }

    public void submit(FishingBobRenderState state, PoseStack poseStack, SubmitNodeCollector node, CameraRenderState camera)
    {
        //render line
        poseStack.pushPose();
        float xa = (float) state.lineOriginOffset.x;
        float ya = (float) state.lineOriginOffset.y;
        float za = (float) state.lineOriginOffset.z;
        float width = Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState.appropriateLineWidth;
        node.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) ->
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


        //render tackle
        poseStack.pushPose();
        poseStack.scale(1F, -1F, 1F);
        poseStack.translate(0, -1.6, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.entityYaw));

        //starcatcher:base
        node.submitModel(
                state.skin, state, poseStack,
                RenderTypes.entityCutout(state.skinRL.withPrefix("textures/entity/tackle/").withSuffix(".png")),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY, 0xffffffff, null, state.outlineColor, null
        );

        poseStack.popPose();

        super.submit(state, poseStack, node, camera);
    }

    public static HumanoidArm getHoldingArm(Player owner)
    {
        return owner.getMainHandItem().is(SCTags.RODS) ? owner.getMainArm() : owner.getMainArm().getOpposite();
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

        if (entity.getOwner() instanceof Player player)
        {
            float swing = player.getAttackAnim(partialTicks);
            float swing2 = Mth.sin(Mth.sqrt(swing) * (float) Math.PI);
            Vec3 playerPos = this.getPlayerHandPos(player, swing2, partialTicks);
            Vec3 hookPos = entity.getPosition(partialTicks).add(0.0, 0.25, 0.0);
            state.lineOriginOffset = playerPos.subtract(hookPos);
        }
        else
            state.lineOriginOffset = Vec3.ZERO;

        state.entityYaw = entity.yRotO;

        state.skin = BOB_MODELS.get(SCDataAttachments.get(entity, SCDataAttachments.TACKLE_SKIN));
        state.skinRL = SCDataAttachments.get(entity, SCDataAttachments.TACKLE_SKIN);
    }
}