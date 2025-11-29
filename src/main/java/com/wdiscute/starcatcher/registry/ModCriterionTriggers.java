package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.advancement.MinigameCompletedTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ModCriterionTriggers {
    DeferredRegister<CriterionTrigger<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, Starcatcher.MOD_ID);

    Supplier<MinigameCompletedTrigger> MINIGAME_COMPLETED = REGISTRY.register("minigame_completed", MinigameCompletedTrigger::new);
}
