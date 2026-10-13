package com.wdiscute.starcatcher.datagen.fish;

import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Textures;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import net.minecraft.data.worldgen.BootstapContext;

import java.util.List;

public class PresetRestrictions
{
    public static final List<AbstractFishRestriction> OVERWORLD =
            List.of(
                    DimensionRestriction.OVERWORLD
            );

    public static FishProperties empty(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty();
    }

    public static FishProperties overworldVoid(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(FluidRestriction.VOID)
                .addRestriction(ElevationRestriction.BELOW_MINUS_SIXTY_FOUR)
                ;
    }

    public static FishProperties lake(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.LAKES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties lakeMountain(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withBaseChance(10)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.LAKES)
                .addRestriction(ElevationBias.MOUNTAIN)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties coldLake(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.ICY))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.COLD_LAKES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties iceSpikes(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.ICY))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.ICE_SPIKES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties coldLakeMountain(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withBaseChance(10)
                .withTextures(Textures.DEFAULT.withTank(Textures.ICY))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.COLD_LAKES)
                .addRestriction(ElevationBias.MOUNTAIN)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties warmLake(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.WARM_LAKES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties swamp(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.SWAMPS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BaitRestriction.MURKWATER_BAIT)
                ;
    }

    public static FishProperties darkOakForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.DARK_FOREST)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties forest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.FOREST)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties cherryGrove(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.CHERRY_GROVES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BaitRestriction.CHERRY_BAIT)
                ;
    }

    public static FishProperties jungle(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.JUNGLES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties bambooJungle(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.BAMBOO_JUNGLE)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties flowerForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.FLOWER_FOREST)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties sunflowerPlains(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.SUNFLOWER_PLAINS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties river(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.RIVERS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties coldRiver(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.COLD_RIVERS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties normalOceans(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.NORMAL_OCEANS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties deepOcean(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.DEEP_OCEANS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties allOceans(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.ALL_OCEANS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties warmOcean(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.WARM_OCEANS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties frozenOceans(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.ICY))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.FROZEN_OCEAN)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties beach(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.BEACHES)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties mushroomFields(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.MUSHROOM_FIELDS)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties caves(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.CAVE))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ZERO_TO_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BiomeRestriction.UNDERGROUND)
                ;
    }

    public static FishProperties dripstoneCaves(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.CAVE))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.DRIPSTONE_CAVES)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BaitRestriction.DRIPSTONE_BAIT)
                ;
    }

    public static FishProperties lushCaves(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.CAVE))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.LUSH_CAVES)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BaitRestriction.LUSH_BAIT)
                ;
    }

    public static FishProperties deepslate(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.CAVE))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties deepDark(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.DEEP_DARK))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.DEEP_DARK)
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BaitRestriction.SCULK_BAIT)
                ;
    }

    public static FishProperties surfaceLava(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.LAVA_OVERWORLD))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(BiomeRestriction.LAKES)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties caveLava(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.LAVA_OVERWORLD))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ZERO_TO_FIFTY)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties deepslateLava(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.LAVA_OVERWORLD))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties netherLava(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties crimsonForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.CRIMSON_FOREST)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties warpedForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.WARPED_FOREST)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties netherLavaBasaltDeltas(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.BASALT_DELTAS)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties soulSandValley(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.SOUL_SAND_VALLEY)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties endAir(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.END_VOID))
                .addRestriction(DimensionRestriction.END)
                .addRestriction(FluidRestriction.AIR)
                ;
    }

    public static FishProperties endVoid(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.END_VOID))
                .addRestriction(DimensionRestriction.END)
                .addRestriction(ElevationRestriction.END_VOID)
                .addRestriction(FluidRestriction.VOID)
                ;
    }

    public static FishProperties endOuterIslandsAir(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.END_VOID))
                .addRestriction(DimensionRestriction.END)
                .addRestriction(BiomeRestriction.OUTER_ISLANDS)
                .addRestriction(FluidRestriction.AIR)
                ;
    }
}
