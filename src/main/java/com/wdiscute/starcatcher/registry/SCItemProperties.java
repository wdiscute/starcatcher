package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface SCItemProperties
{

    static void addCustomItemProperties()
    {
        for (DeferredHolder<Item, ? extends Item> item : SCItems.RODS_REGISTRY.getEntries())
        {
            ItemProperties.register(
                    item.get(),
                    Starcatcher.rl("cast"),
                    (stack, level, entity, seed) ->
                    {
                        if (entity == null) return 0.0f;
                        if (SCDataComponents.getOrDefault(stack, SCDataComponents.BOBBER, MaybeStack.EMPTY).isEmpty())
                            return 1f;
                        if (SCDataComponents.getOrDefault(stack, SCDataComponents.HOOK, MaybeStack.EMPTY).isEmpty())
                            return 1f;
                        return !SCDataAttachments.get(entity, SCDataAttachments.FISHING_BOB).isEmpty() && (entity.getMainHandItem() == stack || (entity.getOffhandItem() == stack)) ? 1.0f : 0.0f;
                    }
            );
        }

        ItemProperties.register(
                SCItems.MESSAGE.get(),
                Starcatcher.rl("message"),
                (stack, level, entity, seed) ->
                {
                    if (entity == null) return 0.0f;

                    Message message = SCDataComponents.get(stack, SCDataComponents.MESSAGE);
                    if (message == null) return 0;

                    if (message.background().equals(Message.BACKGROUND_NETHER))
                        return 1;

                    if (message.background().equals(Message.BACKGROUND_END))
                        return 2;

                    return 0f;
                }
        );

    }
}
