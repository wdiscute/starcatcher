package com.wdiscute.starcatcher.minigame.modifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AbstractFishingModifier {
    FishingMinigameScreen screen;
    int length; //-1 for infinite
    int tickCount = 0;

    public AbstractFishingModifier(FishingMinigameScreen screen, int length) {
        this.screen = screen;
        this.length = length;
    }

    public void render(GuiGraphics guiGraphics, float partialTicks, PoseStack poseStack) {

    }

    public boolean tick(){
        tickCount++;
        if (getRemainingTicks() <= 0){
            onRemove();
            return true;
        }

        return false;
    }

    public int getRemainingTicks(){
        if (length == -1) return 99;
        return length - tickCount;
    }

    protected void onRemove(){

    }

    protected void onAdd(){

    }

    public void addModifier(){
        screen.modifiers.add(this);
        onAdd();
    }
}
