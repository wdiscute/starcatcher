package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.RenderTypeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTypeHelper.class)
public class RenderTypeHelperMixin {

    @Inject(method = "getFallbackItemRenderType", at = @At("HEAD"), cancellable = true)
    private static void getFallbackItemRenderType(ItemStack stack, BakedModel model, boolean cull, CallbackInfoReturnable<RenderType> cir) {
        if (FishProperties.Rarity.isGolden(stack)) {
            cir.setReturnValue(SCRenderTypes.RENDER_TYPE_GOLD_ITEM);
        }
    }

}
