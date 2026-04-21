package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.registry.fishrestrictions.BiomeRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.DimensionRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.ElevationRestriction;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

public class DGFintasticFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {

        //
        //,------. ,--.            ,--.                      ,--.   ,--.
        //|  .---' `--' ,--,--,  ,-'  '-.  ,--,--.  ,---.  ,-'  '-. `--'  ,---.
        //|  `--,  ,--. |      \ '-.  .-' ' ,-.  | (  .-'  '-.  .-' ,--. | .--'
        //|  |`    |  | |  ||  |   |  |   \ '-'  | .-'  `)   |  |   |  | \ `--.
        //`--'     `--' `--''--'   `--'    `--`--' `----'    `--'   `--'  `---'
        //


        register(fish(U.holderItem("fintastic", "minnow_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "minnow_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "minnow"))
                .withAlwaysSpawnEntity()
                .addRestrictions(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.FIFTY_TO_HUNDRED,
                        new BiomeRestriction(
                                List.of(),
                                List.of(
                                        BiomeTags.IS_JUNGLE.location(),
                                        SCTags.IS_SWAMP,
                                        BiomeTags.IS_RIVER.location()
                                ),
                                List.of(),
                                List.of(),
                                ""
                        )
                )
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(fish(U.holderItem("fintastic", "featherback_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "featherback_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "featherback"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
        );

        register(fish(U.holderItem("fintastic", "guppy_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "guppy_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "guppy"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLES_AND_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY)
        );

        register(fish(U.holderItem("fintastic", "arapaima_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "arapaima_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "arapaima"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withRarity(FishProperties.Rarity.RARE)
        );

        register(fish(U.holderItem("fintastic", "pleco_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "pleco_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "pleco"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLES_AND_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
        );

        register(fish(U.holderItem("fintastic", "moony_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "moony_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "moony"))
                .withAlwaysSpawnEntity()
                .addRestrictions(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.FIFTY_TO_HUNDRED,
                        new BiomeRestriction(
                                List.of(
                                        Biomes.BEACH.identifier(),
                                        Biomes.MANGROVE_SWAMP.identifier()
                                ),
                                List.of(
                                        SCTags.IS_LUKEWARM_OCEAN,
                                        SCTags.IS_WARM_OCEAN
                                ),
                                List.of(),
                                List.of(),
                                ""
                        )
                )
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
        );

        register(fish(U.holderItem("fintastic", "coelacanth_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("fintastic", "coelacanth"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
        );

        register(fish(U.holderItem("fintastic", "gourami_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "gourami_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "gourami"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
        );

        register(fish(U.holderItem("fintastic", "daphnia_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "daphnia_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "daphnia"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(fish(U.holderItem("fintastic", "artemia_bucket"))
                .withBucketedFish(U.holderItem("fintastic", "artemia_bucket"))
                .withEntityToSpawn(U.holderEntity("fintastic", "artemia"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_LAKE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );
    }
}
