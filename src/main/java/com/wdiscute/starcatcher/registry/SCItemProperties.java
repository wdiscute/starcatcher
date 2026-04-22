package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.nikdo53.neobackports.registry.DeferredHolder;

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
                        if (entity == null) return 0.0f;
                        if (SCDataComponents.getOrDefault(stack, SCDataComponents.BOBBER, SingleStackContainer.empty()).isEmpty())
                            return 1f;
                        if (SCDataComponents.getOrDefault(stack, SCDataComponents.HOOK, SingleStackContainer.empty()).isEmpty())
                            return 1f;
                        boolean b = entity.getMainHandItem() == stack || (entity.getOffhandItem() == stack);
                        boolean b1 = !SCDataAttachments.get(entity, SCDataAttachments.FISHING_BOB).isEmpty();
                        return b1 && b ? 1.0f : 0.0f;
                    }
            );
        }

    }
}
