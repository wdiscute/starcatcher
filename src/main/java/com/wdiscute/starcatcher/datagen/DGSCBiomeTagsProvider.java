package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
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

    public ResourceKey<Biome> rk(Identifier rl)
    {
        return ResourceKey.create(Registries.BIOME, rl);
    }

    private static TagKey<Biome> tk(Identifier rl)
    {
        return TagKey.create(Registries.BIOME, rl);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(tk(SCTags.IS_BEACH))
                .addOptional(rk(BiomeTags.IS_BEACH.location()))
                .addOptionalTag(Tags.Biomes.IS_BEACH)
        ;

        this.tag(tk(SCTags.IS_WARPED_FOREST))
                .addOptional(rk(Biomes.WARPED_FOREST.identifier()))
        ;

        this.tag(tk(SCTags.IS_SOUL_SAND_VALLEY))
                .addOptional(rk(Biomes.SOUL_SAND_VALLEY.identifier()))
        ;

        this.tag(tk(SCTags.IS_BASALT_DELTAS))
                .addOptional(rk(Biomes.BASALT_DELTAS.identifier()))
        ;

        this.tag(tk(SCTags.IS_CRIMSON_FOREST))
                .addOptional(rk(Biomes.CRIMSON_FOREST.identifier()))
        ;

        this.tag(tk(SCTags.IS_BIRCH_FOREST))
                .addOptional(rk(Biomes.BIRCH_FOREST.identifier()))
                .addOptional(rk(Biomes.OLD_GROWTH_BIRCH_FOREST.identifier()))
                .addOptionalTag(Tags.Biomes.IS_BIRCH_FOREST)
        ;

        this.tag(tk(SCTags.IS_CHERRY_GROVE))
                .addOptional(rk(Biomes.CHERRY_GROVE.identifier()))
        ;

        this.tag(tk(SCTags.IS_COLD_LAKE))
                .addOptional(rk(Biomes.SNOWY_TAIGA.identifier()))
                .addOptional(rk(Biomes.SNOWY_BEACH.identifier()))
                .addOptional(rk(Biomes.SNOWY_PLAINS.identifier()))
                .addOptional(rk(Biomes.SNOWY_SLOPES.identifier()))
                .addOptional(rk(Biomes.ICE_SPIKES.identifier()))
                .addOptional(rk(Biomes.FROZEN_PEAKS.identifier()))
                .addOptional(rk(Biomes.JAGGED_PEAKS.identifier()))
        ;

        this.tag(tk(SCTags.IS_COLD_OCEAN))
                .addOptional(rk(Biomes.COLD_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_COLD_OCEAN.identifier()))
                .addOptional(rk(Biomes.FROZEN_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_FROZEN_OCEAN.identifier()))
        ;

        this.tag(tk(SCTags.IS_COLD_RIVER))
                .addOptional(rk(Biomes.FROZEN_RIVER.identifier()))
                .addOptional(rk(Biomes.SNOWY_BEACH.identifier()))
        ;

        this.tag(tk(SCTags.IS_DARK_FOREST))
                .addOptional(rk(Biomes.DARK_FOREST.identifier()))
        ;

        this.tag(tk(SCTags.IS_DEEP_OCEAN))
                .addOptional(rk(Biomes.DEEP_COLD_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_FROZEN_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_LUKEWARM_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_OCEAN.identifier()))
                .addOptionalTag(Tags.Biomes.IS_DEEP_OCEAN)
        ;

        this.tag(tk(SCTags.IS_LUKEWARM_OCEAN))
                .addOptional(rk(Biomes.LUKEWARM_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_LUKEWARM_OCEAN.identifier()))
        ;

        this.tag(tk(SCTags.IS_MUSHROOM_FIELDS))
                .addOptional(rk(Biomes.MUSHROOM_FIELDS.identifier()))
                .addOptionalTag(Tags.Biomes.IS_MUSHROOM)
        ;

        this.tag(tk(SCTags.IS_NORMAL_OCEAN))
                .addOptional(rk(Biomes.OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_OCEAN.identifier()))
        ;

        this.tag(tk(SCTags.IS_OCEAN))
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptional(rk(U.rl("tfc", "deep_ocean")))
                .addOptional(rk(U.rl("tfc", "deep_ocean_trench")))
                .addOptional(rk(U.rl("tfc", "ocean")))
                .addOptional(rk(U.rl("tfc", "ocean_reef")))
        ;

        this.tag(tk(SCTags.IS_RIVER))
                .addOptionalTag(BiomeTags.IS_RIVER)
                .addOptionalTag(tk(U.rl("tfc", "river")))
        ;

        this.tag(tk(SCTags.IS_SWAMP))
                .addOptional(rk(Biomes.SWAMP.identifier()))
                .addOptional(rk(Biomes.MANGROVE_SWAMP.identifier()))
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
        ;

        this.tag(tk(SCTags.IS_WARM_LAKE))
                .addOptionalTag(BiomeTags.IS_SAVANNA)
                .addOptionalTag(BiomeTags.HAS_DESERT_PYRAMID)
                .addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_BADLANDS)
        ;

        this.tag(tk(SCTags.IS_WARM_OCEAN))
                .addOptional(rk(Biomes.WARM_OCEAN.identifier()))
                .addOptional(rk(Biomes.LUKEWARM_OCEAN.identifier()))
                .addOptional(rk(Biomes.DEEP_LUKEWARM_OCEAN.identifier()))
        ;

        this.tag(tk(SCTags.IS_WARM_RIVER))
                .addOptional(rk(Biomes.RIVER.identifier()))
        ;

    }
}
