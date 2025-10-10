package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class DataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        //fish properties
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<FishAndTrophiesPropertiesProvider>) output -> new FishAndTrophiesPropertiesProvider(output,
                       event.getLookupProvider())
        );

        BlockTagsProvider btp = new ModBlocksTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());

        event.getGenerator().addProvider(event.includeServer(), btp);

        event.getGenerator().addProvider(event.includeServer(),
                new ModItemsTagProvider(event.getGenerator().getPackOutput(),
                                event.getLookupProvider(),
                        btp.contentsGetter(),
                        event.getExistingFileHelper()));
    }


}
