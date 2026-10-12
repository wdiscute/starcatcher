package com.wdiscute.starcatcher.datagen.fish;

import com.wdiscute.starcatcher.datagen.fish.compat.*;
import com.wdiscute.starcatcher.fish.*;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public final class DGSCFishProperties
{
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
