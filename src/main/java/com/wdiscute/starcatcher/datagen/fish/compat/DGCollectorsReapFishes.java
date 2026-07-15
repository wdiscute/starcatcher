package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.DimensionRestriction;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGCollectorsReapFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {
        //
        // ,-----.         ,--. ,--.                  ,--.                   ,--.             ,------.
        //'  .--./  ,---.  |  | |  |  ,---.   ,---. ,-'  '-.  ,---.  ,--.--. |  |  ,---.      |  .--. '  ,---.   ,--,--.  ,---.
        //|  |     | .-. | |  | |  | | .-. : | .--' '-.  .-' | .-. | |  .--' `-'  (  .-'      |  '--'.' | .-. : ' ,-.  | | .-. |
        //'  '--'\ ' '-' ' |  | |  | \   --. \ `--.   |  |   ' '-' ' |  |         .-'  `)     |  |\  \  \   --. \ '-'  | | '-' '
        // `-----'  `---'  `--' `--'  `----'  `---'   `--'    `---'  `--'         `----'      `--' '--'  `----'  `--`--' |  |-'
        //                                                                                                               `--'

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("collectorsreap", "platinum_bass")
                        .withBucketedFish("collectorsreap", "platinum_bass_bucket")
                        .withEntityToSpawn("collectorsreap", "platinum_bass")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.EASY.moving())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD//,
//                        new BiomeRestriction(List.of(), List.of(U.rl("collectorsreap", "biome/has_spawn/platinum_bass")),
//                                List.of(), List.of(), "")
                        ),
                "collectorsreap"
        );


        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("collectorsreap", "tiger_prawn")
                        .withBucketedFish("collectorsreap", "tiger_prawn_bucket")
                        .withEntityToSpawn("collectorsreap", "tiger_prawn")
                        .withSizeAndWeight(28, 8, 260, 60)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withRarity(Rarity.RARE)
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD//,
//                        new BiomeRestriction(List.of(), List.of(U.rl("collectorsreap", "biome/has_spawn/tiger_prawn")),
//                                List.of(), List.of(), "")
                        ),
                "collectorsreap"
        );

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("collectorsreap", "clam")
                        .withBucketedFish("collectorsreap", "clam_bucket")
                        .withEntityToSpawn("collectorsreap", "clam")
                        .withSizeAndWeight(new SizeAndWeight(20, 5, 1000, 400))
                        .withBaseChance(1)
                        .withRarity(Rarity.TRASH)
                        .withDifficulty(Difficulty.TRASH)
//                        new BiomeRestriction(List.of(), List.of(U.rl("collectorsreap", "biome/has_spawn/clam")),
//                                List.of(), List.of(), "")
                        ,
                "collectorsreap"
        );

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("collectorsreap", "urchin")
                        .withBucketedFish("collectorsreap", "urchin_bucket")
                        .withEntityToSpawn("collectorsreap", "urchin")
                        .withHasGuideEntry(false)
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD//,
//                        new BiomeRestriction(List.of(), List.of(U.rl("collectorsreap", "biome/has_spawn/urchin")),
//                                List.of(), List.of(), "")
                        ),
                "collectorsreap"
        );


        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("collectorsreap", "chieftain_crab")
                        .withBucketedFish("collectorsreap", "chieftain_crab_bucket")
                        .withEntityToSpawn("collectorsreap", "chieftain_crab")
                        .withSizeAndWeight(28, 8, 260, 60)
                        .withDifficulty(Difficulty.NETHER_CRAB)
                        .withRarity(Rarity.UNCOMMON)
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD//,
//                        new BiomeRestriction(List.of(), List.of(U.rl("collectorsreap", "biome/has_spawn/chieftain_crab")),
//                                List.of(), List.of(), "")
                        )
                        .withItemToOverrideWith(new MaybeStack(SCItems.UNKNOWN_FISH)),
                "collectorsreap"
        );
    }
}
