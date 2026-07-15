package com.wdiscute.starcatcher.shaders;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GoldRenderer implements AutoCloseable {
    public final Map<ResourceLocation, GoldTextureInstance> cache = new HashMap<>();
    public static final GoldRenderer INSTANCE = new GoldRenderer();

    public static TextureAtlasSprite getItemSprite(ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();

        BakedModel model = minecraft.getItemRenderer().getModel(stack, minecraft.level, minecraft.player, 0);

        return model.getParticleIcon(ModelData.EMPTY);
    }

    public static ResourceLocation getTextureLoc(ResourceLocation resourceLoc) {
        return ResourceLocation.fromNamespaceAndPath(resourceLoc.getNamespace(), "textures/" + resourceLoc.getPath() + ".png");
    }

    public GoldTextureInstance getOrCreateItem(ItemStack stack, boolean cull){
        TextureAtlasSprite sprite = getItemSprite(stack);
        ResourceLocation loc = sprite.contents().name();
        return cache.computeIfAbsent(loc, l -> GoldTextureInstance.fromItemStack(l, cull));
    }

    public GoldTextureInstance getOrCreateEntity(ResourceLocation loc, Function<ResourceLocation, RenderType> renderTypeGetter){
        return cache.computeIfAbsent(loc, l -> GoldTextureInstance.fromEntity(l, renderTypeGetter));
    }

    @Override
    public void close() {
        cache.values().forEach(GoldTextureInstance::close);
        cache.clear();
    }

    public static class GoldTextureInstance implements AutoCloseable{
        public final DynamicTexture texture;
        public final RenderType renderType;

        public GoldTextureInstance(ResourceLocation loc, Function<ResourceLocation, RenderType> renderTypeGetter) {
            this.texture = recolorTexture(getNativeImage(getTextureLoc(loc)));
            ResourceLocation resourcelocation = Minecraft.getInstance().getTextureManager().register("starcatcher_gold/" + loc.getPath(), this.texture);
            this.renderType = renderTypeGetter.apply(resourcelocation);
        }


        public static GoldTextureInstance fromItemStack(ResourceLocation loc, boolean cull){
            return new GoldTextureInstance(loc, cull ? RenderType::entityTranslucentCull : RenderType::itemEntityTranslucentCull);
        }

        public static GoldTextureInstance fromEntity(ResourceLocation loc, Function<ResourceLocation, RenderType> renderTypeGetter){
            return new GoldTextureInstance(loc, renderTypeGetter);
        }


        public static NativeImage getNativeImage(ResourceLocation loc){
            try (InputStream stream = Minecraft.getInstance().getResourceManager().getResource(loc).orElseThrow().open()) {

                return NativeImage.read(stream);

            } catch (Exception e) {;
                throw new RuntimeException(e);
            }
        }

        public static DynamicTexture recolorTexture(NativeImage image){

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int colorOriginalRGBA = image.getPixelRGBA(x, y);
                    int colorRecoloredRGBA = GoldShader.recolorGold(colorOriginalRGBA);
                    image.setPixelRGBA(x, y, colorRecoloredRGBA);
                }
            }


            return new DynamicTexture(image);
        }

        @Override
        public void close() {
            texture.close();
        }
    }

}