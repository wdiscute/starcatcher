package com.wdiscute.starcatcher.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.joml.Matrix4f;

import java.io.IOException;

import static net.minecraft.client.renderer.RenderStateShard.*;

@EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class SCRenderTypes
{

    static ShaderInstance rendertypeGuiFadeShader;
    static ShaderInstance goldItemShader;

    public static ShaderInstance getRendertypeGuiFadeShader()
    {
        return rendertypeGuiFadeShader;
    }

    public static final RenderStateShard.TexturingStateShard TEXTURING_GOLD_FISH_GLINT = new RenderStateShard.TexturingStateShard(
            "entity_glint_texturing", SCRenderTypes::setupGoldGlintTexturing, RenderSystem::resetTextureMatrix
    );

    public static final RenderType RENDERTYPE_GOLD_FISH_GLINT = RenderType.create(
            Starcatcher.rl("gold_fish_glint").toString(),
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(Starcatcher.rl("textures/item/gold_fish_shine.png"), false, true))
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setTexturingState(TEXTURING_GOLD_FISH_GLINT)
                    .createCompositeState(true)
    );

    public static void setupGoldGlintTexturing()
    {
        //TODO make it affected by accessability glint speed

        float speedSec = 5f;

        long speedMs = (long)(speedSec * 1000);

        long t = System.currentTimeMillis() % (speedMs);
        double value = (double) t / speedMs;

        Matrix4f matrix4f = new Matrix4f()
                .translation(0.0f, (float) (0 + value), 0.0F)
                .scale(1, 1 / 10f, 1)
                .rotate(Axis.ZP.rotationDegrees(-20f));
        RenderSystem.setTextureMatrix(matrix4f);
    }


    @SubscribeEvent
    static void registerShaders(RegisterShadersEvent event) throws IOException
    {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), Starcatcher.rl("gui_fade"), DefaultVertexFormat.POSITION), (shader) -> rendertypeGuiFadeShader = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), Starcatcher.rl("gold_item"), DefaultVertexFormat.NEW_ENTITY), (shader) -> goldItemShader = shader);
    }

}
