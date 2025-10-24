package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FishAndTrophiesPropertiesProvider extends DatapackBuiltinEntriesProvider
{

    public FishAndTrophiesPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, FishAndTrophiesPropertiesProvider::addConditions, Set.of(Starcatcher.MOD_ID));
    }

    private static void addConditions(final BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        for (FishPropertiesWithModRestriction restricted : RESTRICTED_FPS)
        {
            consumer.accept(createKey(restricted.fp()), new ModLoadedCondition(restricted.modid()));
        }
    }

    private static int customFishCount = 0;
    private static int customTrophyCount = 0;

    //region fps
    public static final List<FishProperties> FPS = new ArrayList<>(
            List.of(

                    //example fish datagen
//                    fish(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD))
//                            .withCustomName("super_rare_cod_of_doom")
//                            .withDaytime(FishProperties.Daytime.MIDNIGHT)
//                            .withWeather(FishProperties.Weather.THUNDER)
//                            .withRarity(FishProperties.Rarity.LEGENDARY)
//                            .withSkipMinigame(true)
//                            .withBaseChance(10)
//                            .withHasGuideEntry(false)
//                            .withMustBeCaughtAboveY(20)
//                            .withMustBeCaughtBelowY(30)
//
//                            .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
//                                    .withDims(Level.OVERWORLD.location())
//                                    .withDimsBlacklist(Level.END.location())
//                                    .withBiomes(Biomes.OLD_GROWTH_PINE_TAIGA.location())
//                                    .withBiomesTags(BiomeTags.IS_JUNGLE.location())
//                                    .withBiomesBlacklist(Biomes.BAMBOO_JUNGLE.location())
//                                    .withBiomesBlacklistTags(BiomeTags.HAS_ANCIENT_CITY.location())
//                                    .withFluids(ResourceLocation.fromNamespaceAndPath("coolmod", "magic_fluid"))
//                            )
//
//                            .withBaitRestrictions(FishProperties.BaitRestrictions.DEFAULT
//                                    .withCorrectBait(BuiltInRegistries.ITEM.getKey(Items.WHEAT))
//                                    .withCorrectBobber(BuiltInRegistries.ITEM.getKey(Items.DIAMOND))
//                                    .withIncorrectBaits(ModItems.STARCATCHER_TWINE.getId())
//                                    .withMustHaveCorrectBait(true)
//                                    .withConsumesBait(false)
//                                    .withCorrectBaitChanceAdded(100)
//                            )
//
//                            .withDifficulty(
//                                    new FishProperties.Difficulty(
//                                            8, 21, 26, 5, 2, false, false, true, true,
//                                            new FishProperties.Treasure(
//                                                    true,
//                                                    BuiltInRegistries.ITEM.getKey(Items.DIAMOND),
//                                                    20),
//                                            false
//                                    )
//                            )
//                    ,


                    //lakes
                    overworldLakeFish(ModItems.OBIDONTIEE),

                    overworldLakeFish(ModItems.SILVERVEIL_PERCH)
                            .withWeather(FishProperties.Weather.RAIN)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldLakeFish(ModItems.ELDERSCALE)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withBaseChance(3),

                    overworldLakeFish(ModItems.DRIFTFIN)
                            .withWeather(FishProperties.Weather.CLEAR),

                    overworldLakeFish(ModItems.TWILIGHT_KOI)
                            .withDaytime(FishProperties.Daytime.MIDNIGHT)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withWeather(FishProperties.Weather.RAIN)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldLakeFish(ModItems.THUNDER_BASS)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withWeather(FishProperties.Weather.THUNDER)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldLakeFish(ModItems.LIGHTNING_BASS)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withWeather(FishProperties.Weather.THUNDER)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldLakeFish(ModItems.BOOT)
                            .withBaseChance(1)
                            .withSkipMinigame(true)
                            .withHasGuideEntry(false),


                    //cold lake
                    overworldColdLakeFish(ModItems.FROSTJAW_TROUT)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldColdLakeFish(ModItems.CRYSTALBACK_TROUT)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldColdLakeFish(ModItems.AURORA)
                            .withRarity(FishProperties.Rarity.LEGENDARY)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withBaseChance(2)
                            .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION),

                    overworldColdLakeFish(ModItems.WINTERY_PIKE),


                    //lake warm
                    overworldWarmLakeFish(ModItems.SANDTAIL)
                            .withDaytime(FishProperties.Daytime.NIGHT),

                    overworldWarmLakeFish(ModItems.MIRAGE_CARP)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withDaytime(FishProperties.Daytime.DAY)
                            .withWeather(FishProperties.Weather.CLEAR)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldWarmLakeFish(ModItems.SCORCHFISH)
                            .withWeather(FishProperties.Weather.CLEAR),

                    overworldWarmLakeFish(ModItems.CACTIFISH)
                            .withDaytime(FishProperties.Daytime.DAY),


                    overworldWarmLakeFish(ModItems.AGAVE_BREAM)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withWeather(FishProperties.Weather.CLEAR)
                            .withDifficulty(FishProperties.Difficulty.HARD),


                    //mountain
                    overworldMountainFish(ModItems.SUNNY_STURGEON)
                            .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDaytime(FishProperties.Daytime.DAY)
                            .withBaseChance(2),

                    overworldMountainFish(ModItems.PEAKDWELLER)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldMountainFish(ModItems.ROCKGILL)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldMountainFish(ModItems.SUN_SEEKING_CARP)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withBaseChance(2)
                            .withDaytime(FishProperties.Daytime.NOON),


                    //swamp
                    overworldSwampFish(ModItems.SLUDGE_CATFISH)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldSwampFish(ModItems.LILY_SNAPPER)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldSwampFish(ModItems.SAGE_CATFISH)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withWeather(FishProperties.Weather.CLEAR),

                    overworldSwampFish(ModItems.MOSSY_BOOT)
                            .withBaseChance(1)
                            .withSkipMinigame(true)
                            .withHasGuideEntry(false),


                    //darkoak forest
                    overworldDarkForestFish(ModItems.PALE_PINFISH)
                            .withDaytime(FishProperties.Daytime.MIDNIGHT)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldDarkForestFish(ModItems.PINFISH)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldDarkForestFish(ModItems.PALE_CARP)
                            .withDaytime(FishProperties.Daytime.DAY),


                    //cherry grove
                    overworldCherryGroveFish(ModItems.BLOSSOMFISH)
                            .withWeather(FishProperties.Weather.CLEAR),

                    overworldCherryGroveFish(ModItems.PETALDRIFT_CARP)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withWeather(FishProperties.Weather.RAIN)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldCherryGroveFish(ModItems.PINK_KOI)
                            .withWeather(FishProperties.Weather.RAIN),

                    overworldCherryGroveFish(ModItems.MORGANITE)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldCherryGroveFish(ModItems.ROSE_SIAMESE_FISH)
                            .withDifficulty(FishProperties.Difficulty.HARD)
                            .withDaytime(FishProperties.Daytime.DAY)
                            .withWeather(FishProperties.Weather.RAIN)
                            .withRarity(FishProperties.Rarity.EPIC),


                    //cold mountain
                    overworldColdMountainFish(ModItems.CRYSTALBACK_STURGEON),

                    overworldColdMountainFish(ModItems.ICETOOTH_STURGEON)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldColdMountainFish(ModItems.BOREAL)
                            .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                            .withRarity(FishProperties.Rarity.LEGENDARY)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withBaseChance(3),

                    overworldColdMountainFish(ModItems.CRYSTALBACK_BOREAL)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),


                    //river
                    overworldRiverFish(ModItems.DOWNFALL_BREAM)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withWeather(FishProperties.Weather.RAIN)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldRiverFish(ModItems.DRIFTING_BREAM)
                            .withDaytime(FishProperties.Daytime.NIGHT),

                    overworldRiverFish(ModItems.WILLOW_BREAM)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withDifficulty(FishProperties.Difficulty.HARD)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withBaseChance(2),

                    overworldRiverFish(ModItems.HOLLOWBELLY_DARTER),

                    overworldRiverFish(ModItems.MISTBACK_CHUB),

                    overworldRiverFish(ModItems.SILVERFIN_PIKE)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldRiverFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.SALMON)),

                    overworldRiverFish(ModItems.DRIED_SEAWEED)
                            .withBaseChance(1)
                            .withSkipMinigame(true)
                            .withHasGuideEntry(false),


                    //cold river
                    overworldColdRiverFish(ModItems.FROSTGILL_CHUB),

                    overworldColdRiverFish(ModItems.CRYSTALBACK_MINNOW)
                            .withDaytime(FishProperties.Daytime.NIGHT),

                    overworldColdRiverFish(ModItems.AZURE_CRYSTALBACK_MINNOW)
                            .withDaytime(FishProperties.Daytime.MIDNIGHT)
                            .withRarity(FishProperties.Rarity.LEGENDARY)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withBaseChance(1)
                            .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION),

                    overworldColdRiverFish(ModItems.BLUE_CRYSTAL_FIN)
                            .withDaytime(FishProperties.Daytime.DAY)
                            .withRarity(FishProperties.Rarity.UNCOMMON),


                    //ocean
                    overworldOceanFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD)),

                    overworldOceanFish(ModItems.SEA_BASS)
                            .withBaseChance(15)
                            .withDaytime(FishProperties.Daytime.DAY),

                    overworldOceanFish(ModItems.IRONJAW_HERRING)
                            .withDifficulty(FishProperties.Difficulty.HARD)
                            .withBaseChance(2)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldOceanFish(ModItems.DEEPJAW_HERRING)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldOceanFish(ModItems.DUSKTAIL_SNAPPER),

                    overworldOceanFish(ModItems.JOEL)
                            .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                            .withBaseChance(1)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withRarity(FishProperties.Rarity.LEGENDARY),

                    overworldOceanFish(ModItems.REDSCALED_TUNA)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldOceanFish(ModItems.WATERLOGGED_BOTTLE)
                            .withBaseChance(1)
                            .withHasGuideEntry(false)
                            .withSkipMinigame(true),

                    //deep ocean
                    overworldDeepOceanFish(ModItems.BIGEYE_TUNA)
                            .withDaytime(FishProperties.Daytime.NIGHT)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    //beach
                    overworldBeachFish(ModItems.CONCH)
                            .withBaseChance(15)
                            .withHasGuideEntry(false)
                            .withSkipMinigame(true),

                    overworldBeachFish(ModItems.CLAM)
                            .withBaseChance(15)
                            .withHasGuideEntry(false)
                            .withSkipMinigame(true),

                    //mushroom islands
                    overworldMushroomFieldsFish(ModItems.SHROOMFISH)
                            .withRarity(FishProperties.Rarity.LEGENDARY)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING),

                    overworldMushroomFieldsFish(ModItems.SPOREFISH)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),


                    //underground
                    overworldUndergroundFish(ModItems.GOLD_FAN),

                    overworldUndergroundFish(ModItems.GEODE_EEL)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withBaseChance(1)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    //caves
                    overworldCavesFish(ModItems.WHITEVEIL),

                    overworldCavesFish(ModItems.BLACK_EEL)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldCavesFish(ModItems.STONEFISH)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldCavesFish(ModItems.AMETHYSTBACK)
                            .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withMustBeCaughtBelowY(-20)
                            .withMustBeCaughtAboveY(-40),

                    //dripstone caves
                    overworldDripstoneCavesFish(ModItems.FOSSILIZED_ANGELFISH)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldDripstoneCavesFish(ModItems.DRIPFIN)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldDripstoneCavesFish(ModItems.YELLOWSTONE_FISH)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withRarity(FishProperties.Rarity.UNCOMMON),


                    //lush caves
                    overworldLushCavesFish(ModItems.LUSH_PIKE)
                            .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withRarity(FishProperties.Rarity.LEGENDARY)
                            .withBaseChance(2),

                    overworldLushCavesFish(ModItems.VIVID_MOSS)
                            .withDifficulty(FishProperties.Difficulty.HARD)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withBaseChance(4),


                    //deepslate
                    overworldDeepslateFish(ModItems.GHOSTLY_PIKE)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withBaseChance(2),

                    overworldDeepslateFish(ModItems.DEEPSLATEFISH)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldDeepslateFish(ModItems.AQUAMARINE_PIKE)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldDeepslateFish(ModItems.GARNET_MACKEREL)
                            .withDifficulty(FishProperties.Difficulty.HARD)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldDeepslateFish(ModItems.BRIGHT_AMETHYST_SNAPPER)
                            .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withBaseChance(2),

                    overworldDeepslateFish(ModItems.DARK_AMETHYST_SNAPPER)
                            .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withBaseChance(2),


                    //deep dark
                    overworldDeepDarkFish(ModItems.SCULKFISH)
                            .withDifficulty(FishProperties.Difficulty.HARD)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    overworldDeepDarkFish(ModItems.WARD)
                            .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withRarity(FishProperties.Rarity.LEGENDARY)
                            .withBaseChance(2),

                    overworldDeepDarkFish(ModItems.GLOWING_DARK)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.HARD),


                    //overworld surface lava
                    overworldSurfaceLava(ModItems.SUNEATER)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldSurfaceLava(ModItems.PYROTROUT)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM),

                    overworldSurfaceLava(ModItems.OBSIDIAN_EEL)
                            .withWeather(FishProperties.Weather.RAIN)
                            .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                            .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                            .withRarity(FishProperties.Rarity.LEGENDARY),

                    //overworld underground lava
                    overworldUndergroundLava(ModItems.MOLTEN_SHRIMP)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    overworldUndergroundLava(ModItems.OBSIDIAN_CRAB)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)
                            .withRarity(FishProperties.Rarity.UNCOMMON),

                    //overworld deepslate lava
                    overworldDeepslateLava(ModItems.SCORCHED_BLOODSUCKER)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN),

                    overworldDeepslateLava(ModItems.MOLTEN_DEEPSLATE_CRAB)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),


                    //nether
                    netherLavaFish(ModItems.EMBERGILL)
                            .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN
                                    .withTreasure(FishProperties.Treasure.NETHER)),

                    netherLavaFish(ModItems.LAVA_CRAB)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP
                                    .withTreasure(FishProperties.Treasure.NETHER)),

                    netherLavaFish(ModItems.MAGMA_FISH)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.HARD
                                    .withTreasure(FishProperties.Treasure.NETHER)),

                    netherLavaFish(ModItems.GLOWSTONE_SEEKER)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM
                                    .withTreasure(FishProperties.Treasure.NETHER)),

                    netherLavaFish(ModItems.CINDER_SQUID)
                            .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY
                                    .withTreasure(FishProperties.Treasure.NETHER))
                            .withRarity(FishProperties.Rarity.RARE)
                            .withBaseChance(2),

                    netherLavaFish(ModItems.SCALDING_PIKE)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM
                                    .withTreasure(FishProperties.Treasure.NETHER)),

                    netherLavaFish(ModItems.GLOWSTONE_PUFFERFISH)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM
                                    .withTreasure(FishProperties.Treasure.NETHER)),

                    netherLavaFish(ModItems.LAVA_CRAB_CLAW)
                            .withBaseChance(1)
                            .withSkipMinigame(true)
                            .withHasGuideEntry(false),

                    //the end
                    endFish(ModItems.CHARFISH)
                            .withRarity(FishProperties.Rarity.RARE)
                            .withDifficulty(FishProperties.Difficulty.HARD),

                    endFish(ModItems.CHORUS_CRAB)
                            .withRarity(FishProperties.Rarity.EPIC)
                            .withDifficulty(FishProperties.Difficulty.EVERYTHING),

                    endFish(ModItems.END_GLOW)
                            .withRarity(FishProperties.Rarity.UNCOMMON)
                            .withDifficulty(FishProperties.Difficulty.MEDIUM)

            ));
    //endregion fps

    //region restricted fps
    public static final List<FishPropertiesWithModRestriction> RESTRICTED_FPS = List.of(

            //list to store all fishes from other mods for compatibility, alongside the modid so it
            //datagens with the neoforge restrictions modifier

            //example of a custom fish using starcatcher's pink_koi as a base for the item

//            FishProperties.DEFAULT
//                    .withFish(baseItem("starcatcher", "pink_koi"))
//                    .withCustomName("Very Cool Mysticcraft Fish")
//                    .withWorldRestrictions(
//                            FishProperties.WorldRestrictions.DEFAULT
//                                    .withBiomes(List.of(baseItem("mysticcraft", "cool_biome")))
//                                    .withFluids(List.of(baseItem("mysticcraft", "magical_water"))))
//                    .withDifficulty(FishProperties.Difficulty.HARD)
//                    .withRarity(FishProperties.Rarity.EPIC)
//                    .withWeather(FishProperties.Weather.RAIN)
//                    .withDaytime(FishProperties.Daytime.MIDNIGHT)
//                    .withMod("mysticcraft")


            //region Tide

            //
            //  ,--.   ,--.    ,--.
            //,-'  '-. `--'  ,-|  |  ,---.
            //'-.  .-' ,--. ' .-. | | .-. :
            //  |  |   |  | \ `-' | \   --.
            //  `--'   `--'  `---'   `----'
            //

            //tide freshwater

            overworldColdLakeFish(fromRL("tide", "trout"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldLakeFish(fromRL("tide", "bass"))
                    .withWeather(FishProperties.Weather.CLEAR)
                    .withMod("tide"),

            overworldLakeFish(fromRL("tide", "yellow_perch"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("tide"),

            overworldMountainFish(fromRL("tide", "bluegill"))
                    .withMod("tide"),

            overworldWarmMountainFish(fromRL("tide", "mint_carp"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldColdRiverFish(fromRL("tide", "pike"))
                    .withMod("tide"),

            overworldWarmLakeFish(fromRL("tide", "guppy"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withMod("tide"),

            overworldColdLakeFish(fromRL("tide", "catfish"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldColdLakeFish(fromRL("tide", "clayfish"))
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("tide"),

            //tide saltwater
            overworldOceanFish(fromRL("tide", "tuna"))
                    .withMod("tide"),

            overworldColdOceanFish(fromRL("tide", "ocean_perch"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldOceanFish(fromRL("tide", "mackerel"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldWarmOceanFish(fromRL("tide", "angelfish"))
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("tide"),

            overworldOceanFish(fromRL("tide", "barracuda"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldWarmOceanFish(fromRL("tide", "sailfish"))
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("tide"),

            //tide underground
            overworldCavesFish(fromRL("tide", "cave_eel"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldCavesFish(fromRL("tide", "crystal_shrimp"))
                    .withMod("tide"),

            overworldCavesFish(fromRL("tide", "iron_tetra"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldCavesFish(fromRL("tide", "glowfish"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldCavesFish(fromRL("tide", "anglerfish"))
                    .withMod("tide"),

            overworldCavesFish(fromRL("tide", "cave_crawler"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldCavesFish(fromRL("tide", "gilded_minnow"))
                    .withMod("tide"),


            //tide deepslate
            overworldDeepslateFish(fromRL("tide", "deep_grouper"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldDeepslateFish(fromRL("tide", "shadow_snapper"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldDeepslateFish(fromRL("tide", "abyss_angler"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withBaseChance(2)
                    .withMod("tide"),

            overworldDeepslateFish(fromRL("tide", "lapis_lanternfish"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldDeepslateFish(fromRL("tide", "luminescent_jellyfish"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldDeepslateFish(fromRL("tide", "crystalline_carp"))
                    .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withMod("tide"),

            overworldDeepslateFish(fromRL("tide", "bedrock_tetra"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                    .withMod("tide"),

            //tide biome specific
            fish(fromRL("tide", "prarie_pike"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
                            .withBiomes(rl("minecraft", "plains")))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            fish(fromRL("tide", "sandskipper"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
                            .withBiomes(rl("minecraft", "desert")))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                    .withMod("tide"),

            overworldCherryGroveFish(fromRL("tide", "blossom_bass"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldFish(fromRL("tide", "oakfish"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withBiomes(rl("minecraft", "forest")))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldColdLakeFish(fromRL("tide", "frostbite_flounder"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldFish(fromRL("tide", "mirage_catfish"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withBiomesTags(rl("minecraft", "is_badlands")))
                    .withMod("tide"),

            overworldDeepDarkFish(fromRL("tide", "echofin_snapper"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            overworldFish(fromRL("tide", "sunspike_goby"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withBiomesTags(rl("minecraft", "is_savanna")))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            overworldFish(fromRL("tide", "birch_trout"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withBiomesTags(StarcatcherTags.IS_BIRCH_FOREST))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldMountainFish(fromRL("tide", "stonefish"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldDripstoneCavesFish(fromRL("tide", "dripstone_darter"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            overworldSwampFish(fromRL("tide", "slimefin_snapper"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                    .withMod("tide"),

            overworldMushroomFieldsFish(fromRL("tide", "sporestalker"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
                    .withMod("tide"),

            overworldJungleFish(fromRL("tide", "leafback"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                    .withMod("tide"),

            overworldLushCavesFish(fromRL("tide", "fluttergill"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldTaigaFish(fromRL("tide", "pine_perch"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            //missing structure restriction support to add windbass and aquathorn from tide mod

            //ride overworld lava
            fish(fromRL("tide", "ember_koi"))
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withFluids(rl("minecraft", "lava")))
                    .withMustBeCaughtAboveY(50)
                    .withMod("tide"),

            fish(fromRL("tide", "inferno_guppy"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withFluids(rl("minecraft", "lava")))
                    .withMustBeCaughtAboveY(50)
                    .withMod("tide"),

            fish(fromRL("tide", "obsidian_pike"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withFluids(rl("minecraft", "lava")))
                    .withMustBeCaughtAboveY(50)
                    .withMod("tide"),

            fish(fromRL("tide", "volcano_tuna"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD
                            .withFluids(rl("minecraft", "lava")))
                    .withMustBeCaughtAboveY(50)
                    .withMod("tide"),

            //tide nether
            netherLavaFish(fromRL("tide", "magma_mackerel"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            netherLavaFish(fromRL("tide", "ashen_perch"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            netherLavaFish(fromRL("tide", "soulscaler"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withBaseChance(3)
                    .withMod("tide"),

            netherLavaFish(fromRL("tide", "warped_guppy"))
                    .withMod("tide"),

            netherLavaFish(fromRL("tide", "crimson_fangjaw"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            netherLavaFish(fromRL("tide", "witherfin"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                    .withMod("tide"),

            netherLavaFish(fromRL("tide", "blazing_swordfish"))
                    .withBaseChance(2)
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                    .withMod("tide"),

            //tide end
            endFish(fromRL("tide", "endstone_perch"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            endFish(fromRL("tide", "enderfin"))
                    .withBaseChance(2)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            endFish(fromRL("tide", "endergazer"))
                    .withBaseChance(2)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withMod("tide"),

            endFish(fromRL("tide", "purpur_pike"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            endFish(fromRL("tide", "chorus_cod"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            endFish(fromRL("tide", "elytrout"))
                    .withBaseChance(2)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            overworldLakeFish(fromRL("tide", "midas_fish"))
                    .withBaseChance(1)
                    .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                    .withMod("tide"),

            endFish(fromRL("tide", "voidseeker"))
                    .withBaseChance(1)
                    .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                    .withMod("tide"),

            overworldOceanFish(fromRL("tide", "shooting_starfish"))
                    .withBaseChance(1)
                    .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDaytime(FishProperties.Daytime.MIDNIGHT)
                    .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                    .withMod("tide"),

            //endregion Tide


            //region Aquaculture 2

            //
            //                                                   ,--.   ,--.                                 ,---.
            // ,--,--.  ,---.  ,--.,--.  ,--,--.  ,---. ,--.,--. |  | ,-'  '-. ,--.,--. ,--.--.  ,---.      '.-.  \
            //' ,-.  | | .-. | |  ||  | ' ,-.  | | .--' |  ||  | |  | '-.  .-' |  ||  | |  .--' | .-. :      .-' .'
            //\ '-'  | ' '-' | '  ''  ' \ '-'  | \ `--. '  ''  ' |  |   |  |   '  ''  ' |  |    \   --.     /   '-.
            // `--`--'  `-|  |  `----'   `--`--'  `---'  `----'  `--'   `--'    `----'  `--'     `----'     '-----'
            //            `--'


            //freshwater
            overworldRiverFish(fromRL("aquaculture", "smallmouth_bass"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("aquaculture"),

            overworldRiverFish(fromRL("aquaculture", "bluegill"))
                    .withMod("aquaculture"),

            overworldRiverFish(fromRL("aquaculture", "brown_trout"))
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withWeather(FishProperties.Weather.CLEAR)
                    .withMod("aquaculture"),

            overworldRiverFish(fromRL("aquaculture", "carp"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("aquaculture"),

            overworldMountainFish(fromRL("aquaculture", "catfish"))
                    .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("aquaculture"),

            overworldMountainFish(fromRL("aquaculture", "gar"))
                    .withMod("aquaculture"),

            overworldLakeFish(fromRL("aquaculture", "minnow"))
                    .withMod("aquaculture"),

            overworldLakeFish(fromRL("aquaculture", "muskellunge"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDaytime(FishProperties.Daytime.MIDNIGHT)
                    .withMod("aquaculture"),

            overworldLakeFish(fromRL("aquaculture", "perch"))
                    .withMod("aquaculture"),

            //arid
            overworldWarmMountainFish(fromRL("aquaculture", "bayad"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withMod("aquaculture"),

            overworldWarmLakeFish(fromRL("aquaculture", "boulti"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("aquaculture"),

            overworldWarmMountainFish(fromRL("aquaculture", "capitaine"))
                    .withMod("aquaculture"),

            overworldWarmMountainFish(fromRL("aquaculture", "synodontis"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withMod("aquaculture"),

            //arctic ocean
            overworldColdOceanFish(fromRL("aquaculture", "atlantic_cod"))
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "blackfish"))
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "pacific_halibut"))
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "atlantic_halibut"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "atlantic_herring"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "pink_salmon"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withWeather(FishProperties.Weather.THUNDER)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "pollock"))
                    .withMod("aquaculture"),

            overworldColdOceanFish(fromRL("aquaculture", "rainbow_trout"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withMod("aquaculture"),

            //saltwater
            overworldOceanFish(fromRL("aquaculture", "jellyfish"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withBaseChance(3)
                    .withMod("aquaculture"),

            overworldOceanFish(fromRL("aquaculture", "red_grouper"))
                    .withMod("aquaculture"),

            overworldOceanFish(fromRL("aquaculture", "tuna"))
                    .withMod("aquaculture"),

            //jungle
            overworldJungleFish(fromRL("aquaculture", "arapaima"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("aquaculture"),

            overworldJungleFish(fromRL("aquaculture", "arrau_turtle"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("aquaculture"),


            overworldJungleFish(fromRL("aquaculture", "piranha"))
                    .withBaitRestrictions(FishProperties.BaitRestrictions.LEGENDARY_BAIT)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                    .withDaytime(FishProperties.Daytime.NOON)
                    .withMod("aquaculture"),

            overworldJungleFish(fromRL("aquaculture", "tambaqui"))
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("aquaculture"),

            //swamp
            overworldSwampFish(fromRL("aquaculture", "leech"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("aquaculture"),

            overworldSwampFish(fromRL("aquaculture", "box_turtle"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("aquaculture"),

            //mushroom island
            overworldMushroomFieldsFish(fromRL("aquaculture", "brown_shrooma"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                    .withMod("aquaculture"),

            overworldMushroomFieldsFish(fromRL("aquaculture", "red_shrooma"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                    .withMod("aquaculture"),

            //anywhere
            overworldFish(fromRL("aquaculture", "goldfish"))
                    .withBaseChance(1)
                    .withMod("aquaculture"),

            //endregion Aquaculture 2

            //region Fish of Thieves

            overworldOceanFish(fromRL("fishofthieves", "splashtail"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("fishofthieves"),

            overworldLakeFish(fromRL("fishofthieves", "pondie"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withBaseChance(2)
                    .withMod("fishofthieves"),

            overworldRiverFish(fromRL("fishofthieves", "islehopper"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withBaseChance(2)
                    .withMod("fishofthieves"),

            overworldWarmOceanFish(fromRL("fishofthieves", "ancientscale"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withMod("fishofthieves"),

            overworldWarmOceanFish(fromRL("fishofthieves", "plentifin"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("fishofthieves"),

            overworldLushCavesFish(fromRL("fishofthieves", "wildsplash"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("fishofthieves"),

            overworldDeepslateFish(fromRL("fishofthieves", "devilfish"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withMod("fishofthieves"),

            overworldColdOceanFish(fromRL("fishofthieves", "battlegill"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("fishofthieves"),

            endFish(fromRL("fishofthieves", "wrecker"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withMod("fishofthieves"),

            overworldOceanFish(fromRL("fishofthieves", "stormfish"))
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withWeather(FishProperties.Weather.THUNDER)
                    .withRarity(FishProperties.Rarity.RARE)
                    .withMod("fishofthieves")

            //endregion Fish of Thieves

    );
    //endregion restricted fps

    //region trophies
    public static final List<TrophyProperties> TROPHIES = List.of(

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_GOLD),
                    TrophyProperties.TrophyType.TROPHY,
                    "Trophy of Masterful Fishing",
                    new TrophyProperties.RarityProgress(50, 0),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_SILVER),
                    TrophyProperties.TrophyType.TROPHY,
                    "Trophy of Masterful Fishing",
                    new TrophyProperties.RarityProgress(25, 0),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_BRONZE),
                    TrophyProperties.TrophyType.TROPHY,
                    "Trophy of Masterful Fishing",
                    new TrophyProperties.RarityProgress(10, 0),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_SILVER),
                    TrophyProperties.TrophyType.TROPHY,
                    "Trophy of Masterful Fishing",
                    new TrophyProperties.RarityProgress(0, 75),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_GOLD),
                    TrophyProperties.TrophyType.TROPHY,
                    "Trophy of Masterful Fishing",
                    new TrophyProperties.RarityProgress(0, 150),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_GOLD),
                    TrophyProperties.TrophyType.TROPHY,
                    "Trophy of Masterful Fishing",
                    new TrophyProperties.RarityProgress(75, 200),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.TROPHY_GOLD),
                    TrophyProperties.TrophyType.TROPHY,
                    "test trophy",
                    new TrophyProperties.RarityProgress(1, 1),
                    new TrophyProperties.RarityProgress(1, 1),
                    new TrophyProperties.RarityProgress(1, 1),
                    new TrophyProperties.RarityProgress(1, 1),
                    new TrophyProperties.RarityProgress(0, 1),
                    new TrophyProperties.RarityProgress(0, 1),
                    TrophyProperties.DEFAULT.chanceToCatch()
            ),


            //                                         ,--.
            // ,---.   ,---.   ,---. ,--.--.  ,---.  ,-'  '-.  ,---.
            //(  .-'  | .-. : | .--' |  .--' | .-. : '-.  .-' (  .-'
            //.-'  `) \   --. \ `--. |  |    \   --.   |  |   .-'  `)
            //`----'   `----'  `---' `--'     `----'   `--'   `----'
            //

            new TrophyProperties(
                    FishProperties.DEFAULT.withFish(ModItems.DRIFTING_WATERLOGGED_BOTTLE)
                            .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN),
                    TrophyProperties.TrophyType.SECRET,
                    "",
                    new TrophyProperties.RarityProgress(6, 15),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    100
            ),

            new TrophyProperties(
                    overworldSurfaceLava(ModItems.SCALDING_BOTTLE),
                    TrophyProperties.TrophyType.SECRET,
                    "",
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    new TrophyProperties.RarityProgress(1, 0),
                    TrophyProperties.RarityProgress.DEFAULT,
                    33
            ),

            new TrophyProperties(
                    overworldSurfaceLava(ModItems.SCALDING_BOTTLE),
                    TrophyProperties.TrophyType.SECRET,
                    "",
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    new TrophyProperties.RarityProgress(1, 10),
                    TrophyProperties.RarityProgress.DEFAULT,
                    33
            ),



            //
            //          ,--.   ,--.
            // ,---.  ,-'  '-. |  ,---.   ,---.  ,--.--.  ,---.
            //| .-. | '-.  .-' |  .-.  | | .-. : |  .--' (  .-'
            //' '-' '   |  |   |  | |  | \   --. |  |    .-'  `)
            // `---'    `--'   `--' `--'  `----' `--'    `----'
            //


            new TrophyProperties(
                    overworldDeepslateFish(ModItems.CRYSTAL_HOOK),
                    TrophyProperties.TrophyType.NONE,
                    "",
                    new TrophyProperties.RarityProgress(6, 15),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    new TrophyProperties.RarityProgress(1, 0),
                    100
            ),

            new TrophyProperties(
                    overworldDeepslateFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND)),
                    TrophyProperties.TrophyType.NONE,
                    "",
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    new TrophyProperties.RarityProgress(1, 4),
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    100
            ),

            new TrophyProperties(
                    netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_SCRAP)),
                    TrophyProperties.TrophyType.NONE,
                    "",
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    new TrophyProperties.RarityProgress(3, 0),
                    33
            ),

            new TrophyProperties(
                    netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_SCRAP)),
                    TrophyProperties.TrophyType.NONE,
                    "",
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    TrophyProperties.RarityProgress.DEFAULT,
                    new TrophyProperties.RarityProgress(0, 10),
                    33
            )


    );

    //endregion trophies

    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder()
            //fishes
            .add(
                    Starcatcher.FISH_REGISTRY, bootstrap ->
                    {
                        //datagen all mod-restricted fishes
                        for (FishPropertiesWithModRestriction restriction : RESTRICTED_FPS)
                            bootstrap.register(createKey(restriction.fp()), restriction.fp());

                        //datagen all starcatcher fishes
                        for (FishProperties fp : FPS)
                            bootstrap.register(createKey(fp), fp);
                    }
            )
            //trophies
            .add(
                    Starcatcher.TROPHY_REGISTRY, bootstrap ->
                    {
                        for (TrophyProperties tp : TROPHIES)
                            bootstrap.register(createKey(tp), tp);
                    }
            );


    public static FishProperties fish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish);
    }

    public static FishProperties overworldFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD);
    }

    public static FishProperties endFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.END);
    }

    public static FishProperties netherLavaFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA);
    }

    public static FishProperties overworldLushCavesFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES)
                .withBaitRestrictions(FishProperties.BaitRestrictions.LUSH_BAIT)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldDeepDarkFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .withBaitRestrictions(FishProperties.BaitRestrictions.SCULK_BAIT)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldSurfaceLava(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA)
                .withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldCavesFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(50).withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldDripstoneCavesFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DRIPSTONE_CAVES)
                .withBaitRestrictions(FishProperties.BaitRestrictions.DRIPSTONE_BAIT)
                .withMustBeCaughtBelowY(50).withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldUndergroundFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldUndergroundLava(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldMountainFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldDeepslateFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(0);
    }

    public static FishProperties overworldDeepslateLava(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA)
                .withMustBeCaughtBelowY(0);
    }

    public static FishProperties overworldColdLakeFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmLakeFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmMountainFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldColdMountainFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldColdOceanFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldColdRiverFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_RIVER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldLakeFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldOceanFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmOceanFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldDeepOceanFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldRiverFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldBeachFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BEACH)
                .withMustBeCaughtAboveY(50);
    }


    public static FishProperties overworldMushroomFieldsFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS)
                .withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldJungleFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldTaigaFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_TAIGA)
                .withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldCherryGroveFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .withBaitRestrictions(FishProperties.BaitRestrictions.CHERRY_BAIT)
                .withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldSwampFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP)
                .withBaitRestrictions(FishProperties.BaitRestrictions.MURKWATER_BAIT)
                .withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldDarkForestFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST)
                .withMustBeCaughtAboveY(50);
    }


    public static ResourceLocation rl(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static Holder<Item> fromRL(String ns, String path)
    {
        return TrustedHolder.createStandAlone(
                BuiltInRegistries.ITEM.holderOwner(),
                ResourceKey.create(Registries.ITEM, rl(ns, path)));
    }

    public static ResourceKey<FishProperties> createKey(FishProperties fp)
    {
        if (fp.customName().isEmpty())
        {
            return ResourceKey.create(
                    Starcatcher.FISH_REGISTRY,
                    Starcatcher.rl(fp.fish().getRegisteredName().replace(":", "_")));
        }
        else
        {
            customFishCount++;
            return ResourceKey.create(
                    Starcatcher.FISH_REGISTRY,
                    Starcatcher.rl(fp.fish().getRegisteredName().replace(":", "_") + "_" + customFishCount));
        }
    }

    public static Holder.Reference<TrophyProperties> register(BootstrapContext<TrophyProperties> bootstrap, TrophyProperties tp)
    {
        return bootstrap.register(createKey(tp), tp);
    }

    public static ResourceKey<TrophyProperties> createKey(TrophyProperties tp)
    {
        customTrophyCount++;
        return ResourceKey.create(
                Starcatcher.TROPHY_REGISTRY,
                Starcatcher.rl(tp.trophyType().getSerializedName() + "_" + tp.fp().fish().getRegisteredName().replace(":", "_") + "_" + customTrophyCount));
    }

}
