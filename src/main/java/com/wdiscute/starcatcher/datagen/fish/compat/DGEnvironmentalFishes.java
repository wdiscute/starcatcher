package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.fishrestrictions.DimensionRestriction;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGEnvironmentalFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        //,------.                     ,--.                                                        ,--.            ,--.
        //|  .---' ,--,--,  ,--.  ,--. `--' ,--.--.  ,---.  ,--,--,  ,--,--,--.  ,---.  ,--,--,  ,-'  '-.  ,--,--. |  |
        //|  `--,  |      \  \  `'  /  ,--. |  .--' | .-. | |      \ |        | | .-. : |      \ '-.  .-' ' ,-.  | |  |
        //|  `---. |  ||  |   \    /   |  | |  |    ' '-' ' |  ||  | |  |  |  | \   --. |  ||  |   |  |   \ '-'  | |  |
        //`------' `--''--'    `--'    `--' `--'     `---'  `--''--' `--`--`--'  `----' `--''--'   `--'    `--`--' `--'
        //

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("environmental", "koi")
                        .withBucketedFish("environmental", "koi_bucket")
                        .withEntityToSpawn("environmental", "koi")
                        .withSizeAndWeight(60, 20, 3000, 2000)
                        .withRarity(Rarity.UNCOMMON)
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD//,
//                        new BiomeRestriction(List.of(U.rl("environmental", "blossom_woods"), U.rl("environmental", "blossom_valleys")),
//                                List.of(), List.of(), List.of(), "")
                        ),
                "environmental"
        );
    }
}
