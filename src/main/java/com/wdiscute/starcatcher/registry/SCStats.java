package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface SCStats
{
    DeferredRegister<Identifier> CUSTOM_STATS =
            DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, Starcatcher.MOD_ID);


    DeferredHolder<Identifier, Identifier> STARCAUGHT_FISH = register("starcaught_fish");
    DeferredHolder<Identifier, Identifier> STARCAUGHT_FISH_MISSED = register("starcaught_fish_missed");
    DeferredHolder<Identifier, Identifier> STARCAUGHT_TREASURES = register("starcaught_treasures");

    DeferredHolder<Identifier, Identifier> TICKS_SPENT_FISHING = register("ticks_spent_fishing");
    DeferredHolder<Identifier, Identifier> BAIT_USED = register("bait_used");

    //rarity
    DeferredHolder<Identifier, Identifier> TRASH_CAUGHT = register("trash_caught");
    DeferredHolder<Identifier, Identifier> COMMON_CAUGHT = register("common_caught");
    DeferredHolder<Identifier, Identifier> UNCOMMON_CAUGHT = register("uncommon_caught");
    DeferredHolder<Identifier, Identifier> RARE_CAUGHT = register("rare_caught");
    DeferredHolder<Identifier, Identifier> EPIC_CAUGHT = register("epic_caught");
    DeferredHolder<Identifier, Identifier> LEGENDARY_CAUGHT = register("legendary_caught");

    DeferredHolder<Identifier, Identifier> PERFECT_CATCHES = register("perfect_catches");
    DeferredHolder<Identifier, Identifier> GOLDEN_CATCHES = register("golden_catches");



    private static DeferredHolder<Identifier, Identifier> register(String key)
    {
        return CUSTOM_STATS.register(key, () -> Starcatcher.rl(key));
    }

    static void register(IEventBus eventBus)
    {
        CUSTOM_STATS.register(eventBus);
    }
}
