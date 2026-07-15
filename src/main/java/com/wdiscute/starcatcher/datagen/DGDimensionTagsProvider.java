package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.fish.DGSCFishProperties;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DGDimensionTagsProvider// extends TagsProvider<LevelStem>
{
    public DGDimensionTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
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
