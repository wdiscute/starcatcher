package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

import java.util.Map;

import static com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry.*;

public class DGTrophies
{
    public static void bootstrap()
    {
//        register(fish(Items.BARRIER.asItem().builtInRegistryHolder())
//                .withMaxLimit(1)
//                .withDifficulty(FishProperties.Difficulty.TRASH)
//                .withHasGuideEntry(false)
//                .addRarityRestriction(
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.COMMON, 5, RarityCountRestriction.RarityCount.CountType.TOTAL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.UNCOMMON, 5, RarityCountRestriction.RarityCount.CountType.TOTAL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.RARE, 5, RarityCountRestriction.RarityCount.CountType.TOTAL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.EPIC, 5, RarityCountRestriction.RarityCount.CountType.TOTAL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 5, RarityCountRestriction.RarityCount.CountType.TOTAL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 5, RarityCountRestriction.RarityCount.CountType.TOTAL),
//
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.COMMON, 5, RarityCountRestriction.RarityCount.CountType.UNIQUE),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.UNCOMMON, 5, RarityCountRestriction.RarityCount.CountType.UNIQUE),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.RARE, 5, RarityCountRestriction.RarityCount.CountType.UNIQUE),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.EPIC, 5, RarityCountRestriction.RarityCount.CountType.UNIQUE),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 5, RarityCountRestriction.RarityCount.CountType.UNIQUE),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 5, RarityCountRestriction.RarityCount.CountType.UNIQUE),
//
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.COMMON, 5, RarityCountRestriction.RarityCount.CountType.ALL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.UNCOMMON, 5, RarityCountRestriction.RarityCount.CountType.ALL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.RARE, 5, RarityCountRestriction.RarityCount.CountType.ALL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.EPIC, 5, RarityCountRestriction.RarityCount.CountType.ALL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 5, RarityCountRestriction.RarityCount.CountType.ALL),
//                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 5, RarityCountRestriction.RarityCount.CountType.ALL)
//                        )
//                .trophy()
//        );

        register(fish(SCBlocks.TROPHY_COPPER.asItem().builtInRegistryHolder())
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withRarity(FishProperties.Rarity.COMMON)
                .addRarityRestriction(
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.COMMON, 25, RarityCountRestriction.RarityCount.CountType.TOTAL)
                )
                .trophy()
        );

        register(fish(SCBlocks.TROPHY_IRON.asItem().builtInRegistryHolder())
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withHasGuideEntry(false)
                .addRarityRestriction(
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 35, RarityCountRestriction.RarityCount.CountType.UNIQUE)
                )
                .trophy()
        );

        register(fish(SCBlocks.TROPHY_GOLD.asItem().builtInRegistryHolder())
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.RARE)
                .withHasGuideEntry(false)
                .addRarityRestriction(
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 100, RarityCountRestriction.RarityCount.CountType.TOTAL),
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 2, RarityCountRestriction.RarityCount.CountType.UNIQUE),
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 2, RarityCountRestriction.RarityCount.CountType.UNIQUE)
                )
                .trophy()
        );

        register(fish(SCBlocks.TROPHY_EMERALD.asItem().builtInRegistryHolder())
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.EPIC)
                .withHasGuideEntry(false)
                .addRarityRestriction(
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 200, RarityCountRestriction.RarityCount.CountType.TOTAL),
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 10, RarityCountRestriction.RarityCount.CountType.UNIQUE)
                )
                .trophy()
        );

        register(fish(SCBlocks.TROPHY_DIAMOND.asItem().builtInRegistryHolder())
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withHasGuideEntry(false)
                .addRarityRestriction(
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 500, RarityCountRestriction.RarityCount.CountType.TOTAL),
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 0, RarityCountRestriction.RarityCount.CountType.ALL)
                )
                .trophy()
        );

        register(fish(SCBlocks.TROPHY_OF_THE_OLDER_ANGLER.asItem().builtInRegistryHolder())
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withHasGuideEntry(false)
                .addRarityRestriction(
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 0, RarityCountRestriction.RarityCount.CountType.ALL),
                        new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 0, RarityCountRestriction.RarityCount.CountType.ALL)
                )
                .trophy()
        );


        //                                         ,--.
        // ,---.   ,---.   ,---. ,--.--.  ,---.  ,-'  '-.  ,---.
        //(  .-'  | .-. : | .--' |  .--' | .-. : '-.  .-' (  .-'
        //.-'  `) \   --. \ `--. |  |    \   --.   |  |   .-'  `)
        //`----'   `----'  `---' `--'     `----'   `--'   `----'
        //

        register(overworldSurfaceFish(SCItems.DRIFTING_WATERLOGGED_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.NONE, 10, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .secret()
        );

        register(overworldSurfaceLava(SCItems.SCALDING_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withHasGuideEntry(false)
                .secret()
        );

        register(overworldSurfaceLava(SCItems.BURNING_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 2, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withHasGuideEntry(false)
                .secret()
        );

        register(overworldSurfaceFish(SCItems.HOPEFUL_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.RARE, 10, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withHasGuideEntry(false)
                .secret()
        );

        register(overworldSurfaceFish(SCItems.HOPELESS_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.RARE, 15, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withHasGuideEntry(false)
                .secret()
        );

        register(overworldSurfaceFish(SCItems.TRUE_BLUE_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.EPIC, 10, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withHasGuideEntry(false)
                .secret()
        );

        register(fish(SCItems.WITHERED_BOTTLE)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .secret()
                .withBaseChance(0)
                .addRestrictions(new BaitRestriction(Map.of(U.rl("wither_skeleton_skull"), 200), "200"))
        );

        //
        //          ,--.   ,--.
        // ,---.  ,-'  '-. |  ,---.   ,---.  ,--.--.  ,---.
        //| .-. | '-.  .-' |  .-.  | | .-. : |  .--' (  .-'
        //' '-' '   |  |   |  | |  | \   --. |  |    .-'  `)
        // `---'    `--'   `--' `--'  `----' `--'    `----'
        //

        register(overworldDeepslateFish(SCItems.AMETHYST_HOOK)
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.EPIC, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .extra()
        );

        register(overworldDeepslateFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND))
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .extra()
        );

        register(netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_SCRAP))
                .withMaxLimit(3)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withHasGuideEntry(false)
                .extra()
        );

        register(netherLavaFish(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                .withMaxLimit(1)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.LEGENDARY, 5, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .extra()
        );

        //neptunium ingot
        register(overworldOceanFish(U.holderItem("aquaculture", "neptunium_ingot"))
                .withMaxLimit(5)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .addRarityRestriction(new RarityCountRestriction.RarityCount(FishProperties.Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                .withPercentageChance(0.05f)
                .extra()
        );

        //frog
        register(overworldSwampFish(SCItems.FROG_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //naturalist
        register(overworldForestFish(SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //magma forged
        register(netherLavaBasaltDeltasFish(SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //magma forged
        register(overworldSwampFish(SCItems.SLIMED_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //bamboo rod
        register(overworldBambooJungleFish(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //obsidian rod
        register(overworldSurfaceLava(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE)
                .withWeather(WeatherRestriction.RAIN)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //boner rod
        register(netherLavaSoulSandValleyFish(SCItems.BONER_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //sky rod
        register(overworldFish(SCItems.SKY_SKIN_SMITHING_TEMPLATE)
                .addRestrictions(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.ABOVE_TWO_HUNDRED)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //lush glowberry
        register(overworldLushCavesFish(SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //humble rod
        register(overworldRiverFish(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );

        //shark rod
        register(overworldRiverFish(SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE)
                .withDifficulty(FishProperties.Difficulty.TRASH)
                .withHasGuideEntry(false)
                .withPercentageChance(0.01f)
                .extra()
        );


    }
}
