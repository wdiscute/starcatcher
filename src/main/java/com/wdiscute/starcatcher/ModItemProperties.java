package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.fishingbob.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;

public class ModItemProperties
{

    public static void addCustomItemProperties() {
        ItemProperties.register(
                ModItems.STARCATCHER_FISHING_ROD.get(),
                Starcatcher.rl("cast"),
                (stack, level, entity, seed) ->
                        stack.getOrDefault(ModDataComponents.CAST.get(), Boolean.FALSE) ? 1.0F : 0.0F
        );
    }


}
