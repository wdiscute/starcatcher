package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.datagen.FishPropertiesWithModRestriction;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record FishProperties(
        Holder<Item> fish,

        int baseChance,
        String customName,
        Rarity rarity,
        WorldRestrictions wr,
        BaitRestrictions br,
        Difficulty dif,
        Daytime daytime,
        Weather weather,
        int mustBeCaughtBelowY,
        int mustBeCaughtAboveY,
        boolean skipMinigame,
        boolean hasGuideEntry
)
{
    public static final Codec<FishProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    //mandatory
                    BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("fish").forGetter(FishProperties::fish),
                    //optional
                    Codec.INT.optionalFieldOf("base_chance", 5).forGetter(FishProperties::baseChance),
                    Codec.STRING.optionalFieldOf("custom_name", "").forGetter(FishProperties::customName),
                    Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter(FishProperties::rarity),
                    WorldRestrictions.CODEC.optionalFieldOf("world_restrictions", WorldRestrictions.DEFAULT).forGetter(FishProperties::wr),
                    BaitRestrictions.CODEC.optionalFieldOf("bait_restrictions", BaitRestrictions.DEFAULT).forGetter(FishProperties::br),
                    Difficulty.CODEC.optionalFieldOf("difficulty", Difficulty.DEFAULT).forGetter(FishProperties::dif),
                    Daytime.CODEC.optionalFieldOf("daytime", Daytime.ALL).forGetter(FishProperties::daytime),
                    Weather.CODEC.optionalFieldOf("weather", Weather.ALL).forGetter(FishProperties::weather),
                    Codec.INT.optionalFieldOf("below_y", Integer.MAX_VALUE).forGetter(FishProperties::mustBeCaughtBelowY),
                    Codec.INT.optionalFieldOf("above_y", Integer.MIN_VALUE).forGetter(FishProperties::mustBeCaughtAboveY),
                    Codec.BOOL.optionalFieldOf("skips_minigame", false).forGetter(FishProperties::skipMinigame),
                    Codec.BOOL.optionalFieldOf("has_guide_entry", true).forGetter(FishProperties::hasGuideEntry)

            ).apply(instance, FishProperties::new)
    );

    public static final Codec<List<FishProperties>> LIST_CODEC = FishProperties.CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, FishProperties> STREAM_CODEC = composite(
            ByteBufCodecs.holderRegistry(Registries.ITEM), FishProperties::fish,
            ByteBufCodecs.VAR_INT, FishProperties::baseChance,
            ByteBufCodecs.STRING_UTF8, FishProperties::customName,
            Rarity.STREAM_CODEC, FishProperties::rarity,
            WorldRestrictions.STREAM_CODEC, FishProperties::wr,
            BaitRestrictions.STREAM_CODEC, FishProperties::br,
            Difficulty.STREAM_CODEC, FishProperties::dif,
            Daytime.STREAM_CODEC, FishProperties::daytime,
            Weather.STREAM_CODEC, FishProperties::weather,
            ByteBufCodecs.VAR_INT, FishProperties::mustBeCaughtBelowY,
            ByteBufCodecs.VAR_INT, FishProperties::mustBeCaughtAboveY,
            ByteBufCodecs.BOOL, FishProperties::skipMinigame,
            ByteBufCodecs.BOOL, FishProperties::hasGuideEntry,
            FishProperties::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<FishProperties>> STREAM_CODEC_LIST = STREAM_CODEC.apply(ByteBufCodecs.list());


    public static final FishProperties DEFAULT = new FishProperties(
            ModItems.MISSINGNO,
            5,
            "",
            Rarity.COMMON,
            WorldRestrictions.DEFAULT,
            BaitRestrictions.DEFAULT,
            Difficulty.DEFAULT,
            Daytime.ALL,
            Weather.ALL,
            Integer.MAX_VALUE,
            Integer.MIN_VALUE,
            false,
            true
    );


    //region with()

    public FishPropertiesWithModRestriction withMod(String modid)
    {
        return new FishPropertiesWithModRestriction(this, modid);
    }

    public FishProperties withFish(Holder<Item> fish)
    {
        return new FishProperties(fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaseChance(int baseChance)
    {
        return new FishProperties(this.fish, baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withCustomName(String customName)
    {
        return new FishProperties(this.fish, this.baseChance, customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withRarity(Rarity rarity)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWorldRestrictions(WorldRestrictions wr)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaitRestrictions(BaitRestrictions br)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDifficulty(Difficulty dif)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDaytime(Daytime daytime)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWeather(Weather weather)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withMustBeCaughtBelowY(int mustBeCaughtBelowY)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withMustBeCaughtAboveY(int mustBeCaughtAboveY)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withSkipMinigame(boolean skipMinigame)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withHasGuideEntry(boolean hasGuideEntry)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY, this.skipMinigame, hasGuideEntry);
    }

    //endregion with()

    //region bait

    public record BaitRestrictions(
            List<ResourceLocation> correctBobber,
            List<ResourceLocation> correctBait,
            boolean consumesBait,
            int correctBaitChanceAdded,
            List<ResourceLocation> incorrectBaits,
            boolean mustHaveCorrectBait)
    {
        public static final Codec<BaitRestrictions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("correct_bobbers", List.of()).forGetter(BaitRestrictions::correctBobber),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("correct_baits", List.of()).forGetter(BaitRestrictions::correctBait),
                        Codec.BOOL.optionalFieldOf("consumes_bait", true).forGetter(BaitRestrictions::consumesBait),
                        Codec.INT.optionalFieldOf("correct_bait_chance_added", 0).forGetter(BaitRestrictions::correctBaitChanceAdded),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("incorrect_baits", List.of()).forGetter(BaitRestrictions::incorrectBaits),
                        Codec.BOOL.optionalFieldOf("must_have_correct_bait", false).forGetter(BaitRestrictions::mustHaveCorrectBait)
                ).apply(instance, BaitRestrictions::new));


        public static final StreamCodec<ByteBuf, BaitRestrictions> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), BaitRestrictions::correctBobber,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), BaitRestrictions::correctBait,
                ByteBufCodecs.BOOL, BaitRestrictions::consumesBait,
                ByteBufCodecs.INT, BaitRestrictions::correctBaitChanceAdded,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), BaitRestrictions::incorrectBaits,
                ByteBufCodecs.BOOL, BaitRestrictions::mustHaveCorrectBait,
                BaitRestrictions::new
        );

        public static final BaitRestrictions DEFAULT = new BaitRestrictions(
                List.of(),
                List.of(),
                true,
                0,
                List.of(),
                false);

        public static final BaitRestrictions CHERRY_BAIT = new BaitRestrictions(
                List.of(),
                List.of(ModItems.CHERRY_BAIT.getId()),
                true,
                15,
                List.of(),
                false);

        public static final BaitRestrictions LUSH_BAIT = new BaitRestrictions(
                List.of(),
                List.of(ModItems.LUSH_BAIT.getId()),
                true,
                15,
                List.of(),
                false);

        public static final BaitRestrictions SCULK_BAIT = new BaitRestrictions(
                List.of(),
                List.of(ModItems.SCULK_BAIT.getId()),
                true,
                15,
                List.of(),
                false);

        public static final BaitRestrictions DRIPSTONE_BAIT = new BaitRestrictions(
                List.of(),
                List.of(ModItems.DRIPSTONE_BAIT.getId()),
                true,
                15,
                List.of(),
                false);

        public static final BaitRestrictions MURKWATER_BAIT = new BaitRestrictions(
                List.of(),
                List.of(ModItems.MURKWATER_BAIT.getId()),
                true,
                15,
                List.of(),
                false);

        public static final BaitRestrictions LEGENDARY_BAIT = new BaitRestrictions(
                List.of(),
                List.of(ModItems.LEGENDARY_BAIT.getId()),
                true,
                15,
                List.of(),
                false);

        public BaitRestrictions withCorrectBobber(ResourceLocation correctBobber)
        {
            return new BaitRestrictions(List.of(correctBobber), this.correctBait, this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withCorrectBobber(List<ResourceLocation> correctBobber)
        {
            return new BaitRestrictions(correctBobber, this.correctBait, this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withCorrectBait(ResourceLocation correctBait)
        {
            return new BaitRestrictions(this.correctBobber, List.of(correctBait), this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withCorrectBait(List<ResourceLocation> correctBait)
        {
            return new BaitRestrictions(this.correctBobber, correctBait, this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withConsumesBait(boolean consumesBait)
        {
            return new BaitRestrictions(this.correctBobber, this.correctBait, consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withCorrectBaitChanceAdded(int correctBaitChanceAdded)
        {
            return new BaitRestrictions(this.correctBobber, this.correctBait, consumesBait, correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withIncorrectBaits(ResourceLocation incorrectBaits)
        {
            return new BaitRestrictions(this.correctBobber, this.correctBait, this.consumesBait, this.correctBaitChanceAdded, List.of(incorrectBaits), this.mustHaveCorrectBait);
        }

        public BaitRestrictions withIncorrectBaits(List<ResourceLocation> incorrectBaits)
        {
            return new BaitRestrictions(this.correctBobber, correctBait, this.consumesBait, this.correctBaitChanceAdded, incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withMustHaveCorrectBait(boolean mustHaveCorrectBait)
        {
            return new BaitRestrictions(this.correctBobber, correctBait, this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, mustHaveCorrectBait);
        }

    }

    //endregion bait

    //region world
    public record WorldRestrictions(
            List<ResourceLocation> dims,
            List<ResourceLocation> dimsBlacklist,
            List<ResourceLocation> biomes,
            List<ResourceLocation> biomesTags,
            List<ResourceLocation> biomesBlacklist,
            List<ResourceLocation> biomesBlacklistTags,
            List<ResourceLocation> fluids
    )
    {
        public static final Codec<WorldRestrictions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("dimensions", List.of()).forGetter(WorldRestrictions::dims),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("dimensions_blacklist", List.of()).forGetter(WorldRestrictions::dimsBlacklist),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes", List.of()).forGetter(WorldRestrictions::biomes),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_tags", List.of()).forGetter(WorldRestrictions::biomesTags),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_blacklist", List.of()).forGetter(WorldRestrictions::biomesBlacklist),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_blacklist_tags", List.of()).forGetter(WorldRestrictions::biomesBlacklistTags),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("fluids", List.of(ResourceLocation.withDefaultNamespace("water"))).forGetter(WorldRestrictions::fluids)
                ).apply(instance, WorldRestrictions::new));


        public static final StreamCodec<ByteBuf, WorldRestrictions> STREAM_CODEC = composite(
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::dims,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::dimsBlacklist,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomes,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesTags,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesBlacklist,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesBlacklistTags,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::fluids,
                WorldRestrictions::new
        );

        public static final WorldRestrictions DEFAULT = new WorldRestrictions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(ResourceLocation.withDefaultNamespace("water")));


        public static final WorldRestrictions OVERWORLD =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location());

        public static final WorldRestrictions OVERWORLD_LUSH_CAVES =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.LUSH_CAVES.location());

        public static final WorldRestrictions OVERWORLD_DRIPSTONE_CAVES =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.DRIPSTONE_CAVES.location());

        public static final WorldRestrictions OVERWORLD_DEEP_DARK =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.DEEP_DARK.location());

        public static final WorldRestrictions OVERWORLD_RIVER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_RIVER);

        public static final WorldRestrictions OVERWORLD_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags((StarcatcherTags.IS_OCEAN));

        public static final WorldRestrictions OVERWORLD_WARM_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_WARM_OCEAN);

        public static final WorldRestrictions OVERWORLD_DEEP_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_DEEP_OCEAN);

        public static final WorldRestrictions OVERWORLD_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesBlacklistTags(List.of(StarcatcherTags.IS_OCEAN, StarcatcherTags.IS_RIVER));

        public static final WorldRestrictions OVERWORLD_FRESHWATER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesBlacklistTags(StarcatcherTags.IS_OCEAN);

        public static final WorldRestrictions OVERWORLD_COLD_FRESHWATER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(List.of(StarcatcherTags.IS_COLD_LAKE, StarcatcherTags.IS_COLD_RIVER));

        public static final WorldRestrictions OVERWORLD_WARM_FRESHWATER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(List.of(StarcatcherTags.IS_WARM_LAKE, StarcatcherTags.IS_WARM_RIVER));

        public static final WorldRestrictions OVERWORLD_WARM_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_WARM_LAKE);

        public static final WorldRestrictions OVERWORLD_COLD_RIVER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_RIVER);

        public static final WorldRestrictions OVERWORLD_COLD_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_OCEAN);

        public static final WorldRestrictions OVERWORLD_COLD_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_LAKE);

        public static final WorldRestrictions OVERWORLD_BEACH =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_BEACH);

        public static final WorldRestrictions OVERWORLD_MUSHROOM_FIELDS =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_MUSHROOM_FIELDS);

        public static final WorldRestrictions OVERWORLD_JUNGLE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(BiomeTags.IS_JUNGLE.location());

        public static final WorldRestrictions OVERWORLD_TAIGA =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(BiomeTags.IS_TAIGA.location());

        public static final WorldRestrictions OVERWORLD_CHERRY_GROVE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_CHERRY_GROVE);

        public static final WorldRestrictions OVERWORLD_SWAMP =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_SWAMP);

        public static final WorldRestrictions OVERWORLD_DARK_FOREST =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_DARK_FOREST);

        public static final WorldRestrictions OVERWORLD_LAVA =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions NETHER_LAVA =
                WorldRestrictions.DEFAULT
                        .withDims(Level.NETHER.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions END =
                WorldRestrictions.DEFAULT
                        .withDims(Level.END.location());

        public WorldRestrictions withDims(ResourceLocation dims)
        {
            return new WorldRestrictions(List.of(dims), this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withDims(List<ResourceLocation> dims)
        {
            return new WorldRestrictions(dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withDimsBlacklist(ResourceLocation dimsBlacklist)
        {
            return new WorldRestrictions(this.dims, List.of(dimsBlacklist), this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withDimsBlacklist(List<ResourceLocation> dimsBlacklist)
        {
            return new WorldRestrictions(this.dims, dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomes(ResourceLocation biome)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, List.of(biome), this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomes(List<ResourceLocation> biomes)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomesTags(ResourceLocation biomesTag)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, List.of(biomesTag), this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomesTags(List<ResourceLocation> biomesTags)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomesBlacklist(List<ResourceLocation> biomesBlacklist)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, biomesBlacklist, this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomesBlacklist(ResourceLocation biomesBlacklist)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, List.of(biomesBlacklist), this.biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomesBlacklistTags(List<ResourceLocation> biomesBlacklistTags)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, biomesBlacklistTags, this.fluids);
        }

        public WorldRestrictions withBiomesBlacklistTags(ResourceLocation biomesBlacklistTags)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, List.of(biomesBlacklistTags), this.fluids);
        }

        public WorldRestrictions withFluids(List<ResourceLocation> fluids)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, fluids);
        }

        public WorldRestrictions withFluids(ResourceLocation fluids)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, List.of(fluids));
        }

    }

    //endregion world


    //region treasure

    public record Treasure(
            boolean hasTreasure,
            ResourceLocation loot,
            int hitReward
    )
    {

        public static final Codec<Treasure> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.optionalFieldOf("has_treasure", false).forGetter(Treasure::hasTreasure),
                        ResourceLocation.CODEC.optionalFieldOf("loot", Starcatcher.rl("none")).forGetter(Treasure::loot),
                        Codec.INT.optionalFieldOf("hit_reward", 0).forGetter(Treasure::hitReward)
                ).apply(instance, Treasure::new));

        public static final StreamCodec<ByteBuf, Treasure> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, Treasure::hasTreasure,
                ResourceLocation.STREAM_CODEC, Treasure::loot,
                ByteBufCodecs.INT, Treasure::hitReward,
                Treasure::new
        );

        public static final Treasure DEFAULT = new Treasure(
                false,
                Starcatcher.rl("waterlogged_satchel"),
                15
        );

        public static final Treasure UNCOMMON = new Treasure(
                true,
                Starcatcher.rl("waterlogged_satchel"),
                15
        );

        public static final Treasure HARD = new Treasure(
                true,
                Starcatcher.rl("treasure"),
                15
        );


        public static final Treasure NETHER = new Treasure(
                true,
                Starcatcher.rl("scalding_treasure"),
                28
        );

    }

    //endregion treasure


    //region dif

    public record Difficulty(
            int speed,
            int reward,
            int rewardThin,
            int penalty,
            int decay,
            boolean hasFirstMarker,
            boolean hasSecondMarker,
            boolean hasFirstThinMarker,
            boolean hasSecondThinMarker,
            Treasure treasure,
            boolean changeRotationOnEveryHit,
            boolean isVanishing
    )
    {

        public static final Difficulty DEFAULT = new Difficulty(
                9,
                20,
                0,
                6,
                1,
                true,
                true,
                false,
                false,
                Treasure.DEFAULT,
                true,
                false
        );

        public static final Difficulty VANISHING_EASY = new Difficulty(
                9,
                20,
                0,
                6,
                1,
                true,
                true,
                false,
                false,
                Treasure.DEFAULT,
                true,
                true
        );

        public static final Difficulty MEDIUM = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                true,
                false,
                true,
                false,
                Treasure.UNCOMMON,
                true,
                false
        );


        public static final Difficulty VANISHING_MEDIUM = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                true,
                false,
                true,
                false,
                Treasure.UNCOMMON,
                true,
                true
        );



        public static final Difficulty HARD = new Difficulty(
                12,
                15,
                35,
                25,
                2,
                true,
                false,
                true,
                false,
                Treasure.HARD,
                true,
                false
        );

        public static final Difficulty HARD_ONLY_THIN = new Difficulty(
                9,
                15,
                20,
                25,
                2,
                false,
                false,
                true,
                true,
                Treasure.HARD,
                true,
                false
        );

        public static final Difficulty THIN_NO_DECAY = new Difficulty(
                9,
                0,
                15,
                30,
                0,
                false,
                false,
                true,
                true,
                Treasure.HARD,
                false,
                false
        );

        public static final Difficulty THIN_NO_DECAY_NOT_FORGIVING = new Difficulty(
                9,
                0,
                15,
                999,
                0,
                false,
                false,
                true,
                true,
                Treasure.HARD,
                false,
                false
        );

        public static final Difficulty SINGLE_BIG_FAST_NO_DECAY = new Difficulty(
                15,
                5,
                0,
                15,
                0,
                true,
                false,
                false,
                false,
                Treasure.HARD,
                false,
                false
        );

        public static final Difficulty VANISHING_SINGLE_BIG_FAST_NO_DECAY = new Difficulty(
                15,
                5,
                0,
                15,
                0,
                true,
                false,
                false,
                false,
                Treasure.HARD,
                false,
                true
        );

        public static final Difficulty SINGLE_BIG_FAST = new Difficulty(
                15,
                5,
                0,
                15,
                2,
                true,
                false,
                false,
                false,
                Treasure.HARD,
                false,
                false
        );

        public static final Difficulty EVERYTHING = new Difficulty(
                12,
                15,
                30,
                15,
                3,
                true,
                true,
                true,
                true,
                Treasure.HARD,
                false,
                false
        );

        public static final Difficulty EVERYTHING_FLIP = new Difficulty(
                12,
                15,
                30,
                15,
                3,
                true,
                true,
                true,
                true,
                Treasure.HARD,
                true,
                false
        );

        public static final Difficulty NON_STOP_ACTION = new Difficulty(
                15,
                18,
                30,
                0,
                10,
                true,
                true,
                false,
                false,
                Treasure.HARD,
                false,
                false
        );

        public Difficulty withTreasure(Treasure treasure)
        {
            return new Difficulty(this.speed, this.reward, this.rewardThin, this.penalty, this.decay, this.hasFirstMarker, this.hasSecondMarker, this.hasFirstThinMarker, this.hasSecondThinMarker, treasure, this.changeRotationOnEveryHit, this.isVanishing);
        }

        public Difficulty withVanishing(boolean vanishing)
        {
            return new Difficulty(this.speed, this.reward, this.rewardThin, this.penalty, this.decay, this.hasFirstMarker, this.hasSecondMarker, this.hasFirstThinMarker, this.hasSecondThinMarker, this.treasure, this.changeRotationOnEveryHit, vanishing);
        }

        public static final Codec<Difficulty> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.optionalFieldOf("speed", DEFAULT.speed).forGetter(Difficulty::speed),
                        Codec.INT.optionalFieldOf("reward", DEFAULT.reward).forGetter(Difficulty::reward),
                        Codec.INT.optionalFieldOf("reward_thin", DEFAULT.rewardThin).forGetter(Difficulty::rewardThin),
                        Codec.INT.optionalFieldOf("penalty", DEFAULT.penalty).forGetter(Difficulty::penalty),
                        Codec.INT.optionalFieldOf("decay", DEFAULT.decay).forGetter(Difficulty::decay),
                        Codec.BOOL.optionalFieldOf("has_first_marker", DEFAULT.hasFirstMarker).forGetter(Difficulty::hasFirstMarker),
                        Codec.BOOL.optionalFieldOf("has_second_marker", DEFAULT.hasSecondMarker).forGetter(Difficulty::hasSecondMarker),
                        Codec.BOOL.optionalFieldOf("has_first_thin_marker", DEFAULT.hasFirstThinMarker).forGetter(Difficulty::hasFirstThinMarker),
                        Codec.BOOL.optionalFieldOf("has_second_thin_marker", DEFAULT.hasSecondThinMarker).forGetter(Difficulty::hasSecondThinMarker),
                        Treasure.CODEC.optionalFieldOf("treasure", Treasure.DEFAULT).forGetter(Difficulty::treasure),
                        Codec.BOOL.optionalFieldOf("change_rotation_every_hit", DEFAULT.changeRotationOnEveryHit).forGetter(Difficulty::changeRotationOnEveryHit),
                        Codec.BOOL.optionalFieldOf("is_vanishing", DEFAULT.isVanishing).forGetter(Difficulty::isVanishing)
                ).apply(instance, Difficulty::new));


        public static final StreamCodec<ByteBuf, Difficulty> STREAM_CODEC = composite(
                ByteBufCodecs.INT, Difficulty::speed,
                ByteBufCodecs.INT, Difficulty::reward,
                ByteBufCodecs.INT, Difficulty::rewardThin,
                ByteBufCodecs.INT, Difficulty::penalty,
                ByteBufCodecs.INT, Difficulty::decay,
                ByteBufCodecs.BOOL, Difficulty::hasFirstMarker,
                ByteBufCodecs.BOOL, Difficulty::hasSecondMarker,
                ByteBufCodecs.BOOL, Difficulty::hasFirstThinMarker,
                ByteBufCodecs.BOOL, Difficulty::hasSecondThinMarker,
                Treasure.STREAM_CODEC, Difficulty::treasure,
                ByteBufCodecs.BOOL, Difficulty::changeRotationOnEveryHit,
                ByteBufCodecs.BOOL, Difficulty::isVanishing,
                Difficulty::new
        );
    }

    //endregion dif

    public enum Rarity implements StringRepresentable
    {
        COMMON("common"),
        UNCOMMON("uncommon"),
        RARE("rare"),
        EPIC("epic"),
        LEGENDARY("legendary");

        public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
        public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);
        private final String key;

        Rarity(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }
    }

    public enum Daytime implements StringRepresentable
    {
        ALL("all"),
        DAY("day"),
        NOON("noon"),
        NIGHT("night"),
        MIDNIGHT("midnight");

        public static final Codec<Daytime> CODEC = StringRepresentable.fromEnum(Daytime::values);
        public static final StreamCodec<FriendlyByteBuf, Daytime> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Daytime.class);
        private final String key;

        Daytime(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }
    }

    public enum Weather implements StringRepresentable
    {
        ALL("all"),
        CLEAR("clear"),
        RAIN("rain"),
        THUNDER("thunder");

        public static final Codec<Weather> CODEC = StringRepresentable.fromEnum(Weather::values);
        public static final StreamCodec<FriendlyByteBuf, Weather> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Weather.class);
        private final String key;

        Weather(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }
    }

    public static List<ResourceLocation> getBiomesAsList(FishProperties fp, Level level)
    {
        level.registryAccess().registry(Registries.BIOME);

        List<ResourceLocation> rls = new ArrayList<>();

        for (ResourceLocation rl : fp.wr.biomesTags)
        {
            TagKey<Biome> biomeBeingChecked = TagKey.create(Registries.BIOME, rl);

            Optional<HolderSet.Named<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(biomeBeingChecked);

            if (optional.isPresent())
            {
                for (Holder<Biome> biomeHolder : optional.get())
                {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for (ResourceLocation rl : fp.wr.biomes)
        {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if (optional.isPresent()) if (!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }

    public static List<ResourceLocation> getBiomesBlacklistAsList(FishProperties fp, Level level)
    {
        level.registryAccess().registry(Registries.BIOME);

        List<ResourceLocation> rls = new ArrayList<>();

        for (ResourceLocation rl : fp.wr.biomesBlacklistTags)
        {
            TagKey<Biome> biomeBeingChecked = TagKey.create(Registries.BIOME, rl);

            Optional<HolderSet.Named<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(biomeBeingChecked);

            if (optional.isPresent())
            {
                for (Holder<Biome> biomeHolder : optional.get())
                {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for (ResourceLocation rl : fp.wr.biomesBlacklist)
        {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if (optional.isPresent()) if (!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }

    public static List<FishProperties> getFPs(Level level)
    {
        return getFPs(level.registryAccess());
    }

    public static List<FishProperties> getFPs(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY).stream().toList();
    }

    public static int getChance(FishProperties fp, Entity entity, ItemStack rod)
    {

        Level level = entity.level();

        int chance = fp.baseChance();

        ItemStack bobber = rod.get(ModDataComponents.BOBBER).stack().copy();
        ItemStack bait = rod.get(ModDataComponents.BAIT).stack().copy();

        //dimension  check
        if (!fp.wr.dims.isEmpty() && !fp.wr().dims().contains(level.dimension().location()))
            return 0;

        if (fp.wr.dimsBlacklist.contains(level.dimension().location()))
            return 0;

        //biome check
        List<ResourceLocation> biomes = getBiomesAsList(fp, level);
        List<ResourceLocation> blacklist = getBiomesBlacklistAsList(fp, level);
        ResourceLocation currentBiome = level.getBiome(entity.blockPosition()).getKey().location();

        if (!biomes.isEmpty() && !biomes.contains(currentBiome))
            return 0;

        if (!blacklist.isEmpty() && blacklist.contains(currentBiome))
            return 0;

        //fluid check
        boolean fluid = fp.wr.fluids.contains(BuiltInRegistries.FLUID.getKey(getSource(level.getFluidState(entity.blockPosition()).getType())));
        boolean fluidAbove = fp.wr.fluids.contains(BuiltInRegistries.FLUID.getKey(getSource(level.getFluidState(entity.blockPosition().above()).getType())));
        boolean fluidBelow = fp.wr.fluids.contains(BuiltInRegistries.FLUID.getKey(getSource(level.getFluidState(entity.blockPosition().below()).getType())));

        if (!fluid && !fluidAbove && !fluidBelow && entity instanceof FishingBobEntity)
            return 0;

        //blacklisted baits
        if (fp.br().incorrectBaits().contains(BuiltInRegistries.ITEM.getKey(bait.getItem())))
        {
            return 0;
        }

        //y level check
        if (entity.position().y > fp.mustBeCaughtBelowY())
        {
            return 0;
        }

        //y level check
        if (entity.position().y < fp.mustBeCaughtAboveY())
        {
            return 0;
        }

        //time check
        if (fp.daytime() != Daytime.ALL)
        {

            //TODO change 24000 to the fraction of level day cycle
            long time = level.getDayTime() % 24000;

            switch (fp.daytime())
            {
                case Daytime.DAY:
                    if (!(time > 23000 || time < 12700)) return 0;
                    break;

                case Daytime.NOON:
                    if (!(time > 3500 && time < 8500)) return 0;
                    break;

                case Daytime.NIGHT:
                    if (!(time < 23000 && time > 12700)) return 0;
                    break;

                case Daytime.MIDNIGHT:
                    if (!(time > 16500 && time < 19500)) return 0;
                    break;
            }
        }

        if(!bait.is(ModItems.METEOROLOGICAL_BAIT))
        {
            //clear check
            if (fp.weather() == Weather.CLEAR && (level.getRainLevel(0) > 0.5 || level.getThunderLevel(0) > 0.5))
            {
                return 0;
            }

            //rain check
            if (fp.weather() == Weather.RAIN && level.getRainLevel(0) < 0.5)
            {
                return 0;
            }

            //thunder check
            if (fp.weather() == Weather.THUNDER && level.getThunderLevel(0) < 0.5)
            {
                return 0;
            }
        }

        //correct bait check
        if (fp.br().mustHaveCorrectBait() && !fp.br().correctBait().contains(BuiltInRegistries.ITEM.getKey(bait.getItem())))
        {
            return 0;
        }

        //correct bait chance bonus
        if (fp.br().correctBait().contains(BuiltInRegistries.ITEM.getKey(bait.getItem())))
        {
            chance += fp.br().correctBaitChanceAdded();
        }

        //correct bobber check
        if (!fp.br().correctBobber().isEmpty() && !fp.br().correctBobber().contains(BuiltInRegistries.ITEM.getKey(bobber.getItem())))
        {
            return 0;
        }

        return chance;
    }

    public static List<FishProperties> getFpsWithGuideEntryForArea(Entity entity)
    {
        List<FishProperties> list = new ArrayList<>();

        for (FishProperties fp : entity.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
            if (getChance(fp, entity, new ItemStack(ModItems.ROD.get())) > 0 && fp.hasGuideEntry)
                list.add(fp);

        return list;
    }

    public static Fluid getSource(Fluid fluid1)
    {
        if (fluid1 instanceof FlowingFluid fluid)
        {
            return fluid.getSource();
        }

        return fluid1;
    }


    //region composite

    static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                T11 t11 = codec11.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
                codec11.encode(encode, getter11.apply(apply));
            }
        };
    }


    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                T11 t11 = codec11.decode(decode);
                T12 t12 = codec12.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
                codec11.encode(encode, getter11.apply(apply));
                codec12.encode(encode, getter12.apply(apply));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13,
            final Function<C, T13> getter13,
            final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> factory
    )
    {
        return new StreamCodec<B, C>()
        {
            @Override
            public C decode(B decode)
            {
                T1 t1 = codec1.decode(decode);
                T2 t2 = codec2.decode(decode);
                T3 t3 = codec3.decode(decode);
                T4 t4 = codec4.decode(decode);
                T5 t5 = codec5.decode(decode);
                T6 t6 = codec6.decode(decode);
                T7 t7 = codec7.decode(decode);
                T8 t8 = codec8.decode(decode);
                T9 t9 = codec9.decode(decode);
                T10 t10 = codec10.decode(decode);
                T11 t11 = codec11.decode(decode);
                T12 t12 = codec12.decode(decode);
                T13 t13 = codec13.decode(decode);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
            }

            @Override
            public void encode(B encode, C apply)
            {
                codec1.encode(encode, getter1.apply(apply));
                codec2.encode(encode, getter2.apply(apply));
                codec3.encode(encode, getter3.apply(apply));
                codec4.encode(encode, getter4.apply(apply));
                codec5.encode(encode, getter5.apply(apply));
                codec6.encode(encode, getter6.apply(apply));
                codec7.encode(encode, getter7.apply(apply));
                codec8.encode(encode, getter8.apply(apply));
                codec9.encode(encode, getter9.apply(apply));
                codec10.encode(encode, getter10.apply(apply));
                codec11.encode(encode, getter11.apply(apply));
                codec12.encode(encode, getter12.apply(apply));
                codec13.encode(encode, getter13.apply(apply));
            }
        };
    }

    //endregion composite

}
