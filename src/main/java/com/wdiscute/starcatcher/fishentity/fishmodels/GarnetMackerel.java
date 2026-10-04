package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class GarnetMackerel extends EntityModel<FishEntityRenderState>
{
    private static final String NAME = "garnet_mackerel";
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
    private final ModelPart fish;

    public GarnetMackerel(ModelPart root)
    {
        super(root);
        this.fish = root.getChild("fish");
    }

    public static Identifier getTexture()
    {
        return Starcatcher.rl("textures/entity/fishes/" + NAME + ".png");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition fish = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -1.0F));
        PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(24, 22).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition rfin = body.addOrReplaceChild("rfin", CubeListBuilder.create().texOffs(26, 0).addBox(-4.0F, 1.0F, -0.001F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.0F, 0.001F, 0.0F, 1.0908F, 0.0F));
        PartDefinition lfin = body.addOrReplaceChild("lfin", CubeListBuilder.create().texOffs(26, 2).addBox(0.0F, 1.0F, -0.001F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -4.0F, 0.001F, 0.0F, -1.0908F, 0.0F));
        PartDefinition fin1 = fish.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, -9.0F, -2.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition fin2 = fish.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(24, 15).addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(16, 15).addBox(0.0F, -5.5F, 6.0F, 0.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}