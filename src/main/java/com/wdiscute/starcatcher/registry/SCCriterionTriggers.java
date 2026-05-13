package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.advancement.MinigameCompletedTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface SCCriterionTriggers
{
    Supplier<MinigameCompletedTrigger> MINIGAME_COMPLETED = MinigameCompletedTrigger::new;


    static void register(IEventBus eventBus)
    {
        eventBus.addListener(SCCriterionTriggers::event);
    }

    static void event (FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CriteriaTriggers.register(MINIGAME_COMPLETED.get()));
    }
}
