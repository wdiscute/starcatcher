package com.wdiscute.starcatcher.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
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
    static ShaderInstance goldItemShader;

    public static final RenderStateShard.TexturingStateShard TEXTURING_GOLD_FISH_GLINT_ITEM = new RenderStateShard.TexturingStateShard(
            "entity_glint_texturing", SCRenderTypes::setupItemGlint, RenderSystem::resetTextureMatrix
    );

    public static final RenderStateShard.TexturingStateShard TEXTURING_GOLD_FISH_GLINT_ENTITY = new RenderStateShard.TexturingStateShard(
            "entity_glint_texturing", SCRenderTypes::setupEntityGlint, RenderSystem::resetTextureMatrix
    );

    public static final RenderType RENDERTYPE_GOLD_FISH_GLINT_ITEM = RenderType.create(
            Starcatcher.rl("gold_fish_glint_item").toString(),
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
                    .setTexturingState(TEXTURING_GOLD_FISH_GLINT_ITEM)
                    .createCompositeState(true)
    );

    public static final RenderType RENDERTYPE_GOLD_FISH_GLINT_ENTITY = RenderType.create(
            Starcatcher.rl("gold_fish_glint_entity").toString(),
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(Starcatcher.rl("textures/item/gold_fish_shine.png"), false, true))
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setTexturingState(TEXTURING_GOLD_FISH_GLINT_ENTITY)
                    .createCompositeState(true)
    );

    public static void setupItemGlint()
    {
        float speedSec = 5f;

        long speedMs = (long)(speedSec * 1000);

        long t = System.currentTimeMillis() % (speedMs);
        float value = (float) t / speedMs;

        Matrix4f matrix4f = new Matrix4f()
                .translation(0.0f, value, 0.0F)
                .scale(1, 1 / 10f, 1)
                .rotate(Axis.ZP.rotationDegrees(-20f));
        RenderSystem.setTextureMatrix(matrix4f);
    }

    public static void setupEntityGlint()
    {
        float speedSec = 5f;

        long speedMs = (long)(speedSec * 1000);

        long t = System.currentTimeMillis() % (speedMs);
        float value = (float) t / speedMs;

        Matrix4f matrix4f = new Matrix4f()
                .translation(0.0f, value, 0.0F)
                .scale(0.5f)
                .rotate(Axis.ZP.rotationDegrees(-20f));
        RenderSystem.setTextureMatrix(matrix4f);
    }


    @SubscribeEvent
    static void registerShaders(RegisterShadersEvent event) throws IOException
    {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), Starcatcher.rl("gold_item"), DefaultVertexFormat.NEW_ENTITY), (shader) -> goldItemShader = shader);
    }
}
