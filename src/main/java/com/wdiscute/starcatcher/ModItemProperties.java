package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import net.minecraft.client.renderer.item.ItemProperties;

public class ModItemProperties
{

    public static void addCustomItemProperties() {
        ItemProperties.register(
                ModItems.ROD.get(),
                Starcatcher.rl("cast"),
                (stack, level, entity, seed) ->
                {
                    if(entity == null) return 0.0f;
                    return entity.getData(ModDataAttachments.FISHING).isEmpty() ? 0.0F : 1.0F;
                }
        );
    }


}
