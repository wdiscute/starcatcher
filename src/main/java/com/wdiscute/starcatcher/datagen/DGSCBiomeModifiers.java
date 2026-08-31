package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DGSCBiomeModifiers
{

    public static final ResourceKey<BiomeModifier> CLAMS =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Starcatcher.rl("clams"));

    public static final ResourceKey<PlacedFeature> CLAMS_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE, Starcatcher.rl("clams"));


    public static void bootstrap(BootstrapContext<BiomeModifier> context)
    {
        HolderGetter<Biome> biomes =
                context.lookup(Registries.BIOME);

        HolderGetter<PlacedFeature> placedFeatures =
                context.lookup(Registries.PLACED_FEATURE);

        context.register(
                CLAMS,
                new BiomeModifiers.AddFeaturesBiomeModifier(
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
