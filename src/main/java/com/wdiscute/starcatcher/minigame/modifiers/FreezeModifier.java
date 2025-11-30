package com.wdiscute.starcatcher.minigame.modifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.gui.GuiGraphics;

public class FreezeModifier extends AbstractFishingModifier{
    public float lastPointerSpeed;

    public FreezeModifier(FishingMinigameScreen screen, int length) {
        super(screen, length);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float partialTicks, PoseStack poseStack) {
        final int smoothing = 3;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f,1f,1f,Math.clamp((getRemainingTicks() - partialTicks)/ smoothing, 0f, 1f));

        guiGraphics.blit(
                FishingMinigameScreen.TEXTURE, screen.width / 2 - 16, screen.height / 2 - 16,
                32, 32, 0, 0, 32, 32, 256, 256);

        RenderSystem.setShaderColor(1f, 1f,1f,1f);
        RenderSystem.disableBlend();
    }

    @Override
    protected void onRemove() {
        super.onRemove();
        screen.pointerSpeed = lastPointerSpeed;
    }


    @Override
    protected void onAdd() {
        super.onAdd();
        lastPointerSpeed = screen.pointerSpeed;
        screen.pointerSpeed = 0;
    }
}
