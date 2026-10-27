package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.Starcatcher;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCAquariumInteractions
{
    DeferredRegister<AbstractInteraction> REGISTRY =
            DeferredRegister.create(Starcatcher.AQUARIUM_INTERACTION, Starcatcher.MOD_ID);

    DeferredHolder<AbstractInteraction, AbstractInteraction> SIMPLE = register("simple", SimpleInteraction::new);
    DeferredHolder<AbstractInteraction, AbstractInteraction> CORAL_FAN = register("coral_fan", CoralFanInteraction::new);
    DeferredHolder<AbstractInteraction, AbstractInteraction> KELP = register("kelp", KelpInteraction::new);
    DeferredHolder<AbstractInteraction, AbstractInteraction> SEAGRASS = register("seagrass", SeagrassInteraction::new);
    DeferredHolder<AbstractInteraction, AbstractInteraction> SEA_PICKLE = register("sea_pickle", SeaPickleInteraction::new);
    DeferredHolder<AbstractInteraction, AbstractInteraction> BONE_MEAL = register("bone_meal", BoneMealInteraction::new);

    static DeferredHolder<AbstractInteraction, AbstractInteraction> register(String name, Supplier<AbstractInteraction> sup)
    {
        return REGISTRY.register(name, sup);
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
