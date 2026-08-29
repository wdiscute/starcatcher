package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AbstractSweetSpotBehaviour
{
    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        ass.pos = instance.getRandomFreePosition(ass.thickness);
    }

    public void tick(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        ass.ticksActive++;

        ass.pos += ass.movingRate * ass.currentRotation;
        if (ass.pos > 360) ass.pos -= 360;
        if (ass.pos < 0) ass.pos += 360;

        ass.alpha -= ass.vanishingRate;

        if (ass.shouldSudokuOnVanish && ass.alpha <= 0) ass.removed = true;
    }

    public void onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
    }

    public void onMiss(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        ass.alpha = 1;
    }

    public void onRemove(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
    }

    public void renderForeground(GuiGraphics guiGraphics, float partialTick, int width, int height, FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
    }

    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick, FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        if (ass.removed) return;

        ScreenUtils.setAlphaF(ass.alpha);
        ass.texture.render(guiGraphics, -48, -48);
    }
}
