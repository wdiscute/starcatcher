package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class DGSCBiomeTagsProvider extends BiomeTagsProvider
{
    public DGSCBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, Starcatcher.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(create(SCTags.IS_BEACH))
                .addOptional(ResourceKey.create(Registries.BIOME, BiomeTags.IS_BEACH.location()))
                .addOptionalTag(Tags.Biomes.IS_BEACH)
        ;

        this.tag(create(SCTags.IS_WARPED_FOREST))
                .addOptional(Biomes.WARPED_FOREST)
        ;

        this.tag(create(SCTags.IS_SOUL_SAND_VALLEY))
                .addOptional(Biomes.SOUL_SAND_VALLEY)
        ;

        this.tag(create(SCTags.IS_BASALT_DELTAS))
                .addOptional(Biomes.BASALT_DELTAS)
        ;

        this.tag(create(SCTags.IS_CRIMSON_FOREST))
                .addOptional(Biomes.CRIMSON_FOREST)
        ;

        this.tag(create(SCTags.IS_BIRCH_FOREST))
                .addOptional(Biomes.BIRCH_FOREST)
                .addOptional(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .addOptionalTag(Tags.Biomes.IS_BIRCH_FOREST)
        ;

        this.tag(create(SCTags.IS_CHERRY_GROVE))
                .addOptional(Biomes.CHERRY_GROVE);

        this.tag(create(SCTags.IS_COLD_LAKE))
                .addOptional(Biomes.SNOWY_TAIGA)
                .addOptional(Biomes.SNOWY_BEACH)
                .addOptional(Biomes.SNOWY_PLAINS)
                .addOptional(Biomes.SNOWY_SLOPES)
                .addOptional(Biomes.ICE_SPIKES)
                .addOptional(Biomes.FROZEN_PEAKS)
                .addOptional(Biomes.JAGGED_PEAKS)
        ;

        this.tag(create(SCTags.IS_COLD_OCEAN))
                .addOptional(Biomes.COLD_OCEAN)
                .addOptional(Biomes.DEEP_COLD_OCEAN)
                .addOptional(Biomes.FROZEN_OCEAN)
                .addOptional(Biomes.DEEP_FROZEN_OCEAN)
        ;

        this.tag(create(SCTags.IS_COLD_RIVER))
                .addOptional(Biomes.FROZEN_RIVER)
                .addOptional(Biomes.SNOWY_BEACH)
        ;

        this.tag(create(SCTags.IS_DARK_FOREST))
                .addOptional(Biomes.DARK_FOREST)
        ;

        this.tag(create(SCTags.IS_DEEP_OCEAN))
                .addOptional(Biomes.DEEP_COLD_OCEAN)
                .addOptional(Biomes.DEEP_FROZEN_OCEAN)
                .addOptional(Biomes.DEEP_LUKEWARM_OCEAN)
                .addOptional(Biomes.DEEP_OCEAN)
                .addOptionalTag(Tags.Biomes.IS_DEEP_OCEAN)
        ;

        this.tag(create(SCTags.IS_LUKEWARM_OCEAN))
                .addOptional(Biomes.LUKEWARM_OCEAN)
                .addOptional(Biomes.DEEP_LUKEWARM_OCEAN)
        ;

        this.tag(create(SCTags.IS_MUSHROOM_FIELDS))
                .addOptional(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(Tags.Biomes.IS_MUSHROOM)
        ;

        this.tag(create(SCTags.IS_NORMAL_OCEAN))
                .addOptional(Biomes.OCEAN)
                .addOptional(Biomes.DEEP_OCEAN)
        ;

        this.tag(create(SCTags.IS_OCEAN))
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptional(ResourceKey.create(Registries.BIOME, Utils.rl("tfc", "deep_ocean")))
                .addOptional(ResourceKey.create(Registries.BIOME, Utils.rl("tfc", "deep_ocean_trench")))
                .addOptional(ResourceKey.create(Registries.BIOME, Utils.rl("tfc", "ocean")))
                .addOptional(ResourceKey.create(Registries.BIOME, Utils.rl("tfc", "ocean_reef")))
        ;

        this.tag(create(SCTags.IS_RIVER))
                .addOptionalTag(BiomeTags.IS_RIVER)
                .addOptionalTag(create(Utils.rl("tfc", "river")))
        ;

        this.tag(create(SCTags.IS_SWAMP))
                .addOptional(Biomes.SWAMP)
                .addOptional(Biomes.MANGROVE_SWAMP)
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
        ;

        this.tag(create(SCTags.IS_WARM_LAKE))
                .addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_BADLANDS)
        ;

        this.tag(create(SCTags.IS_WARM_OCEAN))
                .addOptional(Biomes.WARM_OCEAN)
                .addOptional(Biomes.LUKEWARM_OCEAN)
                .addOptional(Biomes.DEEP_LUKEWARM_OCEAN)
        ;

        this.tag(create(SCTags.IS_WARM_RIVER))
                .addOptional(Biomes.RIVER)
        ;

    }

    private static TagKey<Biome> create(Identifier rl)
    {
        return TagKey.create(Registries.BIOME, rl);
    }
}
