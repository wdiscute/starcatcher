package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

        for(FishPropertiesWithModRestriction restricted : fps)
        {
            consumer.accept(createKey(restricted.fp()), new ModLoadedCondition(restricted.modid()));
        }
    }

    public static final List<FishPropertiesWithModRestriction> fps = List.of(

            //list to store all fishes from other mods for compatibility, alongside the modid so it
            //datagens with the neoforge restrictions modifier

            overworldLakeFish(rl("tide", "tuna"))
                    .withWeather(FishProperties.Weather.RAIN)
                    .withRarity(FishProperties.Rarity.UNCOMMON)
                    .withDifficulty(FishProperties.Difficulty.HARD)
                    .withRestrictions("tide")


    );



    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder()
            .add(
                    Starcatcher.FISH_REGISTRY, bootstrap ->
                    {

                        //datagen all mod-specific fishes
                        for (FishPropertiesWithModRestriction restriction : fps)
                        {
                            register(bootstrap, restriction.fp());
                        }


                        //datagen all starcatcher fishes



                        //lakes
                        register(bootstrap, overworldLakeFish(getKey(ModItems.OBIDONTIEE.get())));

                       register(bootstrap, overworldLakeFish(getKey(ModItems.SILVERVEIL_PERCH.get()))
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));



                        register(bootstrap, overworldLakeFish(getKey(ModItems.ELDERSCALE.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(3));

                        register(bootstrap, overworldLakeFish(getKey(ModItems.DRIFTFIN.get()))
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldLakeFish(getKey(ModItems.TWILIGHT_KOI.get()))
                                        .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldLakeFish(getKey(ModItems.THUNDER_BASS.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withWeather(FishProperties.Weather.THUNDER)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(getKey(ModItems.LIGHTNING_BASS.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withWeather(FishProperties.Weather.THUNDER)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(getKey(ModItems.BOOT.get()))
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));


                        //lake icy
                        register(bootstrap, overworldIcyLakeFish(getKey(ModItems.FROSTJAW_TROUT.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldIcyLakeFish(getKey(ModItems.CRYSTALBACK_TROUT.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldIcyLakeFish(getKey(ModItems.AURORA.get()))
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(2)
                                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));

                        register(bootstrap, overworldIcyLakeFish(getKey(ModItems.WINTERY_PIKE.get())));


                        //lake warm
                        register(
                                bootstrap, overworldWarmLakeFish(getKey(ModItems.SANDTAIL.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT));

                        register(
                                bootstrap, overworldWarmLakeFish(getKey(ModItems.MIRAGE_CARP.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withWeather(FishProperties.Weather.CLEAR)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(
                                bootstrap, overworldWarmLakeFish(getKey(ModItems.SCORCHFISH.get()))
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(
                                bootstrap, overworldWarmLakeFish(getKey(ModItems.CACTIFISH.get()))
                                        .withDaytime(FishProperties.Daytime.DAY));


                        register(
                                bootstrap, overworldWarmLakeFish(getKey(ModItems.AGAVE_BREAM.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withWeather(FishProperties.Weather.CLEAR)
                                        .withDifficulty(FishProperties.Difficulty.HARD));


                        //mountain
                        register(
                                bootstrap, overworldMountainFish(getKey(ModItems.SUNNY_STURGEON.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withBaseChance(2));

                        register(
                                bootstrap, overworldMountainFish(getKey(ModItems.PEAKDWELLER.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(
                                bootstrap, overworldMountainFish(getKey(ModItems.ROCKGILL.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(
                                bootstrap, overworldMountainFish(getKey(ModItems.SUN_SEEKING_CARP.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withBaseChance(2)
                                        .withDaytime(FishProperties.Daytime.NOON));


                        //swamp
                        register(bootstrap, overworldSwampFish(getKey(ModItems.SLUDGE_CATFISH.get()))
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldSwampFish(getKey(ModItems.LILY_SNAPPER.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldSwampFish(getKey(ModItems.SAGE_CATFISH.get()))
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldSwampFish(getKey(ModItems.MOSSY_BOOT.get()))
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));

                        //darkoak forest
                        register(bootstrap, overworldDarkForestFish(getKey(ModItems.PALE_PINFISH.get()))
                                        .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDarkForestFish(getKey(ModItems.PINFISH.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDarkForestFish(getKey(ModItems.PALE_CARP.get()))
                                        .withDaytime(FishProperties.Daytime.DAY));


                        //cherry grove
                        register(bootstrap, overworldCherryGroveFish(getKey(ModItems.BLOSSOMFISH.get()))
                                        .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldCherryGroveFish(getKey(ModItems.PETALDRIFT_CARP.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldCherryGroveFish(getKey(ModItems.PINK_KOI.get()))
                                        .withWeather(FishProperties.Weather.RAIN));

                        register(bootstrap, overworldCherryGroveFish(getKey(ModItems.MORGANITE.get()))
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(
                                bootstrap, overworldCherryGroveFish(getKey(ModItems.ROSE_SIAMESE_FISH.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withRarity(FishProperties.Rarity.EPIC));


                        //mountain icy
                        register(bootstrap, overworldIcyMountainFish(getKey(ModItems.CRYSTALBACK_STURGEON.get())));

                        register(bootstrap, overworldIcyMountainFish(getKey(ModItems.ICETOOTH_STURGEON.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldIcyMountainFish(getKey(ModItems.BOREAL.get()))
                                        .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(3));

                        register(bootstrap, overworldIcyMountainFish(getKey(ModItems.CRYSTALBACK_BOREAL.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));


                        //river
                        register(bootstrap, overworldRiverFish(getKey(ModItems.DOWNFALL_BREAM.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withWeather(FishProperties.Weather.RAIN)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldRiverFish(getKey(ModItems.DRIFTING_BREAM.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldRiverFish(getKey(ModItems.WILLOW_BREAM.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(2));

                        register(bootstrap, overworldRiverFish(getKey(ModItems.HOLLOWBELLY_DARTER.get())));

                        register(bootstrap, overworldRiverFish(getKey(ModItems.MISTBACK_CHUB.get())));

                        register(
                                bootstrap, overworldRiverFish(getKey(ModItems.SILVERFIN_PIKE.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldRiverFish(getKey(Items.SALMON)));

                        register(
                                bootstrap, overworldRiverFish(getKey(ModItems.DRIED_SEAWEED.get()))
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));


                        //icy river
                        register(bootstrap, overworldIcyRiverFish(getKey(ModItems.FROSTGILL_CHUB.get())));

                        register(bootstrap, overworldIcyRiverFish(getKey(ModItems.CRYSTALBACK_MINNOW.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldIcyRiverFish(getKey(ModItems.AZURE_CRYSTALBACK_MINNOW.get()))
                                        .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(1)
                                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));

                        register(bootstrap, overworldIcyRiverFish(getKey(ModItems.BLUE_CRYSTAL_FIN.get()))
                                        .withDaytime(FishProperties.Daytime.DAY)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));


                        //ocean
                        register(bootstrap, overworldOceanFish(getKey(Items.COD)));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.SEA_BASS.get()))
                                        .withBaseChance(15)
                                        .withDaytime(FishProperties.Daytime.DAY));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.IRONJAW_HERRING.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withBaseChance(2)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.DEEPJAW_HERRING.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.DUSKTAIL_SNAPPER.get())));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.JOEL.get()))
                                        .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                                        .withBaseChance(1)
                                        .withRarity(FishProperties.Rarity.LEGENDARY));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.REDSCALED_TUNA.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldOceanFish(getKey(ModItems.WATERLOGGED_BOTTLE.get()))
                                        .withBaseChance(1)
                                        .withHasGuideEntry(false)
                                        .withSkipMinigame(true));

                        //deep ocean
                        register(bootstrap, overworldDeepOceanFish(getKey(ModItems.BIGEYE_TUNA.get()))
                                        .withDaytime(FishProperties.Daytime.NIGHT)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        //beach
                        register(bootstrap, overworldBeachFish(getKey(ModItems.CONCH.get()))
                                        .withBaseChance(15)
                                        .withHasGuideEntry(false)
                                        .withSkipMinigame(true));

                        register(bootstrap, overworldBeachFish(getKey(ModItems.CLAM.get()))
                                        .withBaseChance(15)
                                        .withHasGuideEntry(false)
                                        .withSkipMinigame(true));

                        //mushroom islands
                        register(bootstrap, overworldMushroomFieldsFish(getKey(ModItems.SHROOMFISH.get()))
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING));

                        register(bootstrap, overworldMushroomFieldsFish(getKey(ModItems.SPOREFISH.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.HARD));


                        //underground
                        register(bootstrap, overworldUndergroundFish(getKey(ModItems.GOLD_FAN.get())));

                        register(bootstrap, overworldUndergroundFish(getKey(ModItems.GEODE_EEL.get()))
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withBaseChance(1)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        //caves
                        register(bootstrap, overworldCavesFish(getKey(ModItems.WHITEVEIL.get())));

                        register(bootstrap, overworldCavesFish(getKey(ModItems.BLACK_EEL.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldCavesFish(getKey(ModItems.STONEFISH.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldCavesFish(getKey(ModItems.AMETHYSTBACK.get()))
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withMustBeCaughtBelowY(-20)
                                        .withMustBeCaughtAboveY(-40));

                        //dripstone caves
                        register(bootstrap, overworldCavesFish(getKey(ModItems.FOSSILIZED_ANGELFISH.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldCavesFish(getKey(ModItems.DRIPFIN.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldCavesFish(getKey(ModItems.YELLOWSTONE_FISH.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));


                        //lush caves
                        register(bootstrap, overworldLushCavesFish(getKey(ModItems.LUSH_PIKE.get()))
                                        .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(2));

                        register(bootstrap, overworldLushCavesFish(getKey(ModItems.VIVID_MOSS.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(4));


                        //deepslate
                        register(bootstrap, overworldDeepslateFish(getKey(ModItems.GHOSTLY_PIKE.get()))
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withBaseChance(2));

                        register(bootstrap, overworldDeepslateFish(getKey(ModItems.DEEPSLATEFISH.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDeepslateFish(getKey(ModItems.AQUAMARINE_PIKE.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDeepslateFish(getKey(ModItems.GARNET_MACKEREL.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldDeepslateFish(getKey(ModItems.BRIGHT_AMETHYST_SNAPPER.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withBaseChance(2));

                        register(bootstrap, overworldDeepslateFish(getKey(ModItems.DARK_AMETHYST_SNAPPER.get()))
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withBaseChance(2));


                        //deep dark
                        register(bootstrap, overworldDeepDarkFish(getKey(ModItems.SKULKFISH.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD)
                                        .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldDeepDarkFish(getKey(ModItems.WARD.get()))
                                        .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION)
                                        .withRarity(FishProperties.Rarity.LEGENDARY)
                                        .withBaseChance(2));

                        register(bootstrap, overworldDeepDarkFish(getKey(ModItems.GLOWING_DARK.get()))
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD));


                        //nether
                        register(bootstrap, netherFish(getKey(ModItems.EMBERGILL.get()))
                                        .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherFish(getKey(ModItems.LAVA_CRAB.get()))
                                        .withRarity(FishProperties.Rarity.EPIC)
                                        .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherFish(getKey(ModItems.MAGMA_FISH.get()))
                                        .withRarity(FishProperties.Rarity.UNCOMMON)
                                        .withDifficulty(FishProperties.Difficulty.HARD
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherFish(getKey(ModItems.GLOWSTONE_SEEKER.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherFish(getKey(ModItems.CINDER_SQUID.get()))
                                        .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY
                                                .withTreasure(FishProperties.Treasure.NETHER))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withBaseChance(2));

                        register(bootstrap, netherFish(getKey(ModItems.SCALDING_PIKE.get()))
                                        .withDifficulty(FishProperties.Difficulty.MEDIUM
                                                .withTreasure(FishProperties.Treasure.NETHER)));

                        register(bootstrap, netherFish(getKey(ModItems.LAVA_CRAB_CLAW.get()))
                                        .withBaseChance(1)
                                        .withSkipMinigame(true)
                                        .withHasGuideEntry(false));


                        //the end
                        register(bootstrap, endFish(getKey(ModItems.CHARFISH.get()))
                                        .withRarity(FishProperties.Rarity.RARE)
                                        .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP));


                    }
            );


    public static FishProperties endFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.END);
    }

    public static FishProperties netherFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.NETHER);
    }

    public static FishProperties overworldLushCavesFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldDeepDarkFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldCavesFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(50).withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldUndergroundFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldMountainFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldDeepslateFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish)
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(0);
    }

    public static FishProperties overworldIcyLakeFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmLakeFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmMountainFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldIcyMountainFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldIcyOceanFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldIcyRiverFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_RIVER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldLakeFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldOceanFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldDeepOceanFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldRiverFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldBeachFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BEACH)
                .withMustBeCaughtAboveY(0);
    }


    public static FishProperties overworldMushroomFieldsFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldCherryGroveFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldSwampFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldDarkForestFish(ResourceLocation fish)
    {
        return FishProperties.DEFAULT.withFish(fish).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST)
                .withMustBeCaughtAboveY(0);
    }

    public static Holder.Reference<FishProperties> register(BootstrapContext<FishProperties> bootstrap, FishProperties fp)
    {
        if (fp.customName().isEmpty())
        {
            return bootstrap.register(createKey(fp), fp);
        }
        else
        {
            return bootstrap.register(createKey(fp), fp);
        }
    }

    public static ResourceLocation getKey(Item item)
    {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ResourceLocation rl(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceKey<FishProperties> createKey(FishProperties fp)
    {
        if (fp.customName().isEmpty())
        {
            return ResourceKey.create(Starcatcher.FISH_REGISTRY, Starcatcher.rl(fp.fish().getNamespace() + "_" + fp.fish().getPath()));
        }
        else
        {
            return ResourceKey.create(Starcatcher.FISH_REGISTRY, Starcatcher.rl("starcatcher" + "_" + fp.customName()));
        }
    }
}
