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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(CuboidItemModelWrapper.class)
public class CuboidItemModelWrapperMixin {

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z"))
    public boolean update(List<BakedQuad> instance, @NotNull Collection<? extends BakedQuad> collection, Operation<Boolean> original,
                          @Local(argsOnly = true, name = "item") ItemStack stack, @Local(argsOnly = true, name = "output") ItemStackRenderState output) {
        if (!Rarity.isGolden(stack))
            return original.call(instance, collection);

        output.appendModelIdentityElement("isGolden");
        List<BakedQuad> list = new ArrayList<>();
        for (BakedQuad quad : collection) {
            GoldRenderer.GoldTextureInstance gold = GoldRenderer.INSTANCE.getOrCreateItem(quad);
            list.add(BakedModelRemapper.remapQuad(quad, quad.materialInfo().sprite(),
                    info -> new BakedQuad.MaterialInfo(info.sprite(), info.layer(), gold.renderType, info.tintIndex(), info.shade(), info.lightEmission(), info.ambientOcclusion())
            ));
        }

        return original.call(instance, list);
    }

}
