package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class LividBamboo extends EntityModel<FishEntityRenderState>
{
    private static final String NAME = "livid_bamboo";
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
    private final ModelPart fish;

    public LividBamboo(ModelPart root)
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

        PartDefinition body = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.502F, -8.0F, 3.0F, 0.998F, 16.0F, new CubeDeformation(0.002F))
                .texOffs(0, 0).addBox(-1.5F, -1.5F, -8.0F, 3.0F, 1.998F, 16.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -1.2654F, 0.0F, 0.0F));
        PartDefinition fin1 = body.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, -3.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 1.0F));
        PartDefinition fin2 = body.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(34, 35).addBox(0.0F, 0.0F, -3.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));
        PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -1.5F, -14.0F, 3.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 1.7017F, 0.0F, 0.0F));
        PartDefinition cube_r1 = front.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).addBox(1.7F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1F, -0.65F, -11.5F, 0.3927F, 0.0F, 0.0F));
        PartDefinition cube_r2 = front.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 36).addBox(0.0F, 0.0F, -1.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.5F, -13.0F, 0.0F, 0.0F, -0.1745F));
        PartDefinition cube_r3 = front.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(6, 36).addBox(0.0F, 0.0F, -1.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 1.5F, -13.0F, 0.0F, 0.0F, 0.1745F));
        PartDefinition back = body.addOrReplaceChild("back", CubeListBuilder.create().texOffs(34, 19).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 1.9635F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

}