package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Starcatcher.MOD_ID);

    public static final Supplier<CreativeModeTab> STARCATCHER = REGISTRY.register(
            "starcatcher", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ROD.get()))
                    .title(Component.translatable("creativetab.starcatcher.starcatcher"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.ROD.get());

                        //adds all entries because im lazy
                        for (RegistryObject<Item> item : ModItems.REGISTRY.getEntries())
                            if (!(item.get() == ModItems.MISSINGNO.get() && item.get() == ModItems.ROD.get()))
                                output.accept(item.get());
                    })
                    .build()
    );
}
