package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.*;
import com.wdiscute.starcatcher.registry.fishrestrictions.DaytimeRestriction;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGSpawnFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        // ,---.
        //'   .-'   ,---.   ,--,--. ,--.   ,--. ,--,--,
        //`.  `-.  | .-. | ' ,-.  | |  |.'.|  | |      \
        //.-'    | | '-' ' \ '-'  | |   .'.   | |  ||  |
        //`-----'  |  |-'   `--`--' '--'   '--' `--''--'
        //         `--'

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("spawn", "angler_fish")
                        .withBucketedFish("spawn", "angler_fish_bucket")
                        .withEntityToSpawn("spawn", "angler_fish")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withBaseChance(20),
                "spawn"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("spawn", "herring")
                        .withBucketedFish("spawn", "herring_bucket")
                        .withEntityToSpawn("spawn", "herring")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY.moving()),
                "spawn"
        );

    }
}
