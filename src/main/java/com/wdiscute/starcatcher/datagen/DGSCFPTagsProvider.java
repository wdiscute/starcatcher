package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DGSCFPTagsProvider extends TagsProvider<FishProperties>
{
    public DGSCFPTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, Starcatcher.FISH_REGISTRY_KEY, lookupProvider, Starcatcher.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {


        FishingPropertiesRegistry.PROPERTIES.forEach(o ->
        {
            FishProperties fp = o.getSecond();
            ResourceLocation location = o.getFirst().location();
            if(fp.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH))
            {
                if(fp.rarity().equals(FishProperties.Rarity.TRASH)) tag(SCTags.TRASH_FISHES_FP).addOptional(location);
                if(fp.rarity().equals(FishProperties.Rarity.COMMON)) tag(SCTags.COMMON_FISHES_FP).addOptional(location);
                if(fp.rarity().equals(FishProperties.Rarity.UNCOMMON)) tag(SCTags.UNCOMMON_FISHES_FP).addOptional(location);
                if(fp.rarity().equals(FishProperties.Rarity.RARE)) tag(SCTags.RARE_FISHES_FP).addOptional(location);
                if(fp.rarity().equals(FishProperties.Rarity.EPIC)) tag(SCTags.EPIC_FISHES_FP).addOptional(location);
                if(fp.rarity().equals(FishProperties.Rarity.LEGENDARY)) tag(SCTags.LEGENDARY_FISHES_FP).addOptional(location);
            }
        });


    }
}
