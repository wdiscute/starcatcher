package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.trigger.FishCaughtTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCCriterionTriggers
{
    //done in SCEvents in 1.20.1
//    DeferredRegisterTyped<CriterionTrigger<?>> REGISTRY =
//            DeferredRegisterTyped.create(Registries.TRIGGER_TYPE, Starcatcher.MOD_ID);
//
//    CriteriaTriggers.
//
    public static FishCaughtTrigger FISH;

    public static void register(IEventBus eventBus)
    {
        //REGISTRY.register(eventBus);
    }
}
