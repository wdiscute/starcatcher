package com.wdiscute.starcatcher.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class DGSCDimensionTagsProvider// extends TagsProvider<LevelStem>
{
    public DGSCDimensionTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        //super(output, Registries.LEVEL_STEM, lookupProvider, Starcatcher.MOD_ID, existingFileHelper);
    }

    //@Override
    protected void addTags(HolderLookup.Provider provider)
    {
//        tag(TagKey.create(Registries.LEVEL_STEM, SCTags.IS_OVERWORLD))
//                .add(LevelStem.OVERWORLD)
//                .add(LevelStem.NETHER)
//                .add(LevelStem.END);
    }
}
