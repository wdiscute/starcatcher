package com.wdiscute.starcatcher.datagen.fish;

import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import net.minecraft.data.worldgen.BootstrapContext;

import java.util.List;

public class PresetRestrictions
{
    public static final List<AbstractFishRestriction> OVERWORLD =
            List.of(
                    DimensionRestriction.OVERWORLD
            );

    public static FishProperties empty(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty();
    }

    public static FishProperties overworldVoid(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(FluidRestriction.VOID)
                .addRestriction(ElevationRestriction.BELOW_MINUS_SIXTY_FOUR)
                ;
    }

    public static FishProperties lake(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.lakes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties lakeMountain(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withBaseChance(10)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.lakes())
                .addRestriction(ElevationBias.MOUNTAIN)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties coldLake(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.ICY)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldLakes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties iceSpikes(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.ICY)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.iceSpikes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties coldLakeMountain(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withBaseChance(10)
                .withTextures(FishProperties.ICY)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldLakes())
                .addRestriction(ElevationBias.MOUNTAIN)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties warmLake(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.warmLakes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties swamp(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.swamps())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties darkOakForest(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.darkForest())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties forest(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.forest())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties cherryGrove(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.cherryGroves())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties jungle(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.jungles())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties bambooJungle(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.bambooJungle())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties flowerForest(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.flowerForest())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties sunflowerPlains(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.sunflowerPlains())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties river(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.rivers())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties coldRiver(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldRivers())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties allOceans(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.allOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties deepOcean(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.deepOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties warmOcean(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.warmOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties coldOcean(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties beach(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.beaches())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties mushroomFields(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.mushroomFields())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties caves(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.CAVE)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ZERO_TO_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                .addRestriction(BiomeRestriction.underground())
                ;
    }

    public static FishProperties dripstoneCaves(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.CAVE)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.dripstoneCaves())
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties lushCaves(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.CAVE)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.lushCaves())
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties deepslate(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.CAVE)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties deepDark(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.DEEP_DARK)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.deepDark())
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties surfaceLava(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.LAVA_OVERWORLD)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(BiomeRestriction.lakes())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties caveLava(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.LAVA_OVERWORLD)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ZERO_TO_FIFTY)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties deepslateLava(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.LAVA_OVERWORLD)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties netherLava(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.NETHER)
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties crimsonForest(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.NETHER)
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.crimsonForest())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties warpedForest(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.NETHER)
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.warpedForest())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties netherLavaBasaltDeltas(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.NETHER)
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.basaltDeltas())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties soulSandValley(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.NETHER)
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.soulSandValley())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties endAir(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.END_VOID)
                .addRestriction(DimensionRestriction.END)
                .addRestriction(FluidRestriction.AIR)
                ;
    }

    public static FishProperties endVoid(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.END_VOID)
                .addRestriction(DimensionRestriction.END)
                .addRestriction(ElevationRestriction.END_VOID)
                .addRestriction(FluidRestriction.VOID)
                ;
    }

    public static FishProperties endOuterIslandsAir(BootstrapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(FishProperties.END_VOID)
                .addRestriction(DimensionRestriction.END)
                .addRestriction(BiomeRestriction.outerIslands())
                .addRestriction(FluidRestriction.AIR)
                ;
    }
}
