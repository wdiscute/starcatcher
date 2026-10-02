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

public class Deepslatefish<T extends Entity> extends EntityModel<T>
{
	private static final String NAME = "deepslatefish";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;

	public Deepslatefish(ModelPart root) {
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
		PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(24, 15).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 19).addBox(0.25F, -4.25F, -2.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(24, 23).addBox(-1.25F, -4.25F, -2.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 15).addBox(0.25F, -3.75F, 0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(8, 15).addBox(-1.25F, -3.75F, 0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin1 = fish.addOrReplaceChild("fin1", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));
		PartDefinition cube_r1 = fin1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 21).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 26).addBox(-1.0F, -2.0F, 3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 3.5355F, 0.1213F, 0.7854F, 0.0F, 0.0F));
		PartDefinition cube_r2 = fin1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 25).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.4142F, -0.5858F, 0.7854F, 0.0F, 0.0F));
		PartDefinition cube_r3 = fin1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -1.0F, 2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(26, 11).addBox(-1.0F, 4.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 8).addBox(-1.0F, 3.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 25).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, -2.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition fin2 = fish.addOrReplaceChild("fin2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r4 = fin2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(26, 4).addBox(-1.0F, 2.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.0F, 4.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition cube_r5 = fin2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -0.0503F, -3.1213F, 0.6109F, 0.0F, 0.0F));
		PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 6.0F));
		PartDefinition cube_r6 = fin3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(20, 25).addBox(-1.0F, -3.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(16, 15).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 21).addBox(-1.0F, 1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.75F, 0.75F, 0.7854F, 0.0F, 0.0F));

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