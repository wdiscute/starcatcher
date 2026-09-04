package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;

public class DGSCBiomeModifiers
{

    public static final ResourceKey<BiomeModifier> CLAMS =
            ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Starcatcher.rl("clams"));

    public static final ResourceKey<PlacedFeature> CLAMS_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE, Starcatcher.rl("clams"));


    public static void bootstrap(BootstapContext<BiomeModifier> context)
    {
        HolderGetter<Biome> biomes =
                context.lookup(Registries.BIOME);

        HolderGetter<PlacedFeature> placedFeatures =
                context.lookup(Registries.PLACED_FEATURE);

        context.register(
                CLAMS,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        HolderSet.direct(
                                biomes.getOrThrow(
                                        Biomes.BEACH
                                )
                        ),

                        // Placed feature to add
                        HolderSet.direct(
                                placedFeatures.getOrThrow(
                                        CLAMS_PLACED
                                )
                        ),

                        // Generation step
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
    }
}
