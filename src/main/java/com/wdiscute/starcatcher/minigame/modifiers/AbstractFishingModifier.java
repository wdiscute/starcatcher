package com.wdiscute.starcatcher.minigame.modifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.FishingHitZone;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.Nullable;
import java.util.Optional;

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

    // when you let a zone pass and it has a missPenalty
    public void onMiss(FishingHitZone zone){

    }

    public void onHit(FishingHitZone zone, boolean isFirstHit){

    }

    public void onMissClick(){

    }

    public void onRemove(){

    }

    protected void onAdd(){

    }

    public @Nullable FishingHitZone preZoneAdd(@Nullable FishingHitZone zone){
        return zone;
    }

    public void addModifier(){
        screen.modifiers.add(this);
        onAdd();
    }
}
