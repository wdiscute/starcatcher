package com.wdiscute.starcatcher.bobberentity.tackles;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobberentity.FishingBobRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class KingModel extends EntityModel<FishingBobRenderState>
{
    private static final String NAME = "king";
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
    private final ModelPart root;

    public KingModel(ModelPart root)
    {
        super(root);
        this.root = root.getChild("root");
    }

    public static Identifier getTexture()
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
}