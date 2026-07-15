package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.fish.DGSCFishProperties;
import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
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
        FishRegistration.ALL_FISHABLE_MAP.forEach((fp, rl) ->
        {
            if(fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH))
            {
                if(fp.rarity().equals(Rarity.TRASH)) tag(SCTags.TRASH_ENTRIES_FP).addOptional(rl);
                if(fp.rarity().equals(Rarity.COMMON)) tag(SCTags.COMMON_ENTRIES_FP).addOptional(rl);
                if(fp.rarity().equals(Rarity.UNCOMMON)) tag(SCTags.UNCOMMON_ENTRIES_FP).addOptional(rl);
                if(fp.rarity().equals(Rarity.RARE)) tag(SCTags.RARE_ENTRIES_FP).addOptional(rl);
                if(fp.rarity().equals(Rarity.EPIC)) tag(SCTags.EPIC_ENTRIES_FP).addOptional(rl);
                if(fp.rarity().equals(Rarity.LEGENDARY)) tag(SCTags.LEGENDARY_ENTRIES_FP).addOptional(rl);
            }
        });
    }
}
