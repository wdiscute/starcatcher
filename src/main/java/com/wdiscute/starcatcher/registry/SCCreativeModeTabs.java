package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;


public class SCCreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Starcatcher.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final LetterItem.Message MESSAGE = new LetterItem.Message(
            UUID.randomUUID(),
            "<sclegendary>-dev (wd)</sclegendary>",
            Level.OVERWORLD.identifier(),
            List.of(
                    "",
                    "",
                    "This is a cheated message from creative.",
                    "",
                    "Usually players would write their own",
                    "message and send it to the ocean for",
                    "others to fish later.",
                    "(5% chance if there is any available)",
                    "",
                    "<scgolden>Also did you know it supports markdown?</scgolden>",
                    "learn more about it in the LibTooltips wiki"
            ),
            true
    );

    public static final Supplier<CreativeModeTab> STARCATCHER = CREATIVE_MODE_TABS.register(
            "starcatcher", () -> CreativeModeTab.builder().icon(() -> new ItemStack(SCItems.ROD.get()))
                    .title(Component.translatable("creativetab.starcatcher.starcatcher"))
                    .displayItems((itemDisplayParameters, output) ->
                    {

                        output.accept(SCItems.ROD);

                        //adds items
                        for (DeferredHolder<Item, ? extends Item> item : SCItems.ITEMS.getEntries())
                        {
                            if (item.equals(SCItems.ROD)) continue;

                            if (item.equals(SCItems.BOTTLED_LETTER))
                            {
                                ItemStack is = new ItemStack(SCItems.BOTTLED_LETTER.get());

                                SCDataComponents.set(is, SCDataComponents.MESSAGE, MESSAGE);
                                output.accept(is);
                                continue;
                            }

                            if (item.equals(SCItems.MESSAGE_IN_A_BOTTLE))
                            {
                                ItemStack is = new ItemStack(SCItems.MESSAGE_IN_A_BOTTLE.get());

                                SCDataComponents.set(is, SCDataComponents.MESSAGE, MESSAGE);
                                output.accept(is);
                                continue;
                            }

                            if (item.equals(SCItems.MESSAGE)) continue;

                            output.accept(item.get());
                        }


                        //adds bobbers
                        for (DeferredHolder<Item, ? extends Item> item : SCItems.BOBBERS_REGISTRY.getEntries())
                            output.accept(item.get());

                        //adds hooks
                        for (DeferredHolder<Item, ? extends Item> item : SCItems.HOOKS_REGISTRY.getEntries())
                            output.accept(item.get());

                        //adds templates
                        for (DeferredHolder<Item, ? extends Item> item : SCItems.TEMPLATES_REGISTRY.getEntries())
                            output.accept(item.get());

                        //adds rods besides default
                        for (DeferredHolder<Item, ? extends Item> item : SCItems.RODS_REGISTRY.getEntries())
                            if (!item.equals(SCItems.ROD))
                                output.accept(item.get());

                        //adds fish
                        for (DeferredHolder<Item, ? extends Item> item : SCItems.BUCKETABLE_FISHES_REGISTRY.getEntries())
                            output.accept(item.get());

                    })
                    .build()
    );
}
