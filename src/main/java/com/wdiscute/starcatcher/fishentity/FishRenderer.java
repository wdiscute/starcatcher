package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.HashMap;
import java.util.Map;

public class FishRenderer extends EntityRenderer<FishEntity>
{
    ItemRenderer itemRenderer;
    Map<Item, EntityModel<FishEntity>> map = new HashMap<>();

    public FishRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        itemRenderer = context.getItemRenderer();
        map.put(ModItems.AGAVE_BREAM.get(), new AgaveBream<>(context.bakeLayer(AgaveBream.LAYER_LOCATION)));
        map.put(ModItems.BIGEYE_TUNA.get(), new BigeyeTuna<>(context.bakeLayer(BigeyeTuna.LAYER_LOCATION)));
        map.put(ModItems.BOREAL.get(), new Boreal<>(context.bakeLayer(Boreal.LAYER_LOCATION)));
        map.put(ModItems.CACTIFISH.get(), new CactiFish<>(context.bakeLayer(CactiFish.LAYER_LOCATION)));
        map.put(ModItems.CHARFISH.get(), new Charfish<>(context.bakeLayer(Charfish.LAYER_LOCATION)));
        map.put(ModItems.CRYSTALBACK_BOREAL.get(), new CrystalbackBoreal<>(context.bakeLayer(CrystalbackBoreal.LAYER_LOCATION)));
        map.put(ModItems.CRYSTALBACK_MINNOW.get(), new CrystalbackMinnow<>(context.bakeLayer(CrystalbackMinnow.LAYER_LOCATION)));
        map.put(ModItems.DEEPJAW_HERRING.get(), new DeepjawHerring<>(context.bakeLayer(DeepjawHerring.LAYER_LOCATION)));
        map.put(ModItems.DOWNFALL_BREAM.get(), new DownfallBream<>(context.bakeLayer(DownfallBream.LAYER_LOCATION)));
        map.put(ModItems.DRIFTFIN.get(), new Driftfin<>(context.bakeLayer(Driftfin.LAYER_LOCATION)));
        map.put(ModItems.DRIFTING_BREAM.get(), new DriftingBream<>(context.bakeLayer(DriftingBream.LAYER_LOCATION)));
        map.put(ModItems.DUSKTAIL_SNAPPER.get(), new DusktailSnapper<>(context.bakeLayer(DusktailSnapper.LAYER_LOCATION)));
        map.put(ModItems.LILY_SNAPPER.get(), new LilySnapper<>(context.bakeLayer(LilySnapper.LAYER_LOCATION)));
        map.put(ModItems.PINK_KOI.get(), new PinkKoi<>(context.bakeLayer(PinkKoi.LAYER_LOCATION)));
        map.put(ModItems.SILVERVEIL_PERCH.get(), new SilverveilPerch<>(context.bakeLayer(SilverveilPerch.LAYER_LOCATION)));
        map.put(ModItems.SLUDGE_CATFISH.get(), new SludgeCatfish<>(context.bakeLayer(SludgeCatfish.LAYER_LOCATION)));
        map.put(ModItems.WHITEVEIL.get(), new Whiteveil<>(context.bakeLayer(Whiteveil.LAYER_LOCATION)));
        map.put(ModItems.WILLOW_BREAM.get(), new WillowBream<>(context.bakeLayer(WillowBream.LAYER_LOCATION)));
        map.put(ModItems.WINTERY_PIKE.get(), new WinteryPike<>(context.bakeLayer(WinteryPike.LAYER_LOCATION)));
    }

    @Override
    public void render(FishEntity fish, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(1.0F, -1.0F, -1.0F);

        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));
        //todo fish stuff!!!!!! DONT DELETE
//        poseStack.pushPose();
//
//        poseStack.translate(0, 0.25f, 0);
//
//        poseStack.translate(0.0F, 1.5F, 0.0F);
//        poseStack.scale(-1.0F, -1.0F, 1.0F);
//        poseStack.mulPose(Axis.XP.rotationDegrees(fish.getXRot()));
//        poseStack.mulPose(Axis.YP.rotationDegrees(90 - fish.getYRot()));
//

        if (!fish.isInWater())
        {
            float f = 4.3F * Mth.sin(1F * fish.tickCount + partialTicks);
            poseStack.translate(1.1F, 1.4F, -0.1F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(f));

        }

        if (!fish.getBodyArmorItem().isEmpty())
        {
            if (!renderCustomModel(fish.getBodyArmorItem().getItem(), poseStack, buffer, packedLight))
                this.itemRenderer.renderStatic(
                        fish.getBodyArmorItem(), ItemDisplayContext.FIXED, packedLight,
                        OverlayTexture.NO_OVERLAY, poseStack, buffer, fish.level(), fish.getId());
        }

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(FishEntity fish)
    {
        return null;
    }


    private boolean renderCustomModel(Item fish, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        if(map.containsKey(fish))
        {
            renderModel(BuiltInRegistries.ITEM.getKey(fish).getPath(),map.get(fish), buffer, poseStack, packedLight);
            return true;
        }
        return false;
    }

    private void renderModel(String rl, EntityModel<FishEntity> model, MultiBufferSource buffer, PoseStack poseStack, int packedLight)
    {
        VertexConsumer vertexconsumer = buffer.getBuffer(model.renderType(Starcatcher.rl("textures/entity/fishes/" + rl + ".png")));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
    }


}
