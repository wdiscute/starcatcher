package com.wdiscute.starcatcher.bobberentity.tackles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class KingModel<T extends Entity> extends EntityModel<T>
{
    private static final String NAME = "king";
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
    private final ModelPart root;

    public KingModel(ModelPart root)
    {
        this.root = root.getChild("root");
    }

    public static ResourceLocation getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/king.png");
    }


    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition king = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(3.1F, -8.0F, -3.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 12).addBox(-3.1F, -8.0F, -3.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-3.0F, -8.0F, -3.1F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 22).addBox(-3.0F, -8.0F, 3.1F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2)
    {
        root.render(poseStack, vertexConsumer, i, i1, i2);
    }

    @Override
    public void setupAnim(T fishEntity, float v, float v1, float v2, float v3, float v4)
    {

    }
}