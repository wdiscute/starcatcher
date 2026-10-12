package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.clam.ClamFeature;
import com.wdiscute.starcatcher.datagen.fish.DGSCFishProperties;
import com.wdiscute.starcatcher.registry.SCFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.DatapackRegistryGatherer;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DGSCDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataRegistryEntriesEvent event)
    {
        //fish properties
        DatapackRegistryGatherer gatherer = event.gatherFor("minecraft", Starcatcher.MOD_ID);
        gatherer.add(Starcatcher.FISH_REGISTRY_KEY, DGSCFishProperties::bootstrap);

        //biome modifiers
        event.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DGSCBiomeModifiers::bootstrap);

        //clams feature
        event.add(Registries.FEATURE, bootstrap -> {
            bootstrap.register(
                    SCFeatures.CLAM,
                    new ClamFeature()
            );
        });

        //clams placed feature
        event.add(Registries.PLACED_FEATURE, bootstrap -> {
            HolderGetter<Feature> features =
                    bootstrap.lookup(Registries.FEATURE);

            bootstrap.register(
                    SCFeatures.CLAMS,
                    new PlacedFeature(
                            features.getOrThrow(SCFeatures.CLAM),
                            List.of()
                    )
            );
        });

        //recipes
        event.add(DGSCRecipeProvider.create());

        //block loot table
        event.add(Registries.LOOT_TABLE,
                new LootTableProvider(
                        Set.of(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(DGSCBlockLootTableProvider::new, LootContextParamSets.BLOCK)
                        )
                ));
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        DataGenerator gen = event.getGenerator();

        PackOutput output = gen.getPackOutput();

        //fp tags
        event.createProvider(DGSCFPTagsProvider::new);

        //fish models
        gen.addProvider(true, new DGSCModelProvider(output));

        //block tags
        event.createProvider(DGSCBlocksTagsProvider::new);

        //item tags
        event.createProvider(DGSCItemsTagsProvider::new);

        //advancements
        //gen.addProvider(event.includeServer(), new DGSCAdvancementProvider(output, lookupProvider, existingFileHelper));

        //loot modifiers
        event.createProvider(DGSCLootModifiers::new);

        //biome tags
        event.createProvider(DGSCBiomeTagsProvider::new);

        //data maps
        event.createProvider(DGSCDataMapsProvider::new);

        //data entries
        SCDGDataEntriesProvider.start(gen, output, event.getReloadableLookupProvider());
    }
}
