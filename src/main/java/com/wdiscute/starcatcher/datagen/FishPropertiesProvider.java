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


                        //river
                        register(bootstrap, overworldRiverFish(ModItems.DOWNFALL_BREAM.get())
                                .withDaytime(FishProperties.Daytime.NIGHT)
                                .withWeather(FishProperties.Weather.RAIN));
                        register(bootstrap, overworldRiverFish(ModItems.DRIFTING_BREAM.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));
                        register(bootstrap, overworldRiverFish(ModItems.WILLOW_BREAM.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));

                        register(bootstrap, overworldRiverFish(ModItems.HOLLOWBELLY_DARTER.get()));
                        register(bootstrap, overworldRiverFish(ModItems.MISTBACK_CHUB.get()));
                        register(bootstrap, overworldRiverFish(ModItems.SILVERFIN_PIKE.get()));

                        register(bootstrap, overworldRiverFish(Items.SALMON));


                        //icy river
                        register(bootstrap, overworldIcyRiverFish(ModItems.FROSTGILL_CHUB.get()));
                        register(bootstrap, overworldIcyRiverFish(ModItems.CRYSTALBACK_MINNOW.get())
                                .withDaytime(FishProperties.Daytime.NIGHT));
                        register(bootstrap, overworldIcyRiverFish(ModItems.AZURE_CRYSTALBACK_MINNOW.get())
                                .withDaytime(FishProperties.Daytime.MIDNIGHT));


                        //ocean
                        register(bootstrap, overworldOceanFish(Items.COD));
                        register(bootstrap, overworldOceanFish(ModItems.IRONJAW_HERRING.get()));
                        register(bootstrap, overworldOceanFish(ModItems.DEEPJAW_HERRING.get()));
                        register(bootstrap, overworldOceanFish(ModItems.DUSKTAIL_SNAPPER.get()));


                        //nether
                        register(bootstrap, netherFish(ModItems.EMBERGILL.get()));


                    }
            );


    public static FishProperties netherFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.NETHER);
    }

    public static FishProperties overworldIcyLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_LAKE);
    }

    public static FishProperties overworldIcyOceanFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_OCEAN);
    }

    public static FishProperties overworldIcyRiverFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ICY_RIVER);
    }

    public static FishProperties overworldLakeFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE);
    }

    public static FishProperties overworldOceanFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN);
    }

    public static FishProperties overworldRiverFish(Item fish)
    {
        return FishProperties.DEFAULT.withFish(getKey(fish)).withWorldRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER);
    }

    public static Holder.Reference<FishProperties> register(BootstrapContext<FishProperties> bootstrap, FishProperties fp)
    {
        return bootstrap.register(key(fp.fish().getNamespace(), fp.fish().getPath()), fp);
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
