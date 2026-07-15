package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.AbstractMinigameModifier;
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

        // allows modifier to change color
        float[] shaderColor = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3] * ass.alpha);

        RenderSystem.enableBlend();

        // Renders the sprite centered to the top-left corner of the screen, to be moved with poseStack
        FishingMinigameScreen.renderPoseCentered(guiGraphics, ass.texture, 96);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
