package com.wdiscute.starcatcher.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.SCRenderTypes;
import com.wdiscute.starcatcher.shaders.BakedModelRemapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"))
    public void render(ItemRenderer instance, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer vertexConsumer, Operation<Void> original,
                       @Local(argsOnly = true) MultiBufferSource bufferSource){
        boolean isGolden = Rarity.isGolden(stack);
        BakedModel bakedModel = isGolden ? BakedModelRemapper.getOrCreate(model) : model;

        original.call(
                instance,
                bakedModel,
                stack,
                combinedLight,
                combinedOverlay,
                poseStack,
                vertexConsumer
        );

        if (isGolden) {
            original.call(
                    instance,
                    bakedModel,
                    stack,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource.getBuffer(SCRenderTypes.RENDERTYPE_GOLD_FISH_GLINT)
            );
        }


    }
}
