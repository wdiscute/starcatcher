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
                .addRestriction(BiomeRestriction.lakes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties lakeMountain(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withBaseChance(10)
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.lakes())
                .addRestriction(ElevationBias.MOUNTAIN)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties coldLake(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.ICY))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldLakes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties iceSpikes(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.ICY))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.iceSpikes())
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
                .addRestriction(BiomeRestriction.coldLakes())
                .addRestriction(ElevationBias.MOUNTAIN)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties warmLake(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.warmLakes())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties swamp(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.swamps())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties darkOakForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.darkForest())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties forest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.forest())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }


    public static FishProperties cherryGrove(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.cherryGroves())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties jungle(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.jungles())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties bambooJungle(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.bambooJungle())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties flowerForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.flowerForest())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties sunflowerPlains(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.sunflowerPlains())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties river(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.rivers())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties coldRiver(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldRivers())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties allOceans(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.allOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties deepOcean(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.deepOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties warmOcean(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.warmOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties coldOcean(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.coldOceans())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties beach(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.beaches())
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties mushroomFields(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.mushroomFields())
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
                .addRestriction(BiomeRestriction.underground())
                ;
    }

    public static FishProperties dripstoneCaves(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.CAVE))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.dripstoneCaves())
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties lushCaves(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.CAVE))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(BiomeRestriction.lushCaves())
                .addRestriction(FluidRestriction.WATER)
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
                .addRestriction(BiomeRestriction.deepDark())
                .addRestriction(ElevationRestriction.BELOW_ZERO)
                .addRestriction(FluidRestriction.WATER)
                ;
    }

    public static FishProperties surfaceLava(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.LAVA_OVERWORLD))
                .addRestriction(DimensionRestriction.OVERWORLD)
                .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                .addRestriction(BiomeRestriction.lakes())
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
                .addRestriction(BiomeRestriction.crimsonForest())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties warpedForest(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.warpedForest())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties netherLavaBasaltDeltas(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.basaltDeltas())
                .addRestriction(FluidRestriction.LAVA)
                ;
    }

    public static FishProperties soulSandValley(BootstapContext<FishProperties> context)
    {
        return FishProperties.empty()
                .withTextures(Textures.DEFAULT.withTank(Textures.NETHER))
                .addRestriction(DimensionRestriction.NETHER)
                .addRestriction(BiomeRestriction.soulSandValley())
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
                .addRestriction(BiomeRestriction.outerIslands())
                .addRestriction(FluidRestriction.AIR)
                ;
    }
}
