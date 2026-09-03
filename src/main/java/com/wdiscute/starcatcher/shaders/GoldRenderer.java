package com.wdiscute.starcatcher.shaders;

import com.mojang.blaze3d.platform.NativeImage;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
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

    public static TextureAtlasSprite getItemSprite(ItemStack stack)
    {
        Minecraft minecraft = Minecraft.getInstance();

        //BakedModel model = minecraft.getItemModelResolver().getModel(stack, minecraft.level, minecraft.player, 0);

        return null; //model.getParticleIcon(ModelData.EMPTY);
    }

    public static Identifier getTextureLoc(Identifier resourceLoc)
    {
        return Utils.rl(resourceLoc.getNamespace(), "textures/" + resourceLoc.getPath() + ".png");
    }

    public GoldTextureInstance getOrCreateItem(ItemStack stack, boolean cull)
    {
        TextureAtlasSprite sprite = getItemSprite(stack);
        Identifier loc = sprite.contents().name();
        return cache.computeIfAbsent(loc, l -> GoldTextureInstance.fromItemStack(l, cull));
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

        public GoldTextureInstance(Identifier loc, Function<Identifier, RenderType> renderTypeGetter)
        {
            this.texture = recolorTexture(getNativeImage(getTextureLoc(loc)));
            Identifier rl = Starcatcher.rl("starcatcher_gold/" + loc.getPath());
            Minecraft.getInstance().getTextureManager().register(rl, this.texture);
            this.renderType = renderTypeGetter.apply(rl);
        }


        public static GoldTextureInstance fromItemStack(Identifier loc, boolean cull)
        {
            return null; //new GoldTextureInstance(loc, cull ? RenderType::entityTranslucentCull : RenderType::itemEntityTranslucentCull);
        }

        public static GoldTextureInstance fromEntity(Identifier loc, Function<Identifier, RenderType> renderTypeGetter)
        {
            return new GoldTextureInstance(loc, renderTypeGetter);
        }


        public static NativeImage getNativeImage(Identifier loc)
        {
            try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(loc).orElseThrow().open())
            {

                return NativeImage.read(stream);

            } catch (Exception e)
            {
                ;
                throw new RuntimeException(e);
            }
        }

        public static DynamicTexture recolorTexture(NativeImage image)
        {

            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    //int colorOriginalRGBA = image.getPixelRGBA(x, y);
                    //int colorRecoloredRGBA = GoldShader.recolorGold(colorOriginalRGBA);
                    //image.setPixelRGBA(x, y, colorRecoloredRGBA);
                }
            }


            return null; //new DynamicTexture(image);
        }

        @Override
        public void close()
        {
            texture.close();
        }
    }

}