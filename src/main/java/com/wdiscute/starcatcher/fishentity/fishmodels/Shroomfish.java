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

public class Shroomfish<T extends Entity> extends EntityModel<T>
{
	private static final String NAME = "shroomfish";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;

	public Shroomfish(ModelPart root) {
		this.fish = root.getChild("fish");
	}

	public static ResourceLocation getTexture()
	{
		return Starcatcher.rl("textures/entity/fishes/" + NAME + ".png");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fish = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -1.0F));
		PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin1 = fish.addOrReplaceChild("fin1", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));
		PartDefinition mushroom1 = fin1.addOrReplaceChild("mushroom1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.5F));
		PartDefinition cube_r1 = mushroom1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -4.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r2 = mushroom1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -4.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition mushroom2 = fin1.addOrReplaceChild("mushroom2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 2.5F));
		PartDefinition cube_r3 = mushroom2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(18, 23).addBox(0.0F, -3.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r4 = mushroom2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 23).addBox(0.0F, -3.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, 0.0F, -0.7854F, 0.0F));
		PartDefinition mushroom3 = fin1.addOrReplaceChild("mushroom3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 7.5F));
		PartDefinition cube_r5 = mushroom3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 23).addBox(0.0F, -3.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r6 = mushroom3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(18, 23).addBox(0.0F, -3.0F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.5F, 0.0F, -0.7854F, 0.0F));
		PartDefinition mushroom4 = fin1.addOrReplaceChild("mushroom4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 10.5F));
		PartDefinition cube_r7 = mushroom4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(24, 23).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r8 = mushroom4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(24, 23).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition mushroom5 = fin1.addOrReplaceChild("mushroom5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.5F));
		PartDefinition cube_r9 = mushroom5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(24, 23).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r10 = mushroom5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(24, 23).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition bone = fin1.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin2 = fish.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(18, 16).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(10, 16).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 6.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2)
	{
		fish.render(poseStack, vertexConsumer, i, i1, i2);
	}

	@Override
	public void setupAnim(T fishEntity, float v, float v1, float v2, float v3, float v4)
	{

	}
}