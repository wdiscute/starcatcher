package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

public class DGSCFPTagsProvider extends KeyTagProvider<FishProperties>
{
    public DGSCFPTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, Starcatcher.FISH_REGISTRY_KEY, lookupProvider, Starcatcher.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        FishRegistration.ALL_FISHABLE_MAP.forEach((fp, rl) ->
        {
            if(fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH))
            {
                if(fp.rarity().equals(Rarity.TRASH)) tag(SCTags.TRASH_ENTRIES_FP).addOptional(rk(rl));
                if(fp.rarity().equals(Rarity.COMMON)) tag(SCTags.COMMON_ENTRIES_FP).addOptional(rk(rl));
                if(fp.rarity().equals(Rarity.UNCOMMON)) tag(SCTags.UNCOMMON_ENTRIES_FP).addOptional(rk(rl));
                if(fp.rarity().equals(Rarity.RARE)) tag(SCTags.RARE_ENTRIES_FP).addOptional(rk(rl));
                if(fp.rarity().equals(Rarity.EPIC)) tag(SCTags.EPIC_ENTRIES_FP).addOptional(rk(rl));
                if(fp.rarity().equals(Rarity.LEGENDARY)) tag(SCTags.LEGENDARY_ENTRIES_FP).addOptional(rk(rl));
            }
        });
    }

    private static ResourceKey<FishProperties> rk(Identifier id)
    {
        return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, id);
    }

    //private static ResourceKey<FishProperties> rk(FishProperties fp)
    //{
    //    return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, BuiltInRegistries.ITEM.getKey(item));
    //}
}
