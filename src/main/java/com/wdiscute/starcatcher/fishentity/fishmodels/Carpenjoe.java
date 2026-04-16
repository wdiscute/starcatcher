package com.wdiscute.starcatcher.fishentity.fishmodels;

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

public class Carpenjoe<T extends Entity> extends EntityModel<T>
{
	private static final String NAME = "carpernjoe";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;


	public Carpenjoe(ModelPart root) {
		this.fish = root.getChild("fish");
	}

	public static ResourceLocation getTexture()
	{
		return Starcatcher.rl("textures/entity/fishes/" + NAME + ".png");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fish = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));
		PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin1 = fish.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, -6.0F, -4.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin2 = fish.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(4, 21).addBox(0.0F, -5.0F, 3.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(6, 21).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin4 = fish.addOrReplaceChild("fin4", CubeListBuilder.create().texOffs(20, 13).addBox(0.0F, 0.0F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition tail = fish.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 13).addBox(0.0F, -5.0F, 4.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin5 = fish.addOrReplaceChild("fin5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = fin5.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 17).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, -1.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition fin6 = fish.addOrReplaceChild("fin6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r2 = fin6.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, -1.0F, 0.0F, 0.0F, 2.3562F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fish.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T fishEntity, float v, float v1, float v2, float v3, float v4)
	{

	}
}