package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class GlowstonePufferfish extends EntityModel<FishEntityRenderState>
{
    private static final String NAME = "glowstone_pufferfish";
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
    private final ModelPart fish;

    public GlowstonePufferfish(ModelPart root)
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

        PartDefinition fish = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));
        PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition lfin = body.addOrReplaceChild("lfin", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, -0.99F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -7.0F, -2.0F));
        PartDefinition rfin = body.addOrReplaceChild("rfin", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -0.99F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -7.0F, -2.0F));
        PartDefinition spikes = body.addOrReplaceChild("spikes", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, -9.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition spikes_back_bottom_r1 = spikes.addOrReplaceChild("spikes_back_bottom_r1", CubeListBuilder.create().texOffs(15, 20).addBox(-8.0F, 0.0F, -0.001F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 4.001F, 0.7854F, 0.0F, 0.0F));
        PartDefinition spikes_front_bottom_r1 = spikes.addOrReplaceChild("spikes_front_bottom_r1", CubeListBuilder.create().texOffs(15, 20).addBox(-8.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));
        PartDefinition spikes_back_right_r1 = spikes.addOrReplaceChild("spikes_back_right_r1", CubeListBuilder.create().texOffs(9, 17).addBox(-1.0F, 0.0F, -0.001F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.0F, 4.001F, 0.0F, 0.7854F, 0.0F));
        PartDefinition spikes_back_left_r1 = spikes.addOrReplaceChild("spikes_back_left_r1", CubeListBuilder.create().texOffs(9, 17).addBox(0.0F, 0.0F, -0.001F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.0F, 4.001F, 0.0F, -0.7854F, 0.0F));
        PartDefinition spikes_front_right_r1 = spikes.addOrReplaceChild("spikes_front_right_r1", CubeListBuilder.create().texOffs(5, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition spikes_front_left_r1 = spikes.addOrReplaceChild("spikes_front_left_r1", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition spikes_back_top_r1 = spikes.addOrReplaceChild("spikes_back_top_r1", CubeListBuilder.create().texOffs(23, 18).addBox(-8.0F, -1.0F, -0.001F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.0F, 4.001F, -0.7854F, 0.0F, 0.0F));
        PartDefinition spikes_front_top_r1 = spikes.addOrReplaceChild("spikes_front_top_r1", CubeListBuilder.create().texOffs(15, 17).addBox(-8.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.0F, -4.0F, 0.7854F, 0.0F, 0.0F));
        PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(25, 23).addBox(0.0F, -4.0F, 6.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}