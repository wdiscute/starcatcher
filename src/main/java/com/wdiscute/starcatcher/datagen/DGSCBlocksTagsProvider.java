package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DGSCBlocksTagsProvider extends BlockTagsProvider
{

    public DGSCBlocksTagsProvider(PackOutput output,
                                  CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, Starcatcher.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {

    }
}
