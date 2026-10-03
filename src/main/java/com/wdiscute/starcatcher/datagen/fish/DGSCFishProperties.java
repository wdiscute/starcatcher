package com.wdiscute.starcatcher.datagen.fish;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.fish.compat.*;
import com.wdiscute.starcatcher.fish.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.data.worldgen.BootstrapContext;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class DGSCFishProperties
{

    private DGSCFishProperties()
    {
    }

    public static DatapackBuiltinEntriesProvider create(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> worldRegistries,
            CompletableFuture<HolderLookup.Provider> reloadableRegistries,
            RegistrySetBuilder setBuilder
    )
    {
        return DatapackBuiltinEntriesProvider.forReloadableLayer(
                output,
                "FishingProperties",
                worldRegistries,
                reloadableRegistries,
                setBuilder,
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

    public static final List<String> MODS_TO_ACTUALLY_DATAGEN =
            List.of(
                    "minecraft",
                    "starcatcher",

                    //replace line under with the mod
                    ""


                    , ""
            );

    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
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
}
