package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.starcatcher.registry.FishProperties;

public class DGTideFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {
        //
        //  ,--.   ,--.    ,--.
        //,-'  '-. `--'  ,-|  |  ,---.
        //'-.  .-' ,--. ' .-. | | .-. :
        //  |  |   |  | \ `-' | \   --.
        //  `--'   `--'  `---'   `----'
        //

        //freshwater
        register(
                overworldRiverFish(U.holderItem("tide", "yellow_perch"))
                        .withBucketedFish(U.holderItem("tide", "yellow_perch_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "yellow_perch"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27, 11, 500, 352))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
        );

        register(
                overworldColdLakeFish(U.holderItem("tide", "rainbow_trout"))
                        .withBucketedFish(U.holderItem("tide", "rainbow_trout_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "rainbow_trout"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35, 8, 1600, 1200))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldRiverFish(U.holderItem("tide", "largemouth_bass"))
                        .withBucketedFish(U.holderItem("tide", "largemouth_bass_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "largemouth_bass"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldRiverFish(U.holderItem("tide", "brook_trout"))
                        .withBucketedFish(U.holderItem("tide", "brook_trout_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "brook_trout"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35, 8, 1600, 1200))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldRiverFish(U.holderItem("tide", "white_crappie"))
                        .withBucketedFish(U.holderItem("tide", "white_crappie_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "white_crappie"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35, 8, 1600, 1200))
                        .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldRiverFish(U.holderItem("tide", "black_crappie"))
                        .withBucketedFish(U.holderItem("tide", "black_crappie_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "black_crappie"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35, 8, 1600, 1200))
                        .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );


        register(
                overworldRiverFish(U.holderItem("tide", "carp"))
                        .withBucketedFish(U.holderItem("tide", "carp_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "carp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );


        register(
                overworldColdRiverFish(U.holderItem("tide", "bluegill"))
                        .withBucketedFish(U.holderItem("tide", "bluegill_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "bluegill"))
                        .withSizeAndWeight(FishProperties.sizeWeight(20, 5, 400, 100))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );


        register(
                overworldWarmLakeFish(U.holderItem("tide", "guppy"))
                        .withBucketedFish(U.holderItem("tide", "guppy_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "guppy"))
                        .withSizeAndWeight(FishProperties.sizeWeight(20, 5, 400, 100))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );


        register(
                overworldWarmLakeFish(U.holderItem("tide", "walleye"))
                        .withBucketedFish(U.holderItem("tide", "walleye_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "walleye"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
        );


        register(
                overworldWarmLakeFish(U.holderItem("tide", "catfish"))
                        .withBucketedFish(U.holderItem("tide", "catfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "catfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
        );


        register(
                overworldCherryGroveFish(U.holderItem("tide", "blossom_bass"))
                        .withBucketedFish(U.holderItem("tide", "blossom_bass_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "blossom_bass"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA_ONE_THIN)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        register(
                overworldJungleFish(U.holderItem("tide", "arapaima"))
                        .withBucketedFish(U.holderItem("tide", "arapaima_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "arapaima"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        register(
                overworldWarmLakeFish(U.holderItem("tide", "mirage_catfish"))
                        .withBucketedFish(U.holderItem("tide", "mirage_catfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "mirage_catfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 10000, 3000))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldColdLakeFish(U.holderItem("tide", "frostbite_flounder"))
                        .withBucketedFish(U.holderItem("tide", "frostbite_flounder_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "frostbite_flounder"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldWarmLakeFish(U.holderItem("tide", "sand_tiger_shark"))
                        .withBucketedFish(U.holderItem("tide", "sand_tiger_shark_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "sand_tiger_shark"))
                        .withSizeAndWeight(FishProperties.sizeWeight(400, 120, 16000, 11000))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA_ONE_THIN)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        register(
                overworldColdRiverFish(U.holderItem("tide", "sturgeon"))
                        .withBucketedFish(U.holderItem("tide", "sturgeon_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "sturgeon"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN_NO_DECAY)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        register(
                overworldSwampFish(U.holderItem("tide", "slimy_salmon"))
                        .withBucketedFish(U.holderItem("tide", "slimy_salmon_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "slimy_salmon"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        register(
                overworldLakeFish(U.holderItem("tide", "mooneye"))
                        .withBucketedFish(U.holderItem("tide", "mooneye_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "mooneye"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_THIN_FAST)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        register(
                overworldRiverFish(U.holderItem("tide", "bull_shark"))
                        .withBucketedFish(U.holderItem("tide", "bull_shark_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "bull_shark"))
                        .withSizeAndWeight(FishProperties.sizeWeight(400, 120, 16000, 11000))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_THIN_FAST)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        //oceans
        register(
                overworldOceanFish(U.holderItem("tide", "mackerel"))
                        .withBucketedFish(U.holderItem("tide", "mackerel_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "mackerel"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.EASY)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "tuna"))
                        .withBucketedFish(U.holderItem("tide", "tuna_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "tuna"))
                        .withSizeAndWeight(FishProperties.sizeWeight(150, 50, 120000, 60000))
                        .withDifficulty(FishProperties.Difficulty.EASY)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "red_snapper"))
                        .withBucketedFish(U.holderItem("tide", "red_snapper_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "red_snapper"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "snook"))
                        .withBucketedFish(U.holderItem("tide", "snook_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "snook"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "anchovy"))
                        .withBucketedFish(U.holderItem("tide", "anchovy_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "anchovy"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "flounder"))
                        .withBucketedFish(U.holderItem("tide", "flounder_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "flounder"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldWarmOceanFish(U.holderItem("tide", "angelfish"))
                        .withBucketedFish(U.holderItem("tide", "angelfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "angelfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                        .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldMushroomFieldsFish(U.holderItem("tide", "spore_stalker"))
                        .withBucketedFish(U.holderItem("tide", "spore_stalker_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "spore_stalker"))
                        .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldWarmOceanFish(U.holderItem("tide", "mahi_mahi"))
                        .withBucketedFish(U.holderItem("tide", "mahi_mahi_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "mahi_mahi"))
                        .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_BIG_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldWarmOceanFish(U.holderItem("tide", "sailfish"))
                        .withBucketedFish(U.holderItem("tide", "sailfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "sailfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_THIN)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldWarmOceanFish(U.holderItem("tide", "swordfish"))
                        .withBucketedFish(U.holderItem("tide", "swordfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "swordfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN_NO_DECAY)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "manta_ray"))
                        .withBucketedFish(U.holderItem("tide", "manta_ray_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "manta_ray"))
                        .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN_NO_DECAY)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        //todo ocean monument structure restriction
        register(
                overworldColdOceanFish(U.holderItem("tide", "aquathorn"))
                        .withBucketedFish(U.holderItem("tide", "aquathorn_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "aquathorn"))
                        .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
                        .withRarity(FishProperties.Rarity.EPIC)
                        .withBaseChance(2)
        );


        register(
                overworldOceanFish(U.holderItem("tide", "neptune_koi"))
                        .withBucketedFish(U.holderItem("tide", "neptune_koi_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "neptune_koi"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        register(
                overworldOceanFish(U.holderItem("tide", "pluto_snail"))
                        .withBucketedFish(U.holderItem("tide", "pluto_snail_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "pluto_snail"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "sun_emblem"))
                        .withBucketedFish(U.holderItem("tide", "sun_emblem_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "sun_emblem"))
                        .withSizeAndWeight(FishProperties.sizeWeight(10, 3, 40, 10))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        register(
                overworldOceanFish(U.holderItem("tide", "saturn_cuttlefish"))
                        .withBucketedFish(U.holderItem("tide", "saturn_cuttlefish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "saturn_cuttlefish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        register(
                overworldOceanFish(U.holderItem("tide", "marstilus"))
                        .withBucketedFish(U.holderItem("tide", "marstilus_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "marstilus"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        register(
                overworldOceanFish(U.holderItem("tide", "uranias_pisces"))
                        .withBucketedFish(U.holderItem("tide", "uranias_pisces_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "uranias_pisces"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );

        register(
                overworldOceanFish(U.holderItem("tide", "great_white_shark"))
                        .withBucketedFish(U.holderItem("tide", "great_white_shark_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "great_white_shark"))
                        .withSizeAndWeight(FishProperties.sizeWeight(400, 120, 16000, 11000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withRarity(FishProperties.Rarity.EPIC)
        );

        register(
                overworldDeepOceanFish(U.holderItem("tide", "shooting_starfish"))
                        .withBucketedFish(U.holderItem("tide", "shooting_starfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "shooting_starfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(400, 120, 16000, 11000))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withBaseChance(1)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .withRarity(FishProperties.Rarity.EPIC)
        );

        register(
                overworldDeepOceanFish(U.holderItem("tide", "coelacanth"))
                        .withBucketedFish(U.holderItem("tide", "coelacanth_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "coelacanth"))
                        .withSizeAndWeight(FishProperties.sizeWeight(400, 120, 16000, 11000))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA_MOVING)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.RAIN)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .withRarity(FishProperties.Rarity.LEGENDARY)
        );


        //underground
        register(
                overworldUndergroundFish(U.holderItem("tide", "cave_eel"))
                        .withBucketedFish(U.holderItem("tide", "cave_eel_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "cave_eel"))
                        .withSizeAndWeight(FishProperties.sizeWeight(500, 150, 10000, 2000))
                        .withDifficulty(FishProperties.Difficulty.EASY)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldUndergroundFish(U.holderItem("tide", "deep_grouper"))
                        .withBucketedFish(U.holderItem("tide", "deep_grouper_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "deep_grouper"))
                        .withSizeAndWeight(FishProperties.sizeWeight(50, 15, 1000, 200))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldUndergroundFish(U.holderItem("tide", "cave_crawler"))
                        .withBucketedFish(U.holderItem("tide", "cave_crawler_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "cave_crawler"))
                        .withSizeAndWeight(FishProperties.sizeWeight(50, 15, 1000, 200))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "shadow_snapper"))
                        .withBucketedFish(U.holderItem("tide", "shadow_snapper_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "shadow_snapper"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldUndergroundFish(U.holderItem("tide", "glowfish"))
                        .withBucketedFish(U.holderItem("tide", "glowfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "glowfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldCavesFish(U.holderItem("tide", "anglerfish"))
                        .withBucketedFish(U.holderItem("tide", "anglerfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "anglerfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "abyss_angler"))
                        .withBucketedFish(U.holderItem("tide", "abyss_angler_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "abyss_angler"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldUndergroundFish(U.holderItem("tide", "iron_tetra"))
                        .withBucketedFish(U.holderItem("tide", "iron_tetra_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "iron_tetra"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.EIGHT_STONE_SPOTS)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldUndergroundFish(U.holderItem("tide", "dripstone_darter"))
                        .withBucketedFish(U.holderItem("tide", "dripstone_darter_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "dripstone_darter"))
                        .withSizeAndWeight(FishProperties.sizeWeight(6, 2, 7, 6))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "lapis_lanternfish"))
                        .withBucketedFish(U.holderItem("tide", "lapis_lanternfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "lapis_lanternfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "crystal_shrimp"))
                        .withBucketedFish(U.holderItem("tide", "crystal_shrimp_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "crystal_shrimp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(10, 3, 20, 10))
                        .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "crystalline_carp"))
                        .withBucketedFish(U.holderItem("tide", "crystalline_carp_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "crystalline_carp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "luminescent_jellyfish"))
                        .withBucketedFish(U.holderItem("tide", "crystalline_carp_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "crystalline_carp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldCavesFish(U.holderItem("tide", "gilded_minnow"))
                        .withBucketedFish(U.holderItem("tide", "gilded_minnow_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "gilded_minnow"))
                        .withSizeAndWeight(FishProperties.sizeWeight(6, 4, 5, 3))
                        .withDifficulty(FishProperties.Difficulty.FOUR_THIN)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                overworldCavesFish(U.holderItem("tide", "bedrock_tetra"))
                        .withBucketedFish(U.holderItem("tide", "bedrock_tetra_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "bedrock_tetra"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_STONE_SPOTS)
                        .withRarity(FishProperties.Rarity.RARE)
        );

/*        register( doesnt exist on 1.20
                overworldCavesFish(U.holderItem("tide", "windbass"))
                        .withBucketedFish(U.holderItem("tide", "windbass_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "windbass"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.FOUR_BIG_MOVING)
                        .withBaseChance(2)
                        .withRarity(FishProperties.Rarity.EPIC)
        );*/

        register(
                overworldDeepDarkFish(U.holderItem("tide", "echo_snapper"))
                        .withBucketedFish(U.holderItem("tide", "echo_snapper_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "echo_snapper"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_BIG_MOVING)
                        .withRarity(FishProperties.Rarity.EPIC)
        );

        register(
                overworldDeepDarkFish(U.holderItem("tide", "chasm_eel"))
                        .withBucketedFish(U.holderItem("tide", "chasm_eel_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "chasm_eel"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.TWO_THIN)
                        .withRarity(FishProperties.Rarity.EPIC)
        );

        register(
                overworldDeepslateFish(U.holderItem("tide", "midas_fish"))
                        .withBucketedFish(U.holderItem("tide", "echo_snapper_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "echo_snapper"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withRarity(FishProperties.Rarity.LEGENDARY)
                        .withDifficulty(FishProperties.Difficulty.SINGLE_THIN_FAST)
                        .withBaseChance(1)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
        );

        register(
                overworldCavesFish(U.holderItem("tide", "devils_hole_pupfish"))
                        .withBucketedFish(U.holderItem("tide", "devils_hole_pupfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "devils_hole_pupfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35, 25, 1000, 700))
                        .withRarity(FishProperties.Rarity.LEGENDARY)
                        .withDifficulty(FishProperties.Difficulty.SINGLE_THIN_FAST)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .addRestrictions(DimensionRestriction.OVERWORLD,
                                new ElevationRestriction(20, 30, "")
                        )
        );


        //lava
        register(
                overworldSurfaceLava(U.holderItem("tide", "magma_mackerel"))
                        .withBucketedFish(U.holderItem("tide", "magma_mackerel_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "magma_mackerel"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldSurfaceLava(U.holderItem("tide", "ember_koi"))
                        .withBucketedFish(U.holderItem("tide", "ember_koi_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "ember_koi"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldSurfaceLava(U.holderItem("tide", "ash_perch"))
                        .withBucketedFish(U.holderItem("tide", "ash_perch_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "ash_perch"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldUndergroundLava(U.holderItem("tide", "obsidian_pike"))
                        .withBucketedFish(U.holderItem("tide", "obsidian_pike_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "obsidian_pike"))
                        .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
        );

        register(
                overworldUndergroundLava(U.holderItem("tide", "volcano_tuna"))
                        .withBucketedFish(U.holderItem("tide", "volcano_tuna_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "volcano_tuna"))
                        .withSizeAndWeight(FishProperties.sizeWeight(150, 50, 120000, 60000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(
                netherLavaCrimsonForestFish(U.holderItem("tide", "crimson_fangjaw"))
                        .withBucketedFish(U.holderItem("tide", "crimson_fangjaw_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "crimson_fangjaw"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                netherLavaWarpedForestFish(U.holderItem("tide", "warped_guppy"))
                        .withBucketedFish(U.holderItem("tide", "warped_guppy_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "warped_guppy"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
        );


        register(
                netherLavaSoulSandValleyFish(U.holderItem("tide", "soulscale"))
                        .withBucketedFish(U.holderItem("tide", "soulscale_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "soulscale"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                netherLavaFish(U.holderItem("tide", "witherfin"))
                        .withBucketedFish(U.holderItem("tide", "witherfin_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "witherfin"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                netherLavaFish(U.holderItem("tide", "inferno_guppy"))
                        .withBucketedFish(U.holderItem("tide", "inferno_guppy_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "inferno_guppy"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.RARE)
        );

        register(
                netherLavaFish(U.holderItem("tide", "blazing_swordfish"))
                        .withBucketedFish(U.holderItem("tide", "blazing_swordfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "blazing_swordfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.EPIC)
        );


        //void
        register(
                endVoidFishing(U.holderItem("tide", "amber_rockfish"))
                        .withBucketedFish(U.holderItem("tide", "amber_rockfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "amber_rockfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.TWO_STONE_SPOTS_EASY)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withBaseChance(20)
        );

        register(
                endVoidFishing(U.holderItem("tide", "pale_clubfish"))
                        .withBucketedFish(U.holderItem("tide", "pale_clubfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "pale_clubfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(300, 150, 26000, 7000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withBaseChance(20)
        );

        register(
                endVoidFishing(U.holderItem("tide", "enderfin"))
                        .withBucketedFish(U.holderItem("tide", "enderfin_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "enderfin"))
                        .withSizeAndWeight(FishProperties.sizeWeight(300, 150, 16000, 7000))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withBaseChance(20)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "incandescent_larva"))
                        .withBucketedFish(U.holderItem("tide", "enderfin_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "enderfin"))
                        .withSizeAndWeight(FishProperties.sizeWeight(6, 4, 5, 3))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withBaseChance(20)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "bedrock_bug"))
                        .withBucketedFish(U.holderItem("tide", "bedrock_bug_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "bedrock_bug"))
                        .withSizeAndWeight(FishProperties.sizeWeight(300, 150, 26000, 7000))
                        .withDifficulty(FishProperties.Difficulty.TWO_STONE_SPOTS_EASY)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withBaseChance(20)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "sleepy_carp"))
                        .withBucketedFish(U.holderItem("tide", "sleepy_carp_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "sleepy_carp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withBaseChance(20)
        );

        register(
                endVoidFishing(U.holderItem("tide", "chorus_cod"))
                        .withBucketedFish(U.holderItem("tide", "chorus_cod_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "chorus_cod"))
                        .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                endVoidFishing(U.holderItem("tide", "ender_glider"))
                        .withBucketedFish(U.holderItem("tide", "ender_glider_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "ender_glider"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                endVoidFishing(U.holderItem("tide", "endergazer"))
                        .withBucketedFish(U.holderItem("tide", "endergazer_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "endergazer"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "blue_neonfish"))
                        .withBucketedFish(U.holderItem("tide", "blue_neonfish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "blue_neonfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "judgment_fish"))
                        .withBucketedFish(U.holderItem("tide", "judgment_fish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "judgment_fish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "deep_blue"))
                        .withBucketedFish(U.holderItem("tide", "deep_blue_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "deep_blue"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                endVoidFishing(U.holderItem("tide", "violet_carp"))
                        .withBucketedFish(U.holderItem("tide", "violet_carp_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "violet_carp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_BIG_VANISHING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "nephrosilu"))
                        .withBucketedFish(U.holderItem("tide", "nephrosilu_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "nephrosilu"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_THIN)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withBaseChance(15)
        );

        register(
                endVoidFishing(U.holderItem("tide", "red_40"))
                        .withBucketedFish(U.holderItem("tide", "red_40_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "red_40"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_THIN)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withBaseChance(10)
        );

        register(
                endVoidFishing(U.holderItem("tide", "dutchman_sock"))
                        .withBucketedFish(U.holderItem("tide", "dutchman_sock_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "dutchman_sock"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_THIN_MOVING)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withBaseChance(10)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "vengeance"))
                        .withBucketedFish(U.holderItem("tide", "vengeance_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "vengeance"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.THREE_BIG_TWO_THIN_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withBaseChance(10)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "pentapus"))
                        .withBucketedFish(U.holderItem("tide", "pentapus_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "pentapus"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.FOUR_THIN_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withBaseChance(10)
        );

        register(
                endVoidFishing(U.holderItem("tide", "elytrout"))
                        .withBucketedFish(U.holderItem("tide", "elytrout_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "elytrout"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withBaseChance(10)
        );

        register(
                endVoidFishing(U.holderItem("tide", "mantyvern"))
                        .withBucketedFish(U.holderItem("tide", "mantyvern_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "mantyvern"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_VANISHING)
                        .withRarity(FishProperties.Rarity.EPIC)
                        .withBaseChance(10)
        );

        register(
                endVoidFishing(U.holderItem("tide", "snatcher_squid"))
                        .withBucketedFish(U.holderItem("tide", "snatcher_squid_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "snatcher_squid"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 1300, 700))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA_ONE_THIN)
                        .withRarity(FishProperties.Rarity.EPIC)
                        .withBaseChance(10)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "darkness_eater"))
                        .withBucketedFish(U.holderItem("tide", "darkness_eater_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "darkness_eater"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.HEAVY_EIGHT_AQUA)
                        .withRarity(FishProperties.Rarity.EPIC)
                        .withBaseChance(10)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "shadow_shark"))
                        .withBucketedFish(U.holderItem("tide", "shadow_shark_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "shadow_shark"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.TWO_AQUA_ONE_THIN)
                        .withRarity(FishProperties.Rarity.EPIC)
                        .withBaseChance(10)
        );

        register(
                endVoidFishing(U.holderItem("tide", "voidseeker"))
                        .withBucketedFish(U.holderItem("tide", "voidseeker_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "voidseeker"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION_AQUA)
                        .withRarity(FishProperties.Rarity.LEGENDARY)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .withBaseChance(3)
        );

        register(
                overworldVoidFishing(U.holderItem("tide", "alpha_fish"))
                        .withBucketedFish(U.holderItem("tide", "alpha_fish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "alpha_fish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000))
                        .withSkipMinigame(true)
                        .withRarity(FishProperties.Rarity.LEGENDARY)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .withBaseChance(3)
        );

        register(
                endVoidFishing(U.holderItem("tide", "dragon_fish"))
                        .withBucketedFish(U.holderItem("tide", "dragon_fish_bucket"))
                        .withEntityToSpawn(U.holderEntity("tide", "dragon_fish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(500, 150, 700000, 130000))
                        .withDifficulty(FishProperties.Difficulty.WITHER)
                        .withRarity(FishProperties.Rarity.LEGENDARY)
                        .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                        .withBaseChance(3)
        );

    }
}
