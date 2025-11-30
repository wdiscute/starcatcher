package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItemProperties
{

    public static void addCustomItemProperties()
    {

        for (DeferredHolder<Item, ? extends Item> item : ModItems.RODS_REGISTRY.getEntries())
        {
            ItemProperties.register(
                    item.get(),
                    Starcatcher.rl("cast"),
                    (stack, level, entity, seed) ->
                    {
                        if(entity == null) return 0.0f;
                        return  !entity.getData(ModDataAttachments.FISHING).isEmpty() && (entity.getMainHandItem() == stack || (entity.getOffhandItem() == stack)) ? 1.0f : 0.0f;
                    }
            );
        }

    }
}
