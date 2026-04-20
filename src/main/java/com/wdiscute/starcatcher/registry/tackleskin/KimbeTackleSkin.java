package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class KimbeTackleSkin extends AbstractTackleSkin
{
    @Override
    public ModelLayerLocation getLayerLocation()
    {
        return new ModelLayerLocation(Starcatcher.rl("kimbe"), "main");
    }

    @Override
    public Identifier getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/kimbe.png");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "root", CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(9, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(6, 16).addBox(-1.0F, 1.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 14).addBox(0.0F, 1.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 12).addBox(-1.5F, -3.0F, -2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 16).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 7).addBox(-0.5F, -3.0F, -2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(-6, 0).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}
