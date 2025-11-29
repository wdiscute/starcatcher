package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        //fish properties
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        gen.addProvider(
                event.includeServer(),
                (DataProvider.Factory<FishAndTrophiesPropertiesProvider>) output -> new FishAndTrophiesPropertiesProvider(output,
                        registries)
        );

        //item models
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        gen.addProvider(event.includeServer(), new ModItemModelProvider(output, existingFileHelper));

        //block tags
        BlockTagsProvider btp = new ModBlocksTagProvider(output, registries, existingFileHelper);
        gen.addProvider(event.includeServer(), btp);

        //item tags
        ItemTagsProvider itp = new ModItemsTagProvider(output, registries, btp.contentsGetter(), existingFileHelper);
        gen.addProvider(event.includeServer(), itp);

        gen.addProvider(event.includeServer(), new ModAdvancementProvider(output, registries, existingFileHelper));
    }
}
