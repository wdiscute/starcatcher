package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DGSCBiomeModifierProvider extends DatapackBuiltinEntriesProvider
{
    public static final RegistrySetBuilder REGISTRY = new RegistrySetBuilder().add(ForgeRegistries.Keys.BIOME_MODIFIERS, DGSCBiomeModifierProvider::bootstrap);

    public DGSCBiomeModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, REGISTRY, Set.of(Starcatcher.MOD_ID));
    }

    public static void bootstrap(BootstapContext<BiomeModifier> context)
    {
        HolderSet.Named<Biome> isOverworld = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD);
        HolderSet.Named<Biome> isNether = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_NETHER);
        HolderSet.Named<Biome> isEnd = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_END);

//        context.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Starcatcher.rl("fish_spawn_overworld")),
//                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(isOverworld,
//                new MobSpawnSettings.SpawnerData(ModEntities.FISH.get(), 10, 2, 3)));
//
//        context.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Starcatcher.rl("fish_spawn_nether")),
//                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(isNether,
//                        new MobSpawnSettings.SpawnerData(ModEntities.FISH.get(), 10, 2, 3)));
//
//        context.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Starcatcher.rl("fish_spawn_end")),
//                BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(isEnd,
//                        new MobSpawnSettings.SpawnerData(ModEntities.FISH.get(), 10, 2, 3)));
    }

    @Override
    public String getName()
    {
        return "starcatcher_fish_spawns";
    }
}
