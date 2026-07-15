package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGTideFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {
        //
        //  ,--.   ,--.    ,--.
        //,-'  '-. `--'  ,-|  |  ,---.
        //'-.  .-' ,--. ' .-. | | .-. :
        //  |  |   |  | \ `-' | \   --.
        //  `--'   `--'  `---'   `----'
        //

        //
        //        ,--.
        //,--.--. `--' ,--.  ,--.  ,---.  ,--.--.
        //|  .--' ,--.  \  `'  /  | .-. : |  .--'
        //|  |    |  |   \    /   \   --. |  |
        //`--'    `--'    `--'     `----' `--'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "yellow_perch")
                        .withBucketedFish("tide", "yellow_perch_bucket")
                        .withEntityToSpawn("tide", "yellow_perch")
                        .withSizeAndWeight(27, 11, 500, 352)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "largemouth_bass")
                        .withBucketedFish("tide", "largemouth_bass_bucket")
                        .withEntityToSpawn("tide", "largemouth_bass")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "smallmouth_bass")
                        .withBucketedFish("tide", "smallmouth_bass_bucket")
                        .withEntityToSpawn("tide", "smallmouth_bass")
                        .withSizeAndWeight(20, 5, 800, 200)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "pike")
                        .withBucketedFish("tide", "pike_bucket")
                        .withEntityToSpawn("tide", "pike")
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "brook_trout")
                        .withBucketedFish("tide", "brook_trout_bucket")
                        .withEntityToSpawn("tide", "brook_trout")
                        .withSizeAndWeight(35, 8, 1600, 1200)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "white_crappie")
                        .withBucketedFish("tide", "white_crappie_bucket")
                        .withEntityToSpawn("tide", "white_crappie")
                        .withSizeAndWeight(35, 8, 1600, 1200)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "black_crappie")
                        .withBucketedFish("tide", "black_crappie_bucket")
                        .withEntityToSpawn("tide", "black_crappie")
                        .withSizeAndWeight(35, 8, 1600, 1200)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "carp")
                        .withBucketedFish("tide", "carp_bucket")
                        .withEntityToSpawn("tide", "carp")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        //
        //               ,--.    ,--.     ,--.          ,--.
        // ,---.  ,---.  |  |  ,-|  |     |  |  ,--,--. |  |,-.   ,---.
        //| .--' | .-. | |  | ' .-. |     |  | ' ,-.  | |     /  | .-. :
        //\ `--. ' '-' ' |  | \ `-' |     |  | \ '-'  | |  \  \  \   --.
        // `---'  `---'  `--'  `---'      `--'  `--`--' `--'`--'  `----'
        //
        FishRegistration.register(
                context,
                PresetRestrictions.coldLake(context)
                        .withFish("tide", "walleye")
                        .withBucketedFish("tide", "walleye_bucket")
                        .withEntityToSpawn("tide", "walleye")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.MEDIUM_FROZEN)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldRiver(context)
                        .withFish("tide", "sturgeon")
                        .withBucketedFish("tide", "sturgeon_bucket")
                        .withEntityToSpawn("tide", "sturgeon")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.MEDIUM_FROZEN)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        //
        //               ,--.    ,--.     ,--.          ,--.
        // ,---.  ,---.  |  |  ,-|  |     |  |  ,--,--. |  |,-.   ,---.
        //| .--' | .-. | |  | ' .-. |     |  | ' ,-.  | |     /  | .-. :
        //\ `--. ' '-' ' |  | \ `-' |     |  | \ '-'  | |  \  \  \   --.
        // `---'  `---'  `--'  `---'      `--'  `--`--' `--'`--'  `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.coldLake(context)
                        .withFish("tide", "rainbow_trout")
                        .withBucketedFish("tide", "rainbow_trout_bucket")
                        .withEntityToSpawn("tide", "rainbow_trout")
                        .withSizeAndWeight(35, 8, 1600, 1200)
                        .withDifficulty(Difficulty.MEDIUM_FROZEN)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldLake(context)
                        .withFish("tide", "frostbite_flounder")
                        .withBucketedFish("tide", "frostbite_flounder_bucket")
                        .withEntityToSpawn("tide", "frostbite_flounder")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.HARD_FROZEN)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        //
        //,--.          ,--.
        //|  |  ,--,--. |  |,-.   ,---.
        //|  | ' ,-.  | |     /  | .-. :
        //|  | \ '-'  | |  \  \  \   --.
        //`--'  `--`--' `--'`--'  `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.lake(context)
                        .withFish("tide", "bluegill")
                        .withBucketedFish("tide", "bluegill_bucket")
                        .withEntityToSpawn("tide", "bluegill")
                        .withSizeAndWeight(20, 5, 400, 100)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "tide"
        );


        //
        //                                            ,--.          ,--.
        //,--.   ,--.  ,--,--. ,--.--. ,--,--,--.     |  |  ,--,--. |  |,-.   ,---.
        //|  |.'.|  | ' ,-.  | |  .--' |        |     |  | ' ,-.  | |     /  | .-. :
        //|   .'.   | \ '-'  | |  |    |  |  |  |     |  | \ '-'  | |  \  \  \   --.
        //'--'   '--'  `--`--' `--'    `--`--`--'     `--'  `--`--' `--'`--'  `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("tide", "guppy")
                        .withBucketedFish("tide", "guppy_bucket")
                        .withEntityToSpawn("tide", "guppy")
                        .withSizeAndWeight(20, 5, 400, 100)
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE)
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("tide", "catfish")
                        .withBucketedFish("tide", "catfish_bucket")
                        .withEntityToSpawn("tide", "catfish")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE_MOVING)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );


        //
        // ,--.    ,--.                                                               ,--.  ,---. ,--.
        // |  |-.  `--'  ,---.  ,--,--,--.  ,---.       ,---.   ,---.   ,---.   ,---. `--' /  .-' `--'  ,---.
        // | .-. ' ,--. | .-. | |        | | .-. :     (  .-'  | .-. | | .-. : | .--' ,--. |  `-, ,--. | .--'
        // | `-' | |  | ' '-' ' |  |  |  | \   --.     .-'  `) | '-' ' \   --. \ `--. |  | |  .-' |  | \ `--.
        //  `---'  `--'  `---'  `--`--`--'  `----'     `----'  |  |-'   `----'  `---' `--' `--'   `--'  `---'
        //                                                     `--'

        FishRegistration.register(
                context,
                PresetRestrictions.cherryGrove(context)
                        .withFish("tide", "blossom_bass")
                        .withBucketedFish("tide", "blossom_bass_bucket")
                        .withEntityToSpawn("tide", "blossom_bass")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.jungle(context)
                        .withFish("tide", "arapaima")
                        .withBucketedFish("tide", "arapaima_bucket")
                        .withEntityToSpawn("tide", "arapaima")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE)
                , "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("tide", "mirage_catfish")
                        .withBucketedFish("tide", "mirage_catfish_bucket")
                        .withEntityToSpawn("tide", "mirage_catfish")
                        .withSizeAndWeight(100, 50, 10000, 3000)
                        .withDifficulty(Difficulty.HARD_MIRAGE)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("tide", "sand_tiger_shark")
                        .withBucketedFish("tide", "sand_tiger_shark_bucket")
                        .withEntityToSpawn("tide", "sand_tiger_shark")
                        .withSizeAndWeight(400, 120, 16000, 11000)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE)
                        .withRarity(Rarity.RARE),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.swamp(context)
                        .withFish("tide", "slimy_salmon")
                        .withBucketedFish("tide", "slimy_salmon_bucket")
                        .withEntityToSpawn("tide", "slimy_salmon")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.RARE),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.lake(context)
                        .withFish("tide", "mooneye")
                        .withBucketedFish("tide", "mooneye_bucket")
                        .withEntityToSpawn("tide", "mooneye")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.HARD.withHP(300))
                        .withRarity(Rarity.EPIC),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish("tide", "bull_shark")
                        .withBucketedFish("tide", "bull_shark_bucket")
                        .withEntityToSpawn("tide", "bull_shark")
                        .withSizeAndWeight(400, 120, 16000, 11000)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withBaseChance(2)
                        .withRarity(Rarity.EPIC),
                "tide"
        );


        //
        //
        //  ,---.   ,---.  ,---.   ,--,--. ,--,--,   ,---.
        // | .-. | | .--' | .-. : ' ,-.  | |      \ (  .-'
        // ' '-' ' \ `--. \   --. \ '-'  | |  ||  | .-'  `)
        //  `---'   `---'  `----'  `--`--' `--''--' `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "mackerel")
                        .withBucketedFish("tide", "mackerel_bucket")
                        .withEntityToSpawn("tide", "mackerel")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "ocean_perch")
                        .withBucketedFish("tide", "ocean_perch_bucket")
                        .withEntityToSpawn("tide", "ocean_perch")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "tuna")
                        .withBucketedFish("tide", "tuna_bucket")
                        .withEntityToSpawn("tide", "tuna")
                        .withSizeAndWeight(150, 50, 120000, 60000)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "red_snapper")
                        .withBucketedFish("tide", "red_snapper_bucket")
                        .withEntityToSpawn("tide", "red_snapper")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "snook")
                        .withBucketedFish("tide", "snook_bucket")
                        .withEntityToSpawn("tide", "snook")
                        .withSizeAndWeight(27.0f, 11, 500, 352)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "anchovy")
                        .withBucketedFish("tide", "anchovy_bucket")
                        .withEntityToSpawn("tide", "anchovy")
                        .withSizeAndWeight(27.0f, 11, 500, 352)
                        .withDifficulty(Difficulty.EASY.vanishing())
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "flounder")
                        .withBucketedFish("tide", "flounder_bucket")
                        .withEntityToSpawn("tide", "flounder")
                        .withSizeAndWeight(27.0f, 11, 500, 352)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        //
        //
        //,--.   ,--.  ,--,--. ,--.--. ,--,--,--.      ,---.   ,---.  ,---.   ,--,--. ,--,--,
        //|  |.'.|  | ' ,-.  | |  .--' |        |     | .-. | | .--' | .-. : ' ,-.  | |      \
        //|   .'.   | \ '-'  | |  |    |  |  |  |     ' '-' ' \ `--. \   --. \ '-'  | |  ||  |
        //'--'   '--'  `--`--' `--'    `--`--`--'      `---'   `---'  `----'  `--`--' `--''--'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.warmOcean(context)
                        .withFish("tide", "angelfish")
                        .withBucketedFish("tide", "angelfish_bucket")
                        .withEntityToSpawn("tide", "angelfish")
                        .withSizeAndWeight(27.0f, 11, 500, 352)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmOcean(context)
                        .withFish("tide", "mahi_mahi")
                        .withBucketedFish("tide", "mahi_mahi_bucket")
                        .withEntityToSpawn("tide", "mahi_mahi")
                        .withSizeAndWeight(70, 50, 4000, 2000)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmOcean(context)
                        .withFish("tide", "sailfish")
                        .withBucketedFish("tide", "sailfish_bucket")
                        .withEntityToSpawn("tide", "sailfish")
                        .withSizeAndWeight(70, 50, 4000, 2000)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmOcean(context)
                        .withFish("tide", "swordfish")
                        .withBucketedFish("tide", "swordfish_bucket")
                        .withEntityToSpawn("tide", "swordfish")
                        .withSizeAndWeight(70, 50, 4000, 2000)
                        .withRarity(Rarity.RARE),
                "tide"
        );


        //
        //                             ,--.                                             ,--.         ,--.                      ,--.
        // ,--,--,--. ,--.,--.  ,---.  |  ,---.  ,--.--.  ,---.   ,---.  ,--,--,--.     `--'  ,---.  |  |  ,--,--. ,--,--,   ,-|  |  ,---.
        // |        | |  ||  | (  .-'  |  .-.  | |  .--' | .-. | | .-. | |        |     ,--. (  .-'  |  | ' ,-.  | |      \ ' .-. | (  .-'
        // |  |  |  | '  ''  ' .-'  `) |  | |  | |  |    ' '-' ' ' '-' ' |  |  |  |     |  | .-'  `) |  | \ '-'  | |  ||  | \ `-' | .-'  `)
        // `--`--`--'  `----'  `----'  `--' `--' `--'     `---'   `---'  `--`--`--'     `--' `----'  `--'  `--`--' `--''--'  `---'  `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.mushroomFields(context)
                        .withFish("tide", "spore_stalker")
                        .withBucketedFish("tide", "spore_stalker_bucket")
                        .withEntityToSpawn("tide", "spore_stalker")
                        .withSizeAndWeight(70, 50, 4000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(200))
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmOcean(context)
                        .withFish("tide", "manta_ray")
                        .withBucketedFish("tide", "manta_ray_bucket")
                        .withEntityToSpawn("tide", "manta_ray")
                        .withSizeAndWeight(70, 50, 4000, 2000)
                        .withDifficulty(Difficulty.HARD.withHP(200))
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "aquathorn")
                        .withBucketedFish("tide", "aquathorn_bucket")
                        .withEntityToSpawn("tide", "aquathorn")
                        .withSizeAndWeight(70, 50, 4000, 2000)
                        .addRestriction(StructureRestriction.OCEAN_MONUMENT)
                        .withDifficulty(Difficulty.HARD.moving().withHP(200))
                        .withRarity(Rarity.EPIC)
                , "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "neptune_koi")
                        .withBucketedFish("tide", "neptune_koi_bucket")
                        .withEntityToSpawn("tide", "neptune_koi")
                        .withSizeAndWeight(60, 13, 3500, 731)
                        .addRestriction(MoonPhaseRestriction.NEW_MOON)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(200))
                        .withRarity(Rarity.EPIC)
                , "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "pluto_snail")
                        .withBucketedFish("tide", "pluto_snail_bucket")
                        .withEntityToSpawn("tide", "pluto_snail")
                        .withSizeAndWeight(60, 13, 3500, 731)
                        .addRestriction(MoonPhaseRestriction.CRESCENT_PHASES)
                        .withDifficulty(Difficulty.HARD.moving().withHP(200))
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "sun_emblem")
                        .withBucketedFish("tide", "sun_emblem_bucket")
                        .withEntityToSpawn("tide", "sun_emblem")
                        .withSizeAndWeight(10, 3, 40, 10)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .addRestriction(MoonPhaseRestriction.FULL_MOON)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(200))
                        .withRarity(Rarity.EPIC)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "saturn_cuttlefish")
                        .withBucketedFish("tide", "saturn_cuttlefish_bucket")
                        .withEntityToSpawn("tide", "saturn_cuttlefish")
                        .withSizeAndWeight(60, 13, 3500, 731)
                        .addRestriction(MoonPhaseRestriction.FIRST_QUARTER)
                        .withDifficulty(Difficulty.HARD.moving().withHP(200))
                        .withRarity(Rarity.EPIC)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "marstilus")
                        .withBucketedFish("tide", "marstilus_bucket")
                        .withEntityToSpawn("tide", "marstilus")
                        .withSizeAndWeight(60, 13, 3500, 731)
                        .addRestriction(MoonPhaseRestriction.WANING_PHASES)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(200))
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "uranias_pisces")
                        .withBucketedFish("tide", "uranias_pisces_bucket")
                        .withEntityToSpawn("tide", "uranias_pisces")
                        .withSizeAndWeight(60, 13, 3500, 731)
                        .addRestriction(MoonPhaseRestriction.THIRD_QUARTER)
                        .withDifficulty(Difficulty.HARD.moving().withHP(200))
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("tide", "great_white_shark")
                        .withBucketedFish("tide", "great_white_shark_bucket")
                        .withEntityToSpawn("tide", "great_white_shark")
                        .withSizeAndWeight(400, 120, 16000, 11000)
                        .withDifficulty(Difficulty.HARD.withHP(300))
                        .withDaytimeRestriction(DaytimeRestriction.DAWN_AND_DUSK)
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepOcean(context)
                        .withFish("tide", "shooting_starfish")
                        .withBucketedFish("tide", "shooting_starfish_bucket")
                        .withEntityToSpawn("tide", "shooting_starfish")
                        .withSizeAndWeight(400, 120, 16000, 11000)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .addRestriction(MoonPhaseRestriction.FULL_MOON)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .withDifficulty(Difficulty.HARD.moving().vanishing().withHP(300))
                        .withRarity(Rarity.LEGENDARY),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepOcean(context)
                        .withFish("tide", "coelacanth")
                        .withBucketedFish("tide", "coelacanth_bucket")
                        .withEntityToSpawn("tide", "coelacanth")
                        .withSizeAndWeight(400, 120, 16000, 11000)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.RAIN)
                        .withDifficulty(Difficulty.HARD.vanishing().moving().withHP(300))
                        .withRarity(Rarity.LEGENDARY),
                "tide"
        );


        //
        //
        //  ,---.  ,--,--. ,--.  ,--.  ,---.   ,---.
        // | .--' ' ,-.  |  \  `'  /  | .-. : (  .-'
        // \ `--. \ '-'  |   \    /   \   --. .-'  `)
        //  `---'  `--`--'    `--'     `----' `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.caves(context)
                        .withFish("tide", "cave_eel")
                        .withBucketedFish("tide", "cave_eel_bucket")
                        .withEntityToSpawn("tide", "cave_eel")
                        .withSizeAndWeight(500, 150, 10000, 2000)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "deep_grouper")
                        .withBucketedFish("tide", "deep_grouper_bucket")
                        .withEntityToSpawn("tide", "deep_grouper")
                        .withSizeAndWeight(50, 15, 1000, 200)
                        .withDifficulty(Difficulty.EASY_DEEPSLATE)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caves(context)
                        .withFish("tide", "cave_crawler")
                        .withBucketedFish("tide", "cave_crawler_bucket")
                        .withEntityToSpawn("tide", "cave_crawler")
                        .withSizeAndWeight(50, 15, 1000, 200)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caves(context)
                        .withFish("tide", "glowfish")
                        .withBucketedFish("tide", "glowfish_bucket")
                        .withEntityToSpawn("tide", "glowfish")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caves(context)
                        .withFish("tide", "anglerfish")
                        .withBucketedFish("tide", "anglerfish_bucket")
                        .withEntityToSpawn("tide", "anglerfish")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caves(context)
                        .withFish("tide", "iron_tetra")
                        .withBucketedFish("tide", "iron_tetra_bucket")
                        .withEntityToSpawn("tide", "iron_tetra")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.EASY.moving().withHP(100))
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caves(context)
                        .withFish("tide", "gilded_minnow")
                        .withBucketedFish("tide", "gilded_minnow_bucket")
                        .withEntityToSpawn("tide", "gilded_minnow")
                        .withSizeAndWeight(6, 4, 5, 3)
                        .withDifficulty(Difficulty.MEDIUM.withHP(400))
                        .withBaseChance(2)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("tide", "windbass")
                        .withBucketedFish("tide", "windbass_bucket")
                        .withEntityToSpawn("tide", "windbass")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .addRestriction(DimensionRestriction.OVERWORLD)
                        .addRestriction(FluidRestriction.WATER)
                        .addRestriction(StructureRestriction.TRIAL_CHAMBERS)
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("tide", "devils_hole_pupfish")
                        .withBucketedFish("tide", "devils_hole_pupfish_bucket")
                        .withEntityToSpawn("tide", "devils_hole_pupfish")
                        .withSizeAndWeight(35, 25, 1000, 700)
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.HARD.vanishing().moving().withHP(200))
                        .addRestrictions(FluidRestriction.WATER)
                        .addRestrictions(DimensionRestriction.OVERWORLD)
                        .addRestrictions(new ElevationRestriction(20, 30, ""))
                , "tide"
        );

        //
        //    ,--.         ,--.                   ,--.
        //  ,-|  | ,--.--. `--'  ,---.   ,---.  ,-'  '-.  ,---.  ,--,--,   ,---.       ,---.  ,--,--. ,--.  ,--.  ,---.   ,---.
        // ' .-. | |  .--' ,--. | .-. | (  .-'  '-.  .-' | .-. | |      \ | .-. :     | .--' ' ,-.  |  \  `'  /  | .-. : (  .-'
        // \ `-' | |  |    |  | | '-' ' .-'  `)   |  |   ' '-' ' |  ||  | \   --.     \ `--. \ '-'  |   \    /   \   --. .-'  `)
        //  `---'  `--'    `--' |  |-'  `----'    `--'    `---'  `--''--'  `----'      `---'  `--`--'    `--'     `----' `----'
        //                      `--'

        FishRegistration.register(
                context,
                PresetRestrictions.dripstoneCaves(context)
                        .withFish("tide", "dripstone_darter")
                        .withBucketedFish("tide", "dripstone_darter_bucket")
                        .withEntityToSpawn("tide", "dripstone_darter")
                        .withSizeAndWeight(6, 2, 7, 6)
                        .withDifficulty(Difficulty.MEDIUM_DRIPSTONE)
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );


        //
        //    ,--.                                 ,--.            ,--.
        //  ,-|  |  ,---.   ,---.   ,---.   ,---.  |  |  ,--,--. ,-'  '-.  ,---.
        // ' .-. | | .-. : | .-. : | .-. | (  .-'  |  | ' ,-.  | '-.  .-' | .-. :
        // \ `-' | \   --. \   --. | '-' ' .-'  `) |  | \ '-'  |   |  |   \   --.
        //  `---'   `----'  `----' |  |-'  `----'  `--'  `--`--'   `--'    `----'
        //                         `--'

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "bedrock_tetra")
                        .withBucketedFish("tide", "bedrock_tetra_bucket")
                        .withEntityToSpawn("tide", "bedrock_tetra")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.withHP(500))
                        .withBaseChance(2)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "luminescent_jellyfish")
                        .withBucketedFish("tide", "crystalline_carp_bucket")
                        .withEntityToSpawn("tide", "crystalline_carp")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.moving())
                        .withRarity(Rarity.RARE)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "crystalline_carp")
                        .withBucketedFish("tide", "crystalline_carp_bucket")
                        .withEntityToSpawn("tide", "crystalline_carp")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.moving().withHP(200))
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "shadow_snapper")
                        .withBucketedFish("tide", "shadow_snapper_bucket")
                        .withEntityToSpawn("tide", "shadow_snapper")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.EASY_DEEPSLATE)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "abyss_angler")
                        .withBucketedFish("tide", "abyss_angler_bucket")
                        .withEntityToSpawn("tide", "abyss_angler")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "lapis_lanternfish")
                        .withBucketedFish("tide", "lapis_lanternfish_bucket")
                        .withEntityToSpawn("tide", "lapis_lanternfish")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "crystal_shrimp")
                        .withBucketedFish("tide", "crystal_shrimp_bucket")
                        .withEntityToSpawn("tide", "crystal_shrimp")
                        .withSizeAndWeight(10, 3, 20, 10)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE)
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("tide", "midas_fish")
                        .withBucketedFish("tide", "echo_snapper_bucket")
                        .withEntityToSpawn("tide", "echo_snapper")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.HARD.vanishing().moving().withHP(300))
                        .addRestriction(DimensionRestriction.OVERWORLD)
                        .addRestriction(FluidRestriction.WATER)
                        .addRestriction(new LuckRestriction(5, ""))
                        .withPercentageChance(0.001f)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish("tide", "chasm_eel")
                        .withBucketedFish("tide", "chasm_eel_bucket")
                        .withEntityToSpawn("tide", "chasm_eel")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withRarity(Rarity.EPIC),
                "tide"
        );

        //
        //    ,--.                                ,--.                  ,--.
        //  ,-|  |  ,---.   ,---.   ,---.       ,-|  |  ,--,--. ,--.--. |  |,-.
        // ' .-. | | .-. : | .-. : | .-. |     ' .-. | ' ,-.  | |  .--' |     /
        // \ `-' | \   --. \   --. | '-' '     \ `-' | \ '-'  | |  |    |  \  \
        //  `---'   `----'  `----' |  |-'       `---'   `--`--' `--'    `--'`--'
        //                         `--'

        FishRegistration.register(
                context,
                PresetRestrictions.deepDark(context)
                        .withFish("tide", "echo_snapper")
                        .withBucketedFish("tide", "echo_snapper_bucket")
                        .withEntityToSpawn("tide", "echo_snapper")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD_DEEP_DARK)
                        .withRarity(Rarity.EPIC),
                "tide"
        );


        //
        // ,--.
        // |  |  ,--,--. ,--.  ,--.  ,--,--.
        // |  | ' ,-.  |  \  `'  /  ' ,-.  |
        // |  | \ '-'  |   \    /   \ '-'  |
        // `--'  `--`--'    `--'     `--`--'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.surfaceLava(context)
                        .withFish("tide", "magma_mackerel")
                        .withBucketedFish("tide", "magma_mackerel_bucket")
                        .withEntityToSpawn("tide", "magma_mackerel")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.surfaceLava(context)
                        .withFish("tide", "ember_koi")
                        .withBucketedFish("tide", "ember_koi_bucket")
                        .withEntityToSpawn("tide", "ember_koi")
                        .withSizeAndWeight(60, 13, 3500, 731)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.surfaceLava(context)
                        .withFish("tide", "ash_perch")
                        .withBucketedFish("tide", "ash_perch_bucket")
                        .withEntityToSpawn("tide", "ash_perch")
                        .withSizeAndWeight(27.0f, 11, 500, 352)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caveLava(context)
                        .withFish("tide", "obsidian_pike")
                        .withBucketedFish("tide", "obsidian_pike_bucket")
                        .withEntityToSpawn("tide", "obsidian_pike")
                        .withSizeAndWeight(75, 20, 5000, 3000)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.caveLava(context)
                        .withFish("tide", "volcano_tuna")
                        .withBucketedFish("tide", "volcano_tuna_bucket")
                        .withEntityToSpawn("tide", "volcano_tuna")
                        .withSizeAndWeight(150, 50, 120000, 60000)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.UNCOMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.crimsonForest(context)
                        .withFish("tide", "crimson_fangjaw")
                        .withBucketedFish("tide", "crimson_fangjaw_bucket")
                        .withEntityToSpawn("tide", "crimson_fangjaw")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish("tide", "warped_guppy")
                        .withBucketedFish("tide", "warped_guppy_bucket")
                        .withEntityToSpawn("tide", "warped_guppy")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withRarity(Rarity.RARE),
                "tide"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.soulSandValley(context)
                        .withFish("tide", "soulscale")
                        .withBucketedFish("tide", "soulscale_bucket")
                        .withEntityToSpawn("tide", "soulscale")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD.vanishing())
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish("tide", "witherfin")
                        .withBucketedFish("tide", "witherfin_bucket")
                        .withEntityToSpawn("tide", "witherfin")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish("tide", "inferno_guppy")
                        .withBucketedFish("tide", "inferno_guppy_bucket")
                        .withEntityToSpawn("tide", "inferno_guppy")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish("tide", "blazing_swordfish")
                        .withBucketedFish("tide", "blazing_swordfish_bucket")
                        .withEntityToSpawn("tide", "blazing_swordfish")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD.moving().withHP(300))
                        .withBaseChance(2)
                        .withRarity(Rarity.EPIC),
                "tide"
        );


        //
        //                    ,--.    ,--.
        // ,--.  ,--.  ,---.  `--'  ,-|  |
        //  \  `'  /  | .-. | ,--. ' .-. |
        //   \    /   ' '-' ' |  | \ `-' |
        //    `--'     `---'  `--'  `---'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "amber_rockfish")
                        .withBucketedFish("tide", "amber_rockfish_bucket")
                        .withEntityToSpawn("tide", "amber_rockfish")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "pale_clubfish")
                        .withBucketedFish("tide", "pale_clubfish_bucket")
                        .withEntityToSpawn("tide", "pale_clubfish")
                        .withSizeAndWeight(300, 150, 26000, 7000)
                        .withDifficulty(Difficulty.EASY.vanishing())
                        .withRarity(Rarity.COMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "enderfin")
                        .withBucketedFish("tide", "enderfin_bucket")
                        .withEntityToSpawn("tide", "enderfin")
                        .withSizeAndWeight(300, 150, 16000, 7000)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "incandescent_larva")
                        .withBucketedFish("tide", "enderfin_bucket")
                        .withEntityToSpawn("tide", "enderfin")
                        .withSizeAndWeight(6, 4, 5, 3)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "bedrock_bug")
                        .withBucketedFish("tide", "bedrock_bug_bucket")
                        .withEntityToSpawn("tide", "bedrock_bug")
                        .withSizeAndWeight(300, 150, 26000, 7000)
                        .withDifficulty(Difficulty.EASY)
                        .withRarity(Rarity.COMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "sleepy_carp")
                        .withBucketedFish("tide", "sleepy_carp_bucket")
                        .withEntityToSpawn("tide", "sleepy_carp")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON)
                        , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "chorus_cod")
                        .withBucketedFish("tide", "chorus_cod_bucket")
                        .withEntityToSpawn("tide", "chorus_cod")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.UNCOMMON)
                        .withBaseChance(4)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "ender_glider")
                        .withBucketedFish("tide", "ender_glider_bucket")
                        .withEntityToSpawn("tide", "ender_glider")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving(2))
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "endergazer")
                        .withBucketedFish("tide", "endergazer_bucket")
                        .withEntityToSpawn("tide", "endergazer")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "blue_neonfish")
                        .withBucketedFish("tide", "blue_neonfish_bucket")
                        .withEntityToSpawn("tide", "blue_neonfish")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                        .withBaseChance(15),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "judgment_fish")
                        .withBucketedFish("tide", "judgment_fish_bucket")
                        .withEntityToSpawn("tide", "judgment_fish")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "deep_blue")
                        .withBucketedFish("tide", "deep_blue_bucket")
                        .withEntityToSpawn("tide", "deep_blue")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "violet_carp")
                        .withBucketedFish("tide", "violet_carp_bucket")
                        .withEntityToSpawn("tide", "violet_carp")
                        .withSizeAndWeight(60, 20, 6000, 4000)
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "nephrosilu")
                        .withBucketedFish("tide", "nephrosilu_bucket")
                        .withEntityToSpawn("tide", "nephrosilu")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.EASY)
                        .withBaseChance(4)
                        .withRarity(Rarity.UNCOMMON)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "red_40")
                        .withBucketedFish("tide", "red_40_bucket")
                        .withEntityToSpawn("tide", "red_40")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(200))
                        .withBaseChance(3)
                        .withRarity(Rarity.RARE)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "dutchman_sock")
                        .withBucketedFish("tide", "dutchman_sock_bucket")
                        .withEntityToSpawn("tide", "dutchman_sock")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(200))
                        .withBaseChance(3)
                        .withRarity(Rarity.RARE)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "vengeance")
                        .withBucketedFish("tide", "vengeance_bucket")
                        .withEntityToSpawn("tide", "vengeance")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(200))
                        .withBaseChance(3)
                        .withRarity(Rarity.RARE)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldOcean(context)
                        .withFish("tide", "pentapus")
                        .withBucketedFish("tide", "pentapus_bucket")
                        .withEntityToSpawn("tide", "pentapus")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(200))
                        .withBaseChance(3)
                        .withRarity(Rarity.RARE)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "elytrout")
                        .withBucketedFish("tide", "elytrout_bucket")
                        .withEntityToSpawn("tide", "elytrout")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(200))
                        .withRarity(Rarity.RARE)
                        .withBaseChance(3)
                , "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "mantyvern")
                        .withBucketedFish("tide", "mantyvern_bucket")
                        .withEntityToSpawn("tide", "mantyvern")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving().withHP(300))
                        .withBaseChance(2),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "snatcher_squid")
                        .withBucketedFish("tide", "snatcher_squid_bucket")
                        .withEntityToSpawn("tide", "snatcher_squid")
                        .withSizeAndWeight(40, 20, 1300, 700)
                        .withDifficulty(Difficulty.HARD.moving().withHP(300))
                        .withRarity(Rarity.EPIC)
                        .withBaseChance(2),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "darkness_eater")
                        .withBucketedFish("tide", "darkness_eater_bucket")
                        .withEntityToSpawn("tide", "darkness_eater")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD.moving().withHP(300))
                        .withRarity(Rarity.EPIC)
                        .withBaseChance(2),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.overworldVoid(context)
                        .withFish("tide", "shadow_shark")
                        .withBucketedFish("tide", "shadow_shark_bucket")
                        .withEntityToSpawn("tide", "shadow_shark")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD.moving().withHP(300))
                        .withRarity(Rarity.EPIC)
                        .withBaseChance(2),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "voidseeker")
                        .withBucketedFish("tide", "voidseeker_bucket")
                        .withEntityToSpawn("tide", "voidseeker")
                        .withSizeAndWeight(60, 20, 7000, 2000)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(300))
                        .withRarity(Rarity.LEGENDARY)
                        .withBaseChance(1),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldOcean(context)
                        .withFish("tide", "alpha_fish")
                        .withBucketedFish("tide", "alpha_fish_bucket")
                        .withEntityToSpawn("tide", "alpha_fish")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withDifficulty(Difficulty.HARD.moving().withHP(600))
                        .withRarity(Rarity.LEGENDARY)
                        .withBaseChance(1),
                "tide"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.endVoid(context)
                        .withFish("tide", "dragon_fish")
                        .withBucketedFish("tide", "dragon_fish_bucket")
                        .withEntityToSpawn("tide", "dragon_fish")
                        .withSizeAndWeight(500, 150, 700000, 130000)
                        .withDifficulty(Difficulty.VOIDBITER)
                        .withRarity(Rarity.LEGENDARY)
                        .withBaseChance(1),
                "tide"
        );

    }
}
