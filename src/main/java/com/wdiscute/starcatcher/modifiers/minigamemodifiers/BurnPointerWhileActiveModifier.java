package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.x;

public class BurnPointerWhileActiveModifier extends AbstractTimedModifier
{
    private final int rampTime;
    private final float extraSpeed;

    public BurnPointerWhileActiveModifier(int length, int rampTime, float extraSpeed)
    {
        super(length);
        this.length = length;
        this.rampTime = rampTime;
        this.extraSpeed = extraSpeed;
    }

    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(instance, guiGraphics, partialTick, width, height);

        float alpha = 1 - (instance.handleBaseSpeed - instance.handleSpeed / 2) / (instance.handleSpeed - instance.handleSpeed / 2);

        RenderSystem.setShaderColor(1, 1, 1, alpha);
        RenderSystem.enableBlend();

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getModelManager()
                .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                .getSprite(ResourceLocation.withDefaultNamespace("block/fire_1"));

        guiGraphics.blit(width / 2 - 8, height / 2 - 16, 0, 16, 16, sprite);
        guiGraphics.blit(width / 2 - 8, height / 2 - 8, 0, 16, 16, sprite);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        onMiss(instance);
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        super.tick(instance);
        float currentSpeed = instance.handleSpeed;

        float increaseValueEveryTick = Math.abs(extraSpeed) / rampTime;

        float targetSpeed = instance.handleBaseSpeed + extraSpeed;

        //who knows wtf is going on here tbh
        //speed up / starting
        if (tickCount <= rampTime)
        {
            //if the increase is bigger than the target speed, set to target speed, otherwise increase by the value every tick
            instance.handleSpeed = Math.abs(currentSpeed) + increaseValueEveryTick > targetSpeed ? targetSpeed : currentSpeed + Math.signum(currentSpeed) * increaseValueEveryTick;
        }

        //slowdown / ending
        if (tickCount >= length - rampTime)
        {
            float newPointerSpeed = currentSpeed - Math.signum(currentSpeed) * increaseValueEveryTick;
            instance.handleSpeed = Math.abs(instance.handleBaseSpeed) < newPointerSpeed ? newPointerSpeed : instance.handleBaseSpeed;
        }
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        tickCount = 0;
        Minecraft.getInstance().player.playSound(SoundEvents.BLAZE_BURN, 0.9f, 1f);
        Minecraft.getInstance().player.playSound(SoundEvents.BLAZE_SHOOT, 0.3f, 0.6f);
        Minecraft.getInstance().player.playSound(SoundEvents.FIRE_EXTINGUISH, 0.2f, 0.7f);
    }

    @Override
    public void onRemove(FishingMinigameScreen instance)
    {
        super.onRemove(instance);
        instance.handleSpeed = instance.handleBaseSpeed;
    }
}
