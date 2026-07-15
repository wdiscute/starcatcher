package com.wdiscute.starcatcher.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.wdiscute.starcatcher.registry.SCRenderTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

import java.util.function.Supplier;

public class ShaderUtils {

    /**
     * each of the vectors holds a start value and an end value (clamped 0.0 - 1.0)
     */
    public static void setUpFadeShader(float width, float height,
                                       Vec2 fadeLeft, Vec2 fadeRight, Vec2 fadeUp, Vec2 fadeDown, boolean invertAlpha) {

        ShaderInstance shader = SCRenderTypes.getRendertypeGuiFadeShader();
        if (shader == null) {
            throw new IllegalStateException("Rendertype GuiFadeShader is null");
        }

        //because opengl is tweaking
        if (fadeRight.x == fadeRight.y){
            fadeRight = new Vec2(fadeRight.x, fadeRight.y + 0.001f);
        }

        if (fadeDown.x == fadeDown.y){
            fadeDown = new Vec2(fadeDown.x, fadeDown.y + 0.001f);
        }

        shader.safeGetUniform("FADE_LEFT").set(fadeLeft.x, fadeLeft.y);
        shader.safeGetUniform("FADE_RIGHT").set(fadeRight.x, fadeRight.y);
        shader.safeGetUniform("FADE_UP").set(fadeUp.x, fadeUp.y);
        shader.safeGetUniform("FADE_DOWN").set(fadeDown.x, fadeDown.y);
        shader.safeGetUniform("u_resolution").set(width, height);
        shader.safeGetUniform("u_invertAlpha").set(invertAlpha ? 1 : 0);

    }

    static void innerBlit(
            Supplier<ShaderInstance> shader, Runnable shaderSetUp, GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        RenderSystem.setShaderTexture(0, atlasLocation);
        RenderSystem.setShader(shader);
        RenderSystem.enableBlend();
        shaderSetUp.run();

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).setUv(minU, minV);
        bufferbuilder.addVertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).setUv(minU, maxV);
        bufferbuilder.addVertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).setUv(maxU, maxV);
        bufferbuilder.addVertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).setUv(maxU, minV);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

        RenderSystem.disableBlend();
    }



    public static void blitWithShader(Supplier<ShaderInstance> shader, Runnable shaderSetUp, GuiGraphics guiGraphics, ResourceLocation atlasLocation, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        blitWithShader(shader, shaderSetUp, guiGraphics, atlasLocation, x, y, 0, (float)uOffset, (float)vOffset, uWidth, vHeight, 256, 256);
    }


    public static void blitWithShader(
            Supplier<ShaderInstance> shader, Runnable shaderSetUp, GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int blitOffset,
            float uOffset,
            float vOffset,
            int uWidth,
            int vHeight,
            int textureWidth,
            int textureHeight
    ) {
        blitWithShader(
                shader,shaderSetUp, guiGraphics,
                atlasLocation,
                x,
                x + uWidth,
                y,
                y + vHeight,
                blitOffset,
                uWidth,
                vHeight,
                uOffset,
                vOffset,
                textureWidth,
                textureHeight
        );
    }

    public static void blitWithShader(
            Supplier<ShaderInstance> shader, Runnable shaderSetUp, GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int width,
            int height,
            float uOffset,
            float vOffset,
            int uWidth,
            int vHeight,
            int textureWidth,
            int textureHeight
    ) {
        blitWithShader(
                shader,shaderSetUp, guiGraphics,
                atlasLocation, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight
        );
    }

    public static void blitWithShader(
            Supplier<ShaderInstance> shader,Runnable shaderSetUp, GuiGraphics guiGraphics,
            ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight
    ) {
        blitWithShader( shader,shaderSetUp, guiGraphics, atlasLocation, x, y, width, height, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }

    static void blitWithShader(
            Supplier<ShaderInstance> shader,Runnable shaderSetUp, GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            int uWidth,
            int vHeight,
            float uOffset,
            float vOffset,
            int textureWidth,
            int textureHeight
    ) {
        innerBlit(
                shader, shaderSetUp, guiGraphics,
                atlasLocation,
                x1,
                x2,
                y1,
                y2,
                blitOffset,
                (uOffset + 0.0F) / (float)textureWidth,
                (uOffset + (float)uWidth) / (float)textureWidth,
                (vOffset + 0.0F) / (float)textureHeight,
                (vOffset + (float)vHeight) / (float)textureHeight
        );
    }


}
