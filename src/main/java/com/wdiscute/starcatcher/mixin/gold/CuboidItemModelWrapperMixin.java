package com.wdiscute.starcatcher.mixin.gold;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.shaders.BakedModelRemapper;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(CuboidItemModelWrapper.class)
public class CuboidItemModelWrapperMixin
{

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState$LayerRenderState;setQuads(Lnet/minecraft/client/resources/model/geometry/ItemQuads;)V"))
    private void update(ItemStackRenderState.LayerRenderState instance, ItemQuads quads, Operation<Void> original, @Local(argsOnly = true, name = "item") ItemStack item, @Local(argsOnly = true, name = "output") ItemStackRenderState output)
    {
        if (!Rarity.isGolden(item))
        {
            original.call(instance, quads);
            return;
        }

        output.appendModelIdentityElement("isGolden");

        ItemQuads remapped = new ItemQuads(
                remapQuads(quads.all()),
                remapQuads(quads.solid()),
                remapQuads(quads.translucent())
        );

        original.call(instance, remapped);
    }

    private static List<BakedQuad> remapQuads(List<BakedQuad> quads)
    {
        List<BakedQuad> remapped = new ArrayList<>(quads.size());

        for (BakedQuad quad : quads)
        {
            GoldRenderer.GoldTextureInstance gold =
                    GoldRenderer.INSTANCE.getOrCreateItem(quad);

            remapped.add(
                    BakedModelRemapper.remapQuad(
                            quad,
                            quad.materialInfo().sprite(),
                            info -> new BakedQuad.MaterialInfo(
                                    info.sprite(),
                                    info.layer(),
                                    gold.renderType,
                                    info.itemGlintRenderType(),
                                    info.itemGlintSpecialRenderType(),
                                    info.tintIndex(),
                                    info.shadeDirectionOverride(),
                                    info.lightEmission(),
                                    info.ambientOcclusion()
                            )
                    )
            );
        }

        return remapped;
    }
}