package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.fish.DGSCFishProperties;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DGSCDataGenerators
{
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Starcatcher.FISH_REGISTRY_KEY, DGSCFishProperties::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, DGSCBiomeModifiers::bootstrap)
            ;

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        PackOutput output = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //fish properties
        DGSCFishProperties provider = new DGSCFishProperties(output, event.getLookupProvider());
        CompletableFuture<HolderLookup.Provider> lookupProvider = provider.getRegistryProvider();
        gen.addProvider(event.includeServer(), provider);

        //fp tags
        gen.addProvider(event.includeServer(), new DGSCFPTagsProvider(output, lookupProvider, existingFileHelper));

        //fish models
        gen.addProvider(event.includeServer(), new DGSCItemModelProvider(output, existingFileHelper));

        //block tags
        BlockTagsProvider btp = new DGSCBlocksTagsProvider(output, lookupProvider, existingFileHelper);
        gen.addProvider(event.includeServer(), btp);

        //item tags
        ItemTagsProvider itp = new DGSCItemsTagsProvider(output, lookupProvider, btp.contentsGetter(), existingFileHelper);
        gen.addProvider(event.includeServer(), itp);

        //advancements
        //gen.addProvider(event.includeServer(), new DGSCAdvancementProvider(output, lookupProvider, existingFileHelper));

        //loot modifiers
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

        //data entries
        SCDGDataEntriesProvider.start(gen, output, event.includeServer());

    }
}
