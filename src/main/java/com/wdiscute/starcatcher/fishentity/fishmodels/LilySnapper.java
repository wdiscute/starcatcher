package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class LilySnapper extends EntityModel<FishEntityRenderState>
{
	private static final String NAME = "lily_snapper";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;


	public LilySnapper(ModelPart root) {
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
		PartDefinition bone1 = fish.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(12, 22).addBox(0.0F, -4.0F, 5.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bone2 = fish.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(22, 14).addBox(0.0F, 0.0F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bone3 = fish.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(12, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition bone4 = fish.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -6.0F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = fish.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
}