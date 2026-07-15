package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.trigger.FishCaughtTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCCriterionTriggers
{
    DeferredRegister<CriterionTrigger<?>> REGISTRY =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Starcatcher.MOD_ID);

    Supplier<FishCaughtTrigger> FISH = REGISTRY.register("fish_caught", FishCaughtTrigger::new);

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
