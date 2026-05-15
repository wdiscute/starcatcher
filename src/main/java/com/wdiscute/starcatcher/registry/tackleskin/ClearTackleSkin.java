package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ClearTackleSkin extends AbstractTackleSkin
{

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl("clear"), "main");

    @Override
    public ModelLayerLocation getLayerLocation()
    {
        return MODEL_LAYER_LOCATION;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/clear.png");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "root", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 8).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 12).addBox(-1.0F, 1.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(8, 8).addBox(0.0F, 1.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}
