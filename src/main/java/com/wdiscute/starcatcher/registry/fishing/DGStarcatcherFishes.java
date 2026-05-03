package com.wdiscute.starcatcher.registry.fishing;

import com.wdiscute.starcatcher.io.Constellations;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.registry.fishing.compat.DGTrophies;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry.*;

public class DGStarcatcherFishes
{
    public static final List<FishProperties> STARCATCHER_FISHES = new ArrayList<>();
    public static void bootstrap()
    {
        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.OBIDONTIEE)
                .withStar(Constellations.UrsaMinor.EPSILON_UMI)
                .withSizeAndWeight(FishProperties.sizeWeight(17.7f, 5, 1200, 200)));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.MORGANITE)
                .withStar(Constellations.UrsaMinor.POLARIS)
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(120, 80, 7000, 1000))
                .withWeather(WeatherRestriction.RAIN)
                .withBaseChance(8)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.SILVERVEIL_PERCH)
                .withStar(Constellations.UrsaMinor.YILDUN)
                .withSeasons(SeasonRestriction.SPRING_WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(27.0f, 11, 500, 352))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.ELDERSCALE)
                .withStar(Constellations.UrsaMinor.ZETA_UMI)
                .withSizeAndWeight(FishProperties.sizeWeight(160.0f, 85, 2300, 652))
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withBaseChance(3));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.DRIFTFIN)
                .withStar(Constellations.UrsaMinor.KOCHAB)
                .withSizeAndWeight(FishProperties.sizeWeight(16.0f, 3, 167, 70))
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withWeather(WeatherRestriction.CLEAR));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.TWILIGHT_KOI)
                .withStar(Constellations.UrsaMinor.ETA_UMI)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 13, 3500, 731))
                .withBaseChance(25)
                .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                .withRarity(FishProperties.Rarity.EPIC)
                .withWeather(WeatherRestriction.RAIN)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.THUNDER_BASS)
                .withStar(Constellations.UrsaMinor.PHERKAD)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1200, 800))
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withRarity(FishProperties.Rarity.RARE)
                .withBaseChance(15)
                .withWeather(WeatherRestriction.THUNDER)
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherBucketAndEntity(overworldLakeFish(SCItems.LIGHTNING_BASS)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1300, 620))
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withBaseChance(15)
                .withRarity(FishProperties.Rarity.RARE)
                .withWeather(WeatherRestriction.THUNDER)
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING));

        register(overworldLakeFish(SCItems.BOOT).withBaseChance(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.TRASH)
                .withHasGuideEntry(false));


        //cold lake
        registerStarcatcherBucketAndEntity(overworldColdLakeFish(SCItems.FROSTJAW_TROUT)
                .withStar(Constellations.UrsaMajor.ALKAID)
                .withSizeAndWeight(FishProperties.sizeWeight(35, 8, 1600, 1200))
                .withDifficulty(FishProperties.Difficulty.FOUR_BIG_VANISHING)
        );

        registerStarcatcherBucketAndEntity(overworldColdLakeFish(SCItems.CRYSTALBACK_TROUT)
                .withStar(Constellations.UrsaMajor.MIZAR)
                .withSizeAndWeight(FishProperties.sizeWeight(35, 8, 1600, 1200))
                .withSeasons(SeasonRestriction.AUTUMN_WINTER)
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldColdLakeFish(SCItems.AURORA)
                .withStar(Constellations.UrsaMajor.ALIOTH)
                .withSizeAndWeight(FishProperties.sizeWeight(10, 8, 120, 30))
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withBaseChance(2)
                .withDifficulty(FishProperties.Difficulty.AURORA));

        registerStarcatcherBucketAndEntity(overworldColdLakeFish(SCItems.WINTERY_PIKE)
                .withStar(Constellations.UrsaMajor.MEGREZ)
                .withSeasons(SeasonRestriction.AROUND_WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING));


        //lake warm
        registerStarcatcherBucketAndEntity(overworldWarmLakeFish(SCItems.SANDTAIL)
                .withStar(Constellations.UrsaMajor.PHECDA)
                .withSizeAndWeight(FishProperties.sizeWeight(200, 100, 1600, 1200))
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT));

        registerStarcatcherBucketAndEntity(overworldWarmLakeFish(SCItems.MIRAGE_CARP)
                .withStar(Constellations.UrsaMajor.MERAK)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withWeather(WeatherRestriction.CLEAR)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldWarmLakeFish(SCItems.SCORCHFISH)
                .withStar(Constellations.UrsaMajor.DUBHE)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                .withSeasons(SeasonRestriction.NOT_WINTER)
                .withWeather(WeatherRestriction.CLEAR));

        registerStarcatcherBucketAndEntity(overworldWarmLakeFish(SCItems.CACTIFISH)
                .withStar(Constellations.Cassiopeia.CAPH)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 10000, 3000))
                .withSeasons(SeasonRestriction.SUMMER)
                .withDaytimeRestriction(DaytimeRestriction.DAY));

        registerStarcatcherBucketAndEntity(overworldWarmLakeFish(SCItems.AGAVE_BREAM)
                .withStar(Constellations.Cassiopeia.SHEDAR)
                .withSizeAndWeight(FishProperties.sizeWeight(36, 12, 2000, 1000))
                .withRarity(FishProperties.Rarity.RARE)
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withWeather(WeatherRestriction.CLEAR)
                .withDifficulty(FishProperties.Difficulty.HARD));


        //mountain
        registerStarcatcherBucketAndEntity(overworldMountainFish(SCItems.SUNNY_STURGEON)
                .withStar(Constellations.Cassiopeia.NAVI)
                .withSizeAndWeight(FishProperties.sizeWeight(400, 200, 100000, 50000))
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withRarity(FishProperties.Rarity.RARE)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withBaseChance(2));

        registerStarcatcherBucketAndEntity(overworldMountainFish(SCItems.PEAKDWELLER)
                .withStar(Constellations.Cassiopeia.RUCHBAH)
                .withSeasons(SeasonRestriction.AROUND_AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 10000, 5000))
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherBucketAndEntity(overworldMountainFish(SCItems.ROCKGILL)
                .withStar(Constellations.Cassiopeia.SEGIN)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 10000, 5000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldMountainFish(SCItems.SUN_SEEKING_CARP)
                .withStar(Constellations.TAURUS.ELNATH)
                .withSeasons(SeasonRestriction.AROUND_SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                .withRarity(FishProperties.Rarity.RARE)
                .withBaseChance(15)
                .withDaytimeRestriction(DaytimeRestriction.NOON));


        //swamp
        registerStarcatcherBucketAndEntity(overworldSwampFish(SCItems.SLUDGE_CATFISH)
                .withStar(Constellations.TAURUS.TAU_TARI)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 10000, 3000))
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldSwampFish(SCItems.LILY_SNAPPER)
                .withStar(Constellations.TAURUS.AIN)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldSwampFish(SCItems.SAGE_CATFISH)
                .withStar(Constellations.TAURUS.ALDEBARAN)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 50, 10000, 3000))
                .withSeasons(SeasonRestriction.AROUND_WINTER)
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withWeather(WeatherRestriction.CLEAR));

        register(overworldSwampFish(SCItems.MOSSY_BOOT).withBaseChance(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.TRASH)
                .withHasGuideEntry(false));


        //darkoak forest
        registerStarcatcherBucketAndEntity(overworldDarkForestFish(SCItems.PALE_PINFISH)
                .withStar(Constellations.TAURUS.ZETA_TAURI)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 150, 100))
                .withSeasons(SeasonRestriction.AROUND_WINTER)
                .withBaseChance(15)
                .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.TWO_AQUA_ONE_THIN));

        registerStarcatcherBucketAndEntity(overworldDarkForestFish(SCItems.PINFISH)
                .withStar(Constellations.TAURUS.GAMMA_TAURI)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 5, 150, 100))
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldDarkForestFish(SCItems.PALE_CARP)
                .withStar(Constellations.TAURUS.LAMBDA_TAURI)
                .withSeasons(SeasonRestriction.AROUND_WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                .withDaytimeRestriction(DaytimeRestriction.DAY));


        //cherry grove
        registerStarcatcherBucketAndEntity(overworldCherryGroveFish(SCItems.VESANI)
                .withStar(Constellations.TAURUS.OMICRON_TAURI)
                .withSeasons(SeasonRestriction.SPRING_WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(10, 3, 67, 0))
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDifficulty(FishProperties.Difficulty.NO_SWEET_SPOTS));

        registerStarcatcherBucketAndEntity(overworldCherryGroveFish(SCItems.BLOSSOMFISH)
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                .withWeather(WeatherRestriction.CLEAR));

        registerStarcatcherBucketAndEntity(overworldCherryGroveFish(SCItems.PETALDRIFT_CARP)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 6000, 4000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withWeather(WeatherRestriction.RAIN)
                .withBaseChance(8)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldCherryGroveFish(SCItems.PINK_KOI)
                .withSeasons(SeasonRestriction.SPRING_AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 3000, 2000))
                .withBaseChance(8)
                .withWeather(WeatherRestriction.RAIN));

        registerStarcatcherBucketAndEntity(overworldCherryGroveFish(SCItems.ROSE_SIAMESE_FISH)
                .withSeasons(SeasonRestriction.SPRING_AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1000, 500))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withBaseChance(8)
                .withWeather(WeatherRestriction.RAIN)
                .withRarity(FishProperties.Rarity.EPIC));


        //cold mountain
        registerStarcatcherBucketAndEntity(overworldColdMountainFish(SCItems.CRYSTALBACK_STURGEON)
                .withSizeAndWeight(FishProperties.sizeWeight(400, 200, 100000, 50000)));

        registerStarcatcherBucketAndEntity(overworldColdMountainFish(SCItems.ICETOOTH_STURGEON)
                .withSeasons(SeasonRestriction.AROUND_SPRING)
                .withSizeAndWeight(FishProperties.sizeWeight(400, 200, 100000, 50000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldColdMountainFish(SCItems.BOREAL)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 15, 1000, 200))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withBaseChance(3));

        registerStarcatcherBucketAndEntity(overworldColdMountainFish(SCItems.CRYSTALBACK_BOREAL)
                .withSeasons(SeasonRestriction.AROUND_WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 15, 6000, 2000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM));


        //river
        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.DOWNFALL_BREAM)
                .withSizeAndWeight(FishProperties.sizeWeight(36, 12, 2000, 1000))
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withBaseChance(18)
                .withWeather(WeatherRestriction.RAIN)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.DRIFTING_BREAM)
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(36, 12, 2000, 1000))
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.WILLOW_BREAM)
                .withSeasons(SeasonRestriction.SUMMER_AUTUMN)
                .withSizeAndWeight(FishProperties.sizeWeight(36, 12, 2000, 1000))
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                .withRarity(FishProperties.Rarity.EPIC));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.HOLLOWBELLY_DARTER)
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(6, 2, 7, 6))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.MISTBACK_CHUB)
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1400, 600)));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.BLUEGIGI)
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withSizeAndWeight(FishProperties.sizeWeight(20, 5, 400, 100)));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.SILVERFIN_PIKE)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldRiverFish(SCItems.CARPENJOE)
                .withSizeAndWeight(FishProperties.sizeWeight(178, 0, 72000, 0))
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                .withBaseChance(2)
                .withRarity(FishProperties.Rarity.EPIC));

        register(overworldRiverFish(SCItems.DRIED_SEAWEED)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.TRASH)
                .withBaseChance(1)
                .withHasGuideEntry(false));


        //cold river
        registerStarcatcherBucketAndEntity(overworldColdRiverFish(SCItems.FROSTGILL_CHUB)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 1400, 600))
                .withSeasons(SeasonRestriction.NOT_WINTER));

        registerStarcatcherBucketAndEntity(overworldColdRiverFish(SCItems.CRYSTALBACK_MINNOW)
                .withSizeAndWeight(FishProperties.sizeWeight(6, 4, 5, 3))
                .withSeasons(SeasonRestriction.WINTER)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT));

        registerStarcatcherBucketAndEntity(overworldColdRiverFish(SCItems.AZURE_CRYSTALBACK_MINNOW)
                .withSizeAndWeight(FishProperties.sizeWeight(6, 4, 5, 3))
                .withBaseChance(25)
                .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION_THREE_BIG));

        registerStarcatcherBucketAndEntity(overworldColdRiverFish(SCItems.BLUE_CRYSTAL_FIN)
                .withSeasons(SeasonRestriction.AROUND_SPRING)
                .withSizeAndWeight(FishProperties.sizeWeight(12, 4, 70, 30))
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON));


        //ocean
        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.SEA_BASS)
                .withSeasons(SeasonRestriction.NOT_SPRING)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                .withBaseChance(15)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withDaytimeRestriction(DaytimeRestriction.DAY));

        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.BLUE_HERRING)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 12, 1600, 1100))
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withBaseChance(3)
                .withRarity(FishProperties.Rarity.RARE)
                .withDaytimeRestriction(DaytimeRestriction.DAY));

        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.IRONJAW_HERRING)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 8, 300, 100))
                .withDifficulty(FishProperties.Difficulty.EIGHT_THIN_VANISHING)
                .withBaseChance(2)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.DEEPJAW_HERRING)
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 8, 300, 100))
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.DUSKTAIL_SNAPPER)
                .withSeasons(SeasonRestriction.SPRING_SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000)));

        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.JOEL)
                .withSeasons(SeasonRestriction.SUMMER)
                .withSizeAndWeight(FishProperties.sizeWeight(69, 0, 2000, 600))
                .withDifficulty(FishProperties.Difficulty.JOEL)
                .withBaseChance(1)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY));

        registerStarcatcherBucketAndEntity(overworldOceanFish(SCItems.REDSCALED_TUNA)
                .withSizeAndWeight(FishProperties.sizeWeight(150, 50, 120000, 60000))
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST));

        //deep ocean
        registerStarcatcherBucketAndEntity(overworldDeepOceanFish(SCItems.BIGEYE_TUNA)
                .withSeasons(SeasonRestriction.SPRING_WINTER)
                .withSizeAndWeight(FishProperties.sizeWeight(150, 50, 120000, 60000))
                .withBaseChance(8)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING));

        //beach
        register(overworldBeachFish(SCBlocks.CONCH.asItem().builtInRegistryHolder())
                .withSizeAndWeight(FishProperties.sizeWeight(5, 2, 100, 49))
                .withBaseChance(1)
                .withRarity(FishProperties.Rarity.TRASH)
                .withDifficulty(FishProperties.Difficulty.TRASH));

        register(overworldBeachFish(SCBlocks.CLAM.asItem().builtInRegistryHolder())
                .withSizeAndWeight(FishProperties.sizeWeight(20, 5, 1000, 400))
                .withBaseChance(1)
                .withRarity(FishProperties.Rarity.TRASH)
                .withDifficulty(FishProperties.Difficulty.TRASH));

        //mushroom islands
        registerStarcatcherBucketAndEntity(overworldMushroomFieldsFish(SCItems.SHROOMFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withDifficulty(FishProperties.Difficulty.EIGHT_THIN_MOVING_VANISHING));

        registerStarcatcherBucketAndEntity(overworldMushroomFieldsFish(SCItems.SPOREFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING));


        //underground
        registerStarcatcherBucketAndEntity(overworldUndergroundFish(SCItems.GOLD_FAN)
                .withSizeAndWeight(FishProperties.sizeWeight(70, 50, 4000, 2000)));

        registerStarcatcherOnlyEntity(overworldUndergroundFish(SCItems.GEODE_EEL)
                .withSizeAndWeight(FishProperties.sizeWeight(500, 150, 10000, 2000))
                .withRarity(FishProperties.Rarity.EPIC)
                .withBaseChance(1)
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING));

        //caves
        registerStarcatcherBucketAndEntity(overworldCavesFish(SCItems.WHITEVEIL)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 30, 3000, 2000))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING));

        registerStarcatcherOnlyEntity(overworldCavesFish(SCItems.BLACK_EEL)
                .withSizeAndWeight(FishProperties.sizeWeight(500, 150, 6000, 2000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldCavesFish(SCItems.STONEFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(300, 150, 26000, 7000))
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDifficulty(FishProperties.Difficulty.STONEFISH));

        registerStarcatcherBucketAndEntity(fish(SCItems.AMETHYSTBACK)
                .withSizeAndWeight(FishProperties.sizeWeight(300, 150, 16000, 7000))
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                .withRarity(FishProperties.Rarity.EPIC)
                .addRestrictions(DimensionRestriction.OVERWORLD,
                        new ElevationRestriction(-40, -20, "")));

        //dripstone caves
        registerStarcatcherBucketAndEntity(overworldDripstoneCavesFish(SCItems.FOSSILIZED_ANGELFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(700, 150, 36000, 7000))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.TWO_THIN_NO_DECAY));

        registerStarcatcherBucketAndEntity(overworldDripstoneCavesFish(SCItems.DRIPFIN)
                .withSizeAndWeight(FishProperties.sizeWeight(300, 150, 16000, 7000))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING));

        registerStarcatcherBucketAndEntity(overworldDripstoneCavesFish(SCItems.YELLOWSTONE_FISH)
                .withSizeAndWeight(FishProperties.sizeWeight(600, 150, 22000, 7000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON));


        //lush caves
        registerStarcatcherBucketAndEntity(overworldLushCavesFish(SCItems.LUSH_PIKE)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                .withDifficulty(FishProperties.Difficulty.HEAVY_EIGHT_AQUA_MOVING)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withBaseChance(2));

        registerStarcatcherBucketAndEntity(overworldLushCavesFish(SCItems.VIVID_MOSS)
                .withSizeAndWeight(FishProperties.sizeWeight(120, 70, 7000, 3000))
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withBaseChance(4));

        registerStarcatcherBucketAndEntity(fish(SCItems.THE_QUARRISH)
                .withSizeAndWeight(FishProperties.sizeWeight(620, 270, 700000, 300000))
                .withDifficulty(FishProperties.Difficulty.HEAVY_FIVE_NORMAL)
                .withRarity(FishProperties.Rarity.EPIC)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES_AND_JUNGLES));


        //deepslate
        registerStarcatcherBucketAndEntity(overworldDeepslateFish(SCItems.GHOSTLY_PIKE)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withBaseChance(2));

        registerStarcatcherBucketAndEntity(overworldDeepslateFish(SCItems.DEEPSLATEFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(420, 70, 70000, 20000))
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherBucketAndEntity(overworldDeepslateFish(SCItems.AQUAMARINE_PIKE)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(overworldDeepslateFish(SCItems.GARNET_MACKEREL)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 2000, 1500))
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldDeepslateFish(SCItems.BRIGHT_AMETHYST_SNAPPER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withRarity(FishProperties.Rarity.EPIC)
                .withBaseChance(2));

        registerStarcatcherBucketAndEntity(overworldDeepslateFish(SCItems.DARK_AMETHYST_SNAPPER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 20, 7000, 2000))
                .withDifficulty(FishProperties.Difficulty.EIGHT_THIN_MOVING)
                .withRarity(FishProperties.Rarity.EPIC)
                .withBaseChance(2));


        //deep dark
        registerStarcatcherBucketAndEntity(overworldDeepDarkFish(SCItems.SCULKFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(30, 10, 2000, 600))
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON));

        registerStarcatcherBucketAndEntity(overworldDeepDarkFish(SCItems.WARD)
                .withSizeAndWeight(FishProperties.sizeWeight(50, 10, 2600, 600))
                .withDifficulty(FishProperties.Difficulty.HEAVY_EIGHT_AQUA)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withBaseChance(2));

        registerStarcatcherBucketAndEntity(overworldDeepDarkFish(SCItems.GLOWING_DARK)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 10, 3000, 600))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_MOVING));


        //overworld surface lava
        registerStarcatcherBucketAndEntity(overworldSurfaceLava(SCItems.SUNEATER)
                .withSizeAndWeight(FishProperties.sizeWeight(100, 10, 3000, 600))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_MOVING));

        registerStarcatcherBucketAndEntity(overworldSurfaceLava(SCItems.PYROTROUT)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 1200, 700))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherOnlyEntity(overworldSurfaceLava(SCItems.OBSIDIAN_EEL)
                .withSizeAndWeight(FishProperties.sizeWeight(500, 150, 70000, 13000))
                .withWeather(WeatherRestriction.RAIN)
                .withBaseChance(8)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withRarity(FishProperties.Rarity.LEGENDARY));

        //overworld underground lava
        registerStarcatcherBucketAndEntity(overworldUndergroundLava(SCItems.MOLTEN_SHRIMP)
                .withSizeAndWeight(FishProperties.sizeWeight(10, 3, 20, 10))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherOnlyEntity(overworldUndergroundLava(SCItems.OBSIDIAN_CRAB)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 8, 700, 300))
                .withDifficulty(FishProperties.Difficulty.OBSIDIAN_CRAB)
                .withRarity(FishProperties.Rarity.EPIC));

        //overworld deepslate lava
        registerStarcatcherBucketAndEntity(overworldDeepslateLava(SCItems.SCORCHED_BLOODSUCKER)
                .withSizeAndWeight(FishProperties.sizeWeight(60, 30, 1700, 300))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherOnlyEntity(overworldDeepslateLava(SCItems.MOLTEN_DEEPSLATE_CRAB)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 8, 700, 300))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.DEEPSLATE_CRAB));


        //nether
        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.EMBERGILL)
                .withSizeAndWeight(FishProperties.sizeWeight(220, 70, 5700, 900))
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherOnlyEntity(netherLavaFish(SCItems.LAVA_CRAB)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 8, 700, 300))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.NETHER_CRAB));

        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.MAGMA_FISH)
                .withSizeAndWeight(FishProperties.sizeWeight(120, 40, 3700, 900))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.GLOWSTONE_SEEKER)
                .withSizeAndWeight(FishProperties.sizeWeight(120, 40, 3700, 900))
                .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION_THREE_BIG));

        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.CINDER_SQUID)
                .withSizeAndWeight(FishProperties.sizeWeight(40, 20, 1300, 700))
                .withDifficulty(FishProperties.Difficulty.FOUR_AQUA)
                .withRarity(FishProperties.Rarity.RARE)
                .withBaseChance(2));

        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.CERBERAY)
                .withSizeAndWeight(FishProperties.sizeWeight(200, 53, 4000, 300))
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT)
                .withDifficulty(FishProperties.Difficulty.CERBERAY));

        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.SCALDING_PIKE)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 20, 5000, 3000))
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING));

        registerStarcatcherBucketAndEntity(netherLavaFish(SCItems.GLOWSTONE_PUFFERFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(35, 25, 1000, 700))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING));

        registerStarcatcherBucketAndEntity(netherLavaBasaltDeltasFish(SCItems.WILLISH)
                .withSizeAndWeight(FishProperties.sizeWeight(75, 25, 4000, 700))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING));

        register(netherLavaFish(SCItems.LAVA_CRAB_CLAW).withBaseChance(1)
                .withRarity(FishProperties.Rarity.TRASH)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false));


        //the end
        registerStarcatcherBucketAndEntity(endFish(SCItems.CHARFISH)
                .withSizeAndWeight(FishProperties.sizeWeight(135, 25, 4000, 700))
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD));

        registerStarcatcherOnlyEntity(endFish(SCItems.CHORUS_CRAB)
                .withSizeAndWeight(FishProperties.sizeWeight(15, 8, 700, 300))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.END_CRAB));

        registerStarcatcherBucketAndEntity(endFish(SCItems.END_GLOW)
                .withSizeAndWeight(FishProperties.sizeWeight(235, 25, 7000, 700))
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM));

        registerStarcatcherBucketAndEntity(endOuterIslandsFish(SCItems.VOIDBITER)
                .withSizeAndWeight(FishProperties.sizeWeight(50, 15, 2000, 200))
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.VOIDBITER)
                .addRestrictions(BaitRestriction.LEGENDARY_BAIT));


    }
}
