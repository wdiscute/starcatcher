package com.wdiscute.starcatcher;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
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


    public static final Supplier<CreativeModeTab> LAICAPS =
            CREATIVE_MODE_TABS.register(
                    "starcatcher", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STARCATCHER_FISHING_ROD.get()))
                            .title(Component.translatable("creativetab.starcatcher.starcatcher"))
                            .displayItems((itemDisplayParameters, output) ->
                            {

                                //fishing
                                output.accept(ModItems.FISHING_GUIDE.get());
                                output.accept(ModItems.STARCATCHER_FISHING_ROD.get());
                                output.accept(ModItems.STARCATCHER_TWINE.get());

                                output.accept(ModItems.BAIT_SAVING_BOBBER.get());
                                output.accept(ModItems.DIFFICULTY_BOBBER.get());
                                output.accept(ModItems.CREEPER_BOBBER.get());
                                output.accept(ModItems.TREASURE_BOBBER.get());
                                output.accept(ModItems.FAST_BITING_BOBBER.get());
                                output.accept(ModItems.WATERLOGGED_SATCHEL.get());
                                output.accept(ModItems.FISH_BONES.get());

                                //fishes
                                //lake
                                output.accept(ModItems.OBIDONTIEE.get());

                                //river
                                output.accept(ModItems.SILVERFIN_PIKE.get());
                                output.accept(ModItems.WILLOW_BREAM.get());
                                output.accept(ModItems.DRIFTING_BREAM.get());
                                output.accept(ModItems.DOWNFALL_BREAM.get());
                                output.accept(ModItems.HOLLOWBELLY_DARTER.get());
                                output.accept(ModItems.MISTBACK_CHUB.get());

                                //saltwater
                                output.accept(ModItems.IRONJAW_HERRING.get());
                                output.accept(ModItems.DEEPJAW_HERRING.get());
                                output.accept(ModItems.DUSKTAIL_SNAPPER.get());

                                //icy
                                output.accept(ModItems.FROSTGILL_CHUB.get());
                                output.accept(ModItems.CRYSTALBACK_MINNOW.get());
                                output.accept(ModItems.AZURE_CRYSTALBACK_MINNOW.get());

                                //nether
                                output.accept(ModItems.EMBERGILL.get());


                            })
                            .build()
            );
}
