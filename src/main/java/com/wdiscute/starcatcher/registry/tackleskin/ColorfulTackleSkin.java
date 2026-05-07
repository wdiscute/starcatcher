package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class ColorfulTackleSkin extends AbstractTackleSkin
{
    @Override
    public Identifier getName()
    {
        return Starcatcher.rl("colorful");
    }
}
