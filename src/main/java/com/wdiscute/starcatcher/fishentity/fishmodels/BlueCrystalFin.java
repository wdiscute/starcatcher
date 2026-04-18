package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class BlueCrystalFin extends EntityModel<FishEntityRenderState>
{
	private static final String NAME = "blue_crystal_fin";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;


	public BlueCrystalFin(ModelPart root) {
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

		PartDefinition fish = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
		PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -4.0F, 2.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin1 = fish.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -7.0F, -3.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin2 = fish.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(16, 19).addBox(0.0F, -4.0F, 4.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(15, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
}