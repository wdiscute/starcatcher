package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.Utils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SilverfinPike<T extends Entity> extends EntityModel<T>
{
	private static final String NAME = "silverfin_pike";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;

	public SilverfinPike(ModelPart root) {
		this.fish = root.getChild("fish");
	}

	public static ResourceLocation getTexture()
	{
		return Starcatcher.rl("textures/entity/fishes/" + NAME + ".png");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body2 = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, -1.0F));
		PartDefinition body = body2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin1 = body2.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, -5.0F, -2.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin2 = body2.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(12, 13).addBox(0.0F, -4.0F, 5.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin3 = body2.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(20, 17).addBox(0.0F, 0.0F, -2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin4 = body2.addOrReplaceChild("fin4", CubeListBuilder.create().texOffs(20, 13).addBox(0.0F, 0.0F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

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

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		renderToBuffer(poseStack, buffer, packedLight, packedOverlay, Utils.toColorF(red, green, blue, alpha));
	}
}