package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class FreezeHandleModifier extends AbstractTimedModifier
{
    private final int rampTime;

    public static final ResourceLocation FROZEN = Starcatcher.rl("textures/gui/minigame/modifiers/freeze_center.png");

    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(instance, guiGraphics, partialTick, width, height);
        float alpha = 1 - (instance.handleSpeed - instance.handleBaseSpeed / 2) / (instance.handleBaseSpeed - instance.handleBaseSpeed / 2);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        RenderSystem.enableBlend();
        guiGraphics.blit(FROZEN, width / 2 - 16, height / 2 - 16, 32, 32, 0, 0, 32, 32, 32, 32);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
    }

    @Override
    public String toString()
    {
        return "[FreezeHandleModifier@" + Integer.toHexString(hashCode()) + "] (tick:" + tickCount + " / length: " + length + " /  ramp: " + rampTime + " )";
    }

    public FreezeHandleModifier(int length, int rampTime)
    {
        super(length);
        this.rampTime = rampTime;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        //cancel if any modifiers with the CancelFrozenEffect interface are active
        if (instance.getModifiers().stream().anyMatch(o -> o instanceof CancelFrozenEffect))
            removed = true;
        onMiss(instance);
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        super.tick(instance);

        float currentSpeed = instance.handleSpeed;

        float decreaseTime = Math.abs(instance.handleBaseSpeed) / rampTime;

        //who knows wtf is going on here tbh
        if (tickCount <= rampTime)
        {
            instance.handleSpeed = Math.abs(currentSpeed) < decreaseTime ? 0 : currentSpeed - Math.signum(currentSpeed) * decreaseTime;
        }

        if (tickCount >= length - rampTime)
        {
            float newPointerSpeed = currentSpeed + instance.currentRotation * decreaseTime;
            instance.handleSpeed = Math.min(instance.handleBaseSpeed, newPointerSpeed);
        }

        if(tickCount > length) removed = true;
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        tickCount = 0;
        Minecraft.getInstance().player.playSound(SoundEvents.GLASS_BREAK, 0.4f, 1f);
        Minecraft.getInstance().player.playSound(SoundEvents.SNOW_BREAK, 1f, 1f);
    }

    @Override
    public void onRemove(FishingMinigameScreen instance)
    {
        super.onRemove(instance);
        instance.handleSpeed = instance.handleBaseSpeed;
    }
}
