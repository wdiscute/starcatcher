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

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FishPropertiesProvider extends DatapackBuiltinEntriesProvider
{

    public FishPropertiesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, Set.of(Starcatcher.MOD_ID));
    }


    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder()
            .add(
                    Starcatcher.FISH_REGISTRY, bootstrap ->
                    {
                        //lake
                        register(bootstrap, overworldLakeFish(ModItems.OBIDONTIEE.get()));

                        register(bootstrap, overworldLakeFish(ModItems.SILVERVEIL_PERCH.get()));

                        register(bootstrap, overworldLakeFish(ModItems.ELDERSCALE.get())
                                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withBaseChance(3));

                        register(bootstrap, overworldLakeFish(ModItems.DRIFTFIN.get()));



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



                        //icy river
                        register(bootstrap, overworldIcyRiverFish(ModItems.FROSTGILL_CHUB.get()));

                        register(bootstrap, overworldIcyRiverFish(ModItems.CRYSTALBACK_MINNOW.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldIcyRiverFish(ModItems.AZURE_CRYSTALBACK_MINNOW.get())
                                .withDaytime(FishProperties.Daytime.MIDNIGHT)
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(1)
                                .withDifficulty(FishProperties.Difficulty.NON_STOP_ACTION));



                        //ocean
                        register(bootstrap, overworldOceanFish(Items.COD));

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

                        //underground
                        register(bootstrap, overworldUndergroundFish(ModItems.WHITEVEIL.get()));

                        register(bootstrap, overworldDeepslateFish(ModItems.GHOSTLY_PIKE.get())
                                .withRarity(FishProperties.Rarity.UNCOMMON)
                                .withBaseChance(2));

                        register(bootstrap, overworldUndergroundFish(ModItems.GOLD_FAN.get()));

                        register(bootstrap, overworldUndergroundFish(ModItems.BLACK_EEL.get()));

                        register(bootstrap, overworldUndergroundFish(ModItems.AMETHYSTBACK.get())
                                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST)
                                .withRarity(FishProperties.Rarity.EPIC)
                                .withMustBeCaughtBellowY(-20)
                                .withMustBeCaughtAboveY(-40));

                        //lush caves
                        register(bootstrap, overworldLushCavesFish(ModItems.LUSH_PIKE.get())
                                .withDifficulty(FishProperties.Difficulty.THIN_NO_DECAY)
                                .withRarity(FishProperties.Rarity.LEGENDARY)
                                .withBaseChance(2));

                        //nether
                        register(bootstrap, netherFish(ModItems.EMBERGILL.get()));

                        register(bootstrap, netherFish(ModItems.SCALDING_PIKE.get()));

                    }
            );


    public static FishProperties netherFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.NETHER);
    }

    public static FishProperties overworldLushCavesFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES).withMustBeCaughtBellowY(50);
    }

    public static FishProperties overworldUndergroundFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD).withMustBeCaughtBellowY(50).withMustBeCaughtAboveY(0);
    }

    public static FishProperties overworldDeepslateFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish))
                .withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD).withMustBeCaughtBellowY(0);
    }

    public static FishProperties overworldIcyLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_LAKE).withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldIcyOceanFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_OCEAN).withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldIcyRiverFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_RIVER).withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE).withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldOceanFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN).withMustBeCaughtAboveY(50);
    }

    public static FishProperties overworldRiverFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER).withMustBeCaughtAboveY(50);
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
