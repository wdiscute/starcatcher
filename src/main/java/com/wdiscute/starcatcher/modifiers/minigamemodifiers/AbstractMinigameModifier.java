package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class AbstractMinigameModifier implements Modifier
{
    final String translationOverride;

    @Override
    public final List<Component> getDescription(boolean shift)
    {
        if(translationOverride.equals("hide")) return List.of();

        if(translationOverride.isEmpty())
        {
            return getNonOverriddenDescription(shift);
        }

        return List.of(Component.translatable(translationOverride));
    }

    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        return List.of(Component.translatable("tooltip.modifier." + getIdentifier().toLanguageKey()));
    }

    public void mouseScrolled(FishingMinigameScreen instance, double mouseX, double mouseY, double scrollX, double scrollY)
    {
    }

    protected AbstractMinigameModifier(String translationOverride)
    {
        this.translationOverride = translationOverride;
    }

    public boolean removed = false;

    public void onAdd(FishingMinigameScreen instance)
    {
        removed = false;
    }

    /**
     * Runs when removed or the minigame ends
     */
    public void onRemove(FishingMinigameScreen instance)
    {
    }

    /**
     * Transforms an ActiveSweetSpot before it gets added.
     * Setting spot removed to true cancels it
     */
    public ActiveSweetSpot onSpotAdded(FishingMinigameScreen instance, ActiveSweetSpot spot)
    {
        return spot;
    }

    /**
     * Runs right before the sweetSpotBehaviour hit
     *
     * @return whether the hit should be cancelled
     */
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        return false;
    }

    public void onMiss(FishingMinigameScreen instance)
    {
    }

    public void tick(FishingMinigameScreen instance)
    {
    }

    public void onKeyPress(FishingMinigameScreen instance, int key, int scanCode, int keyModifiers)
    {
    }

    public boolean shouldDarkenWheel(FishingMinigameScreen instance)
    {
        return false;
    }

    public void onKeyReleased(FishingMinigameScreen instance, int key, int scanCode, int keyModifiers)
    {
    }

    public void renderBackground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
    }

    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
    }

    /**
     * Disables rendering the included pointer
     */
    public boolean disablePointerRendering(FishingMinigameScreen instance)
    {
        return false;
    }

    /**
     * Has the correctly rotated poseStack already
     */
    public void renderOnPointer(FishingMinigameScreen instance, GuiGraphics guiGraphics, PoseStack poseStack, float partialTick)
    {
    }

    public boolean disableSweetSpotRendering(FishingMinigameScreen instance, ActiveSweetSpot spot)
    {
        return false;
    }

    public void renderOnSweetSpot(FishingMinigameScreen instance, GuiGraphics guiGraphics, PoseStack poseStack, ActiveSweetSpot spot, float partialTick)
    {
    }

    public boolean forceAwardTreasure(FishingMinigameScreen instance)
    {
        return false;
    }

    public boolean skipHitParticles(FishingMinigameScreen instance)
    {
        return false;
    }

    public boolean skipMissSound(FishingMinigameScreen instance)
    {
        return false;
    }

    public boolean skipHitSound(FishingMinigameScreen instance)
    {
        return false;
    }

    public boolean flipRodAndProgressDisplay(FishingMinigameScreen instance)
    {
        return false;
    }

    public boolean preventLosingMinigame(FishingMinigameScreen fishingMinigameScreen)
    {
        return false;
    }

    public boolean canHitSpot(FishingMinigameScreen fishingMinigameScreen, ActiveSweetSpot ass)
    {
        return true;
    }
}
