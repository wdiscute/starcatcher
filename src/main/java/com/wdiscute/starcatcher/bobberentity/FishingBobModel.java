package com.wdiscute.starcatcher.bobberentity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class FishingBobModel extends EntityModel<FishingBobRenderState>
{
    private final ModelPart root;

    public FishingBobModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
    }
}
