package com.wdiscute.starcatcher.registry.fishing;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.BaitRestriction;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import com.wdiscute.starcatcher.registry.fishing.compat.*;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class FishingPropertiesRegistry
{

    public static void register()
    {
        DGTrophies.bootstrap();
        DGMinecraftFishes.bootstrap();
        DGStarcatcherFishes.bootstrap();
        DGTideFishes.bootstrap();
        DGAquacultureFishes.bootstrap();
        DGFishOfThievesFishes.bootstrap();
        DGNetherDepthsUpgradeFishes.bootstrap();
        DGSullysModFishes.bootstrap();
        DGUpgradeAquaticFishes.bootstrap();
        DGEnvironmentalFishes.bootstrap();
        DGBetterEndFishes.bootstrap();
        DGCollectorsReapFishes.bootstrap();
        DGMinersDelightFishes.bootstrap();
        DGAlexsCavesFishes.bootstrap();
        DGCrittersAndCompanionsFishes.bootstrap();
        DGHybridAquaticFishes.bootstrap();
        DGAquamiraeFishes.bootstrap();
        DGTerraFirmaCraftFishes.bootstrap();
        DGUnusualFishFishes.bootstrap();
        DGSpawnFishes.bootstrap();
        DGFintasticFishes.bootstrap();
    }

    //region builders
    public static FishProperties.Builder fish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish);
    }

    public static FishProperties.Builder overworldFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD);
    }

    public static FishProperties.Builder endFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.END)
                .addModifier(SCMinigameModifiers.TELEPORT);
    }

    public static FishProperties.Builder endOuterIslandsFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.END_OUTER_ISLANDS)
                .addModifier(SCMinigameModifiers.TELEPORT);
    }

    public static FishProperties.Builder netherLavaFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder netherLavaCrimsonForestFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_CRIMSON_FOREST)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder netherLavaWarpedForestFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_WARPED_FOREST)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder netherLavaSoulSandValleyFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_SOUL_SAND_VALLEY)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder netherLavaBasaltDeltasFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.NETHER_LAVA_BASALT_DELTAS)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder overworldLushCavesFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUSH_CAVES)
                .addRestrictions(BaitRestriction.LUSH_BAIT);
    }

    public static FishProperties.Builder overworldDeepDarkFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_DARK)
                .addRestrictions(BaitRestriction.SCULK_BAIT);
    }

    public static FishProperties.Builder overworldSurfaceFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SURFACE);
    }

    public static FishProperties.Builder overworldSurfaceLava(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_SURFACE)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder overworldCavesFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_STONE_CAVES);
    }

    public static FishProperties.Builder overworldDripstoneCavesFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DRIPSTONE_CAVES)
                .addRestrictions(BaitRestriction.DRIPSTONE_BAIT);
    }

    public static FishProperties.Builder overworldUndergroundFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_UNDERGROUND);
    }

    public static FishProperties.Builder overworldUndergroundLava(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_UNDERGROUND)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder overworldMountainFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MOUNTAIN);
    }

    public static FishProperties.Builder overworldDeepslateFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEPSLATE);
    }

    public static FishProperties.Builder overworldDeepslateLava(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAVA_DEEPSLATE)
                .addModifier(SCMinigameModifiers.BURN_ON_MISS);
    }

    public static FishProperties.Builder overworldColdLakeFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_LAKE)
                .withDifficulty(FishProperties.Difficulty.EASY.addModifiers(List.of(SCMinigameModifiers.FREEZE_ON_MISS)));
    }

    public static FishProperties.Builder overworldWarmLakeFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE);
    }

    public static FishProperties.Builder overworldWarmMountainFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE);
    }

    public static FishProperties.Builder overworldColdMountainFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_MOUNTAIN)
                .withDifficulty(FishProperties.Difficulty.EASY.addModifiers(List.of(SCMinigameModifiers.FREEZE_ON_MISS)));
    }

    public static FishProperties.Builder overworldColdOceanFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withDifficulty(FishProperties.Difficulty.EASY.addModifiers(List.of(SCMinigameModifiers.FREEZE_ON_MISS)))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN);
    }

    public static FishProperties.Builder overworldColdRiverFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .withDifficulty(FishProperties.Difficulty.EASY.addModifiers(List.of(SCMinigameModifiers.FREEZE_ON_MISS)))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_RIVER);
    }

    public static FishProperties.Builder overworldLakeFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LAKE);
    }

    public static FishProperties.Builder overworldOceanFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_ALL_OCEANS);
    }

    public static FishProperties.Builder overworldWarmOceanFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN);
    }

    public static FishProperties.Builder overworldDeepOceanFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN);
    }

    public static FishProperties.Builder overworldRiverFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER);
    }

    public static FishProperties.Builder overworldBeachFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BEACH);
    }

    public static FishProperties.Builder overworldMushroomFieldsFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS);
    }

    public static FishProperties.Builder overworldBambooJungleFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_BAMBOO_JUNGLE);
    }

    public static FishProperties.Builder overworldJungleFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE);
    }

    public static FishProperties.Builder overworldTaigaFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_TAIGA);
    }

    public static FishProperties.Builder overworldCherryGroveFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_CHERRY_GROVE)
                .addRestrictions(BaitRestriction.CHERRY_BAIT);
    }

    public static FishProperties.Builder overworldSwampFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .addRestrictions(BaitRestriction.MURKWATER_BAIT);
    }

    public static FishProperties.Builder overworldDarkForestFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DARK_FOREST);
    }

    public static FishProperties.Builder overworldForestFish(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_FOREST);
    }

    public static FishProperties.Builder overworldVoidFishing(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_VOID);
    }

    public static FishProperties.Builder netherVoidFishing(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.NETHER_VOID);
    }

    public static FishProperties.Builder endVoidFishing(Holder<Item> fish)
    {
        return FishProperties.builder().withFish(fish)
                .addRestrictions(FishProperties.WorldRestrictions.END_VOID);
    }

    //endregion

    public static final List<Pair<ResourceKey<FishProperties>, FishProperties>> PROPERTIES = new ArrayList<>();
    private static final List<ResourceKey<FishProperties>> COMPAT_KEYS = new ArrayList<>();

    static ResourceKey<FishProperties> createKey(FishProperties fp)
    {
        if (fp.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH))
            return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, ResourceLocation.parse(fp.catchInfo().fish().getRegisteredName()));

        return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, ResourceLocation.parse(
                fp.catchInfo().fish().getKey().location().getNamespace() + ":" +
                        fp.catchInfo().fishEntryType().name().toLowerCase(Locale.ROOT) + "_" +
                        fp.catchInfo().fish().getKey().location().getPath()
                ));
    }

    public static void registerStarcatcherBucketAndEntity(FishProperties.Builder builder)
    {
        builder.withBucketedFish(SCItems.STARCAUGHT_BUCKET);
        builder.withEntityToSpawn(U.holderEntity("starcatcher", "fish"));
        DGStarcatcherFishes.STARCATCHER_FISHES.add(builder.build());
        register(builder);
    }

    public static void registerStarcatcherOnlyEntity(FishProperties.Builder builder)
    {
        builder.withEntityToSpawn(U.holderEntity("starcatcher", "fish"));
        register(builder);
    }


    public static void register(FishProperties.Builder builder)
    {
        FishProperties properties = builder.build();
        ResourceKey<FishProperties> key = FishingPropertiesRegistry.createKey(properties);
        PROPERTIES.add(Pair.of(key, properties));
        String namespace = key.location().getNamespace();
        if (!namespace.equals("minecraft") && !namespace.equals("starcatcher"))
            COMPAT_KEYS.add(key);
    }

    public static void registerConditions(BiConsumer<ResourceKey<?>, ICondition> consumer)
    {
        for (ResourceKey<FishProperties> compatKey : COMPAT_KEYS)
        {
            //fix for hybrid aquatic as their modid is hybrid_aquatic but items use hybrid-aquatic
            if (compatKey.location().getNamespace().equals("hybrid-aquatic"))
            {
                consumer.accept(compatKey, new ModLoadedCondition("hybrid_aquatic"));
                continue;
            }
            consumer.accept(compatKey, new ModLoadedCondition(compatKey.location().getNamespace()));
        }
    }

    public static void bootstrap(BootstrapContext<FishProperties> context)
    {
        PROPERTIES.forEach(p -> context.register(p.getFirst(), p.getSecond()));
    }
}
