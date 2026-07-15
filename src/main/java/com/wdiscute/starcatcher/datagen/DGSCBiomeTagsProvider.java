package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DGSCBiomeTagsProvider extends BiomeTagsProvider
{
    public DGSCBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, Starcatcher.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(create(SCTags.IS_BEACH))
                .addOptional(BiomeTags.IS_BEACH.location())
                .addOptionalTag(Tags.Biomes.IS_BEACH)
        ;

        this.tag(create(SCTags.IS_WARPED_FOREST))
                .addOptional(Biomes.WARPED_FOREST.location())
        ;

        this.tag(create(SCTags.IS_SOUL_SAND_VALLEY))
                .addOptional(Biomes.SOUL_SAND_VALLEY.location())
        ;

        this.tag(create(SCTags.IS_BASALT_DELTAS))
                .addOptional(Biomes.BASALT_DELTAS.location())
        ;

        this.tag(create(SCTags.IS_CRIMSON_FOREST))
                .addOptional(Biomes.CRIMSON_FOREST.location())
        ;

        this.tag(create(SCTags.IS_BIRCH_FOREST))
                .addOptional(Biomes.BIRCH_FOREST.location())
                .addOptional(Biomes.OLD_GROWTH_BIRCH_FOREST.location())
                .addOptionalTag(Tags.Biomes.IS_BIRCH_FOREST)
        ;

        this.tag(create(SCTags.IS_CHERRY_GROVE))
                .addOptional(Biomes.CHERRY_GROVE.location());

        this.tag(create(SCTags.IS_COLD_LAKE))
                .addOptional(Biomes.SNOWY_TAIGA.location())
                .addOptional(Biomes.SNOWY_BEACH.location())
                .addOptional(Biomes.SNOWY_PLAINS.location())
                .addOptional(Biomes.SNOWY_SLOPES.location())
                .addOptional(Biomes.ICE_SPIKES.location())
                .addOptional(Biomes.FROZEN_PEAKS.location())
                .addOptional(Biomes.JAGGED_PEAKS.location())
        ;

        this.tag(create(SCTags.IS_COLD_OCEAN))
                .addOptional(Biomes.COLD_OCEAN.location())
                .addOptional(Biomes.DEEP_COLD_OCEAN.location())
                .addOptional(Biomes.FROZEN_OCEAN.location())
                .addOptional(Biomes.DEEP_FROZEN_OCEAN.location())
        ;

        this.tag(create(SCTags.IS_COLD_RIVER))
                .addOptional(Biomes.FROZEN_RIVER.location())
                .addOptional(Biomes.SNOWY_BEACH.location())
        ;

        this.tag(create(SCTags.IS_DARK_FOREST))
                .addOptional(Biomes.DARK_FOREST.location())
        ;

        this.tag(create(SCTags.IS_DEEP_OCEAN))
                .addOptional(Biomes.DEEP_COLD_OCEAN.location())
                .addOptional(Biomes.DEEP_FROZEN_OCEAN.location())
                .addOptional(Biomes.DEEP_LUKEWARM_OCEAN.location())
                .addOptional(Biomes.DEEP_OCEAN.location())
                .addOptionalTag(Tags.Biomes.IS_DEEP_OCEAN)
        ;

        this.tag(create(SCTags.IS_LUKEWARM_OCEAN))
                .addOptional(Biomes.LUKEWARM_OCEAN.location())
                .addOptional(Biomes.DEEP_LUKEWARM_OCEAN.location())
        ;

        this.tag(create(SCTags.IS_MUSHROOM_FIELDS))
                .addOptional(Biomes.MUSHROOM_FIELDS.location())
                .addOptionalTag(Tags.Biomes.IS_MUSHROOM.location())
        ;

        this.tag(create(SCTags.IS_NORMAL_OCEAN))
                .addOptional(Biomes.OCEAN.location())
                .addOptional(Biomes.DEEP_OCEAN.location())
        ;

        this.tag(create(SCTags.IS_OCEAN))
                .addOptionalTag(BiomeTags.IS_OCEAN)
                .addOptional(Utils.rl("tfc", "deep_ocean"))
                .addOptional(Utils.rl("tfc", "deep_ocean_trench"))
                .addOptional(Utils.rl("tfc", "ocean"))
                .addOptional(Utils.rl("tfc", "ocean_reef"))
        ;

        this.tag(create(SCTags.IS_RIVER))
                .addOptionalTag(BiomeTags.IS_RIVER)
                .addOptionalTag(Utils.rl("tfc", "river"))
        ;

        this.tag(create(SCTags.IS_SWAMP))
                .addOptional(Biomes.SWAMP.location())
                .addOptional(Biomes.MANGROVE_SWAMP.location())
                .addOptionalTag(Tags.Biomes.IS_SWAMP)
        ;

        this.tag(create(SCTags.IS_WARM_LAKE))
                .addOptionalTag(Tags.Biomes.IS_DESERT)
                .addOptionalTag(Tags.Biomes.IS_BADLANDS)
        ;

        this.tag(create(SCTags.IS_WARM_OCEAN))
                .addOptional(Biomes.WARM_OCEAN.location())
                .addOptional(Biomes.LUKEWARM_OCEAN.location())
                .addOptional(Biomes.DEEP_LUKEWARM_OCEAN.location())
        ;

        this.tag(create(SCTags.IS_WARM_RIVER))
                .addOptional(Biomes.RIVER.location())
        ;

    }

    private static TagKey<Biome> create(ResourceLocation rl)
    {
        return TagKey.create(Registries.BIOME, rl);
    }
}
