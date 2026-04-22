package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DGSCFishingPropertiesProvider extends DatapackBuiltinEntriesProvider
{

    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder().add(Starcatcher.FISH_REGISTRY_KEY, FishingPropertiesRegistry::bootstrap);

    public static CompletableFuture<HolderLookup.Provider> lookup = null;

    public DGSCFishingPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        lookup = registries;

        FishingPropertiesRegistry.register(); //register all entries before anything else
        super(output, registries, REGISTRY, DGSCFishingPropertiesProvider::addConditions, Set.of(
                Starcatcher.MOD_ID,
                "minecraft",
                "tide",
                "aquaculture",
                "fishofthieves",
                "netherdepthsupgrade",
                "sullysmod",
                "upgrade_aquatic",
                "environmental",
                "collectorsreap",
                "miners_delight",
                "alexscaves",
                "crittersandcompanions",
                "aquamirae",
                "hybrid_aquatic",
                "hybrid-aquatic",
                "tfc",
                "betterend",
                "unusualfishmod",
                "spawn",
                "fintastic"
                //That's a lot of compatibilities
        ));
    }

    private static void addConditions(final BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        FishingPropertiesRegistry.registerConditions(consumer);
    }

    @Override
    public String getName()
    {
        return "FishingProperties";
    }
}
