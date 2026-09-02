package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;

public class Nikdo53Modifier extends AbstractMinigameModifier
{
    public static final ScreenUtils.Image HANDLE_SMALL = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_handle_1.png"), 128, 128);
    public static final ScreenUtils.Image HANDLE_LARGE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_handle_2.png"), 128, 128);
    public static final Identifier WHEEL = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_wheel.png");

    public int handleLayer = 0;
    public int maxHandleLayer;
    public boolean isHoldingLeft = false;
    public boolean isHoldingRight = false;

    public static final MapCodec<Nikdo53Modifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("max_layers").forGetter(mod -> mod.maxHandleLayer),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, Nikdo53Modifier::new));

    public Nikdo53Modifier(int extra_layers, String translationOverride)
    {
        super(translationOverride);
        this.maxHandleLayer = extra_layers;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        instance.modifierData.put(getIdentifier(), maxHandleLayer);
    }

    @Override
    public boolean canHitSpot(FishingMinigameScreen fishingMinigameScreen, ActiveSweetSpot ass)
    {
        return getSpotLayer(ass) == handleLayer;
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        instance.kimbeMarkerColor = SCColors.GREEN;
        instance.kimbeMarkerPos = instance.getHandlePosPrecise();

        if (getSpotLayer(ass) == handleLayer)
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
        if (scrollY < 0)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            handleLayer--;
        }

        if (scrollY > 0)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            handleLayer++;
        }

        if (handleLayer > maxHandleLayer)
            handleLayer = maxHandleLayer;

        if (handleLayer < 0)
            handleLayer = 0;

        super.mouseScrolled(instance, mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void onKeyPress(FishingMinigameScreen instance, int key, int scanCode, int keyModifiers)
    {
        if (key == getOptions().keyLeft.getKey().getValue())
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            handleLayer--;
            isHoldingLeft = true;
        }

        if (key == getOptions().keyRight.getKey().getValue())
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.6f, 1f);
            handleLayer++;
            isHoldingRight = true;
        }

        if (handleLayer > maxHandleLayer)
            handleLayer = maxHandleLayer;

        if (handleLayer < 0)
            handleLayer = 0;
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        instance.getModifiers().forEach(o ->
        {
            if (o instanceof KimbeMarkerModifier k)
                k.removed = true;
        });

        //decrease kimbe markers alpha
        int color = instance.kimbeMarkerColor;

        int alpha = (color >>> 24) & 0xff;


        alpha = Math.max(0, alpha - 20);

        color = (color & 0x00ffffff) | (alpha << 24);

        instance.kimbeMarkerColor = color;
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
        return Minecraft.getInstance().level.getRandom().nextIntBetweenInclusive(0, maxHandleLayer);
    }

    @Override
    public void renderOnHandle(FishingMinigameScreen instance, GuiGraphicsExtractor g, PoseStack poseStack, float partialTick)
    {
        if (handleLayer == 0)
            HANDLE_SMALL.render(g, -64, -64);
        else
            HANDLE_LARGE.render(g, -64, -64);
    }

    @Override
    public void renderOnSweetSpot(FishingMinigameScreen instance, GuiGraphicsExtractor guiGraphics, PoseStack poseStack, ActiveSweetSpot ass, float partialTick)
    {
        if (ass.behaviour == null) return;

        poseStack.pushPose();

        int layer = getSpotLayer(ass);

        poseStack.translate(0, -9 * layer, 0);

        // Dim when not in use
        if (handleLayer != layer)
            ScreenUtils.setColorF(1, 0.3f, 0.3f, 0.3f);

        ass.behaviour.render(guiGraphics, poseStack, partialTick, instance, ass);

        poseStack.popPose();
    }

    @Override
    public boolean shouldDarkenWheel(FishingMinigameScreen instance)
    {
        return handleLayer != 0;
    }

    @Override
    public void renderBackground(FishingMinigameScreen instance, GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(instance, guiGraphics, partialTick, width, height);
        Matrix3x2fStack poseStack = guiGraphics.pose();
        poseStack.pushMatrix();

        // kapiten reference!1!1!1!1!!
        poseStack.translate(width >> 1, height >> 1);

        for (int i = maxHandleLayer; i > 0; i--)
        {
            // Dim when not in use
            if (handleLayer != i)
                ScreenUtils.setColorF(1, 0.5f, 0.5f, 0.5f);

            float increase = (i - 1) * 0.22f + 1;

            ScreenUtils.Image image = new ScreenUtils.Image(WHEEL, (int) (96 * increase), (int) (96 * increase));
            image.render(guiGraphics, (int) (-48 * increase), (int) (-48 * increase));

        }
        poseStack.popMatrix();
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        instance.kimbeMarkerColor = 0xffff6767;
        instance.kimbeMarkerPos = instance.getHandlePosPrecise();

        super.onMiss(instance);
    }

    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphicsExtractor g, float partialTick, int width, int height)
    {
        Matrix3x2fStack poseStack = g.pose();
        poseStack.pushMatrix();

        float centerX = (float) width / 2;
        float centerY = (float) height / 2;

        poseStack.translate(centerX, centerY);
        poseStack.rotate((float) Math.toRadians(instance.kimbeMarkerPos));
        poseStack.translate(-centerX, -centerY);

        ScreenUtils.outline(g, (int) centerX, (int) centerY - 34 - maxHandleLayer * 7, 2, 34 + maxHandleLayer * 7, instance.kimbeMarkerColor);

        poseStack.pushMatrix();

        //render A
        instance.buttons.render(g, width / 2 - 50, height / 2 + 50,
                48, isHoldingLeft ? 16 : 0, 32, 16);

        //render D
        instance.buttons.render(g, width / 2 + 18, height / 2 + 50,
                80, isHoldingRight ? 16 : 0, 32, 16);
    }

    @Override
    public boolean disableHandleRendering(FishingMinigameScreen instance)
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
    public Identifier getIdentifier()
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
