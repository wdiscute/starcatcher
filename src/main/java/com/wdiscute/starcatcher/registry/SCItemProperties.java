package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SCItemProperties
{

    public static void addCustomItemProperties()
    {

        for (DeferredHolder<Item, ? extends Item> item : SCItems.RODS_REGISTRY.getEntries())
        {
            ItemProperties.register(
                    item.get(),
                    Starcatcher.rl("cast"),
                    (stack, level, entity, seed) ->
                    {

                    }
            );
        }

    }
}
