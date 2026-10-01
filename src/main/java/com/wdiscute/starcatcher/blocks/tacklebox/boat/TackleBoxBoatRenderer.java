package com.wdiscute.starcatcher.blocks.tacklebox.boat;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.Utils;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.DyeColor;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class TackleBoxBoatRenderer extends EntityRenderer<TackleBoxBoatEntity>
{
    TackleBoxBoatModel boxModel;
    private final Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public TackleBoxBoatRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        boxModel = new TackleBoxBoatModel(context.getModelSet().bakeLayer(TackleBoxBoatModel.LAYER_LOCATION));
        this.shadowRadius = 0.8F;
        this.boatResources = Stream.of(Boat.Type.values())
                .collect(
                        ImmutableMap.toImmutableMap(
                                type -> type,
                                type ->
                                {
                                    ResourceLocation rl = ResourceLocation.tryParse(type.getName());
                                    if (rl == null)
                                        throw new RuntimeException("Starcatcher couldn't parse boat type " + rl);
                                    else
                                        rl = rl.withPrefix("textures/entity/boat/").withSuffix(".png");


                                    return Pair.of(rl,
                                            type.isRaft() ? new RaftModel(context.bakeLayer(ModelLayers.createBoatModelName(type)))
                                                    : new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(type))));


                                })

                );
    }

    @Override
    public void render(TackleBoxBoatEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        float f = (float) entity.getHurtTime() - partialTicks;
        float f1 = entity.getDamage() - partialTicks;
        if (f1 < 0.0F)
            f1 = 0.0F;

        if (f > 0.0F)
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float) entity.getHurtDir()));

        if (!Mth.equal(entity.getBubbleAngle(partialTicks), 0.0F))
            poseStack.mulPose(new Quaternionf().setAngleAxis(entity.getBubbleAngle(partialTicks) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));

        Pair<ResourceLocation, ListModel<Boat>> pair = getModelWithLocation(entity);
        ResourceLocation resourcelocation = pair.getFirst();
        ListModel<Boat> listmodel = pair.getSecond();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listmodel.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(listmodel.renderType(resourcelocation));
        listmodel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

        if (!entity.isUnderWater())
            if (listmodel instanceof WaterPatchModel waterpatchmodel)
                waterpatchmodel.waterPatch().render(poseStack, buffer.getBuffer(RenderType.waterMask()), packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        poseStack.translate(0, -1.32, 0.5);

        if (entity.getVariant().isRaft())
            poseStack.translate(0, -0.31, 0);

        boxModel.setupAnim(entity, partialTicks, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);

        //render box
        boxModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity))),
                packedLight, OverlayTexture.NO_OVERLAY, 0xffffffff);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TackleBoxBoatEntity entity)
    {
        return Starcatcher.rl("textures/entity/tackle_box_boat/tackle_box_" + DyeColor.byId(entity.getEntityData().get(TackleBoxBoatEntity.BOX_COLOR)) + ".png");
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat)
    {
        return this.boatResources.get(boat.getVariant());
    }
}
