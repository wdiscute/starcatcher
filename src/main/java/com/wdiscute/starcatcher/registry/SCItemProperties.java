package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlock;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;
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

        for (DeferredHolder<Item, ? extends Item> item : SCItems.TACKLE_BOX_BOAT_REGISTRY.getEntries())
        {
            ItemProperties.register(
                    item.get(),
                    Starcatcher.rl("tackle_box_color"),
                    (stack, level, entity, seed) ->
                            switch (stack.get(SCDataComponents.TACKLE_BOX_COLOR))
                            {
                                case null -> 9;
                                case WHITE -> 0;
                                case ORANGE -> 1;
                                case MAGENTA -> 2;
                                case LIGHT_BLUE -> 3;
                                case YELLOW -> 4;
                                case LIME -> 5;
                                case PINK -> 6;
                                case GRAY -> 7;
                                case LIGHT_GRAY -> 8;
                                case CYAN -> 9;
                                case PURPLE -> 10;
                                case BLUE -> 11;
                                case BROWN -> 12;
                                case GREEN -> 13;
                                case RED -> 14;
                                default -> 15;
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
