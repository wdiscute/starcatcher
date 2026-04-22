package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class BurnPointerWhileActiveModifier extends AbstractTimedModifier
{
    public static final Identifier TEXTURE = U.rl("minecraft", "textures/block/fire_0.png");
    private final int rampTime;
    private final float extraSpeed;

    public static final MapCodec<BurnPointerWhileActiveModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength),
                    Codec.INT.fieldOf("ramp_time").forGetter(mod -> mod.rampTime),
                    Codec.INT.fieldOf("extra_speed").forGetter(mod -> mod.rampTime)
            ).apply(instance, BurnPointerWhileActiveModifier::new));

    public BurnPointerWhileActiveModifier(int length, int rampTime, int extraSpeed)
    {
        super(length);
        this.rampTime = rampTime;
        this.extraSpeed = extraSpeed;
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder()
    {
        return SCMinigameModifiers.FROZEN_POINTER;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        onMiss();
    }

    @Override
    public void tick()
    {
        super.tick();
        float currentSpeed = instance.pointerSpeed;

        float increaseValueEveryTick = Math.abs(extraSpeed) / rampTime;

        float targetSpeed = instance.pointerBaseSpeed + extraSpeed;

        //who knows wtf is going on here tbh
        //speed up / starting
        if (tickCount <= rampTime)
        {
            //if the increase is bigger than the target speed, set to target speed, otherwise increase by the value every tick
            instance.pointerSpeed = Math.abs(currentSpeed) + increaseValueEveryTick > targetSpeed ? targetSpeed : currentSpeed + Math.signum(currentSpeed) * increaseValueEveryTick;
        }

        //slowdown / ending
        if (tickCount >= length - rampTime)
        {
            float newPointerSpeed = currentSpeed - U.sign(currentSpeed) * increaseValueEveryTick;
            instance.pointerSpeed = Math.abs(instance.pointerBaseSpeed) < newPointerSpeed ? newPointerSpeed : instance.pointerBaseSpeed;
        }

    }

    @Override
    public void onMiss()
    {
        tickCount = 0;
        Minecraft.getInstance().player.playSound(SoundEvents.BLAZE_BURN, 0.9f, 1f);
        Minecraft.getInstance().player.playSound(SoundEvents.BLAZE_SHOOT, 0.3f, 0.6f);
        Minecraft.getInstance().player.playSound(SoundEvents.FIRE_EXTINGUISH, 0.2f, 0.7f);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        instance.pointerSpeed = instance.pointerBaseSpeed;
    }

    @Override
    public void renderForeground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(guiGraphics, partialTick, width, height);
        float alpha = 1 - (instance.pointerBaseSpeed - instance.pointerSpeed / 2) / (instance.pointerSpeed - instance.pointerSpeed / 2);
        int yoffset = tickCount % 32;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                width / 2 - 8, height / 2 - 8 - 7, 16, 16, 0, yoffset * 16, 16, 16,
                16, 512, ARGB.colorFromFloat(alpha, 1, 1, 1));
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                width / 2 - 8, height / 2 - 8, 16, 16, 0, (yoffset + 8) * 16, 16, 16,
                16, 512, ARGB.colorFromFloat(alpha, 1, 1, 1));
    }
}
