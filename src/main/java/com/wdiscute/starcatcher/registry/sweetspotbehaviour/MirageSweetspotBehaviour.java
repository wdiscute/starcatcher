package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.gui.GuiGraphics;

public class MirageSweetspotBehaviour extends AbstractSweetSpotBehaviour
{

    @Override
    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onAdd(instance, ass);
        ass.canHit = false;
        ass.alpha = 0;
    }

    @Override
    public void tick(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.tick(instance, ass);
        ass.alpha += 0.16f;
    }

    @Override
    public void onMiss(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {

    }

    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick, FishingMinigameScreen instance, ActiveSweetSpot ass)
    {

        int mirageRange = 50;
        int deadZone = 20;

        float distance = Math.abs(instance.handlePos - ass.pos);
        distance = Math.min(distance, 360f - distance);

        //if outside mirage range
        if (distance > 60)
        {
            //if spot has been active for too long
            if (ass.ticksActive > ass.seed % 40 + 80)
            {
                //decrease alpha
                ass.alpha -= 0.1f;
                //
                if (ass.alpha < 0)
                    ass.removed = true;
            }

            super.render(guiGraphics, poseStack, partialTick, instance, ass);
            return;
        }


        ass.alpha = Math.min(
                Math.max(distance - deadZone, 0f) / (mirageRange - deadZone),
                1f
        );


        //if(ass.alpha == 0)
        //    ass.removed = true;

        super.render(guiGraphics, poseStack, partialTick, instance, ass);
    }
}
