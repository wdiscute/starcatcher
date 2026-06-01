package com.wdiscute.starcatcher.bobberentity.tackles;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobberentity.FishingBobRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class FrogModel extends EntityModel<FishingBobRenderState>
{
    private static final String NAME = "frog";
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
    private final ModelPart root;

    public FrogModel(ModelPart root)
    {
        super(root);
        this.root = root.getChild("root");
    }

    public static Identifier getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/frog.png");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "root", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 19).addBox(-1.0F, 1.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(6, 18).addBox(0.0F, 1.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 15).addBox(-2.0F, -4.0F, -0.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(-8, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 18).addBox(-3.0F, -2.0F, -2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 22).addBox(2.0F, -2.0F, -2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 9).addBox(2.0F, -2.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 9).addBox(-3.0F, -2.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 24).addBox(-1.0F, -1.5F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}