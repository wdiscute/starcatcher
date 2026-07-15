package com.wdiscute.starcatcher.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.RenderTypeHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderTypeHelper.class)
public class RenderTypeHelperMixin
{
    @WrapMethod(method = "getFallbackItemRenderType")
    private static RenderType getFallbackItemRenderType(ItemStack stack, BakedModel model, boolean cull, Operation<RenderType> original)
    {
        if (Rarity.isGolden(stack))
        {
            try
            {
                return GoldRenderer.INSTANCE.getOrCreateItem(stack, cull).renderType;
            } catch (Exception e)
            {
                Starcatcher.LOGGER.error(String.valueOf(e));
                e.printStackTrace(); // more robust logging is for pussies
            }
        }
        return original.call(stack, model, cull);
    }

}
