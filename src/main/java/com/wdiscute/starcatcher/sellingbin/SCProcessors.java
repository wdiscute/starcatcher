package com.wdiscute.starcatcher.sellingbin;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.*;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredHolder;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCProcessors
{
    public static final DeferredRegisterTyped<AbstractProcessor> SELLING_BIN_PROCESSORS =
            DeferredRegisterTyped.create(SellingBin.SELLING_BIN, Starcatcher.MOD_ID);

    public static DeferredHolder<AbstractProcessor, AbstractProcessor> FISHES_PROCESSOR = register("fishes_processor", () -> FishProcessor.DEFAULT);

    public static DeferredHolder<AbstractProcessor, AbstractProcessor> register(String name, Supplier<AbstractProcessor> sup)
    {
        return SELLING_BIN_PROCESSORS.register(name, sup);
    }

    public static void register(IEventBus eventBus)
    {
        SELLING_BIN_PROCESSORS.register(eventBus);
    }
}
