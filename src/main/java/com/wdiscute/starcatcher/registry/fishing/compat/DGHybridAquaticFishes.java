package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.registry.fishrestrictions.DaytimeRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.WeatherRestriction;
import com.wdiscute.starcatcher.registry.FishProperties;

public class DGHybridAquaticFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {

        //Literally The Coolest Mod Ever (this fact has been fact-checked by true fisherman ✅)

        //
        // ,--.  ,--.           ,--.            ,--.    ,--.       ,---.                               ,--.   ,--.
        // |  '--'  | ,--. ,--. |  |-.  ,--.--. `--'  ,-|  |      /  O  \   ,---.  ,--.,--.  ,--,--. ,-'  '-. `--'  ,---.
        // |  .--.  |  \  '  /  | .-. ' |  .--' ,--. ' .-. |     |  .-.  | | .-. | |  ||  | ' ,-.  | '-.  .-' ,--. | .--'
        // |  |  |  |   \   '   | `-' | |  |    |  | \ `-' |     |  | |  | ' '-' | '  ''  ' \ '-'  |   |  |   |  | \ `--.
        // `--'  `--' .-'  /     `---'  `--'    `--'  `---'      `--' `--'  `-|  |  `----'   `--`--'   `--'   `--'  `---'
        //            `---'                                                   `--'                                                                   |_|

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "clownfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "clownfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(8, 3, 140, 60))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR)
        );

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "surgeonfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "surgeonfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 600, 200))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "blowfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "blowfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(18, 4, 5, 3)) // Haven't measured this yet.
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "boxfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "boxfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(27, 13, 180, 90)) // The weight is just an estimate.
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "damselfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "damselfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(6, 3, 18, 5))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "moray_eel"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "moray_eel"))
                        .withSizeAndWeight(FishProperties.sizeWeight(180, 70, 21000, 8000))
                        .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "stingray"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "stingray"))
                        .withSizeAndWeight(FishProperties.sizeWeight(50, 30, 4000, 1000)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "lionfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "lionfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(33, 5, 900, 150)) // I don't think I've measured this.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "needlefish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "needlefish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(100, 10, 2300, 300)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "stonefish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "stonefish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35, 5, 1500, 500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "parrotfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "parrotfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 10, 1600, 1400)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "seahorse"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "seahorse"))
                        .withSizeAndWeight(FishProperties.sizeWeight(15, 10, 20, 15)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "triggerfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "triggerfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldWarmOceanFish(U.holderItem("hybrid_aquatic", "flying_fish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "flying_fish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(19, 11, 570, 330)) 
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "anglerfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "anglerfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(10.5f, 7.5f, 585, 295))
                        .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "barreleye"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "barreleye"))
                        .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 99.25f, 42.5f))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "dragonfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "dragonfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(12, 3, 41.5f, 11.5f))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "coelacanth"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "coelacanth"))
                        .withSizeAndWeight(FishProperties.sizeWeight(175, 25, 59500, 8000))
                        .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "oarfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "oarfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(500, 300, 122727.25f, 61363.6f))
                        .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withWeather(WeatherRestriction.THUNDER));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "ratfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "ratfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "snailfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "snailfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(19.65f, 9.15f, 84, 76))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "john_dory"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "john_dory"))
                        .withSizeAndWeight(FishProperties.sizeWeight(50, 15, 3846.15f, 1153.85f))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "flashlight_fish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "flashlight_fish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(20, 10, 80, 40))
                        .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "squirrelfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "squirrelfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(21, 3, 300, 100)) // The weight is an estimate.
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "tuna"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "tuna"))
                        .withSizeAndWeight(FishProperties.sizeWeight(300, 100, 680250, 226750))
                        .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "mahi"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "mahi"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured this yet.
                        .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "mackerel"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "mackerel"))
                        .withSizeAndWeight(FishProperties.sizeWeight(48, 18, 1250, 750))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldColdOceanFish(U.holderItem("hybrid_aquatic", "herring"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "herring"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 663.4f, 436.6f))
                        .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "sea_bass"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "sea_bass"))
                        .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "sheepshead_wrasse"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "wrasse"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "pearlfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "pearlfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(14.45f, 2.15f, 2.675f, 1.435f))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldOceanFish(U.holderItem("hybrid_aquatic", "rockfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "rockfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "opah"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "opah"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldDeepOceanFish(U.holderItem("hybrid_aquatic", "ocean_sunfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "ocean_sunfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(230, 100, 1273500, 1026500))
                        .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                        .withRarity(FishProperties.Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR));

        register(
                overworldCherryGroveFish(U.holderItem("hybrid_aquatic", "goldfish"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "goldfish"))
                        .withSizeAndWeight(FishProperties.sizeWeight(20, 5, 200, 100))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldRiverFish(U.holderItem("hybrid_aquatic", "carp"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "carp"))
                        .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 8000, 4000))
                        .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "golden_dorado"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "golden_dorado"))
                        .withSizeAndWeight(FishProperties.sizeWeight(37, 20, 6500, 3500))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.RARE));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "cichlid"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "cichlid"))
                        .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 5290, 3710))
                        .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "tetra"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "tetra"))
                        .withSizeAndWeight(FishProperties.sizeWeight(4, 1, 0.37f, 0.12f))
                        .withDifficulty(FishProperties.Difficulty.HARD)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "tiger_barb"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "tiger_barb"))
                        .withSizeAndWeight(FishProperties.sizeWeight(10, 3, 1.9f, 0.5f))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "betta"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "betta"))
                        .withSizeAndWeight(FishProperties.sizeWeight(7, 1, 1.8f, 0.3f))
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.UNCOMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "danio"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "danio"))
                        .withSizeAndWeight(FishProperties.sizeWeight(4.4f, 0.6f, 0.7f, 0.2f))
                        .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "gourami"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "gourami"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "discus"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "discus"))
                        .withSizeAndWeight(FishProperties.sizeWeight(90, 10, 4300, 2500)) // Haven't measured yet.
                        .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                        .withRarity(FishProperties.Rarity.COMMON));

        register(
                overworldJungleFish(U.holderItem("hybrid_aquatic", "pleco"))
                        .withEntityToSpawn(U.holderEntity("hybrid_aquatic", "pleco"))
                        .withSizeAndWeight(FishProperties.sizeWeight(35.5f, 25.5f, 170.1f, 122.2f))
                        .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
                        .withRarity(FishProperties.Rarity.COMMON));
    }
}
