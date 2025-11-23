package com.wdiscute.starcatcher.bob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.FastColor;

public class FishingBobModel<T extends FishingBobEntity> extends HierarchicalModel<T>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl("fishing_bob"), "main");
    private final ModelPart root;

    public FishingBobModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-1.0F, 1.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 7).addBox(0.0F, 1.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(FishingBobEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root()
    {
        return root;
    }
}
