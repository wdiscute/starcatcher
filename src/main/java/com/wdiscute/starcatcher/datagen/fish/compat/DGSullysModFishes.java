package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGSullysModFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {
        //
        // ,---.            ,--. ,--.           ,--.             ,--.   ,--.            ,--.
        //'   .-'  ,--.,--. |  | |  | ,--. ,--. |  |  ,---.      |   `.'   |  ,---.   ,-|  |
        //`.  `-.  |  ||  | |  | |  |  \  '  /  `-'  (  .-'      |  |'.'|  | | .-. | ' .-. |
        //.-'    | '  ''  ' |  | |  |   \   '        .-'  `)     |  |   |  | ' '-' ' \ `-' |
        //`-----'   `----'  `--' `--' .-'  /         `----'      `--'   `--'  `---'   `---'
        //                            `---'

        //todo
        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("sullysmod", "piranha")
                        .withBucketedFish("sullysmod", "piranha_bucket")
                        .withEntityToSpawn("sullysmod", "piranha")
                        .withSizeAndWeight(30, 10, 500, 300)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.HARD.moving()),
                "sullysmod"
                //.addRestrictions(DimensionRestriction.OVERWORLD,
                //        new BiomeRestriction(List.of(), List.of(U.rl("sullysmod", "biome/piranha_spawn_in")), List.of(), List.of(), ""))
        );

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("sullysmod", "lanternfish")
                        .withBucketedFish("sullysmod", "lanternfish_bucket")
                        .withEntityToSpawn("sullysmod", "lanternfish")
                        .withSizeAndWeight(100, 50, 15000, 10000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.HARD),
                "sullysmod"
                //.addRestrictions(DimensionRestriction.OVERWORLD,
                //        new BiomeRestriction(List.of(), List.of(U.rl("sullysmod", "biome/lanternfish_spawn_in")), List.of(), List.of(), ""))
        );
    }
}
