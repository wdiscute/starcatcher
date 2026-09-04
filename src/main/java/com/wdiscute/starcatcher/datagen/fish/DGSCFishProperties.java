package com.wdiscute.starcatcher.datagen.fish;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.DGSCDataGenerators;
import com.wdiscute.starcatcher.datagen.fish.compat.*;
import com.wdiscute.starcatcher.fish.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DGSCFishProperties extends DatapackBuiltinEntriesProvider
{
    public DGSCFishProperties(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries)
    {
        super(
                output,
                registries,
                DGSCDataGenerators.BUILDER,
                Set.of(
                        "minecraft",

                        Starcatcher.MOD_ID,

                        "create",

                        "tide",
                        "aquaculture",
                        "fishofthieves",
                        "netherdepthsupgrade",
                        "environmental",
                        "miners_delight",
                        "crittersandcompanions",
                        "hybrid_aquatic",

                        "alexscaves",
                        "collectorsreap",
                        "sullysmod",
                        "upgrade_aquatic",
                        "spawn",
                        "unusualfishmod"
                )
        );
    }

    @Override
    public void registerConditions(BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        runningOnlyForConditions = true;
        bootstrap(null);
        conditionsFps.forEach(pair -> consumer.accept(pair.getFirst(), new ModLoadedCondition(pair.getSecond())));
        runningOnlyForConditions = false;

        super.registerConditions(consumer);
    }

    static boolean runningOnlyForConditions = false;
    static List<Pair<ResourceKey<FishProperties>, String>> conditionsFps = new ArrayList<>();

    public static final List<String> MODS_TO_ACTUALLY_DATAGEN =
            List.of(
                    "minecraft",
                    "starcatcher",

                    //replace line under with the mod
                    ""


                    , ""
            );

    public static void bootstrap(@Nullable BootstapContext<FishProperties> context)
    {
        //vanilla
        DGTrophies.bootstrap(context);
        DGMinecraftFishes.bootstrap(context);

        //starcatcher
        DGStarcatcherFishes.bootstrap(context);

        //starcatcher compat
        DGCreateFishes.bootstrap(context);

        //compat
        DGTideFishes.bootstrap(context);
        //DGAquacultureFishes.bootstrap(context);
        //DGFishOfThievesFishes.bootstrap(context);
        //DGNetherDepthsUpgradeFishes.bootstrap(context);
        //DGEnvironmentalFishes.bootstrap(context);
        //DGMinersDelightFishes.bootstrap(context);
        //DGCrittersAndCompanionsFishes.bootstrap(context);
        //DGHybridAquaticFishes.bootstrap(context);
        //todo dont forget to add to the list of ids
        //DGEternalStarlightFishes.bootstrap(context);

        //DGAlexsCavesFishes.bootstrap(context);
        //DGCollectorsReapFishes.bootstrap(context);
        //DGSullysModFishes.bootstrap(context);
        //DGBetterEndFishes.bootstrap(context);
        //DGUpgradeAquaticFishes.bootstrap(context);
        //DGSpawnFishes.bootstrap(context);
        //DGUnusualFishFishes.bootstrap(context);

        FishRegistration.ALL_FISHABLE.sort(Comparator.comparing(o -> o.catchInfo().fish().identifier().toLanguageKey()));
        FishRegistration.STARCATCHER_FISHABLE.sort(Comparator.comparing(o -> o.catchInfo().fish().identifier().toLanguageKey()));
        FishRegistration.ALL_FISHABLE_MAP = FishRegistration.ALL_FISHABLE_MAP.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(
                        Comparator.comparing(o -> o.catchInfo().fish().identifier().toLanguageKey())
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    @Override
    public String getName()
    {
        return "FishingProperties";
    }
}
