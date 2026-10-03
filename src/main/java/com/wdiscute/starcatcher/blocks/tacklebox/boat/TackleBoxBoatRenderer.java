package com.wdiscute.starcatcher.blocks.tacklebox.boat;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.Utils;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.boat.AbstractBoatModel;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.object.boat.RaftModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import org.joml.Quaternionf;

import javax.swing.*;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class TackleBoxBoatRenderer extends EntityRenderer<TackleBoxBoatEntity, TackleBoxBoatState>
{
    private final Map<TackleBoxBoatEntity.Type, Pair<Identifier, AbstractBoatModel>> boatResources;
    private final Model.Simple waterPatchModel;
    TackleBoxBoatModel boxModel;

    public static ModelLayerLocation createBoatModelName(TackleBoxBoatEntity.Type type)
    {
        Identifier location = Identifier.parse(type.name().toLowerCase(Locale.ROOT));
        return new ModelLayerLocation(location.withPrefix("boat/"), "main");
    }

    public TackleBoxBoatRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.boxModel = new TackleBoxBoatModel(context.getModelSet().bakeLayer(TackleBoxBoatModel.LAYER_LOCATION));
        this.waterPatchModel = new Model.Simple(context.bakeLayer(ModelLayers.BOAT_WATER_PATCH), t -> RenderTypes.waterMask());
        this.shadowRadius = 0.8F;
        this.boatResources = Stream.of(TackleBoxBoatEntity.Type.values())
                .collect(
                        ImmutableMap.toImmutableMap(
                                type -> type,
                                type -> Pair.of(Utils.rl(type.name().toLowerCase(Locale.ROOT)).withPrefix("textures/entity/boat/").withSuffix(".png"),
                                        type.isRaft() ? new RaftModel(context.bakeLayer(createBoatModelName(type)))
                                                : new BoatModel(context.bakeLayer(createBoatModelName(type)))
                                )
                        )
                );
    }

    @Override
    public void submit(TackleBoxBoatState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.rotate(Axis.YP.rotationDegrees(180.0F - state.yRot));
        float hurt = state.hurtTime;
        if (hurt > 0.0F)
            poseStack.rotate(Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * state.damageTime / 10.0F * state.hurtDir));

        if (!state.isUnderWater && !Mth.equal(state.bubbleAngle, 0.0F))
            poseStack.rotate(new Quaternionf().setAngleAxis(state.bubbleAngle * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));

        Pair<Identifier, AbstractBoatModel> pair = boatResources.get(state.type);

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.rotate(Axis.YP.rotationDegrees(90.0F));

        //render tackle box
        {
            poseStack.pushPose();

            poseStack.rotate(Axis.YP.rotationDegrees(270.0F));
            poseStack.translate(0, -1.32, 0.5);

            if(state.type.isRaft())
                poseStack.translate(0, -0.31, 0);

            submitNodeCollector.submitModel(boxModel, state, poseStack,
                    Starcatcher.rl("textures/entity/tackle_box_boat/tackle_box_" + state.color + ".png"),
                    state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }

        submitNodeCollector.submitModel(pair.getSecond(), state, poseStack, pair.getFirst(), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        if(state.type.isRaft)
            poseStack.translate(0, 0.1, 0);
        this.submitTypeAdditions(state, poseStack, submitNodeCollector, state.lightCoords);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords)
    {
        if (!state.isUnderWater)
            submitNodeCollector.submitModel(
                    this.waterPatchModel, Unit.INSTANCE, poseStack, Starcatcher.MISSINGNO, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor
            );
    }

    public TackleBoxBoatState createRenderState()
    {
        return new TackleBoxBoatState();
    }

    public void extractRenderState(TackleBoxBoatEntity entity, TackleBoxBoatState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.hurtTime = entity.getHurtTime() - partialTicks;
        state.hurtDir = entity.getHurtDir();
        state.damageTime = Math.max(entity.getDamage() - partialTicks, 0.0F);
        state.bubbleAngle = entity.getBubbleAngle(partialTicks);
        state.isUnderWater = entity.isUnderWater();
        state.rowingTimeLeft = entity.getRowingTime(0, partialTicks);
        state.rowingTimeRight = entity.getRowingTime(1, partialTicks);

        state.type = TackleBoxBoatEntity.Type.byType(entity.getEntityData().get(TackleBoxBoatEntity.WOOD_TYPE));
        state.color = DyeColor.byId(entity.getEntityData().get(TackleBoxBoatEntity.BOX_COLOR));

        state.openState = entity.openAnimationState;
        state.closedState = entity.closeAnimationState;
    }
}
