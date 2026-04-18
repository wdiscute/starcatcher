package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.registry.minigamemodifiers.AbstractMinigameModifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class AbstractSweetSpotBehaviour
{
    public int ticksActive;
    protected FishingMinigameScreen instance;
    protected ActiveSweetSpot ass;

    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        this.instance = instance;
        this.ass = ass;
        ass.pos = instance.getRandomFreePosition(ass.thickness);
    }

    public void tick()
    {
        ticksActive++;

        ass.pos += ass.movingRate * ass.currentRotation;
        if (ass.pos > 360) ass.pos -= 360;
        if (ass.pos < 0) ass.pos += 360;

        ass.alpha -= ass.vanishingRate;

        if (ass.shouldSudokuOnVanish && ass.alpha <= 0) ass.removed = true;
    }

    public void onHit()
    {
        ass.onHitModifiers.forEach(mod -> {
            AbstractMinigameModifier modifier = mod.get();
            modifier.tickCount = 0;
            modifier.removed = false;
            instance.addModifier(modifier);
        });
    }

    public void onRemove()
    {
    }

    public void renderForeground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
    }

    public void render(GuiGraphicsExtractor guiGraphics, PoseStack poseStack, float partialTick)
    {
        if (ass.removed) return;

        // Renders the sprite centered to the top-left corner of the screen, to be moved with poseStack
        FishingMinigameScreen.renderPoseCentered(guiGraphics, ass.texture, 96);
    }
}
