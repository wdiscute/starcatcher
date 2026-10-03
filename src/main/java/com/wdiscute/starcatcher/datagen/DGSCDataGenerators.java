package com.wdiscute.starcatcher.datagen;

import com.wdiscute.sellingbin.datagen.DGSBModBlockLootTableProvider;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.clam.ClamFeature;
import com.wdiscute.starcatcher.datagen.fish.DGSCFishProperties;
import com.wdiscute.starcatcher.registry.SCFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DGSCDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        DataGenerator gen = event.getGenerator();

        PackOutput output = gen.getPackOutput();
        //ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //registrysetbuilder
        RegistrySetBuilder reloadableRegistry = new RegistrySetBuilder();

        //recipe
        reloadableRegistry.add(DGSCRecipeProvider.create());

        //block loot table
        reloadableRegistry.add(
                Registries.LOOT_TABLE,
                new LootTableProvider(
                        Set.of(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(DGSCBlockLootTableProvider::new, LootContextParamSets.BLOCK)
                        )
                )
        );

        //world registry set builder
        RegistrySetBuilder worldRegistry = new RegistrySetBuilder();

        //fish properties
        worldRegistry.add(Starcatcher.FISH_REGISTRY_KEY, DGSCFishProperties::bootstrap);


        //biome modifiers
        worldRegistry.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DGSCBiomeModifiers::bootstrap);

        //features
        worldRegistry.add(Registries.FEATURE, bootstrap -> {
            bootstrap.register(
                    SCFeatures.CLAM,
                    new ClamFeature()
            );
        });

        worldRegistry.add(Registries.PLACED_FEATURE, bootstrap -> {
            HolderGetter<Feature> features =
                    bootstrap.lookup(Registries.FEATURE);

            bootstrap.register(
                    SCFeatures.CLAMS,
                    new PlacedFeature(
                            features.getOrThrow(SCFeatures.CLAM),
                            List.of(
                                    // your placement modifiers
                            )
                    )
            );
        });

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getReloadableLookupProvider();

        //fp tags
        //gen.addProvider(true, new DGSCFPTagsProvider(output, lookupProvider));

        //fish models
        gen.addProvider(true, new DGSCModelProvider(output));

        //block tags
        event.createProvider(DGSCBlocksTagsProvider::new);

        //item tags
        event.createProvider(DGSCItemsTagsProvider::new);

        //advancements
        //gen.addProvider(event.includeServer(), new DGSCAdvancementProvider(output, lookupProvider, existingFileHelper));

        //loot modifiers
        gen.addProvider(true, new DGSCLootModifiers(output, lookupProvider));

        //biome tags
        gen.addProvider(true, new DGSCBiomeTagsProvider(output, lookupProvider));

        //data maps
        gen.addProvider(true, new DGSCDataMapsProvider(output, lookupProvider));

        //data entries
        SCDGDataEntriesProvider.start(gen, output, lookupProvider);

        event.createWorldRegistryObjects(
                worldRegistry,
                Set.of(
                        "minecraft",
                        Starcatcher.MOD_ID
                )
        );

        event.createReloadableRegistryObjects(
                reloadableRegistry,
                Set.of(
                        "minecraft",
                        Starcatcher.MOD_ID,

                        "create",

                        "tide",
                        "aquaculture",
                        "fishofthieves",
                        "netherdepthsupgrade",
                        "environmental",
                        "miners_delight",
                        "crittersandcompanions",
                        "hybrid_aquatic",

                        "alexscaves",
                        "collectorsreap",
                        "sullysmod",
                        "upgrade_aquatic",
                        "spawn",
                        "unusualfishmod"
                ),
                "FishingProperties"
        );
    }
}
