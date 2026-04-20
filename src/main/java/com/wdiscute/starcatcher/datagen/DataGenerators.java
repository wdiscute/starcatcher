package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        event.createDatapackRegistryObjects(
                new RegistrySetBuilder()
                        .add(Starcatcher.FISH_REGISTRY_KEY, FishingPropertiesRegistry::bootstrap)
        );

        DataGenerator gen = event.getGenerator();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput output = gen.getPackOutput();

        //fish properties
        event.createProvider(DGSCFishingPropertiesProvider::new);


        event.createProvider(DGSCBiomeModifierProvider::new);
        //gen.addProvider(event.includeServer(), new DGSCBiomeModifierProvider(output, lookupProvider));

        //fish models
        event.createProvider(DGSCItemModelProvider::new);

        //block tags
        event.createProvider(DGSCBlocksTagsProvider::new);

        //item tags
        event.createProvider(DGSCItemsTagsProvider::new);

        //fp tags
        event.createProvider(DGSCFPTagsProvider::new);

        //advancements
        //gen.addProvider(event.includeServer(), new DGSCAdvancementProvider(output, lookupProvider, existingFileHelper));

        //loot modifiers
        event.createProvider(DGSCLootModifiers::new);

        //biome tags
        event.createProvider(DGSCBiomeTagsProvider::new);

        //loot table
        gen.addProvider(true, new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(DGSCBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        //recipes
        event.getGenerator().addProvider(true, new DGSCRecipeProvider.Runner(output, lookupProvider));

        //data maps
        event.createProvider(DGSCDataMapsProvider::new);

    }
}
