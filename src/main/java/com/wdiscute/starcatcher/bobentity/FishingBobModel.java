package com.wdiscute.starcatcher.bobentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class FishingBobModel<T extends FishingBobEntity> extends HierarchicalModel<T>
{
    private final ModelPart root;

    public FishingBobModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    @Override
    public void setupAnim(FishingBobEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color)
    {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root()
    {
        return root;
    }
}
