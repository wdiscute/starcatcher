package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FishPropertiesProvider extends DatapackBuiltinEntriesProvider
{

    public FishPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, Set.of(Starcatcher.MOD_ID));
    }


    public static List<FishProperties> FISHES;

    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder()
            .add(
                    Starcatcher.FISH_REGISTRY, bootstrap ->
                    {

                        //lake
                        register(bootstrap, overworldLakeFish(ModItems.OBIDONTIEE.get()));

                        register(bootstrap, overworldLakeFish(ModItems.SILVERVEIL_PERCH.get())
                                .withWeather(FishProperties.Weather.RAIN)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(ModItems.ELDERSCALE.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withBaseChance(3));

                        register(bootstrap, overworldLakeFish(ModItems.DRIFTFIN.get())
                                .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldLakeFish(ModItems.TWILIGHT_KOI.get())
                                .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withWeather(FishProperties.Weather.RAIN)
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldLakeFish(ModItems.THUNDER_BASS.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withWeather(FishProperties.Weather.THUNDER)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(ModItems.LIGHTNING_BASS.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withWeather(FishProperties.Weather.THUNDER)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldLakeFish(ModItems.BOOT.get())
                                .withBaseChance(1)
                                .withSkipMinigame(true)
                                .withHasGuideEntry(false));


                        //lake icy
                        register(bootstrap, overworldIcyLakeFish(ModItems.FROSTJAW_TROUT.get())
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldIcyLakeFish(ModItems.CRYSTALBACK_TROUT.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldIcyLakeFish(ModItems.AURORA.get())
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(2)
                                .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));

                        register(bootstrap, overworldIcyLakeFish(ModItems.WINTERY_PIKE.get()));


                        //lake warm
                        register(bootstrap, overworldWarmLakeFish(ModItems.SANDTAIL.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldWarmLakeFish(ModItems.MIRAGE_CARP.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withDaytime(FishProperties.Daytime.DAY)
                                .withWeather(FishProperties.Weather.CLEAR)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldWarmLakeFish(ModItems.SCORCHFISH.get())
                                .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldWarmLakeFish(ModItems.CACTIFISH.get())
                                .withDaytime(FishProperties.Daytime.DAY));


                        register(bootstrap, overworldWarmLakeFish(ModItems.AGAVE_BREAM.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withWeather(FishProperties.Weather.CLEAR)
                                .withDifficulty(FishProperties.Difficulty.HARD));



                        //mountain
                        register(bootstrap, overworldMountainFish(ModItems.SUNNY_STURGEON.get())
                                .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDaytime(FishProperties.Daytime.DAY)
                                .withBaseChance(2));

                        register(bootstrap, overworldMountainFish(ModItems.PEAKDWELLER.get())
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldMountainFish(ModItems.ROCKGILL.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldMountainFish(ModItems.SUN_SEEKING_CARP.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withBaseChance(2)
                                .withDaytime(FishProperties.Daytime.NOON));


                        //swamp
                        register(bootstrap, overworldSwampFish(ModItems.SLUDGE_CATFISH.get())
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldSwampFish(ModItems.LILY_SNAPPER.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldSwampFish(ModItems.SAGE_CATFISH.get())
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldSwampFish(ModItems.MOSSY_BOOT.get())
                                .withBaseChance(1)
                                .withSkipMinigame(true)
                                .withHasGuideEntry(false));

                        //darkoak forest
                        register(bootstrap, overworldDarkForestFish(ModItems.PALE_PINFISH.get())
                                .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDarkForestFish(ModItems.PINFISH.get())
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDarkForestFish(ModItems.PALE_CARP.get())
                                .withDaytime(FishProperties.Daytime.DAY));


                        //cherry grove
                        register(bootstrap, overworldCherryGroveFish(ModItems.BLOSSOMFISH.get())
                                .withWeather(FishProperties.Weather.CLEAR));

                        register(bootstrap, overworldCherryGroveFish(ModItems.PETALDRIFT_CARP.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withWeather(FishProperties.Weather.RAIN)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldCherryGroveFish(ModItems.PINK_KOI.get())
                                .withWeather(FishProperties.Weather.RAIN));

                        register(bootstrap, overworldCherryGroveFish(ModItems.MORGANITE.get())
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldCherryGroveFish(ModItems.ROSE_SIAMESE_FISH.get())
                                .withDifficulty(FishProperties.Difficulty.HARD)
                                .withDaytime(FishProperties.Daytime.DAY)
                                .withWeather(FishProperties.Weather.RAIN)
                                .withRarity(FishProperties.Rarity.EPIC));


                        //mountain icy
                        register(bootstrap, overworldIcyMountainFish(ModItems.CRYSTALBACK_STURGEON.get()));

                        register(bootstrap, overworldIcyMountainFish(ModItems.ICETOOTH_STURGEON.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldIcyMountainFish(ModItems.BOREAL.get())
                                .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING)
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(3));

                        register(bootstrap, overworldIcyMountainFish(ModItems.CRYSTALBACK_BOREAL.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));



                        //river
                        register(bootstrap, overworldRiverFish(ModItems.DOWNFALL_BREAM.get())
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withWeather(FishProperties.Weather.RAIN)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldRiverFish(ModItems.DRIFTING_BREAM.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldRiverFish(ModItems.WILLOW_BREAM.get())
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withDifficulty(FishProperties.Difficulty.HARD)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withBaseChance(2));

                        register(bootstrap, overworldRiverFish(ModItems.HOLLOWBELLY_DARTER.get()));

                        register(bootstrap, overworldRiverFish(ModItems.MISTBACK_CHUB.get()));

                        register(bootstrap, overworldRiverFish(ModItems.SILVERFIN_PIKE.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldRiverFish(Items.SALMON));

                        register(bootstrap, overworldRiverFish(ModItems.DRIED_SEAWEED.get())
                                .withBaseChance(1)
                                .withSkipMinigame(true)
                                .withHasGuideEntry(false));



                        //icy river
                        register(bootstrap, overworldIcyRiverFish(ModItems.FROSTGILL_CHUB.get()));

                        register(bootstrap, overworldIcyRiverFish(ModItems.CRYSTALBACK_MINNOW.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldIcyRiverFish(ModItems.AZURE_CRYSTALBACK_MINNOW.get())
                                .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(1)
                                .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));

                        register(bootstrap, overworldIcyRiverFish(ModItems.BLUE_CRYSTAL_FIN.get())
                                .withDaytime(FishProperties.Daytime.DAY)
                                .withRarity(FishProperties.Rarity.UNCOMMON));


                        //ocean
                        register(bootstrap, overworldOceanFish(Items.COD));

                        register(bootstrap, overworldOceanFish(ModItems.SEA_BASS.get())
                                .withBaseChance(15)
                                .withDaytime(FishProperties.Daytime.DAY));

                        register(bootstrap, overworldOceanFish(ModItems.IRONJAW_HERRING.get())
                                .withDifficulty(FishProperties.Difficulty.HARD)
                                .withBaseChance(2)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldOceanFish(ModItems.DEEPJAW_HERRING.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldOceanFish(ModItems.DUSKTAIL_SNAPPER.get()));

                        register(bootstrap, overworldOceanFish(ModItems.JOEL.get())
                                .withDifficulty(FishProperties.Difficulty.EVERYTHING)
                                .withBaseChance(1)
                                .withRarity(FishProperties.Rarity.LEGENDARY));

                        register(bootstrap, overworldOceanFish(ModItems.REDSCALED_TUNA.get())
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldOceanFish(ModItems.WATERLOGGED_BOTTLE.get())
                                .withBaseChance(1)
                                .withHasGuideEntry(false)
                                .withSkipMinigame(true));

                        //beach
                        register(bootstrap, overworldBeachFish(ModItems.CONCH.get())
                                .withBaseChance(15)
                                .withHasGuideEntry(false)
                                .withSkipMinigame(true));

                        register(bootstrap, overworldBeachFish(ModItems.CLAM.get())
                                .withBaseChance(15)
                                .withHasGuideEntry(false)
                                .withSkipMinigame(true));

                        //mushroom islands
                        register(bootstrap, overworldMushroomFieldsFish(ModItems.SHROOMFISH.get())
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY_NOT_FORGIVING));

                        register(bootstrap, overworldMushroomFieldsFish(ModItems.SPOREFISH.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDifficulty(FishProperties.Difficulty.HARD));


                        //underground
                        register(bootstrap, overworldUndergroundFish(ModItems.GOLD_FAN.get()));

                        register(bootstrap, overworldUndergroundFish(ModItems.GEODE_EEL.get())
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withBaseChance(1)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        //caves
                        register(bootstrap, overworldCavesFish(ModItems.WHITEVEIL.get()));

                        register(bootstrap, overworldCavesFish(ModItems.BLACK_EEL.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldCavesFish(ModItems.STONEFISH.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldCavesFish(ModItems.AMETHYSTBACK.get())
                                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withMustBeCaughtBelowY(-20)
                                .withMustBeCaughtAboveY(-40));

                        //dripstone caves
                        register(bootstrap, overworldCavesFish(ModItems.FOSSILIZED_ANGELFISH.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldCavesFish(ModItems.DRIPFIN.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldCavesFish(ModItems.YELLOWSTONE_FISH.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withRarity(FishProperties.Rarity.UNCOMMON));


                        //lush caves
                        register(bootstrap, overworldLushCavesFish(ModItems.LUSH_PIKE.get())
                                .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(2));

                        register(bootstrap, overworldLushCavesFish(ModItems.VIVID_MOSS.get())
                                .withDifficulty(FishProperties.Difficulty.HARD)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withBaseChance(4));


                        //deepslate
                        register(bootstrap, overworldDeepslateFish(ModItems.GHOSTLY_PIKE.get())
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withBaseChance(2));

                        register(bootstrap, overworldDeepslateFish(ModItems.DEEPSLATEFISH.get())
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, overworldDeepslateFish(ModItems.AQUAMARINE_PIKE.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, overworldDeepslateFish(ModItems.GARNET_MACKEREL.get())
                                .withDifficulty(FishProperties.Difficulty.HARD)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldDeepslateFish(ModItems.BRIGHT_AMETHYST_SNAPPER.get())
                                .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN)
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withBaseChance(2));

                        register(bootstrap, overworldDeepslateFish(ModItems.DARK_AMETHYST_SNAPPER.get())
                                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withBaseChance(2));


                        //deep dark
                        register(bootstrap, overworldDeepDarkFish(ModItems.SKULKFISH.get())
                                .withDifficulty(FishProperties.Difficulty.HARD)
                                .withRarity(FishProperties.Rarity.UNCOMMON));

                        register(bootstrap, overworldDeepDarkFish(ModItems.WARD.get())
                                .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION)
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(2));

                        register(bootstrap, overworldDeepDarkFish(ModItems.GLOWING_DARK.get())
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withDifficulty(FishProperties.Difficulty.HARD));


                        //nether
                        register(bootstrap, netherFish(ModItems.EMBERGILL.get())
                                .withDifficulty(FishProperties.Difficulty.HARD_ONLY_THIN));

                        register(bootstrap, netherFish(ModItems.LAVA_CRAB.get())
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP));

                        register(bootstrap, netherFish(ModItems.MAGMA_FISH.get())
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withDifficulty(FishProperties.Difficulty.HARD));

                        register(bootstrap, netherFish(ModItems.GLOWSTONE_SEEKER.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM));

                        register(bootstrap, netherFish(ModItems.CINDER_SQUID.get())
                                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_NO_DECAY)
                                .withRarity(FishProperties.Rarity.RARE)
                                .withBaseChance(2));

                        register(bootstrap, netherFish(ModItems.SCALDING_PIKE.get()));

                        register(bootstrap, netherFish(ModItems.LAVA_CRAB_CLAW.get())
                                .withBaseChance(1)
                                .withSkipMinigame(true)
                                .withHasGuideEntry(false));



                        //the end
                        register(bootstrap, endFish(ModItems.CHARFISH.get())
                                .withRarity(FishProperties.Rarity.RARE)
                                .withDifficulty(FishProperties.Difficulty.EVERYTHING_FLIP));


                    }
            );


    public static FishProperties endFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.END);
    }

    public static FishProperties netherFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.NETHER);
    }

    public static FishProperties overworldLushCavesFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldDeepDarkFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldCavesFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(50).withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldUndergroundFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(50);
    }

    public static FishProperties overworldMountainFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldDeepslateFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withMustBeCaughtBelowY(0);
    }

    public static FishProperties overworldIcyLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldWarmMountainFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldIcyMountainFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_LAKE)
                .withMustBeCaughtAboveY(100);
    }

    public static FishProperties overworldIcyOceanFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldIcyRiverFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_RIVER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldOceanFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldRiverFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withMustBeCaughtAboveY(50)
                .withMustBeCaughtBelowY(100);
    }

    public static FishProperties overworldBeachFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BEACH)
                .withMustBeCaughtAboveY(0);
    }


    public static FishProperties overworldMushroomFieldsFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldCherryGroveFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldSwampFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP)
                .withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldDarkForestFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST)
                .withMustBeCaughtAboveY(0);
    }


    public static Holder.Reference<FishProperties> register(BootstrapContext<FishProperties> bootstrap, FishProperties fp)
    {
        if (fp.customName().isEmpty())
        {
            return bootstrap.register(key(fp.fish().getNamespace(), fp.fish().getPath()), fp);
        }
        else
        {
            return bootstrap.register(key("starcatcher", fp.customName()), fp);
        }
    }

    public static ResourceLocation getKey(Item item)
    {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ResourceKey<FishProperties> key(String namespace, String path)
    {
        return ResourceKey.create(Starcatcher.FISH_REGISTRY, Starcatcher.rl(namespace + "_" + path));
    }

}
