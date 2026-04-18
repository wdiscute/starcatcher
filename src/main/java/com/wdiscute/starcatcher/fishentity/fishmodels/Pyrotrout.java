package com.wdiscute.starcatcher.fishentity.fishmodels;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class Pyrotrout extends EntityModel<FishEntityRenderState>
{
	private static final String NAME = "pyrotrout";
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl(NAME), "main");
	private final ModelPart fish;

	public Pyrotrout(ModelPart root) {
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

		PartDefinition body2 = partdefinition.addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, -1.0F));
		PartDefinition body = body2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -3.0F, 2.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(22, 0).addBox(-1.0F, -5.0F, -6.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin1 = body2.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -9.0F, -4.0F, 0.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin2 = body2.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(20, 14).addBox(0.0F, -8.0F, 6.0F, 0.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin3 = body2.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(22, 7).addBox(0.0F, 0.0F, 3.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition fin4 = body2.addOrReplaceChild("fin4", CubeListBuilder.create().texOffs(0, 28).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}