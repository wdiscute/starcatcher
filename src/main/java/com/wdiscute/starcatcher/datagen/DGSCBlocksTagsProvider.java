package com.wdiscute.starcatcher.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DGSCBlocksTagsProvider extends BlockTagsProvider
{

    public DGSCBlocksTagsProvider(PackOutput output,
                                  CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, Starcatcher.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        for (DeferredHolder<Block, ? extends Block> entry : SCBlocks.TACKLE_BOXES.getEntries())
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(entry.value());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(SCBlocks.AQUARIUM.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(SCBlocks.STAND.value())
                .add(SCBlocks.DISPLAY.value())
                .add(SBBlocks.SELLING_BIN.value())
        ;
    }
}
