package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix3x2fc;
import org.joml.Quaternionf;

import java.util.function.Supplier;

public class TeleportModifier extends AbstractTimedModifier
{
    public static final Identifier OVERLAY = Starcatcher.rl("textures/gui/minigame/modifiers/teleport.png");

    private float kimbePosition = U.r.nextInt(359);

    public static final MapCodec<TeleportModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength)
            ).apply(instance, TeleportModifier::new));

    public TeleportModifier(int length) {
        super(length);
    }

    public TeleportModifier() {
        super();
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.TELEPORT;
    }

    @Override
    public boolean onHit(ActiveSweetSpot ass)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.6f, 1f);
        instance.addParticles(instance.pointerPos, 20, 0x7935de);
        instance.pointerPos = kimbePosition;
        kimbePosition = ass.pos;
        return super.onHit(ass);
    }

    @Override
    public boolean skipRenderingKimbeMarker()
    {
        return true;
    }

    @Override
    public boolean skipHitParticles()
    {
        return true;
    }

    @Override
    public boolean skipHitSound()
    {
        return true;
    }

    @Override
    public boolean skipMissSound()
    {
        return true;
    }

    @Override
    public void onMiss()
    {
        super.onMiss();
        Minecraft.getInstance().player.playSound(SoundEvents.SHULKER_AMBIENT, 0.6f, 1f);
        instance.addParticles(instance.pointerPos, 20, 0x7935de);
        instance.pointerPos = U.r.nextFloat(360);
    }

    @Override
    public void renderBackground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(guiGraphics, partialTick, width, height);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                OVERLAY, width / 2 - 48, height / 2 - 48,
                96, 96, 0, 0, 96, 96, 96, 96);
    }


    @Override
    public void renderForeground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(guiGraphics, partialTick, width, height);
        renderKimbeMarker(guiGraphics, width, height);
    }

    public void renderKimbeMarker(GuiGraphicsExtractor guiGraphics, int width, int height)
    {
        Matrix3x2fStack poseStack = guiGraphics.pose();
        poseStack.pushMatrix();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY);
        poseStack.mul((Matrix3x2fc) new Quaternionf().rotateZ((float) Math.toRadians(kimbePosition)));
        poseStack.translate(-centerX, -centerY);

        int color = ARGB.colorFromFloat(
                (float) U.intToRed(0x653bea) / 255,
                (float) U.intToGreen(0x653bea) / 255,
                (float) U.intToBlue(0x653bea) / 255,
                0.6f);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                FishingMinigameScreen.TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                64, 64, 128, 128, 64, 64, 256, 256, color);

        poseStack.popMatrix();
    }

}
