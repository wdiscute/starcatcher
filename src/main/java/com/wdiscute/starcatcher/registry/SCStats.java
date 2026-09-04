package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.nikdo53.neobackports.registry.DeferredHolder;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

public interface SCStats
{
    DeferredRegisterTyped<ResourceLocation> CUSTOM_STATS =
            DeferredRegisterTyped.create(BuiltInRegistries.CUSTOM_STAT, Starcatcher.MOD_ID);


    DeferredHolder<ResourceLocation, ResourceLocation> STARCAUGHT_FISH = register("starcaught_fish");
    DeferredHolder<ResourceLocation, ResourceLocation> STARCAUGHT_FISH_MISSED = register("starcaught_fish_missed");
    DeferredHolder<ResourceLocation, ResourceLocation> STARCAUGHT_TREASURES = register("starcaught_treasures");

    DeferredHolder<ResourceLocation, ResourceLocation> TICKS_SPENT_FISHING = register("ticks_spent_fishing");
    DeferredHolder<ResourceLocation, ResourceLocation> BAIT_USED = register("bait_used");

    //rarity
    DeferredHolder<ResourceLocation, ResourceLocation> TRASH_CAUGHT = register("trash_caught");
    DeferredHolder<ResourceLocation, ResourceLocation> COMMON_CAUGHT = register("common_caught");
    DeferredHolder<ResourceLocation, ResourceLocation> UNCOMMON_CAUGHT = register("uncommon_caught");
    DeferredHolder<ResourceLocation, ResourceLocation> RARE_CAUGHT = register("rare_caught");
    DeferredHolder<ResourceLocation, ResourceLocation> EPIC_CAUGHT = register("epic_caught");
    DeferredHolder<ResourceLocation, ResourceLocation> LEGENDARY_CAUGHT = register("legendary_caught");

    DeferredHolder<ResourceLocation, ResourceLocation> PERFECT_CATCHES = register("perfect_catches");
    DeferredHolder<ResourceLocation, ResourceLocation> GOLDEN_CATCHES = register("golden_catches");



    private static DeferredHolder<ResourceLocation, ResourceLocation> register(String key)
    {
        return CUSTOM_STATS.register(key, () -> Starcatcher.rl(key));
    }

    static void register(IEventBus eventBus)
    {
        CUSTOM_STATS.register(eventBus);
    }
}
