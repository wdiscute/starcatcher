package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGAquacultureFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        //                                                   ,--.   ,--.                                 ,---.
        // ,--,--.  ,---.  ,--.,--.  ,--,--.  ,---. ,--.,--. |  | ,-'  '-. ,--.,--. ,--.--.  ,---.      '.-.  \
        //' ,-.  | | .-. | |  ||  | ' ,-.  | | .--' |  ||  | |  | '-.  .-' |  ||  | |  .--' | .-. :      .-' .'
        //\ '-'  | ' '-' | '  ''  ' \ '-'  | \ `--. '  ''  ' |  |   |  |   '  ''  ' |  |    \   --.     /   '-.
        // `--`--'  `-|  |  `----'   `--`--'  `---'  `----'  `--'   `--'    `----'  `--'     `----'     '-----'
        //            `--'

        //freshwater
        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("aquaculture", "smallmouth_bass")
                        .withBucketedFish("aquaculture", "smallmouth_bass_bucket")
                        .withEntityToSpawn("aquaculture", "smallmouth_bass")
                        .withSizeAndWeight(30, 10, 1500, 500)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                , "aquaculture"
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("aquaculture", "bluegill")
                        .withBucketedFish("aquaculture", "smallmouth_bass_bucket")
                        .withEntityToSpawn("aquaculture", "smallmouth_bass")
                        .withSizeAndWeight(15, 3, 300, 200)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
                , "aquaculture"
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("aquaculture", "brown_trout")
                        .withBucketedFish("aquaculture", "brown_trout_bucket")
                        .withEntityToSpawn("aquaculture", "brown_trout")
                        .withSizeAndWeight(45, 15, 3000, 2000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.CLEAR),
                "aquaculture"
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("aquaculture", "carp")
                        .withBucketedFish("aquaculture", "carp_bucket")
                        .withEntityToSpawn("aquaculture", "carp")
                        .withSizeAndWeight(60, 20, 10000, 4000)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD)
                        .withWeather(WeatherRestriction.RAIN),
                "aquaculture"
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("aquaculture", "catfish")
                        .withBucketedFish("aquaculture", "catfish_bucket")
                        .withEntityToSpawn("aquaculture", "catfish")
                        .withSizeAndWeight(150, 40, 100000, 25000)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withWeather(WeatherRestriction.RAIN),
                "aquaculture"
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("aquaculture", "gar")
                        .withBucketedFish("aquaculture", "gar_bucket")
                        .withEntityToSpawn("aquaculture", "gar")
                        .withSizeAndWeight(160, 30, 160000, 20000)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.lake(context)
                        .withFish("aquaculture", "minnow")
                        .withBucketedFish("aquaculture", "minnow_bucket")
                        .withEntityToSpawn("aquaculture", "minnow")
                        .withSizeAndWeight(6, 4, 10, 4)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.lake(context)
                        .withFish("aquaculture", "muskellunge")
                        .withBucketedFish("aquaculture", "muskellunge_bucket")
                        .withEntityToSpawn("aquaculture", "muskellunge")
                        .withSizeAndWeight(100, 10, 7000, 3000)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.lake(context)
                        .withFish("aquaculture", "perch")
                        .withBucketedFish("aquaculture", "perch_bucket")
                        .withEntityToSpawn("aquaculture", "perch")
                        .withSizeAndWeight(20, 5, 500, 200)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
                , "aquaculture"
        );

        //warm lakes
        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("aquaculture", "bayad")
                        .withBucketedFish("aquaculture", "bayad_bucket")
                        .withEntityToSpawn("aquaculture", "bayad")
                        .withSizeAndWeight(170, 30, 150000, 20000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("aquaculture", "boulti")
                        .withBucketedFish("aquaculture", "boulti_bucket")
                        .withEntityToSpawn("aquaculture", "boulti")
                        .withSizeAndWeight(40, 10, 4000, 300)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(Difficulty.HARD_MIRAGE)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("aquaculture", "capitaine")
                        .withBucketedFish("aquaculture", "capitaine_bucket")
                        .withEntityToSpawn("aquaculture", "capitaine")
                        .withSizeAndWeight(130, 50, 12000, 3000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish("aquaculture", "synodontis")
                        .withBucketedFish("aquaculture", "synodontis_bucket")
                        .withEntityToSpawn("aquaculture", "synodontis")
                        .withSizeAndWeight(35, 15, 1000, 300)
                        .withDifficulty(Difficulty.HARD_MIRAGE_MOVING)
                        .withRarity(Rarity.EPIC)
                , "aquaculture"
        );

        //cold ocean
        FishRegistration.register(
                context,
                PresetRestrictions.coldOcean(context)
                        .withFish("aquaculture", "atlantic_cod")
                        .withBucketedFish("aquaculture", "atlantic_cod_bucket")
                        .withEntityToSpawn("aquaculture", "atlantic_cod")
                        .withSizeAndWeight(100, 50, 15000, 10000)
                        .withDifficulty(Difficulty.EASY_FROZEN)
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldOcean(context)
                        .withFish("aquaculture", "blackfish")
                        .withBucketedFish("aquaculture", "blackfish_bucket")
                        .withEntityToSpawn("aquaculture", "blackfish")
                        .withSizeAndWeight(50, 20, 5000, 3000)
                        .withDifficulty(Difficulty.EASY_FROZEN)
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.UNCOMMON),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldOcean(context)
                        .withFish("aquaculture", "pacific_halibut")
                        .withBucketedFish("aquaculture", "pacific_halibut_bucket")
                        .withEntityToSpawn("aquaculture", "pacific_halibut")
                        .withSizeAndWeight(150, 50, 80000, 5000)
                        .withDifficulty(Difficulty.MEDIUM_FROZEN)
                        .withRarity(Rarity.UNCOMMON)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.coldOcean(context)
                        .withFish("aquaculture", "atlantic_halibut")
                        .withBucketedFish("aquaculture", "atlantic_halibut_bucket")
                        .withEntityToSpawn("aquaculture", "atlantic_halibut")
                        .withSizeAndWeight(200, 80, 150000, 10000)
                        .withDifficulty(Difficulty.MEDIUM_FROZEN)
                        .withRarity(Rarity.UNCOMMON)
                        .withWeather(WeatherRestriction.RAIN),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "atlantic_herring")
                        .withBucketedFish("aquaculture", "atlantic_herring_bucket")
                        .withEntityToSpawn("aquaculture", "atlantic_herring")
                        .withSizeAndWeight(25, 5, 200, 100)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.DAY),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "pink_salmon")
                        .withBucketedFish("aquaculture", "pink_salmon_bucket")
                        .withEntityToSpawn("aquaculture", "pink_salmon")
                        .withSizeAndWeight(50, 10, 2000, 1000)
                        .withRarity(Rarity.EPIC)
                        .withWeather(WeatherRestriction.THUNDER)
                        .withDifficulty(Difficulty.HARD)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "pollock")
                        .withBucketedFish("aquaculture", "pollock_bucket")
                        .withEntityToSpawn("aquaculture", "pollock")
                        .withSizeAndWeight(70, 30, 5000, 4000)
                        .withDifficulty(Difficulty.EASY)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "rainbow_trout")
                        .withBucketedFish("aquaculture", "rainbow_trout_bucket")
                        .withEntityToSpawn("aquaculture", "rainbow_trout")
                        .withSizeAndWeight(60, 20, 2000, 1500)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY),
                "aquaculture"
        );

        //ocean
        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "jellyfish")
                        .withBucketedFish("aquaculture", "jellyfish_bucket")
                        .withEntityToSpawn("aquaculture", "jellyfish")
                        .withSizeAndWeight(100, 70, 50, 10)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.HARD)
                        .withBaseChance(3),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "red_grouper")
                        .withBucketedFish("aquaculture", "red_grouper_bucket")
                        .withEntityToSpawn("aquaculture", "red_grouper")
                        .withSizeAndWeight(100, 50, 15000, 10000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "tuna")
                        .withBucketedFish("aquaculture", "tuna_bucket")
                        .withEntityToSpawn("aquaculture", "tuna")
                        .withSizeAndWeight(200, 100, 200000, 150000),
                "aquaculture"
        );

        //jungle
        FishRegistration.register(
                context,
                PresetRestrictions.jungle(context)
                        .withFish("aquaculture", "arapaima")
                        .withBucketedFish("aquaculture", "arapaima_bucket")
                        .withEntityToSpawn("aquaculture", "arapaima")
                        .withSizeAndWeight(250, 50, 50000, 150000)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD)
                        .withWeather(WeatherRestriction.RAIN),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.jungle(context)
                        .withFish("aquaculture", "arrau_turtle")
                        .withEntityToSpawn("aquaculture", "arrau_turtle")
                        .withSizeAndWeight(100, 30, 80000, 150000)
                        .withDifficulty(Difficulty.MEDIUM),
                "aquaculture"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.jungle(context)
                        .withFish("aquaculture", "piranha")
                        .withBucketedFish("aquaculture", "piranha_bucket")
                        .withEntityToSpawn("aquaculture", "piranha")
                        .withSizeAndWeight(30, 10, 500, 300)
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withDaytimeRestriction(DaytimeRestriction.NOON),
                "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.jungle(context)
                        .withFish("aquaculture", "tambaqui")
                        .withBucketedFish("aquaculture", "tambaqui_bucket")
                        .withEntityToSpawn("aquaculture", "tambaqui")
                        .withSizeAndWeight(100, 30, 150000, 10000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM),
                "aquaculture"
        );

        //mushroom island
        FishRegistration.register(
                context,
                PresetRestrictions.mushroomFields(context)
                        .withFish("aquaculture", "brown_shrooma")
                        .withBucketedFish("aquaculture", "brown_shrooma_bucket")
                        .withEntityToSpawn("aquaculture", "brown_shrooma")
                        .withSizeAndWeight(100, 20, 3000, 500)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving())
                , "aquaculture"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.mushroomFields(context)
                        .withFish("aquaculture", "red_shrooma")
                        .withBucketedFish("aquaculture", "brown_shrooma_bucket")
                        .withEntityToSpawn("aquaculture", "brown_shrooma")
                        .withSizeAndWeight(100, 20, 3000, 500)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving())
                , "aquaculture"
        );

        //anywhere
        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "goldfish")
                        .withSizeAndWeight(15, 5, 100, 5)
                        .withBaseChance(1)
                        //0.5% chance
                        .withPercentageChance(0.005f),
                "aquaculture"
        );


        //extras
        //neptunium ingot
        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish("aquaculture", "neptunium_ingot")
                        .withMaxLimit(5)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(new RarityCountRestriction.RarityCount(
                                Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                        .withPercentageChance(0.05f)
                        .extra(),
                "aquaculture"
        );

    }
}
