package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.fish.DGSCFishProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DGSCDataGenerators
{
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Starcatcher.FISH_REGISTRY_KEY, DGSCFishProperties::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DGSCBiomeModifiers::bootstrap)
            ;

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        PackOutput output = gen.getPackOutput();
        //ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //fish properties
        DGSCFishProperties provider = new DGSCFishProperties(output, event.getLookupProvider());
        CompletableFuture<HolderLookup.Provider> lookupProvider = provider.getRegistryProvider();
        gen.addProvider(true, provider);

        //fp tags
        gen.addProvider(true, new DGSCFPTagsProvider(output, lookupProvider));

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

        //loot table
        gen.addProvider(true, new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(DGSCBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        //recipes
        event.getGenerator().addProvider(true, new DGSCRecipeProvider.Runner(output, lookupProvider));

        //data maps
        gen.addProvider(true, new DGSCDataMapsProvider(output, lookupProvider));

        //data entries
        SCDGDataEntriesProvider.start(gen, output, lookupProvider);
    }
}
