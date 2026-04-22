package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.joml.Matrix3x2fStack;

import java.util.function.Supplier;

public class Nikdo53Modifier extends AbstractMinigameModifier
{
    public static final Identifier POINTER_SMALL = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_pointer_1.png");
    public static final Identifier POINTER_LARGE = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_pointer_2.png");
    public static final Identifier WHEEL = Starcatcher.rl("textures/gui/minigame/modifiers/nikdo53_wheel.png");

    public int pointerLayer = 0;
    public int maxPointerLayer;
    public boolean isHoldingLeft = false;
    public boolean isHoldingRight = false;

    public static final MapCodec<Nikdo53Modifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("layers", 2).forGetter(mod -> mod.maxPointerLayer + 1)
            ).apply(instance, Nikdo53Modifier::new));


    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.NIKDO53_MODIFIER;
    }

    public Nikdo53Modifier(){
        this(2);
    }

    // This one probably shouldn't be used, but it sure is funny
    public Nikdo53Modifier(int layers){
       this.maxPointerLayer = layers - 1;
    }

    @Override
    public boolean onHit(ActiveSweetSpot spot) {
        if (getSpotLayer(spot) == pointerLayer) {
            putSpotLayer(spot, getRandomLayer());
            return super.onHit(spot);
        }
        return true;
    }

    @Override
    public void onKeyReleased(int key, int scanCode, int keyModifiers) {
        if (key == getOptions().keyLeft.getKey().getValue()) {
            isHoldingLeft = false;
        }

        if (key == getOptions().keyRight.getKey().getValue()){
            isHoldingRight = false;
        }

    }

    private static Options getOptions() {
        return Minecraft.getInstance().options;
    }

    @Override
    public void onKeyPress(int key, int scanCode, int keyModifiers) {
        if (key == getOptions().keyLeft.getKey().getValue()) {
            pointerLayer--;
            isHoldingLeft = true;
        }

       if (key == getOptions().keyRight.getKey().getValue()){
           pointerLayer++;
           isHoldingRight = true;
       }

       if (pointerLayer > maxPointerLayer)
           pointerLayer = maxPointerLayer;

       if (pointerLayer < 0)
           pointerLayer = 0;
    }

    @Override
    public ActiveSweetSpot onSpotAdded(ActiveSweetSpot spot) {
        int layer = getRandomLayer();
        putSpotLayer(spot, layer);
        return super.onSpotAdded(spot);
    }

    private int getRandomLayer() {
        return Minecraft.getInstance().level.getRandom().nextIntBetweenInclusive(0, maxPointerLayer);
    }

    @Override
    public void renderOnPointer(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, float partialTick)
    {
        if (pointerLayer == 0) {
            FishingMinigameScreen.renderPoseCentered(guiGraphics, POINTER_SMALL, 128);
        } else {
            FishingMinigameScreen.renderPoseCentered(guiGraphics, POINTER_LARGE, 128);
        }
    }

    @Override
    public void renderOnSweetSpot(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, ActiveSweetSpot spot, float partialTick) {
        if (spot.behaviour == null) return;

        poseStack.pushMatrix();

        int layer = getSpotLayer(spot);

        poseStack.translate(0, -9 * layer);

        // Dim when not in use
        spot.behaviour.render(guiGraphics, poseStack, partialTick, pointerLayer != layer ? 0xff7b7b7b : 0xffffffff);

        poseStack.popMatrix();
    }

    @Override
    public void renderBackground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height) {
        super.renderBackground(guiGraphics, partialTick, width, height);
        Matrix3x2fStack poseStack = guiGraphics.pose();

        //render A
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FishingMinigameScreen.TEXTURE, width / 2 - 40, height / 2 + 40, 32, 16, isHoldingLeft ? 32 : 0, 128, 32, 16, 256, 256);

        //render D
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FishingMinigameScreen.TEXTURE, width / 2 + 8, height / 2 + 40, 32, 16, isHoldingRight ? 32 : 0, 144, 32, 16, 256, 256);


        poseStack.pushMatrix();

        // kapiten reference!1!1!1!1!!
        poseStack.translate(width >> 1, height >> 1);

        // Dim when not in use
        FishingMinigameScreen.renderPoseCentered(guiGraphics, WHEEL, 128, pointerLayer != 1 ? 0x7b7b7b : 0xffffffff);

        poseStack.popMatrix();
    }

    @Override
    public boolean disablePointerRendering() {
        return true;
    }

    @Override
    public boolean disableSweetSpotRendering(ActiveSweetSpot spot) {
        return true;
    }

    private static int getSpotLayer(ActiveSweetSpot spot) {
        return (int) spot.extraData.get(53);
    }

    private static void putSpotLayer(ActiveSweetSpot spot, int layer) {
        spot.extraData.put(53, layer);
    }
}
