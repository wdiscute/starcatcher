package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FishPropertiesProvider extends DatapackBuiltinEntriesProvider
{

    public FishPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, FishPropertiesProvider::addConditions, Set.of(Starcatcher.MOD_ID));
    }

    private static void addConditions(final BiConsumer<ResourceKey<?>, ICondition> consumer)
    {

        for(FishPropertiesWithModRestriction restricted : RESTRICTED_FPS)
        {
            consumer.accept(createKey(restricted.fp()), new ModLoadedCondition(restricted.modid()));
        }
    }

    //region restricted
    public static final List<FishPropertiesWithModRestriction> RESTRICTED_FPS = List.of(

            //list to store all fishes from other mods for compatibility, alongside the modid so it
            //datagens with the neoforge restrictions modifier

            //example of a custom fish using starcatcher's pink_koi as a base for the item

//            FishProperties.DEFAULT
//                    .withFish(rl("starcatcher", "pink_koi"))
//                    .withCustomName("Very Cool Mysticcraft Fish")
//                    .withWorldRestrictions(
//                            FishProperties.WorldRestrictions.DEFAULT
//                                    .withBiomes(List.of(rl("mysticcraft", "cool_biome")))
//                                    .withFluids(List.of(rl("mysticcraft", "magical_water"))))
//                    .withDifficulty(FishProperties.Difficulty.HARD)
//                    .withRarity(FishProperties.Rarity.EPIC)
//                    .withWeather(FishProperties.Weather.RAIN)
//                    .withDaytime(FishProperties.Daytime.MIDNIGHT)
//                    .withMod("mysticcraft")


            //tide freshwater

            overworldColdFreshwaterFish(fromRL("tide", "trout"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.DAY)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldFreshwaterFish(fromRL("tide", "bass"))
                    .withWeather(FishProperties.Weather.CLEAR)
                    .withMod("tide"),

            overworldFreshwaterFish(fromRL("tide", "yellow_perch"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withMod("tide"),

            overworldFreshwaterFish(fromRL("tide", "bluegill"))
                    .withMod("tide"),

            overworldWarmFreshwaterFish(fromRL("tide", "mint_carp"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withWeather(FishProperties.Weather.RAIN)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldColdFreshwaterFish(fromRL("tide", "pike"))
                    .withMod("tide"),

            overworldWarmFreshwaterFish(fromRL("tide", "guppy"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withDaytime(FishProperties.Daytime.NIGHT)
                    .withMod("tide"),

            overworldColdFreshwaterFish(fromRL("tide", "catfish"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            overworldColdFreshwaterFish(fromRL("tide", "clayfish"))
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

            fish(fromRL("tide", "blossom_bass"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withMod("tide"),

            fish(fromRL("tide", "oakfish"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
                            .withBiomes(rl("minecraft", "forest")))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            overworldColdFreshwaterFish(fromRL("tide", "frostbite_flounder"))
                    .withDifficulty(FishProperties.Difficulty.MEDIUM)
                    .withMod("tide"),

            fish(fromRL("tide", "mirage_catfish"))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                    .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
                            .withBiomesTags(rl("minecraft", "is_badlands")))
                    .withMod("tide"),

            overworldDeepDarkFish(fromRL("tide", "echofin_snapper"))
                    .withRarity(FishProperties.Rarity.RARE)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            fish(fromRL("tide", "sunspike_goby"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
                            .withBiomesTags(rl("minecraft", "is_savanna")))
                    .withRarity(FishProperties.Rarity.EPIC)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withMod("tide"),

            fish(fromRL("tide", "birch_trout"))
                    .withWorldRestrictions(FishProperties.WorldRestrictions.DEFAULT
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

            overworldFreshwaterFish(fromRL("tide", "midas_fish"))
                    .withBaseChance(1)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP)
                    .withMod("tide"),

            endFish(fromRL("tide", "voidseeker"))
                    .withBaseChance(1)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                    .withMod("tide"),

            overworldOceanFish(fromRL("tide", "shooting_starfish"))
                    .withBaseChance(1)
                    .withRarity(FishProperties.Rarity.LEGENDARY)
                    .withDaytime(FishProperties.Daytime.MIDNIGHT)
                    .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                    .withMod("tide")


            );
    //endregion restricted

    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder()
            .add(
                    Starcatcher.FISH_REGISTRY, bootstrap ->
                    {

                        //datagen all mod-restricted fishes
                        for (FishPropertiesWithModRestriction restriction : RESTRICTED_FPS)
                        {
                            register(bootstrap, restriction.fp());
                        }

                        //datagen all starcatcher fishes

                        //lakes
                        register(bootstrap, overworldLakeFish(ModItems.OBIDONTIEE));

                        register(bootstrap, overworldLakeFish(ModItems.SILVERVEIL_PERCH)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(ModItems.ELDERSCALE)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(3));

                        register(bootstrap, overworldLakeFish(ModItems.DRIFTFIN)
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldLakeFish(ModItems.TWILIGHT_KOI)
                                        .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldLakeFish(ModItems.THUNDER_BASS)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withWeather(FishProperties.Weather.THUNDER)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(ModItems.LIGHTNING_BASS)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withWeather(FishProperties.Weather.THUNDER)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(ModItems.BOOT)
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));


                        //cold lake
                        register(bootstrap, overworldColdLakeFish(ModItems.FROSTJAW_TROUT)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldColdLakeFish(ModItems.CRYSTALBACK_TROUT)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldColdLakeFish(ModItems.AURORA)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(2)
                                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));

                        register(bootstrap, overworldColdLakeFish(ModItems.WINTERY_PIKE));


                        //lake warm
                        register(bootstrap, overworldWarmLakeFish(ModItems.SANDTAIL)
                                        .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldWarmLakeFish(ModItems.MIRAGE_CARP)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withWeather(FishProperties.Weather.CLEAR)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldWarmLakeFish(ModItems.SCORCHFISH)
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldWarmLakeFish(ModItems.CACTIFISH)
                                        .withDaytime(FishProperties.Daytime.DAY));


                        register(bootstrap, overworldWarmLakeFish(ModItems.AGAVE_BREAM)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withWeather(FishProperties.Weather.CLEAR)
                                        .withDifficulty(FishProperties.Difficulty.HARD));


                        //mountain
                        register(bootstrap, overworldMountainFish(ModItems.SUNNY_STURGEON)
                                        .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withBaseChance(2));

                        register(bootstrap, overworldMountainFish(ModItems.PEAKDWELLER)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldMountainFish(ModItems.ROCKGILL)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldMountainFish(ModItems.SUN_SEEKING_CARP)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withBaseChance(2)
                                        .withDaytime(FishProperties.Daytime.NOON));


                        //swamp
                        register(bootstrap, overworldSwampFish(ModItems.SLUDGE_CATFISH)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldSwampFish(ModItems.LILY_SNAPPER)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldSwampFish(ModItems.SAGE_CATFISH)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldSwampFish(ModItems.MOSSY_BOOT)
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));

                        //darkoak forest
                        register(bootstrap, overworldDarkForestFish(ModItems.PALE_PINFISH)
                                        .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDarkForestFish(ModItems.PINFISH)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDarkForestFish(ModItems.PALE_CARP)
                                        .withDaytime(FishProperties.Daytime.DAY));


                        //cherry grove
                        register(bootstrap, overworldCherryGroveFish(ModItems.BLOSSOMFISH)
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldCherryGroveFish(ModItems.PETALDRIFT_CARP)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldCherryGroveFish(ModItems.PINK_KOI)
                                        .withWeather(FishProperties.Weather.RAIN));

                        register(bootstrap, overworldCherryGroveFish(ModItems.MORGANITE)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(
                                bootstrap, overworldCherryGroveFish(ModItems.ROSE_SIAMESE_FISH)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withRarity(FishProperties.Rarity.EPIC));


                        //cold mountain
                        register(bootstrap, overworldColdMountainFish(ModItems.CRYSTALBACK_STURGEON));

                        register(bootstrap, overworldColdMountainFish(ModItems.ICETOOTH_STURGEON)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldColdMountainFish(ModItems.BOREAL)
                                        .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(3));

                        register(bootstrap, overworldColdMountainFish(ModItems.CRYSTALBACK_BOREAL)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));


                        //river
                        register(bootstrap, overworldRiverFish(ModItems.DOWNFALL_BREAM)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldRiverFish(ModItems.DRIFTING_BREAM)
                                        .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldRiverFish(ModItems.WILLOW_BREAM)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(2));

                        register(bootstrap, overworldRiverFish(ModItems.HOLLOWBELLY_DARTER));

                        register(bootstrap, overworldRiverFish(ModItems.MISTBACK_CHUB));

                        register(
                                bootstrap, overworldRiverFish(ModItems.SILVERFIN_PIKE)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        //register(bootstrap, overworldRiverFish(Items.SALMON)));

                        register(
                                bootstrap, overworldRiverFish(ModItems.DRIED_SEAWEED)
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));


                        //cold river
                        register(bootstrap, overworldColdRiverFish(ModItems.FROSTGILL_CHUB));

                        register(bootstrap, overworldColdRiverFish(ModItems.CRYSTALBACK_MINNOW)
                                        .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldColdRiverFish(ModItems.AZURE_CRYSTALBACK_MINNOW)
                                        .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(1)
                                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));

                        register(bootstrap, overworldColdRiverFish(ModItems.BLUE_CRYSTAL_FIN)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));


                        //ocean
                        // register(bootstrap, overworldOceanFish(Items.COD)));

                        register(bootstrap, overworldOceanFish(ModItems.SEA_BASS)
                                        .withBaseChance(15)
                                        .withDaytime(FishProperties.Daytime.DAY));

                        register(bootstrap, overworldOceanFish(ModItems.IRONJAW_HERRING)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withBaseChance(2)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldOceanFish(ModItems.DEEPJAW_HERRING)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldOceanFish(ModItems.DUSKTAIL_SNAPPER));

                        register(bootstrap, overworldOceanFish(ModItems.JOEL)
                                        .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                                        .withBaseChance(1)
                                        .withRarity(FishProperties.Rarity.LEGENDARY));

                        register(bootstrap, overworldOceanFish(ModItems.REDSCALED_TUNA)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldOceanFish(ModItems.WATERLOGGED_BOTTLE)
                                        .withBaseChance(1)
                                        .withHasGuideEntry(false)
                                        .withSkipMinigame(true));

                        //deep ocean
                        register(bootstrap, overworldDeepOceanFish(ModItems.BIGEYE_TUNA)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        //beach
                        register(bootstrap, overworldBeachFish(ModItems.CONCH)
                                        .withBaseChance(15)
                                        .withHasGuideEntry(false)
                                        .withSkipMinigame(true));

                        register(bootstrap, overworldBeachFish(ModItems.CLAM)
                                        .withBaseChance(15)
                                        .withHasGuideEntry(false)
                                        .withSkipMinigame(true));

                        //mushroom islands
                        register(bootstrap, overworldMushroomFieldsFish(ModItems.SHROOMFISH)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING));

                        register(bootstrap, overworldMushroomFieldsFish(ModItems.SPOREFISH)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.HARD));


                        //underground
                        register(bootstrap, overworldUndergroundFish(ModItems.GOLD_FAN));

                        register(bootstrap, overworldUndergroundFish(ModItems.GEODE_EEL)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withBaseChance(1)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        //caves
                        register(bootstrap, overworldCavesFish(ModItems.WHITEVEIL));

                        register(bootstrap, overworldCavesFish(ModItems.BLACK_EEL)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldCavesFish(ModItems.STONEFISH)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldCavesFish(ModItems.AMETHYSTBACK)
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withMustBeCaughtBelowY(-20)
                                        .withMustBeCaughtAboveY(-40));

                        //dripstone caves
                        register(bootstrap, overworldDripstoneCavesFish(ModItems.FOSSILIZED_ANGELFISH)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDripstoneCavesFish(ModItems.DRIPFIN)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDripstoneCavesFish(ModItems.YELLOWSTONE_FISH)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));


                        //lush caves
                        register(bootstrap, overworldLushCavesFish(ModItems.LUSH_PIKE)
                                        .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(2));

                        register(bootstrap, overworldLushCavesFish(ModItems.VIVID_MOSS)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(4));


                        //deepslate
                        register(bootstrap, overworldDeepslateFish(ModItems.GHOSTLY_PIKE)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(2));

                        register(bootstrap, overworldDeepslateFish(ModItems.DEEPSLATEFISH)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDeepslateFish(ModItems.AQUAMARINE_PIKE)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDeepslateFish(ModItems.GARNET_MACKEREL)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldDeepslateFish(ModItems.BRIGHT_AMETHYST_SNAPPER)
                                        .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withBaseChance(2));

                        register(bootstrap, overworldDeepslateFish(ModItems.DARK_AMETHYST_SNAPPER)
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withBaseChance(2));


                        //deep dark
                        register(bootstrap, overworldDeepDarkFish(ModItems.SKULKFISH)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldDeepDarkFish(ModItems.WARD)
                                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(2));

                        register(bootstrap, overworldDeepDarkFish(ModItems.GLOWING_DARK)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));


                        //nether
                        register(bootstrap, netherLavaFish(ModItems.EMBERGILL)
                                        .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherLavaFish(ModItems.LAVA_CRAB)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherLavaFish(ModItems.MAGMA_FISH)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherLavaFish(ModItems.GLOWSTONE_SEEKER)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherLavaFish(ModItems.CINDER_SQUID)
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY
                                                .withTreasure(FishProperties.Treasure.NETHER))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withBaseChance(2));

                        register(bootstrap, netherLavaFish(ModItems.SCALDING_PIKE)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherLavaFish(ModItems.LAVA_CRAB_CLAW)
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));


                        //the end
                        register(bootstrap, endFish(ModItems.CHARFISH)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        //.withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP));
                                        .withDifficulty(FishProperties.Difficulty.HARD));


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
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldDeepDarkFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .withMustBeCaughtBelowY(50);
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
                .withMustBeCaughtBelowY(50).withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldUndergroundFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
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

    public static FishProperties overworldFreshwaterFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_FRESHWATER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmFreshwaterFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_FRESHWATER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldColdFreshwaterFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_FRESHWATER)
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
                .withMustBeCaughtAboveY(0);
    }


    public static FishProperties overworldMushroomFieldsFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldJungleFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldTaigaFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_TAIGA)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldCherryGroveFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldSwampFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldDarkForestFish(Holder<Item> fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST)
                .withMustBeCaughtAboveY(0);
    }



    public static ResourceLocation rl(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }



    public static Holder.Reference<FishProperties> register(BootstrapContext<FishProperties> bootstrap, FishProperties fp)
    {
        return bootstrap.register(createKey(fp), fp);
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
            return ResourceKey.create(Starcatcher.FISH_REGISTRY,
                    Starcatcher.rl(fp.fish().getRegisteredName().replace(":", "_")));
        }
        else
        {
            return ResourceKey.create(Starcatcher.FISH_REGISTRY, Starcatcher.rl("starcatcher" + "_" + fp.customName()));
        }
    }
}
