package com.wdiscute.starcatcher.shaders;

import com.mojang.blaze3d.platform.NativeImage;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.model.data.ModelData;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GoldRenderer implements AutoCloseable
{
    public final Map<Identifier, GoldTextureInstance> cache = new HashMap<>();
    public static final GoldRenderer INSTANCE = new GoldRenderer();

    public static Identifier getTextureLoc(Identifier resourceLoc)
    {
        return Utils.rl(resourceLoc.getNamespace(), "textures/" + resourceLoc.getPath() + ".png");
    }

    public GoldTextureInstance getOrCreateItem(BakedQuad quad)
    {
        TextureAtlasSprite sprite = quad.materialInfo().sprite();
        Identifier loc = sprite.contents().name();
        return cache.computeIfAbsent(loc, l -> GoldTextureInstance.fromItemStack(l, quad));
    }

    public GoldTextureInstance getOrCreateEntity(Identifier loc, Function<Identifier, RenderType> renderTypeGetter)
    {
        return cache.computeIfAbsent(loc, l -> GoldTextureInstance.fromEntity(l, renderTypeGetter));
    }

    @Override
    public void close()
    {
        cache.values().forEach(GoldTextureInstance::close);
        cache.clear();
    }

    public static class GoldTextureInstance implements AutoCloseable
    {
        public final DynamicTexture texture;
        public final RenderType renderType;
        public final Identifier id;

        public GoldTextureInstance(Identifier loc, Function<Identifier, RenderType> renderTypeGetter)
        {
            Identifier rl = Starcatcher.rl("starcatcher_gold/" + loc.getPath());
            this.id = rl;
            this.texture = recolorTexture(getNativeImage(getTextureLoc(loc)), loc.getPath());
            Minecraft.getInstance().getTextureManager().register(rl, this.texture);
            this.renderType = renderTypeGetter.apply(rl);
        }



        public static GoldTextureInstance fromItemStack(Identifier loc, BakedQuad quad)
        {
            boolean translucent = quad.materialInfo().layer() == ChunkSectionLayer.TRANSLUCENT;
            return new GoldTextureInstance(loc, translucent ? RenderTypes::entityTranslucent : RenderTypes::entityCutoutCull);
        }

        public static GoldTextureInstance fromEntity(Identifier loc, Function<Identifier, RenderType> renderTypeGetter)
        {
            return new GoldTextureInstance(loc, renderTypeGetter);
        }


        public static NativeImage getNativeImage(Identifier loc)
        {
            try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(loc).orElseThrow(() -> new IllegalArgumentException("Resource not found: " + loc)).open())
            {

                return NativeImage.read(stream);

            } catch (Exception e)
            {

                throw new RuntimeException(e);
            }
        }

        public static DynamicTexture recolorTexture(NativeImage image, String label)
        {

            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    int colorOriginalRGBA = image.getPixel(x, y);
                    int colorRecoloredRGBA = GoldShader.recolorGold(colorOriginalRGBA);
                    image.setPixel(x, y, colorRecoloredRGBA);
                }
            }


            return new DynamicTexture(() -> label, image);
        }

        @Override
        public void close()
        {
            texture.close();
        }
    }

}