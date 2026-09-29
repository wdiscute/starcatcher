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

public class GlowstoneSeeker<T extends Entity> extends EntityModel<T>
{
	private static final String NAME = "glowstone_seeker";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;


	public GlowstoneSeeker(ModelPart root) {
		this.fish = root.getChild("fish");
	}

	public static ResourceLocation getTexture()
	{
		return Starcatcher.rl("textures/entity/fishes/" + NAME + ".png");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fish = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, -1.0F));
		PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition lfin = body.addOrReplaceChild("lfin", CubeListBuilder.create().texOffs(14, 23).addBox(0.0F, 0.0F, -0.001F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -4.0F, -1.999F, 0.0F, -1.0908F, 0.0F));
		PartDefinition rfin = body.addOrReplaceChild("rfin", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, 0.0F, -0.001F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.0F, -1.999F, 0.0F, 1.0908F, 0.0F));
		PartDefinition fin1 = fish.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -7.0F, -3.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin2 = fish.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(14, 14).addBox(0.0F, 0.0F, -2.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin3 = fish.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(26, 0).addBox(0.0F, -5.0F, 6.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
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