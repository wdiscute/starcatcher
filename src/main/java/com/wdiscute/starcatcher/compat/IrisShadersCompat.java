package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.Starcatcher;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.pathways.HandRenderer;
import net.irisshaders.iris.pipeline.ShaderRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPhase;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.irisshaders.iris.pipeline.programs.ShaderKey;
import net.irisshaders.iris.shadows.ShadowRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

//the private methods crash without iris, im too lazy to split it into multiple classes
public class IrisShadersCompat {
    static ShaderInstance getShaderWithIrisFallback(Supplier<ShaderInstance> shaderInstanceSupplier, Supplier<ShaderKey> shaderKeySupplier){
        ShaderKey shaderKey = shaderKeySupplier.get();
        if (shaderKey != null) {
            return fromShaderKey(shaderKey);
        }

        return shaderInstanceSupplier.get();
    }

    public static ShaderInstance withEntityTranslucentFallback(Supplier<ShaderInstance> shaderInstanceSupplier){
        if (isLoaded())
            return getShaderWithIrisFallback(shaderInstanceSupplier, entityTranslucentKey());
        return shaderInstanceSupplier.get();
    }

    private static @NotNull Supplier<ShaderKey> entityTranslucentKey() {
        return () -> {
            // copied from iris game renderer mixin
            if (ShadowRenderer.ACTIVE) {
                return (ShaderKey.SHADOW_ENTITIES_CUTOUT);
            } else if (HandRenderer.INSTANCE.isActive()) {
                return (HandRenderer.INSTANCE.isRenderingSolid() ? ShaderKey.HAND_CUTOUT_DIFFUSE : ShaderKey.HAND_WATER_DIFFUSE);
            } else if (isBlockEntities()) {
                return (ShaderKey.BE_TRANSLUCENT);
            } else if (shouldOverrideShaders()) {
                return (ShaderKey.ENTITIES_TRANSLUCENT);
            }

            return null;
        };
    }

    static ShaderInstance fromShaderKey(ShaderKey shaderKey){
        WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

        if (pipeline instanceof ShaderRenderingPipeline) {
            ShaderInstance override = ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(shaderKey);

            if (override != null) {
                return override;
            }
        }

        throw new RuntimeException("iris goofed up: " + shaderKey);
    }

    private static boolean isBlockEntities() {
        WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

        return pipeline != null && pipeline.getPhase() == WorldRenderingPhase.BLOCK_ENTITIES;
    }

    private static boolean isEntities() {
        WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

        return pipeline != null && pipeline.getPhase() == WorldRenderingPhase.ENTITIES;
    }

    private static boolean shouldOverrideShaders() {
        WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

        if (pipeline instanceof ShaderRenderingPipeline) {
            return ((ShaderRenderingPipeline) pipeline).shouldOverrideShaders();
        } else {
            return false;
        }

    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded("iris");
    }

}
