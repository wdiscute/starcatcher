package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModCreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Starcatcher.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }


    public static final Supplier<CreativeModeTab> STARCATCHER = CREATIVE_MODE_TABS.register(
            "starcatcher", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ROD.get()))
                    .title(Component.translatable("creativetab.starcatcher.starcatcher"))
                    .displayItems((itemDisplayParameters, output) ->
                    {

                        output.accept(ModItems.ROD);

                        //adds all normal items, skips missingno and settings item
                        for (DeferredHolder<Item, ? extends Item> item : ModItems.ITEMS_REGISTRY.getEntries())
                            if (item != ModItems.MISSINGNO && item != ModItems.SETTINGS)
                                output.accept(item.get());

                        //adds all rods besides main rod which is first in list
                        for (DeferredHolder<Item, ? extends Item> item : ModItems.RODS_REGISTRY.getEntries())
                            if (item != ModItems.ROD)
                                output.accept(item.get());

                        //adds blocks and stuff
                        for (DeferredHolder<Item, ? extends Item> item : ModItems.OTHERS_REGISTRY.getEntries())
                                output.accept(item.get());

                    })
                    .build()
    );
}
