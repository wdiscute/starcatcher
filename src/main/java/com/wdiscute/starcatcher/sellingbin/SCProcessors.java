package com.wdiscute.starcatcher.sellingbin;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.*;
import com.wdiscute.starcatcher.Starcatcher;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SCProcessors
{
    public static final DeferredRegister<AbstractProcessor> SELLING_BIN_PROCESSORS =
            DeferredRegister.create(SellingBin.SELLING_BIN_REGISTRY, Starcatcher.MOD_ID);

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
