package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        //Todo: idk what this is
/*        event.createDatapackRegistryObjects(
                new RegistrySetBuilder()
                        .add(Starcatcher.FISH_REGISTRY_KEY, FishingPropertiesRegistry::bootstrap)
        );*/

        DataGenerator gen = event.getGenerator();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput output = gen.getPackOutput();

        //fish properties
        gen.addProvider(
                event.includeServer(),
                new DGSCFishingPropertiesProvider(output, lookupProvider)
        );

        gen.addProvider(event.includeServer(), new DGSCBiomeModifierProvider(output, lookupProvider));

        //fish models
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        gen.addProvider(event.includeServer(), new DGSCItemModelProvider(output, existingFileHelper));

        //block tags
        BlockTagsProvider btp = new DGSCBlocksTagsProvider(output, lookupProvider, existingFileHelper);
        gen.addProvider(event.includeServer(), btp);

        //item tags
        ItemTagsProvider itp = new DGSCItemsTagsProvider(output, lookupProvider, btp.contentsGetter(), existingFileHelper);
        gen.addProvider(event.includeServer(), itp);

        //fp tags
        //todo figure this out
        gen.addProvider(event.includeServer(), new DGSCFPTagsProvider(output, lookupProvider, existingFileHelper));

        //advancements
        gen.addProvider(event.includeServer(), new DGSCAdvancementProvider(output, lookupProvider, existingFileHelper));

        //advancements
        gen.addProvider(event.includeServer(), new DGSCLootModifiers(output));


        //biome tags
        gen.addProvider(event.includeServer(), new DGSCBiomeTagsProvider(output, lookupProvider, existingFileHelper));

        //loot table
        gen.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(DGSCBlockLootTableProvider::new, LootContextParamSets.BLOCK))));

        //recipes
        gen.addProvider(event.includeServer(), new DGSCRecipeProvider(output));

        //data maps
        gen.addProvider(event.includeServer(), new DGSCDataMapsProvider(output, lookupProvider));

    }
}
