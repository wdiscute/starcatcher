package com.wdiscute.starcatcher.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.IrisShadersCompat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class SCRenderTypes extends RenderType {
    public SCRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    static ShaderInstance rendertypeGuiFadeShader;
    static ShaderInstance goldItemShader;


    public static ShaderInstance getRendertypeGuiFadeShader() {
        return rendertypeGuiFadeShader;
    }

    public static ShaderInstance getGoldItemShader() {
        return goldItemShader;
    }

    public static final Function<ResourceLocation, RenderType> RENDER_TYPE_GOLD = Util.memoize(loc ->
            create(Starcatcher.rl("gold_item").toString(),
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    1536,
                    true,
                    true,
                    RenderType.CompositeState.builder()
                            .setShaderState(new RenderStateShard.ShaderStateShard(() -> IrisShadersCompat.withEntityTranslucentFallback(SCRenderTypes::getGoldItemShader)))
                            .setTextureState(new RenderStateShard.TextureStateShard(loc, false, false))
                            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                            .setOutputState(ITEM_ENTITY_TARGET)
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                            .createCompositeState(true)));

    public static final RenderType RENDER_TYPE_GOLD_ITEM = RENDER_TYPE_GOLD.apply(TextureAtlas.LOCATION_BLOCKS);

    @SubscribeEvent
    static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), Starcatcher.rl("gui_fade"), DefaultVertexFormat.POSITION), (shader) -> rendertypeGuiFadeShader = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), Starcatcher.rl("gold_item"), DefaultVertexFormat.NEW_ENTITY),  (shader) -> goldItemShader = shader);
    }

}
