package com.wdiscute.starcatcher.datagen.fish;

import com.wdiscute.starcatcher.fish.*;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class DGStarcatcherFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {
        //
        //,--.          ,--.
        //|  |  ,--,--. |  |,-.   ,---.   ,---.
        //|  | ' ,-.  | |     /  | .-. : (  .-'
        //|  | \ '-'  | |  \  \  \   --. .-'  `)
        //`--'  `--`--' `--'`--'  `----' `----'
        //

        //lakes - trash
        FishRegistration.register(context,
                PresetRestrictions.lake(context).withFish(SCItems.BOOT)
                        .withBaseChance(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.TRASH)
                        .withHasGuideEntry(false)
        );


        //lakes - common
        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.OBIDONTIEE)
                        .withSizeAndWeight(new SizeAndWeight(17.7f, 5, 1200, 200))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.DRIFTFIN)
                        .withSizeAndWeight(new SizeAndWeight(16.0f, 3, 167, 70))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withRarity(Rarity.COMMON)
                        .withFish(SCItems.RAINFIN)
                        .withSizeAndWeight(new SizeAndWeight(32.0f, 9, 9500, 200))
                        .withDifficulty(Difficulty.EASY.withHP(125))
                        .withWeather(WeatherRestriction.RAIN)
        );

        FishRegistration.register(context,
                PresetRestrictions.lakeMountain(context)
                        .withRarity(Rarity.COMMON)
                        .withFish(SCItems.PEAKDWELLER)
                        .withSizeAndWeight(new SizeAndWeight(100, 50, 10000, 5000))
                        .withDifficulty(Difficulty.EASY.withHP(150))
        );

        FishRegistration.register(context,
                PresetRestrictions.lakeMountain(context)
                        .withRarity(Rarity.COMMON)
                        .withFish(SCItems.ROCKGILL)
                        .withSizeAndWeight(new SizeAndWeight(100, 50, 10000, 5000))
                        .withDifficulty(Difficulty.EASY.moving()));


        //lakes - uncommon
        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.SILVERVEIL_PERCH)
                        .withSizeAndWeight(new SizeAndWeight(27.0f, 11, 500, 352))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY.withHP(100).moving())
        );

        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.ELDERSCALE)
                        .withSizeAndWeight(new SizeAndWeight(160.0f, 85, 2300, 652))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY.withHP(100).vanishing())
                        .withWeather(WeatherRestriction.CLEAR)
        );

        FishRegistration.register(context,
                PresetRestrictions.lakeMountain(context)
                        .withFish(SCItems.SUNNY_STURGEON)
                        .withSizeAndWeight(new SizeAndWeight(400, 200, 100000, 50000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
        );


        //lakes - rare
        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.TWILIGHT_KOI)
                        .withSizeAndWeight(new SizeAndWeight(60, 13, 3500, 731))
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
        );

        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.RIPPLE_CATFISH)
                        .withSizeAndWeight(new SizeAndWeight(60, 13, 3500, 731))
                        .withRarity(Rarity.RARE)
                        .withWeather(WeatherRestriction.RAIN)
                        .withDifficulty(Difficulty.MEDIUM.moving())
        );

        FishRegistration.register(context,
                PresetRestrictions.lakeMountain(context)
                        .withFish(SCItems.SUN_SEEKING_CARP)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
        );


        //lakes - epic
        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.THUNDER_BASS)
                        .withSizeAndWeight(new SizeAndWeight(40, 12, 1200, 800))
                        .withRarity(Rarity.EPIC)
                        .withWeather(WeatherRestriction.THUNDER)
                        .withDifficulty(Difficulty.HARD.moving())
        );

        FishRegistration.register(context,
                PresetRestrictions.lake(context)
                        .withFish(SCItems.LIGHTNING_BASS)
                        .withSizeAndWeight(new SizeAndWeight(40, 12, 1300, 620))
                        .withRarity(Rarity.EPIC)
                        .withWeather(WeatherRestriction.THUNDER)
                        .withDifficulty(Difficulty.HARD.vanishing())
        );


        //
        //               ,--.    ,--.     ,--.          ,--.
        // ,---.  ,---.  |  |  ,-|  |     |  |  ,--,--. |  |,-.   ,---.   ,---.
        //| .--' | .-. | |  | ' .-. |     |  | ' ,-.  | |     /  | .-. : (  .-'
        //\ `--. ' '-' ' |  | \ `-' |     |  | \ '-'  | |  \  \  \   --. .-'  `)
        // `---'  `---'  `--'  `---'      `--'  `--`--' `--'`--'  `----' `----'
        //

        //cold lakes - common
        FishRegistration.register(context,
                PresetRestrictions.coldLake(context)
                        .withFish(SCItems.FROSTJAW_TROUT)
                        .withSizeAndWeight(new SizeAndWeight(35, 8, 1600, 1200))
                        .withDifficulty(Difficulty.EASY_FROZEN.withHP(150))
        );

        FishRegistration.register(context,
                PresetRestrictions.coldLakeMountain(context)
                        .withFish(SCItems.CRYSTALBACK_STURGEON)
                        .withSizeAndWeight(new SizeAndWeight(400, 200, 100000, 50000))
                        .withDifficulty(Difficulty.EASY_FROZEN.withHP(150))
        );

        //cold lakes - uncommon
        FishRegistration.register(context,
                PresetRestrictions.coldLakeMountain(context)
                        .withFish(SCItems.ICETOOTH_STURGEON)
                        .withSizeAndWeight(new SizeAndWeight(400, 200, 100000, 50000))
                        .withDifficulty(Difficulty.MEDIUM)
                        .withDifficulty(Difficulty.EASY_FROZEN.vanishing().withHP(150))
                        .withRarity(Rarity.UNCOMMON)
        );

        FishRegistration.register(context,
                PresetRestrictions.coldLake(context)
                        .withFish(SCItems.CRYSTALBACK_TROUT)
                        .withSizeAndWeight(new SizeAndWeight(35, 8, 1600, 1200))
                        .withDifficulty(Difficulty.MEDIUM.withHP(150))
                        .withDifficulty(Difficulty.EASY_FROZEN.moving().withHP(150))
                        .withRarity(Rarity.UNCOMMON)
        );

        //cold lakes - rare
        FishRegistration.register(context,
                PresetRestrictions.coldLake(context)
                        .withFish(SCItems.WINTERY_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withDifficulty(Difficulty.MEDIUM_FROZEN.moving().withHP(200))
                        .withRarity(Rarity.RARE)
        );

        FishRegistration.register(context,
                PresetRestrictions.coldLake(context)
                        .withFish(SCItems.CRYSTALBACK_BOREAL)
                        .withSizeAndWeight(new SizeAndWeight(30, 15, 6000, 2000))
                        .withDifficulty(Difficulty.MEDIUM_FROZEN.vanishing().withHP(200))
                        .withRarity(Rarity.RARE)
        );

        //cold lakes - epic
        FishRegistration.register(context,
                PresetRestrictions.coldLake(context)
                        .withFish(SCItems.BLUE_ICE_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(35, 8, 1600, 1200))
                        .withDifficulty(Difficulty.HARD_FROZEN.vanishing().withHP(250))
                        .withRarity(Rarity.EPIC)
        );

        //cold lakes - legendary
        FishRegistration.register(context,
                PresetRestrictions.coldLakeMountain(context)
                        .withFish(SCItems.BOREAL)
                        .withSizeAndWeight(new SizeAndWeight(30, 15, 1000, 200))
                        .withRarity(Rarity.LEGENDARY)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.BOREAL)
                        
        );

        //ice spices
        FishRegistration.register(context,
                PresetRestrictions.iceSpikes(context)
                        .withFish(SCItems.AURORA)
                        .withSizeAndWeight(new SizeAndWeight(10, 8, 120, 30))
                        .withRarity(Rarity.LEGENDARY)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.AURORA)
        );

        //
        //                                            ,--.          ,--.
        //,--.   ,--.  ,--,--. ,--.--. ,--,--,--.     |  |  ,--,--. |  |,-.   ,---.   ,---.
        //|  |.'.|  | ' ,-.  | |  .--' |        |     |  | ' ,-.  | |     /  | .-. : (  .-'
        //|   .'.   | \ '-'  | |  |    |  |  |  |     |  | \ '-'  | |  \  \  \   --. .-'  `)
        //'--'   '--'  `--`--' `--'    `--`--`--'     `--'  `--`--' `--'`--'  `----' `----'
        //

        //lake warm - common
        FishRegistration.register(context,
                PresetRestrictions.warmLake(context)
                        .withFish(SCItems.SANDTAIL)
                        .withSizeAndWeight(new SizeAndWeight(200, 100, 1600, 1200))
                        .withDifficulty(Difficulty.EASY_MIRAGE)
        );

        FishRegistration.register(context,
                PresetRestrictions.warmLake(context)
                        .withFish(SCItems.SCORCHFISH)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withDifficulty(Difficulty.EASY_MIRAGE)
        );

        //lake warm - uncommon
        FishRegistration.register(context,
                PresetRestrictions.warmLake(context)
                        .withFish(SCItems.MIRAGE_CARP)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
        );

        //lake warm - rare
        FishRegistration.register(context,
                PresetRestrictions.warmLake(context)
                        .withFish(SCItems.AGAVE_BREAM)
                        .withSizeAndWeight(new SizeAndWeight(36, 12, 2000, 1000))
                        .withDifficulty(Difficulty.MEDIUM_MIRAGE_MOVING)
                        .withRarity(Rarity.RARE)
        );

        //lake warm - epic
        FishRegistration.register(context,
                PresetRestrictions.warmLake(context)
                        .withFish(SCItems.CACTIFISH)
                        .withSizeAndWeight(new SizeAndWeight(100, 50, 10000, 3000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD_MIRAGE)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
        );

        //lake warm - legendary
        FishRegistration.register(context,
                PresetRestrictions.warmLake(context)
                        .withFish(SCItems.OASIS_STURGEON)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withDifficulty(Difficulty.OASIS_SURGEON)
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withBaseChance(2)
                        .withRarity(Rarity.LEGENDARY)
        );


        //
        //
        // ,---.  ,--.   ,--.  ,--,--. ,--,--,--.  ,---.
        //(  .-'  |  |.'.|  | ' ,-.  | |        | | .-. |
        //.-'  `) |   .'.   | \ '-'  | |  |  |  | | '-' '
        //`----'  '--'   '--'  `--`--' `--`--`--' |  |-'
        //                                        `--'

        //swamp - trash
        FishRegistration.register(context,
                PresetRestrictions.swamp(context)
                        .withFish(SCItems.MOSSY_BOOT)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.TRASH)
                        .withHasGuideEntry(false)
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish(Items.LILY_PAD)
                        .withSizeAndWeight(new SizeAndWeight(620, 270, 5300, 700))
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.TRASH)
                        .withHasGuideEntry(false)
        );

        //swamp - uncommon
        FishRegistration.register(context,
                PresetRestrictions.swamp(context)
                        .withFish(SCItems.SLUDGE_CATFISH)
                        .withSizeAndWeight(new SizeAndWeight(100, 50, 10000, 3000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
        );

        //swamp - rare
        FishRegistration.register(context,
                PresetRestrictions.swamp(context)
                        .withFish(SCItems.LILY_SNAPPER)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 7000, 2000))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.withHP(200))
        );

        //swamp - epic
        FishRegistration.register(context,
                PresetRestrictions.swamp(context)
                        .withFish(SCItems.SAGE_CATFISH)
                        .withSizeAndWeight(new SizeAndWeight(100, 50, 10000, 3000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.vanishing().moving().withHP(200))
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.CLEAR)
        );


        //
        //    ,--.                  ,--.                          ,--.
        //  ,-|  |  ,--,--. ,--.--. |  |,-.       ,---.   ,--,--. |  |,-.
        // ' .-. | ' ,-.  | |  .--' |     /      | .-. | ' ,-.  | |     /
        // \ `-' | \ '-'  | |  |    |  \  \      ' '-' ' \ '-'  | |  \  \
        //  `---'   `--`--' `--'    `--'`--'      `---'   `--`--' `--'`--'
        //  ,---.                                   ,--.
        // /  .-'  ,---.  ,--.--.  ,---.   ,---.  ,-'  '-.
        // |  `-, | .-. | |  .--' | .-. : (  .-'  '-.  .-'
        // |  .-' ' '-' ' |  |    \   --. .-'  `)   |  |
        // `--'    `---'  `--'     `----' `----'    `--'
        //

        //common
        FishRegistration.register(context,
                PresetRestrictions.darkOakForest(context)
                        .withFish(SCItems.PALE_CARP)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(Difficulty.EASY)
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.darkOakForest(context)
                        .withFish(SCItems.PINFISH)
                        .withSizeAndWeight(new SizeAndWeight(15, 5, 150, 100))
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.darkOakForest(context)
                        .withFish(SCItems.PALE_PINFISH)
                        .withSizeAndWeight(new SizeAndWeight(15, 5, 150, 100))
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.moving())
        );


        //
        //        ,--.
        //  ,---. |  ,---.   ,---.  ,--.--. ,--.--. ,--. ,--.      ,---.  ,--.--.  ,---.  ,--.  ,--.  ,---.
        // | .--' |  .-.  | | .-. : |  .--' |  .--'  \  '  /      | .-. | |  .--' | .-. |  \  `'  /  | .-. :
        // \ `--. |  | |  | \   --. |  |    |  |      \   '       ' '-' ' |  |    ' '-' '   \    /   \   --.
        //  `---' `--' `--'  `----' `--'    `--'    .-'  /        .`-  /  `--'     `---'     `--'     `----'
        //                                          `---'         `---'

        //common
        FishRegistration.register(context,
                PresetRestrictions.cherryGrove(context)
                        .withFish(SCItems.BLOSSOMFISH)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withWeather(WeatherRestriction.CLEAR)
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(context,
                PresetRestrictions.cherryGrove(context)
                        .withFish(SCItems.PINK_KOI)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 3000, 2000))
                        .withDifficulty(Difficulty.EASY)
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.cherryGrove(context)
                        .withFish(SCItems.PETALDRIFT_CARP)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 6000, 4000))
                        .withDifficulty(Difficulty.MEDIUM)
                        .withWeather(WeatherRestriction.RAIN)
                        .withRarity(Rarity.UNCOMMON)
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.cherryGrove(context)
                        .withFish(SCItems.ROSE_SIAMESE_FISH)
                        .withSizeAndWeight(new SizeAndWeight(30, 10, 1000, 500))
                        .withDifficulty(Difficulty.MEDIUM.vanishing().withHP(300))
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.RAIN)
                        .withRarity(Rarity.EPIC)
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.cherryGrove(context)
                        .withFish(SCItems.VESANI)
                        .withSizeAndWeight(new SizeAndWeight(10, 3, 67, 0))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.VESANI.withHP(300))
        );


        //
        // ,--.                        ,--.
        // |  |-.   ,--,--. ,--,--,--. |  |-.   ,---.   ,---.
        // | .-. ' ' ,-.  | |        | | .-. ' | .-. | | .-. |
        // | `-' | \ '-'  | |  |  |  | | `-' | ' '-' ' ' '-' '
        //  `---'   `--`--' `--`--`--'  `---'   `---'   `---'
        //

        //trash
        FishRegistration.register(context,
                PresetRestrictions.bambooJungle(context)
                        .withFish(Items.BAMBOO)
                        .withSizeAndWeight(new SizeAndWeight(620, 270, 5300, 700))
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.TRASH)
                        .withHasGuideEntry(false)
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.bambooJungle(context)
                        .withFish(SCItems.LIVID_BAMBOO)
                        .withSizeAndWeight(new SizeAndWeight(620, 270, 5300, 700))
                        .withDifficulty(Difficulty.HARD.withHP(200))
                        .withRarity(Rarity.EPIC)
        );

        //
        //  ,---. ,--.                                          ,---.                                   ,--.
        // /  .-' |  |  ,---.  ,--.   ,--.  ,---.  ,--.--.     /  .-'  ,---.  ,--.--.  ,---.   ,---.  ,-'  '-.
        // |  `-, |  | | .-. | |  |.'.|  | | .-. : |  .--'     |  `-, | .-. | |  .--' | .-. : (  .-'  '-.  .-'
        // |  .-' |  | ' '-' ' |   .'.   | \   --. |  |        |  .-' ' '-' ' |  |    \   --. .-'  `)   |  |
        // `--'   `--'  `---'  '--'   '--'  `----' `--'        `--'    `---'  `--'     `----' `----'    `--'
        //

        //rare
        FishRegistration.register(context,
                PresetRestrictions.flowerForest(context)
                        .withFish(SCItems.PETAL_BASS)
                        .withSizeAndWeight(new SizeAndWeight(420, 20, 2000, 100))
                        .withDifficulty(Difficulty.MEDIUM.vanishing().moving().withHP(200))
                        .withRarity(Rarity.RARE)
        );


        //
        //                           ,---. ,--.
        // ,---.  ,--.,--. ,--,--,  /  .-' |  |  ,---.  ,--.   ,--.  ,---.  ,--.--.
        //(  .-'  |  ||  | |      \ |  `-, |  | | .-. | |  |.'.|  | | .-. : |  .--'
        //.-'  `) '  ''  ' |  ||  | |  .-' |  | ' '-' ' |   .'.   | \   --. |  |
        //`----'   `----'  `--''--' `--'   `--'  `---'  '--'   '--'  `----' `--'
        //

        //rare
        FishRegistration.register(context,
                PresetRestrictions.sunflowerPlains(context)
                        .withFish(SCItems.SUNFLOWER_CARP)
                        .withSizeAndWeight(new SizeAndWeight(920, 120, 6000, 400))
                        .withDifficulty(Difficulty.MEDIUM.vanishing().moving().withHP(200))
                        .withRarity(Rarity.RARE)
        );


        //
        //       ,--.                     ,--.
        // ,---. |  |  ,---.  ,--.,--.  ,-|  |  ,---.
        //| .--' |  | | .-. | |  ||  | ' .-. | (  .-'
        //\ `--. |  | ' '-' ' '  ''  ' \ `-' | .-'  `)
        // `---' `--'  `---'   `----'   `---'  `----'
        //

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish(SCItems.CLOUDFIN)
                        .withTextures(FishProperties.SKY)
                        .withSizeAndWeight(new SizeAndWeight(920, 120, 0, 0))
                        .addRestriction(DimensionRestriction.OVERWORLD)
                        .addRestriction(ElevationRestriction.ABOVE_TWO_HUNDRED)
                        .addRestriction(FluidRestriction.AIR)
                        .withDifficulty(Difficulty.CLOUDFIN)
                        .withRarity(Rarity.LEGENDARY)
        );

        //
        //        ,--.
        //,--.--. `--' ,--.  ,--.  ,---.  ,--.--.
        //|  .--' ,--.  \  `'  /  | .-. : |  .--'
        //|  |    |  |   \    /   \   --. |  |
        //`--'    `--'    `--'     `----' `--'
        //

        //trash
        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.DRIED_SEAWEED)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.TRASH)
                        .withHasGuideEntry(false)
        );

        //common
        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.DRIFTING_BREAM)
                        .withSizeAndWeight(new SizeAndWeight(36, 12, 2000, 1000))
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.HOLLOWBELLY_DARTER)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.CLEAR)
                        .withSizeAndWeight(new SizeAndWeight(6, 2, 7, 6))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY.moving())
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.MISTBACK_CHUB)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withSizeAndWeight(new SizeAndWeight(30, 10, 1400, 600))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY)
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.SILVERFIN_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY.moving().withHP(100))
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.DOWNFALL_BREAM)
                        .withRarity(Rarity.UNCOMMON)
                        .withSizeAndWeight(new SizeAndWeight(36, 12, 2000, 1000))
                        .withWeather(WeatherRestriction.RAIN)
                        .withDifficulty(Difficulty.EASY.vanishing().withHP(100))
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.BLUEGIGI)
                        .withSizeAndWeight(new SizeAndWeight(20, 5, 400, 100))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(150))
        );

        //epic
        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.WILLOW_BREAM)
                        .withSizeAndWeight(new SizeAndWeight(36, 12, 2000, 1000))
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(200))
                        .withRarity(Rarity.EPIC)
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.CARPENJOE)
                        .withSizeAndWeight(new SizeAndWeight(178, 0, 72000, 0))
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(200))
                        .withRarity(Rarity.EPIC)
        );


        //
        //               ,--.    ,--.             ,--.
        // ,---.  ,---.  |  |  ,-|  |     ,--.--. `--' ,--.  ,--.  ,---.  ,--.--.
        //| .--' | .-. | |  | ' .-. |     |  .--' ,--.  \  `'  /  | .-. : |  .--'
        //\ `--. ' '-' ' |  | \ `-' |     |  |    |  |   \    /   \   --. |  |
        // `---'  `---'  `--'  `---'      `--'    `--'    `--'     `----' `--'
        //

        //common
        FishRegistration.register(
                context,
                PresetRestrictions.coldRiver(context)
                        .withFish(SCItems.FROSTGILL_CHUB)
                        .withSizeAndWeight(new SizeAndWeight(30, 10, 1400, 600))
                        .withDifficulty(Difficulty.EASY_FROZEN)
        );

        //uncommon
        FishRegistration.register(
                context,
                PresetRestrictions.coldRiver(context)
                        .withFish(SCItems.CRYSTALBACK_MINNOW)
                        .withSizeAndWeight(new SizeAndWeight(6, 4, 5, 3))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY_FROZEN.moving())
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
        );

        //rare
        FishRegistration.register(
                context,
                PresetRestrictions.coldRiver(context)
                        .withFish(SCItems.BLUE_CRYSTAL_FIN)
                        .withSizeAndWeight(new SizeAndWeight(12, 4, 70, 30))
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(Difficulty.MEDIUM_FROZEN.moving())
                        .withRarity(Rarity.RARE)
        );

        //legendary
        FishRegistration.register(
                context,
                PresetRestrictions.coldRiver(context)
                        .withFish(SCItems.AZURE_CRYSTALBACK_MINNOW)
                        .withSizeAndWeight(new SizeAndWeight(6, 4, 5, 3))
                        .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.AZURE_CRYSTALBACK_MINNOW)
        );


        //
        //
        //  ,---.   ,---.  ,---.   ,--,--. ,--,--,
        // | .-. | | .--' | .-. : ' ,-.  | |      \
        // ' '-' ' \ `--. \   --. \ '-'  | |  ||  |
        //  `---'   `---'  `----'  `--`--' `--''--'
        //

        //common
        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.SEA_BASS)
                        .withSizeAndWeight(new SizeAndWeight(40, 12, 1600, 1100))
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.DEEPJAW_HERRING)
                        .withSizeAndWeight(new SizeAndWeight(30, 8, 300, 100))
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.DUSKTAIL_SNAPPER)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 7000, 2000))
                        .withDifficulty(Difficulty.EASY.moving())
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.IRONJAW_HERRING)
                        .withSizeAndWeight(new SizeAndWeight(30, 8, 300, 100))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY.vanishing())
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.REDSCALED_TUNA)
                        .withSizeAndWeight(new SizeAndWeight(150, 50, 120000, 60000))
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.moving())
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.BLUE_HERRING)
                        .withSizeAndWeight(new SizeAndWeight(40, 12, 1600, 1100))
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withDifficulty(Difficulty.HARD)
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.BIGEYE_TUNA)
                        .withSizeAndWeight(new SizeAndWeight(150, 50, 120000, 60000))
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving())
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(SCItems.JOEL)
                        .withBaseChance(1)
                        .withSizeAndWeight(new SizeAndWeight(69, 0, 2000, 600))
                        .withDifficulty(Difficulty.JOEL)
                        .withRarity(Rarity.LEGENDARY)
        );

        //
        // ,--.                            ,--.
        // |  |-.   ,---.   ,--,--.  ,---. |  ,---.
        // | .-. ' | .-. : ' ,-.  | | .--' |  .-.  |
        // | `-' | \   --. \ '-'  | \ `--. |  | |  |
        //  `---'   `----'  `--`--'  `---' `--' `--'
        //

        FishRegistration.register(context,
                PresetRestrictions.beach(context)
                        .withFish(SCBlocks.CONCH.asItem())
                        .withSizeAndWeight(new SizeAndWeight(5, 2, 100, 49))
                        .withRarity(Rarity.TRASH)
                        .withDifficulty(Difficulty.TRASH)
        );

        FishRegistration.register(context,
                PresetRestrictions.beach(context)
                        .withFish(SCBlocks.CLAM.asItem())
                        .withSizeAndWeight(new SizeAndWeight(20, 5, 1000, 400))
                        .withRarity(Rarity.TRASH)
                        .withDifficulty(Difficulty.TRASH)
        );

        //
        //                             ,--.
        // ,--,--,--. ,--.,--.  ,---.  |  ,---.  ,--.--.  ,---.   ,---.  ,--,--,--.
        // |        | |  ||  | (  .-'  |  .-.  | |  .--' | .-. | | .-. | |        |
        // |  |  |  | '  ''  ' .-'  `) |  | |  | |  |    ' '-' ' ' '-' ' |  |  |  |
        // `--`--`--'  `----'  `----'  `--' `--' `--'     `---'   `---'  `--`--`--'
        // ,--.         ,--.                      ,--.
        // `--'  ,---.  |  |  ,--,--. ,--,--,   ,-|  |  ,---.
        // ,--. (  .-'  |  | ' ,-.  | |      \ ' .-. | (  .-'
        // |  | .-'  `) |  | \ '-'  | |  ||  | \ `-' | .-'  `)
        // `--' `----'  `--'  `--`--' `--''--'  `---'  `----'
        //

        FishRegistration.register(context,
                PresetRestrictions.mushroomFields(context)
                        .withFish(SCItems.SPOREFISH)
                        .withSizeAndWeight(new SizeAndWeight(70, 50, 4000, 2000))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.moving())
        );

        FishRegistration.register(context,
                PresetRestrictions.mushroomFields(context)
                        .withFish(SCItems.SHROOMFISH)
                        .withSizeAndWeight(new SizeAndWeight(70, 50, 4000, 2000))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.SHROOMFISH)
        );


        //
        //                      ,--.                                                              ,--.
        // ,--.,--. ,--,--,   ,-|  |  ,---.  ,--.--.  ,---.  ,--.--.  ,---.  ,--.,--. ,--,--,   ,-|  |
        // |  ||  | |      \ ' .-. | | .-. : |  .--' | .-. | |  .--' | .-. | |  ||  | |      \ ' .-. |
        // '  ''  ' |  ||  | \ `-' | \   --. |  |    ' '-' ' |  |    ' '-' ' '  ''  ' |  ||  | \ `-' |
        //  `----'  `--''--'  `---'   `----' `--'    .`-  /  `--'     `---'   `----'  `--''--'  `---'
        //                                           `---'


        //common
        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish(SCItems.GOLD_FAN)
                        .withSizeAndWeight(new SizeAndWeight(70, 50, 4000, 2000))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY_STONE)
        );

        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish(SCItems.WHITEVEIL)
                        .withSizeAndWeight(new SizeAndWeight(100, 30, 33000, 7000))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY_STONE.moving())
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish(SCItems.MORGANITE)
                        .withSizeAndWeight(new SizeAndWeight(120, 80, 7000, 1000))
                        .withWeather(WeatherRestriction.RAIN)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM_STONE.withHP(200))
        );

        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish(SCItems.BLACK_EEL)
                        .withSizeAndWeight(new SizeAndWeight(500, 150, 6000, 2000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM_STONE.moving())
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish(SCItems.GEODE_EEL)
                        .withSizeAndWeight(new SizeAndWeight(500, 150, 10000, 2000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD_STONE.withHP(300))
        );

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish(SCItems.AMETHYSTBACK)
                        .withSizeAndWeight(new SizeAndWeight(300, 150, 16000, 7000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD_STONE.withHP(300))
                        .addRestriction(DimensionRestriction.OVERWORLD)
                        .addRestriction(ElevationBias.AMETHYSTBACK)
                        .addRestriction(FluidRestriction.WATER)
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish(SCItems.STONEFISH)
                        .withSizeAndWeight(new SizeAndWeight(300, 150, 26000, 7000))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.STONEFISH)
        );


        //
        //    ,--.         ,--.                   ,--.
        //  ,-|  | ,--.--. `--'  ,---.   ,---.  ,-'  '-.  ,---.  ,--,--,   ,---.
        // ' .-. | |  .--' ,--. | .-. | (  .-'  '-.  .-' | .-. | |      \ | .-. :
        // \ `-' | |  |    |  | | '-' ' .-'  `)   |  |   ' '-' ' |  ||  | \   --.
        //  `---'  `--'    `--' |  |-'  `----'    `--'    `---'  `--''--'  `----'
        //                      `--'

        FishRegistration.register(context,
                PresetRestrictions.dripstoneCaves(context)
                        .withFish(SCItems.DRIPFIN)
                        .withSizeAndWeight(new SizeAndWeight(300, 150, 16000, 7000))
                        .withDifficulty(Difficulty.EASY_DRIPSTONE.moving().withHP(200))
        );

        FishRegistration.register(context,
                PresetRestrictions.dripstoneCaves(context)
                        .withFish(SCItems.YELLOWSTONE_FISH)
                        .withSizeAndWeight(new SizeAndWeight(600, 150, 22000, 7000))
                        .withDifficulty(Difficulty.MEDIUM_DRIPSTONE.withHP(200))
                        .withRarity(Rarity.UNCOMMON)
        );

        FishRegistration.register(context,
                PresetRestrictions.dripstoneCaves(context)
                        .withFish(SCItems.FOSSILIZED_ANGELFISH)
                        .withSizeAndWeight(new SizeAndWeight(700, 150, 36000, 7000))
                        .withDifficulty(Difficulty.HARD_DRIPSTONE.withHP(600))
                        .withRarity(Rarity.RARE)
        );


        //
        // ,--.                  ,--.
        // |  | ,--.,--.  ,---.  |  ,---.       ,---.  ,--,--. ,--.  ,--.  ,---.   ,---.
        // |  | |  ||  | (  .-'  |  .-.  |     | .--' ' ,-.  |  \  `'  /  | .-. : (  .-'
        // |  | '  ''  ' .-'  `) |  | |  |     \ `--. \ '-'  |   \    /   \   --. .-'  `)
        // `--'  `----'  `----'  `--' `--'      `---'  `--`--'    `--'     `----' `----'
        //

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.lushCaves(context)
                        .withFish(SCItems.VIVID_MOSS)
                        .withSizeAndWeight(new SizeAndWeight(120, 70, 7000, 3000))
                        .withDifficulty(Difficulty.MEDIUM.moving().withHP(160))
                        .withRarity(Rarity.UNCOMMON)
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.lushCaves(context)
                        .withFish(SCItems.THE_QUARRISH)
                        .withSizeAndWeight(new SizeAndWeight(620, 270, 700000, 300000))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.withHP(220).withSpeed(4))
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.lushCaves(context)
                        .withFish(SCItems.LUSH_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.vanishing().moving().withHP(300))
        );


        //
        //    ,--.                                 ,--.            ,--.
        //  ,-|  |  ,---.   ,---.   ,---.   ,---.  |  |  ,--,--. ,-'  '-.  ,---.
        // ' .-. | | .-. : | .-. : | .-. | (  .-'  |  | ' ,-.  | '-.  .-' | .-. :
        // \ `-' | \   --. \   --. | '-' ' .-'  `) |  | \ '-'  |   |  |   \   --.
        //  `---'   `----'  `----' |  |-'  `----'  `--'  `--`--'   `--'    `----'
        //                         `--'

        //common
        FishRegistration.register(context,
                PresetRestrictions.deepslate(context)
                        .withFish(SCItems.GARNET_MACKEREL)
                        .withSizeAndWeight(new SizeAndWeight(40, 20, 2000, 1500))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY_DEEPSLATE.moving())
        );

        FishRegistration.register(context,
                PresetRestrictions.deepslate(context)
                        .withFish(SCItems.AQUAMARINE_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY_DEEPSLATE.withHP(200))
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.deepslate(context)
                        .withFish(SCItems.GHOSTLY_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.withHP(200))
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.deepslate(context)
                        .withFish(SCItems.BRIGHT_AMETHYST_SNAPPER)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 7000, 2000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.moving().withHP(400))
        );

        FishRegistration.register(context,
                PresetRestrictions.deepslate(context)
                        .withFish(SCItems.DARK_AMETHYST_SNAPPER)
                        .withSizeAndWeight(new SizeAndWeight(60, 20, 7000, 2000))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.MEDIUM_DEEPSLATE.moving().withHP(400))
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.deepslate(context)
                        .withFish(SCItems.DEEPSLATEFISH)
                        .withSizeAndWeight(new SizeAndWeight(420, 70, 70000, 20000))
                        .withDifficulty(Difficulty.DEEPSLATEFISH)
                        .withBaseChance(2)
                        .withRarity(Rarity.LEGENDARY)
        );

        //
        //    ,--.                                ,--.                  ,--.
        //  ,-|  |  ,---.   ,---.   ,---.       ,-|  |  ,--,--. ,--.--. |  |,-.
        // ' .-. | | .-. : | .-. : | .-. |     ' .-. | ' ,-.  | |  .--' |     /
        // \ `-' | \   --. \   --. | '-' '     \ `-' | \ '-'  | |  |    |  \  \
        //  `---'   `----'  `----' |  |-'       `---'   `--`--' `--'    `--'`--'
        //                         `--'

        //rare
        FishRegistration.register(context,
                PresetRestrictions.deepDark(context)
                        .withFish(SCItems.GLOWING_DARK)
                        .withSizeAndWeight(new SizeAndWeight(100, 10, 3000, 600))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.EASY_DEEP_DARK)
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.deepDark(context)
                        .withFish(SCItems.SCULKFISH)
                        .withSizeAndWeight(new SizeAndWeight(30, 10, 2000, 600))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(500))
                        .withDifficulty(Difficulty.MEDIUM_DEEP_DARK)
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.deepDark(context)
                        .withFish(SCItems.WARD)
                        .withSizeAndWeight(new SizeAndWeight(50, 10, 2600, 600))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.WARD)
        );


        //
        //                                                                ,--.    ,--.
        //  ,---.  ,--.  ,--.  ,---.  ,--.--. ,--.   ,--.  ,---.  ,--.--. |  |  ,-|  |
        // | .-. |  \  `'  /  | .-. : |  .--' |  |.'.|  | | .-. | |  .--' |  | ' .-. |
        // ' '-' '   \    /   \   --. |  |    |   .'.   | ' '-' ' |  |    |  | \ `-' |
        //  `---'     `--'     `----' `--'    '--'   '--'  `---'  `--'    `--'  `---'
        //                           ,---.                             ,--.
        //  ,---.  ,--.,--. ,--.--. /  .-'  ,--,--.  ,---.  ,---.      |  |  ,--,--. ,--.  ,--.  ,--,--.
        // (  .-'  |  ||  | |  .--' |  `-, ' ,-.  | | .--' | .-. :     |  | ' ,-.  |  \  `'  /  ' ,-.  |
        // .-'  `) '  ''  ' |  |    |  .-' \ '-'  | \ `--. \   --.     |  | \ '-'  |   \    /   \ '-'  |
        // `----'   `----'  `--'    `--'    `--`--'  `---'  `----'     `--'  `--`--'    `--'     `--`--'
        //

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.surfaceLava(context)
                        .withFish(SCItems.PYROTROUT)
                        .withSizeAndWeight(new SizeAndWeight(40, 20, 1200, 700))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM)
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.surfaceLava(context)
                        .withFish(SCItems.SUNEATER)
                        .withSizeAndWeight(new SizeAndWeight(100, 10, 3000, 600))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.moving())
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.surfaceLava(context)
                        .withFish(SCItems.OBSIDIAN_EEL)
                        .withSizeAndWeight(new SizeAndWeight(500, 150, 70000, 13000))
                        .withWeather(WeatherRestriction.RAIN)
                        .withDifficulty(Difficulty.MEDIUM.vanishing().moving())
                        .withRarity(Rarity.LEGENDARY)
        );

        //
        //                                                               ,--.    ,--.
        // ,---.  ,--.  ,--.  ,---.  ,--.--. ,--.   ,--.  ,---.  ,--.--. |  |  ,-|  |
        //| .-. |  \  `'  /  | .-. : |  .--' |  |.'.|  | | .-. | |  .--' |  | ' .-. |
        //' '-' '   \    /   \   --. |  |    |   .'.   | ' '-' ' |  |    |  | \ `-' |
        // `---'     `--'     `----' `--'    '--'   '--'  `---'  `--'    `--'  `---'
        //                                               ,--.
        // ,---.  ,--,--. ,--.  ,--.  ,---.   ,---.      |  |  ,--,--. ,--.  ,--.  ,--,--.
        //| .--' ' ,-.  |  \  `'  /  | .-. : (  .-'      |  | ' ,-.  |  \  `'  /  ' ,-.  |
        //\ `--. \ '-'  |   \    /   \   --. .-'  `)     |  | \ '-'  |   \    /   \ '-'  |
        // `---'  `--`--'    `--'     `----' `----'      `--'  `--`--'    `--'     `--`--'
        //

        //rare
        FishRegistration.register(context,
                PresetRestrictions.caveLava(context)
                        .withFish(SCItems.MOLTEN_SHRIMP)
                        .withSizeAndWeight(new SizeAndWeight(10, 3, 20, 10))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD)
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.caveLava(context)
                        .withFish(SCItems.OBSIDIAN_CRAB)
                        .withSizeAndWeight(new SizeAndWeight(15, 8, 700, 300))
                        .withDifficulty(Difficulty.OBSIDIAN_CRAB)
                        .withRarity(Rarity.EPIC)
        );

        //
        //                                                                ,--.    ,--.
        //  ,---.  ,--.  ,--.  ,---.  ,--.--. ,--.   ,--.  ,---.  ,--.--. |  |  ,-|  |
        // | .-. |  \  `'  /  | .-. : |  .--' |  |.'.|  | | .-. | |  .--' |  | ' .-. |
        // ' '-' '   \    /   \   --. |  |    |   .'.   | ' '-' ' |  |    |  | \ `-' |
        //  `---'     `--'     `----' `--'    '--'   '--'  `---'  `--'    `--'  `---'
        //    ,--.                                 ,--.            ,--.               ,--.
        //  ,-|  |  ,---.   ,---.   ,---.   ,---.  |  |  ,--,--. ,-'  '-.  ,---.      |  |  ,--,--. ,--.  ,--.  ,--,--.
        // ' .-. | | .-. : | .-. : | .-. | (  .-'  |  | ' ,-.  | '-.  .-' | .-. :     |  | ' ,-.  |  \  `'  /  ' ,-.  |
        // \ `-' | \   --. \   --. | '-' ' .-'  `) |  | \ '-'  |   |  |   \   --.     |  | \ '-'  |   \    /   \ '-'  |
        //  `---'   `----'  `----' |  |-'  `----'  `--'  `--`--'   `--'    `----'     `--'  `--`--'    `--'     `--`--'
        //                         `--'


        //epic
        FishRegistration.register(context,
                PresetRestrictions.deepslateLava(context)
                        .withFish(SCItems.MOLTEN_DEEPSLATE_CRAB)
                        .withSizeAndWeight(new SizeAndWeight(15, 8, 700, 300))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.DEEPSLATE_CRAB)
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.deepslateLava(context)
                        .withFish(SCItems.SCORCHED_BLOODSUCKER)
                        .withSizeAndWeight(new SizeAndWeight(60, 30, 1700, 300))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.HARD.moving().vanishing())
        );

        //
        //                    ,--.   ,--.
        // ,--,--,   ,---.  ,-'  '-. |  ,---.   ,---.  ,--.--.
        // |      \ | .-. : '-.  .-' |  .-.  | | .-. : |  .--'
        // |  ||  | \   --.   |  |   |  | |  | \   --. |  |
        // `--''--'  `----'   `--'   `--' `--'  `----' `--'
        //

        //trash
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.LAVA_CRAB_CLAW)
                        .withRarity(Rarity.TRASH)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
        );

        //common
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.EMBERGILL)
                        .withSizeAndWeight(new SizeAndWeight(220, 70, 5700, 900))
                        .withDifficulty(Difficulty.EASY)
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.GLOWSTONE_SEEKER)
                        .withSizeAndWeight(new SizeAndWeight(120, 40, 3700, 900))
                        .withDifficulty(Difficulty.EASY)
        );

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.MAGMA_FISH)
                        .withSizeAndWeight(new SizeAndWeight(120, 40, 3700, 900))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY.vanishing())
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.SCALDING_PIKE)
                        .withSizeAndWeight(new SizeAndWeight(75, 20, 5000, 3000))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.CINDER_SQUID)
                        .withSizeAndWeight(new SizeAndWeight(40, 20, 1300, 700))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.GLOWSTONE_PUFFERFISH)
                        .withSizeAndWeight(new SizeAndWeight(35, 25, 1000, 700))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.LAVA_CRAB)
                        .withSizeAndWeight(new SizeAndWeight(15, 8, 700, 300))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.NETHER_CRAB)
        );

        //legendary
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish(SCItems.CERBERAY)
                        .withSizeAndWeight(new SizeAndWeight(200, 53, 4000, 300))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.CERBERAY)
        );


        //
        // ,--.                              ,--.   ,--.          ,--.         ,--.   ,--.
        // |  |-.   ,--,--.  ,---.   ,--,--. |  | ,-'  '-.      ,-|  |  ,---.  |  | ,-'  '-.  ,--,--.  ,---.
        // | .-. ' ' ,-.  | (  .-'  ' ,-.  | |  | '-.  .-'     ' .-. | | .-. : |  | '-.  .-' ' ,-.  | (  .-'
        // | `-' | \ '-'  | .-'  `) \ '-'  | |  |   |  |       \ `-' | \   --. |  |   |  |   \ '-'  | .-'  `)
        //  `---'   `--`--' `----'   `--`--' `--'   `--'        `---'   `----' `--'   `--'    `--`--' `----'
        //

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.netherLavaBasaltDeltas(context)
                        .withFish(SCItems.WILLISH)
                        .withSizeAndWeight(new SizeAndWeight(75, 25, 4000, 700))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
        );

        //
        //               ,--.                                          ,---.                                   ,--.
        // ,---. ,--.--. `--' ,--,--,--.  ,---.   ,---.  ,--,--,      /  .-'  ,---.  ,--.--.  ,---.   ,---.  ,-'  '-.
        //| .--' |  .--' ,--. |        | (  .-'  | .-. | |      \     |  `-, | .-. | |  .--' | .-. : (  .-'  '-.  .-'
        //\ `--. |  |    |  | |  |  |  | .-'  `) ' '-' ' |  ||  |     |  .-' ' '-' ' |  |    \   --. .-'  `)   |  |
        // `---' `--'    `--' `--`--`--' `----'   `---'  `--''--'     `--'    `---'  `--'     `----' `----'    `--'
        //

        //
        //                                                ,--.      ,---.                                   ,--.
        //,--.   ,--.  ,--,--. ,--.--.  ,---.   ,---.   ,-|  |     /  .-'  ,---.  ,--.--.  ,---.   ,---.  ,-'  '-.
        //|  |.'.|  | ' ,-.  | |  .--' | .-. | | .-. : ' .-. |     |  `-, | .-. | |  .--' | .-. : (  .-'  '-.  .-'
        //|   .'.   | \ '-'  | |  |    | '-' ' \   --. \ `-' |     |  .-' ' '-' ' |  |    \   --. .-'  `)   |  |
        //'--'   '--'  `--`--' `--'    |  |-'   `----'  `---'      `--'    `---'  `--'     `----' `----'    `--'
        //                             `--'

        //
        //  ,--.   ,--.                                      ,--.
        //,-'  '-. |  ,---.   ,---.       ,---.  ,--,--,   ,-|  |
        //'-.  .-' |  .-.  | | .-. :     | .-. : |      \ ' .-. |
        //  |  |   |  | |  | \   --.     \   --. |  ||  | \ `-' |
        //  `--'   `--' `--'  `----'      `----' `--''--'  `---'
        //

        //uncommon
        FishRegistration.register(context,
                PresetRestrictions.endAir(context)
                        .withFish(SCItems.END_GLOW)
                        .withSizeAndWeight(new SizeAndWeight(235, 25, 7000, 700))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.withHP(250))
        );

        //rare
        FishRegistration.register(context,
                PresetRestrictions.endAir(context)
                        .withFish(SCItems.CHARFISH)
                        .withSizeAndWeight(new SizeAndWeight(135, 25, 4000, 700))
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.withHP(250))
        );

        //epic
        FishRegistration.register(context,
                PresetRestrictions.endAir(context)
                        .withFish(SCItems.CHORUS_CRAB)
                        .withSizeAndWeight(new SizeAndWeight(15, 8, 700, 300))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.END_CRAB.withHP(350))
        );

        //end void
        FishRegistration.register(context,
                PresetRestrictions.endAir(context)
                        .withFish(SCItems.VOIDBITER)
                        .withSizeAndWeight(new SizeAndWeight(50, 15, 2000, 200))
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.VOIDBITER)
                        
        );

        FishRegistration.register(context,
                PresetRestrictions.endAir(context)
                        .withFish(SCItems.PURPLE_CARP)
                        .withSizeAndWeight(new SizeAndWeight(150, 70, 4000, 1200))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving().withHP(350))
        );

        FishRegistration.register(context,
                PresetRestrictions.endAir(context)
                        .withFish(SCItems.VOIDFIN)
                        .withSizeAndWeight(new SizeAndWeight(250, 60, 6000, 800))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(350))
        );

        //end void outer islands
        FishRegistration.register(context,
                PresetRestrictions.endOuterIslandsAir(context)
                        .withFish(SCItems.SPACEJELLY)
                        .withSizeAndWeight(new SizeAndWeight(100, 20, 10, 1))
                        .withRarity(Rarity.RARE)
                        .addRestriction(StructureRestriction.END_CITIES)
                        .withDifficulty(Difficulty.HARD.moving().withPenalty(25).withHP(250))
        );

        FishRegistration.register(context,
                PresetRestrictions.endOuterIslandsAir(context)
                        .withFish(SCItems.CHORUS_MINNOW)
                        .withSizeAndWeight(new SizeAndWeight(250, 60, 6000, 2200))
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.HARD.vanishing().withHP(250))
        );

        FishRegistration.register(context,
                PresetRestrictions.endOuterIslandsAir(context)
                        .withFish(SCItems.NEBULA_SQUID)
                        .withSizeAndWeight(new SizeAndWeight(40, 20, 1300, 700))
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving().withHP(250))
        );
    }
}
