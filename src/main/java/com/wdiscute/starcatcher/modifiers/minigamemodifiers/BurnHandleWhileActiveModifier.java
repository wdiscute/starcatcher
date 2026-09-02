package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class BurnHandleWhileActiveModifier extends AbstractTimedModifier
{
    public static final ScreenUtils.Image FIRE = new ScreenUtils.Image(
            Utils.rl("textures/block/fire_1.png"), 16, 512
    );

    private final int rampTime;
    private final float extraSpeed;

    public BurnHandleWhileActiveModifier(int length, int rampTime, float extraSpeed)
    {
        super(length);
        this.length = length;
        this.rampTime = rampTime;
        this.extraSpeed = extraSpeed;
    }

    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(instance, guiGraphics, partialTick, width, height);

        float alpha = 1 - (instance.handleBaseSpeed - instance.handleSpeed / 2) / (instance.handleSpeed - instance.handleSpeed / 2);

        long animationTime = System.currentTimeMillis() % 2000;
        int offset = (int) (animationTime * 33 / 2000);
        int offset2 = (offset + 12) % 33;

        ScreenUtils.setColorF(alpha, 1, 1, 1);
        FIRE.render(guiGraphics, width / 2 - 8, height / 2 - 16, 0, 16 * offset, 16, 16);

        ScreenUtils.setColorF(alpha, 1, 1, 1);
        FIRE.render(guiGraphics, width / 2 - 8, height / 2 - 8, 0, 16 * offset2, 16, 16);
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
            float newHandleSpeed = currentSpeed - Math.signum(currentSpeed) * increaseValueEveryTick;
            instance.handleSpeed = Math.abs(instance.handleBaseSpeed) < newHandleSpeed ? newHandleSpeed : instance.handleBaseSpeed;
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
