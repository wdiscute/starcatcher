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
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.joml.Quaternionf;

public class Nikdo53Modifier extends AbstractMinigameModifier
{
    public static final ResourceLocation POINTER_SMALL = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_pointer_1.png");
    public static final ResourceLocation POINTER_LARGE = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_pointer_2.png");
    public static final ResourceLocation WHEEL = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_wheel.png");

    public int pointerLayer = 0;
    public int maxPointerLayer;
    public boolean isHoldingLeft = false;
    public boolean isHoldingRight = false;

    public static final MapCodec<Nikdo53Modifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("max_layers").forGetter(mod -> mod.maxPointerLayer),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, Nikdo53Modifier::new));

    public Nikdo53Modifier(int extra_layers, String translationOverride)
    {
        super(translationOverride);
        this.maxPointerLayer = extra_layers;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        instance.modifierData.put(getIdentifier(), maxPointerLayer);
    }

    @Override
    public boolean canHitSpot(FishingMinigameScreen fishingMinigameScreen, ActiveSweetSpot ass)
    {
        return getSpotLayer(ass) == pointerLayer;
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        instance.kimbeMarkerAlpha = 1;
        instance.kimbeMarkerColor = 0x2ce17d;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();

        if (getSpotLayer(ass) == pointerLayer)
        {
            putSpotLayer(ass, getRandomLayer());
            return super.onHit(instance, ass);
        }
        return true;
    }

    @Override
    public void onKeyReleased(FishingMinigameScreen instance, int key, int scanCode, int keyModifiers)
    {
        if (key == getOptions().keyLeft.getKey().getValue())
            isHoldingLeft = false;

        if (key == getOptions().keyRight.getKey().getValue())
            isHoldingRight = false;
    }

    private static Options getOptions()
    {
        return Minecraft.getInstance().options;
    }

    @Override
    public void mouseScrolled(FishingMinigameScreen instance, double mouseX, double mouseY, double scrollX, double scrollY)
    {
        if(scrollY < 0)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            pointerLayer--;
        }

        if(scrollY > 0)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            pointerLayer++;
        }

        if (pointerLayer > maxPointerLayer)
            pointerLayer = maxPointerLayer;

        if (pointerLayer < 0)
            pointerLayer = 0;

        super.mouseScrolled(instance, mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void onKeyPress(FishingMinigameScreen instance, int key, int scanCode, int keyModifiers)
    {
        if (key == getOptions().keyLeft.getKey().getValue())
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            pointerLayer--;
            isHoldingLeft = true;
        }

        if (key == getOptions().keyRight.getKey().getValue())
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            pointerLayer++;
            isHoldingRight = true;
        }

        if (pointerLayer > maxPointerLayer)
            pointerLayer = maxPointerLayer;

        if (pointerLayer < 0)
            pointerLayer = 0;
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        instance.getModifiers().forEach(o ->
        {
            if(o instanceof KimbeMarkerModifier k)
                k.removed = true;
        });
    }

    @Override
    public ActiveSweetSpot onSpotAdded(FishingMinigameScreen instance, ActiveSweetSpot spot)
    {
        int layer = getRandomLayer();
        putSpotLayer(spot, layer);
        return super.onSpotAdded(instance, spot);
    }

    private int getRandomLayer()
    {
        return Minecraft.getInstance().level.getRandom().nextIntBetweenInclusive(0, maxPointerLayer);
    }

    @Override
    public void renderOnPointer(FishingMinigameScreen instance, GuiGraphics guiGraphics, PoseStack poseStack, float partialTick)
    {
        if (pointerLayer == 0)
            FishingMinigameScreen.renderPoseCentered(guiGraphics, POINTER_SMALL, 128);
        else
            FishingMinigameScreen.renderPoseCentered(guiGraphics, POINTER_LARGE, 128);
    }

    @Override
    public void renderOnSweetSpot(FishingMinigameScreen instance, GuiGraphics guiGraphics, PoseStack poseStack, ActiveSweetSpot ass, float partialTick)
    {
        if (ass.behaviour == null) return;

        poseStack.pushPose();

        int layer = getSpotLayer(ass);

        poseStack.translate(0, -9 * layer, 0);

        // Dim when not in use
        if (pointerLayer != layer)
            RenderSystem.setShaderColor(0.3f, 0.3f, 0.3f, 1);

        ass.behaviour.render(guiGraphics, poseStack, partialTick, instance, ass);

        RenderSystem.setShaderColor(1, 1, 1, 1);

        poseStack.popPose();
    }

    @Override
    public boolean shouldDarkenWheel(FishingMinigameScreen instance)
    {
        return pointerLayer != 0;
    }

    @Override
    public void renderBackground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(instance, guiGraphics, partialTick, width, height);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        // kapiten reference!1!1!1!1!!
        poseStack.translate(width >> 1, height >> 1, 0);


        for (int i = maxPointerLayer; i > 0; i--)
        {
            // Dim when not in use
            if(pointerLayer != i)
                RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1);

            float increase = (i - 1) * 0.22f + 1;

            guiGraphics.blit(WHEEL,
                    (int) (-48 * increase), (int) (-48 * increase),
                    (int) (96 * increase), (int) (96 * increase),
                    0, 0,
                    96, 96,
                    96, 96);

            RenderSystem.setShaderColor(1, 1, 1, 1);
        }


        poseStack.popPose();
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        instance.kimbeMarkerAlpha = 1;
        instance.kimbeMarkerColor = 0xff6767;
        instance.kimbeMarkerPos = instance.getPointerPosPrecise();

        super.onMiss(instance);
    }

    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        float centerX = (float) width / 2;
        float centerY = (float) height / 2;

        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(instance.kimbeMarkerPos)));
        poseStack.translate(-centerX, -centerY, 0);

        RenderSystem.setShaderColor(
                (float) Utils.intToRed(instance.kimbeMarkerColor) / 255,
                (float) Utils.intToGreen(instance.kimbeMarkerColor) / 255,
                (float) Utils.intToBlue(instance.kimbeMarkerColor) / 255,
                instance.kimbeMarkerAlpha);
        RenderSystem.enableBlend();

        guiGraphics.renderOutline((int) centerX, (int) centerY - 34 - maxPointerLayer * 7, 2, 34 + maxPointerLayer * 7, 0xffffffff);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        poseStack.popPose();

        //render A
        guiGraphics.blit(instance.texture, width / 2 - 50, height / 2 + 50, 32, 16,
                112, isHoldingLeft ? 80 : 64, 32, 16, 256, 256);

        //render D
        guiGraphics.blit(instance.texture, width / 2 + 18, height / 2 + 50, 32, 16,
                144, isHoldingRight ? 80 : 64, 32, 16, 256, 256);
    }

    @Override
    public boolean disablePointerRendering(FishingMinigameScreen instance)
    {
        return true;
    }

    @Override
    public boolean disableSweetSpotRendering(FishingMinigameScreen instance, ActiveSweetSpot spot)
    {
        return true;
    }

    private static int getSpotLayer(ActiveSweetSpot spot)
    {
        return (int) spot.extraData.get(53);
    }

    private static void putSpotLayer(ActiveSweetSpot spot, int layer)
    {
        spot.extraData.put(53, layer);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("multi_layer");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[Nikdo53Modifier@" + Integer.toHexString(hashCode()) + "]";
    }
}
