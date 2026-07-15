package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.joml.Quaternionf;

public class TeleportModifier extends AbstractMinigameModifier
{
    public static final ResourceLocation OVERLAY = Starcatcher.rl("textures/gui/minigame/modifiers/teleport.png");

    private float kimbePosition = Utils.r.nextInt(359);

    public static final MapCodec<TeleportModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, TeleportModifier::new));

    public TeleportModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        instance.getModifiers().forEach(o ->
        {
            if (o instanceof KimbeMarkerModifier k)
            {
                k.removed = true;
            }
        });
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.6f, 1f);
        instance.addParticles(instance.handlePos, 20, 0x7935de);
        instance.handlePos = kimbePosition;
        kimbePosition = ass.pos;
        return super.onHit(instance, ass);
    }

    @Override
    public boolean skipHitParticles(FishingMinigameScreen instance)
    {
        return true;
    }

    @Override
    public boolean skipHitSound(FishingMinigameScreen instance)
    {
        return true;
    }

    @Override
    public boolean skipMissSound(FishingMinigameScreen instance)
    {
        return true;
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        Minecraft.getInstance().player.playSound(SoundEvents.SHULKER_AMBIENT, 0.6f, 1f);
        instance.addParticles(instance.handlePos, 20, 0x7935de);
        instance.handlePos = Utils.r.nextFloat(360);
    }

    @Override
    public void renderBackground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(instance, guiGraphics, partialTick, width, height);

        int layers = (int) instance.modifierData.getOrDefault(Starcatcher.rl("multi_layer_modifier"), 0);

        float increase = layers * 0.22f + 1;

        int posX = (int) ((float) width / 2 - 48 * increase);
        int posY = (int) ((float) height / 2 - 48 * increase);

        guiGraphics.blit(OVERLAY,
                posX, posY,
                (int) (96 * increase), (int) (96 * increase),
                0, 0,
                96, 96,
                96, 96
        );
    }


    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(instance, guiGraphics, partialTick, width, height);
        renderKimbeMarker(guiGraphics, width, height);
    }

    public void renderKimbeMarker(GuiGraphics guiGraphics, int width, int height)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        float centerX = width / 2f;
        float centerY = height / 2f;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(kimbePosition + 180)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(
                (float) Utils.intToRed(0x653bea) / 255,
                (float) Utils.intToGreen(0x653bea) / 255,
                (float) Utils.intToBlue(0x653bea) / 255,
                0.6f);

        RenderSystem.enableBlend();

        guiGraphics.renderOutline(width / 2, height / 2 + 8, 2, 28, 0xffffffff);


        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("teleport");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[TeleportModifier@" + Integer.toHexString(hashCode()) + "]";
    }
}
