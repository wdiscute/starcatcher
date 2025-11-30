package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.compat.EclipticSeasonsCompat;
import com.wdiscute.starcatcher.compat.SereneSeasonsCompat;
import com.wdiscute.starcatcher.datagen.FishPropertiesWithModRestriction;
import com.wdiscute.starcatcher.registry.ModItems;
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
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//      <><|    <- fish
public record FishProperties(
        Holder<Item> fish,
        int baseChance,
        String customName,

        SizeAndWeight sw,
        Rarity rarity,
        WorldRestrictions wr,
        BaitRestrictions br,
        Difficulty dif,
        Daytime daytime,
        Weather weather,
        boolean skipMinigame,
        boolean hasGuideEntry
) {
    public static final Codec<FishProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("fish").forGetter(FishProperties::fish),
                    Codec.INT.fieldOf("base_chance").forGetter(FishProperties::baseChance),
                    Codec.STRING.fieldOf("custom_name").forGetter(FishProperties::customName),
                    SizeAndWeight.CODEC.fieldOf("size_and_weight").forGetter(FishProperties::sw),
                    Rarity.CODEC.fieldOf("rarity").forGetter(FishProperties::rarity),
                    WorldRestrictions.CODEC.fieldOf("world_restrictions").forGetter(FishProperties::wr),
                    BaitRestrictions.CODEC.fieldOf("bait_restrictions").forGetter(FishProperties::br),
                    Difficulty.CODEC.fieldOf("difficulty").forGetter(FishProperties::dif),
                    Daytime.CODEC.fieldOf("daytime").forGetter(FishProperties::daytime),
                    Weather.CODEC.fieldOf("weather").forGetter(FishProperties::weather),
                    Codec.BOOL.fieldOf("skips_minigame").forGetter(FishProperties::skipMinigame),
                    Codec.BOOL.fieldOf("has_guide_entry").forGetter(FishProperties::hasGuideEntry)

            ).apply(instance, FishProperties::new)
    );

    public static final Codec<List<FishProperties>> LIST_CODEC = FishProperties.CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, FishProperties> STREAM_CODEC = ExtraComposites.composite(
            ByteBufCodecs.holderRegistry(Registries.ITEM), FishProperties::fish,
            ByteBufCodecs.VAR_INT, FishProperties::baseChance,
            ByteBufCodecs.STRING_UTF8, FishProperties::customName,
            SizeAndWeight.STREAM_CODEC, FishProperties::sw,
            Rarity.STREAM_CODEC, FishProperties::rarity,
            WorldRestrictions.STREAM_CODEC, FishProperties::wr,
            BaitRestrictions.STREAM_CODEC, FishProperties::br,
            Difficulty.STREAM_CODEC, FishProperties::dif,
            Daytime.STREAM_CODEC, FishProperties::daytime,
            Weather.STREAM_CODEC, FishProperties::weather,
            ByteBufCodecs.BOOL, FishProperties::skipMinigame,
            ByteBufCodecs.BOOL, FishProperties::hasGuideEntry,
            FishProperties::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<FishProperties>> STREAM_CODEC_LIST = STREAM_CODEC.apply(ByteBufCodecs.list());


    public static final FishProperties DEFAULT = new FishProperties(
            ModItems.MISSINGNO,
            5,
            "",
            SizeAndWeight.DEFAULT,
            Rarity.COMMON,
            WorldRestrictions.DEFAULT,
            BaitRestrictions.DEFAULT,
            Difficulty.DEFAULT,
            Daytime.ALL,
            Weather.ALL,
            false,
            true
    );


    //region with()

    public FishPropertiesWithModRestriction withMod(String modid) {
        return new FishPropertiesWithModRestriction(this, modid);
    }

    public FishProperties withFish(Holder<Item> fish) {
        return new FishProperties(fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaseChance(int baseChance) {
        return new FishProperties(this.fish, baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withCustomName(String customName) {
        return new FishProperties(this.fish, this.baseChance, customName, this.sw, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withSizeAndWeight(SizeAndWeight sizeAndWeight) {
        return new FishProperties(this.fish, this.baseChance, this.customName, sizeAndWeight, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withRarity(Rarity rarity) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWorldRestrictions(WorldRestrictions wr) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaitRestrictions(BaitRestrictions br) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDifficulty(Difficulty dif) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDaytime(Daytime daytime) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, this.dif, daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWeather(Weather weather) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, this.dif, this.daytime, weather, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withSkipMinigame(boolean skipMinigame) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withHasGuideEntry(boolean hasGuideEntry) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr, this.br, this.dif, this.daytime, this.weather, this.skipMinigame, hasGuideEntry);
    }

    public FishProperties withSeasons(WorldRestrictions.Seasons... seasons) {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.sw, this.rarity, this.wr.withSeasons(seasons), this.br, this.dif, this.daytime, this.weather, this.skipMinigame, this.hasGuideEntry);
    }

    //endregion with()

    //region bait

    public record BaitRestrictions(
            List<ResourceLocation> correctBobber,
            List<ResourceLocation> correctBait,
            boolean consumesBait,
            int correctBaitChanceAdded,
            List<ResourceLocation> incorrectBaits,
            boolean mustHaveCorrectBait) {
        public static final Codec<BaitRestrictions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(ResourceLocation.CODEC).fieldOf("correct_bobbers").forGetter(BaitRestrictions::correctBobber),
                        Codec.list(ResourceLocation.CODEC).fieldOf("correct_baits").forGetter(BaitRestrictions::correctBait),
                        Codec.BOOL.fieldOf("consumes_bait").forGetter(BaitRestrictions::consumesBait),
                        Codec.INT.fieldOf("correct_bait_chance_added").forGetter(BaitRestrictions::correctBaitChanceAdded),
                        Codec.list(ResourceLocation.CODEC).fieldOf("incorrect_baits").forGetter(BaitRestrictions::incorrectBaits),
                        Codec.BOOL.fieldOf("must_have_correct_bait").forGetter(BaitRestrictions::mustHaveCorrectBait)
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

        public static final BaitRestrictions LEGENDARY_BAIT_VOIDBITER = new BaitRestrictions(
                List.of(),
                List.of(ModItems.LEGENDARY_BAIT.getId()),
                true,
                50,
                List.of(),
                false);

        public BaitRestrictions withCorrectBobber(ResourceLocation... correctBobber) {
            return new BaitRestrictions(List.of(correctBobber), this.correctBait, this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withCorrectBait(ResourceLocation... correctBait) {
            return new BaitRestrictions(this.correctBobber, List.of(correctBait), this.consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withConsumesBait(boolean consumesBait) {
            return new BaitRestrictions(this.correctBobber, this.correctBait, consumesBait, this.correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withCorrectBaitChanceAdded(int correctBaitChanceAdded) {
            return new BaitRestrictions(this.correctBobber, this.correctBait, consumesBait, correctBaitChanceAdded, this.incorrectBaits, this.mustHaveCorrectBait);
        }

        public BaitRestrictions withIncorrectBaits(ResourceLocation... incorrectBaits) {
            return new BaitRestrictions(this.correctBobber, this.correctBait, this.consumesBait, this.correctBaitChanceAdded, List.of(incorrectBaits), this.mustHaveCorrectBait);
        }

        public BaitRestrictions withMustHaveCorrectBait(boolean mustHaveCorrectBait) {
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
            List<ResourceLocation> fluids,
            List<Seasons> seasons,
            int mustBeCaughtBelowY,
            int mustBeCaughtAboveY
    ) {
        public enum Seasons implements StringRepresentable {
            ALL("all"),

            SPRING("spring"),
            EARLY_SPRING("early_spring"),
            MID_SPRING("mid_spring"),
            LATE_SPRING("late_spring"),

            SUMMER("summer"),
            EARLY_SUMMER("early_summer"),
            MID_SUMMER("mid_summer"),
            LATE_SUMMER("late_summer"),

            AUTUMN("autumn"),
            EARLY_AUTUMN("early_autumn"),
            MID_AUTUMN("mid_autumn"),
            LATE_AUTUMN("late_autumn"),

            WINTER("winter"),
            EARLY_WINTER("early_winter"),
            MID_WINTER("mid_winter"),
            LATE_WINTER("late_winter");

            public static final Codec<FishProperties.WorldRestrictions.Seasons> CODEC = StringRepresentable.fromEnum(FishProperties.WorldRestrictions.Seasons::values);
            public static final Codec<List<FishProperties.WorldRestrictions.Seasons>> LIST_CODEC = Seasons.CODEC.listOf();
            public static final StreamCodec<RegistryFriendlyByteBuf, FishProperties.WorldRestrictions.Seasons> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(FishProperties.WorldRestrictions.Seasons.class);
            public static final StreamCodec<RegistryFriendlyByteBuf, List<FishProperties.WorldRestrictions.Seasons>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
            private final String key;

            Seasons(String key) {
                this.key = key;
            }

            public String getSerializedName() {
                return this.key;
            }

        }


        public static final WorldRestrictions DEFAULT = new WorldRestrictions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(ResourceLocation.withDefaultNamespace("water")),
                List.of(Seasons.ALL),
                Integer.MAX_VALUE,
                Integer.MIN_VALUE);

        public static final Codec<WorldRestrictions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(ResourceLocation.CODEC).fieldOf("dimensions").forGetter(WorldRestrictions::dims),
                        Codec.list(ResourceLocation.CODEC).fieldOf("dimensions_blacklist").forGetter(WorldRestrictions::dimsBlacklist),
                        Codec.list(ResourceLocation.CODEC).fieldOf("biomes").forGetter(WorldRestrictions::biomes),
                        Codec.list(ResourceLocation.CODEC).fieldOf("biomes_tags").forGetter(WorldRestrictions::biomesTags),
                        Codec.list(ResourceLocation.CODEC).fieldOf("biomes_blacklist").forGetter(WorldRestrictions::biomesBlacklist),
                        Codec.list(ResourceLocation.CODEC).fieldOf("biomes_blacklist_tags").forGetter(WorldRestrictions::biomesBlacklistTags),
                        Codec.list(ResourceLocation.CODEC).fieldOf("fluids").forGetter(WorldRestrictions::fluids),
                        Seasons.LIST_CODEC.fieldOf("seasons").forGetter(WorldRestrictions::seasons),
                        Codec.INT.fieldOf("below_y").forGetter(WorldRestrictions::mustBeCaughtBelowY),
                        Codec.INT.fieldOf("above_y").forGetter(WorldRestrictions::mustBeCaughtAboveY)
                ).apply(instance, WorldRestrictions::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WorldRestrictions> STREAM_CODEC = ExtraComposites.composite(
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::dims,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::dimsBlacklist,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomes,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesTags,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesBlacklist,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesBlacklistTags,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::fluids,
                Seasons.LIST_STREAM_CODEC, WorldRestrictions::seasons,
                ByteBufCodecs.VAR_INT, WorldRestrictions::mustBeCaughtBelowY,
                ByteBufCodecs.VAR_INT, WorldRestrictions::mustBeCaughtAboveY,
                WorldRestrictions::new
        );

        public static final WorldRestrictions OVERWORLD =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location());

        public static final WorldRestrictions OVERWORLD_LUSH_CAVES =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.LUSH_CAVES.location())
                        .withMustBeCaughtBelowY(50);

        public static final WorldRestrictions OVERWORLD_CAVES =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.LUSH_CAVES.location())
                        .withMustBeCaughtBelowY(50)
                        .withMustBeCaughtAboveY(0);

        public static final WorldRestrictions OVERWORLD_DEEPSLATE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withMustBeCaughtBelowY(0);

        public static final WorldRestrictions OVERWORLD_DRIPSTONE_CAVES =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.DRIPSTONE_CAVES.location())
                        .withMustBeCaughtBelowY(50)
                        .withMustBeCaughtAboveY(0);

        public static final WorldRestrictions OVERWORLD_DEEP_DARK =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomes(Biomes.DEEP_DARK.location())
                        .withMustBeCaughtBelowY(50);

        public static final WorldRestrictions OVERWORLD_RIVER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_RIVER)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags((StarcatcherTags.IS_OCEAN))
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_WARM_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_WARM_OCEAN)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_DEEP_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_DEEP_OCEAN)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesBlacklistTags(StarcatcherTags.IS_OCEAN, StarcatcherTags.IS_RIVER)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_FRESHWATER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesBlacklistTags(StarcatcherTags.IS_OCEAN)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_COLD_FRESHWATER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_LAKE, StarcatcherTags.IS_COLD_RIVER)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_WARM_FRESHWATER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_WARM_LAKE, StarcatcherTags.IS_WARM_RIVER)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_WARM_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_WARM_LAKE)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_COLD_RIVER =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_RIVER)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_COLD_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_OCEAN)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_COLD_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_LAKE)
                        .withMustBeCaughtAboveY(50)
                        .withMustBeCaughtBelowY(100);

        public static final WorldRestrictions OVERWORLD_COLD_MOUNTAIN =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_COLD_LAKE)
                        .withMustBeCaughtAboveY(100);

        public static final WorldRestrictions OVERWORLD_BEACH =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_BEACH)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_MUSHROOM_FIELDS =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_MUSHROOM_FIELDS)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_JUNGLE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(BiomeTags.IS_JUNGLE.location())
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_TAIGA =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(BiomeTags.IS_TAIGA.location())
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_CHERRY_GROVE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_CHERRY_GROVE)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_SWAMP =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_SWAMP)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_DARK_FOREST =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withBiomesTags(StarcatcherTags.IS_DARK_FOREST)
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_LAVA_SURFACE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"))
                        .withMustBeCaughtAboveY(50);

        public static final WorldRestrictions OVERWORLD_LAVA_UNDERGROUND =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"))
                        .withMustBeCaughtBelowY(50);

        public static final WorldRestrictions OVERWORLD_UNDERGROUND =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withMustBeCaughtBelowY(50);

        public static final WorldRestrictions OVERWORLD_LAVA_DEEPSLATE =
                WorldRestrictions.DEFAULT
                        .withDims(Level.OVERWORLD.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"))
                        .withMustBeCaughtBelowY(0);

        public static final WorldRestrictions NETHER_LAVA =
                WorldRestrictions.DEFAULT
                        .withDims(Level.NETHER.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions NETHER_LAVA_CRIMSON_FOREST =
                WorldRestrictions.DEFAULT
                        .withDims(Level.NETHER.location())
                        .withBiomes(Biomes.CRIMSON_FOREST.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions NETHER_LAVA_WARPED_FOREST =
                WorldRestrictions.DEFAULT
                        .withDims(Level.NETHER.location())
                        .withBiomes(Biomes.WARPED_FOREST.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions NETHER_LAVA_SOUL_SAND_VALLEY =
                WorldRestrictions.DEFAULT
                        .withDims(Level.NETHER.location())
                        .withBiomes(Biomes.SOUL_SAND_VALLEY.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions NETHER_LAVA_BASALT_DELTAS =
                WorldRestrictions.DEFAULT
                        .withDims(Level.NETHER.location())
                        .withBiomes(Biomes.BASALT_DELTAS.location())
                        .withFluids(ResourceLocation.withDefaultNamespace("lava"));

        public static final WorldRestrictions END =
                WorldRestrictions.DEFAULT
                        .withDims(Level.END.location());

        public static final WorldRestrictions END_OUTER_ISLANDS =
                WorldRestrictions.DEFAULT
                        .withDims(Level.END.location())
                        .withBiomesTags(BiomeTags.IS_END.location())
                        .withBiomesBlacklist(Biomes.THE_END.location());

        public WorldRestrictions withDims(ResourceLocation... dims) {
            return new WorldRestrictions(List.of(dims), this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withDimsBlacklist(ResourceLocation... dimsBlacklist) {
            return new WorldRestrictions(this.dims, List.of(dimsBlacklist), this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withBiomes(ResourceLocation... biome) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, List.of(biome), this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withBiomesTags(ResourceLocation... biomesTag) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, List.of(biomesTag), this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withBiomesBlacklist(ResourceLocation... biomesBlacklist) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, List.of(biomesBlacklist), this.biomesBlacklistTags, this.fluids, this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withBiomesBlacklistTags(ResourceLocation... biomesBlacklistTags) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, List.of(biomesBlacklistTags), this.fluids, this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withFluids(ResourceLocation... fluids) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, List.of(fluids), this.seasons, this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withSeasons(Seasons... seasons) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, Arrays.stream(seasons).toList(), this.mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withMustBeCaughtBelowY(int mustBeCaughtBelowY) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, this.seasons, mustBeCaughtBelowY, this.mustBeCaughtAboveY);
        }

        public WorldRestrictions withMustBeCaughtAboveY(int mustBeCaughtAboveY) {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags, this.fluids, this.seasons, this.mustBeCaughtBelowY, mustBeCaughtAboveY);
        }

    }

    //endregion world


    //region treasure

    public record Treasure(
            boolean hasTreasure,
            ResourceLocation loot,
            int hitReward
    ) {

        public static final Codec<Treasure> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.fieldOf("has_treasure").forGetter(Treasure::hasTreasure),
                        ResourceLocation.CODEC.fieldOf("loot").forGetter(Treasure::loot),
                        Codec.INT.fieldOf("hit_reward").forGetter(Treasure::hitReward)
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
            Markers markers,
            Treasure treasure,
            Extras extras
    ) {
        public record Markers(boolean first, boolean second, boolean firstThin, boolean secondThin) {
            public static final Markers DEFAULT = new Markers(true, true, false, false);
            public static final Markers TTFF = new Markers(true, true, false, false);
            public static final Markers TTTF = new Markers(true, true, true, false);
            public static final Markers TFTF = new Markers(true, false, true, false);
            public static final Markers FFTT = new Markers(false, false, true, true);
            public static final Markers TFFF = new Markers(true, false, false, false);
            public static final Markers TTTT = new Markers(true, true, true, true);

            public static final Codec<Markers> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.BOOL.fieldOf("has_first_marker").forGetter(Markers::first),
                            Codec.BOOL.fieldOf("has_second_marker").forGetter(Markers::second),
                            Codec.BOOL.fieldOf("has_first_thin_marker").forGetter(Markers::firstThin),
                            Codec.BOOL.fieldOf("has_second_thin_marker").forGetter(Markers::secondThin)
                    ).apply(instance, Markers::new));

            public static final StreamCodec<ByteBuf, Markers> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.BOOL, Markers::first,
                    ByteBufCodecs.BOOL, Markers::second,
                    ByteBufCodecs.BOOL, Markers::firstThin,
                    ByteBufCodecs.BOOL, Markers::secondThin,
                    Markers::new
            );

        }

        public record Extras(boolean isFlip, boolean isVanishing, boolean isMoving) {
            public static final Extras TFF = new Extras(true, false, false);
            public static final Extras FFF = new Extras(false, false, false);
            public static final Extras TTT = new Extras(true, true, true);
            public static final Extras TTF = new Extras(true, true, false);
            public static final Extras TFT = new Extras(true, false, true);
            public static final Extras FTF = new Extras(false, true, false);
            public static final Extras FFT = new Extras(false, false, true);

            public static final Extras DEFAULT = TFF;

            public static final Codec<Extras> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.BOOL.fieldOf("flips_rotation_every_hit").forGetter(Extras::isFlip),
                            Codec.BOOL.fieldOf("has_vanishing_markers").forGetter(Extras::isVanishing),
                            Codec.BOOL.fieldOf("has_moving_markers").forGetter(Extras::isMoving)
                    ).apply(instance, Extras::new));

            public static final StreamCodec<ByteBuf, Extras> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.BOOL, Extras::isFlip,
                    ByteBufCodecs.BOOL, Extras::isVanishing,
                    ByteBufCodecs.BOOL, Extras::isMoving,
                    Extras::new
            );

        }

        public static final Difficulty DEFAULT = new Difficulty(
                9,
                20,
                0,
                6,
                1,
                Markers.DEFAULT,
                Treasure.DEFAULT,
                Extras.DEFAULT
        );

        public static final Difficulty EASY_VANISHING = new Difficulty(
                9,
                20,
                0,
                6,
                1,
                Markers.DEFAULT,
                Treasure.DEFAULT,
                Extras.TTF
        );

        public static final Difficulty EASY_MOVING = new Difficulty(
                9,
                20,
                0,
                6,
                1,
                Markers.DEFAULT,
                Treasure.DEFAULT,
                Extras.TTF
        );


        public static final Difficulty REALLY_HEAVY_FISH = new Difficulty(
                10,
                1,
                0,
                6,
                0,
                Markers.TTFF,
                Treasure.DEFAULT,
                Extras.FFF
        );

        public static final Difficulty EASY_NO_FLIP_VANISHING = new Difficulty(
                9,
                20,
                0,
                6,
                1,
                Markers.DEFAULT,
                Treasure.DEFAULT,
                Extras.FTF
        );

        public static final Difficulty EASY_FAST_FISH = new Difficulty(
                13,
                15,
                0,
                6,
                2,
                Markers.TTFF,
                Treasure.DEFAULT,
                Extras.FFF
        );


        public static final Difficulty MEDIUM_FAST_FISH_VANISHING = new Difficulty(
                13,
                10,
                0,
                12,
                2,
                Markers.TTFF,
                Treasure.DEFAULT,
                Extras.FTF
        );


        public static final Difficulty MEDIUM = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                Markers.TFTF,
                Treasure.UNCOMMON,
                Extras.DEFAULT
        );

        public static final Difficulty MEDIUM_MOVING = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                Markers.TFTF,
                Treasure.UNCOMMON,
                Extras.TFT
        );

        public static final Difficulty MEDIUM_MOVING_NO_FLIP = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                Markers.TFTF,
                Treasure.UNCOMMON,
                Extras.FFT
        );

        public static final Difficulty MEDIUM_VANISHING = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                Markers.TFTF,
                Treasure.UNCOMMON,
                Extras.TTF
        );

        public static final Difficulty MEDIUM_VANISHING_MOVING = new Difficulty(
                10,
                15,
                35,
                15,
                1,
                Markers.TTTF,
                Treasure.UNCOMMON,
                Extras.TTT
        );

        public static final Difficulty HARD = new Difficulty(
                12,
                15,
                35,
                25,
                2,
                Markers.TFTF,
                Treasure.HARD,
                Extras.TFF
        );

        public static final Difficulty HARD_VANISHING = new Difficulty(
                12,
                15,
                35,
                25,
                2,
                Markers.TFTF,
                Treasure.HARD,
                Extras.TTF
        );

        public static final Difficulty HARD_MOVING = new Difficulty(
                12,
                15,
                35,
                25,
                2,
                Markers.TFTF,
                Treasure.HARD,
                Extras.TFT
        );

        public static final Difficulty FAT_CATCH = new Difficulty(
                12,
                1,
                0,
                5,
                0,
                Markers.TTFF,
                Treasure.HARD,
                Extras.FFF
        );

        public static final Difficulty HARD_ONLY_THIN = new Difficulty(
                9,
                15,
                20,
                25,
                2,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TFF
        );

        public static final Difficulty HARD_ONLY_THIN_MOVING = new Difficulty(
                9,
                15,
                20,
                25,
                2,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TFT
        );

        public static final Difficulty THIN_NO_DECAY = new Difficulty(
                9,
                0,
                15,
                30,
                0,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TFF
        );

        public static final Difficulty MOVING_THIN_NO_DECAY = new Difficulty(
                9,
                0,
                15,
                30,
                0,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TFT
        );

        public static final Difficulty VOIDBITER = new Difficulty(
                12,
                0,
                10,
                20,
                2,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TTT
        );

        public static final Difficulty THIN_NO_DECAY_NOT_FORGIVING = new Difficulty(
                9,
                0,
                15,
                999,
                0,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TFF
        );

        public static final Difficulty THIN_NO_DECAY_NOT_FORGIVING_MOVING = new Difficulty(
                9,
                0,
                15,
                999,
                0,
                Markers.FFTT,
                Treasure.HARD,
                Extras.TFT
        );

        public static final Difficulty SINGLE_BIG_FAST_NO_DECAY = new Difficulty(
                15,
                5,
                0,
                15,
                0,
                Markers.TFFF,
                Treasure.HARD,
                Extras.FFF
        );

        public static final Difficulty SINGLE_BIG_FAST_NO_DECAY_VANISHING = new Difficulty(
                15,
                5,
                0,
                15,
                0,
                Markers.TFFF,
                Treasure.HARD,
                Extras.FTF
        );

        public static final Difficulty SINGLE_BIG_FAST = new Difficulty(
                15,
                5,
                0,
                15,
                2,
                Markers.TFFF,
                Treasure.HARD,
                Extras.FFF
        );

        public static final Difficulty SINGLE_BIG_FAST_MOVING = new Difficulty(
                15,
                5,
                0,
                15,
                2,
                Markers.TFFF,
                Treasure.HARD,
                Extras.FFT
        );

        public static final Difficulty EVERYTHING = new Difficulty(
                12,
                15,
                30,
                15,
                3,
                Markers.TTTT,
                Treasure.HARD,
                Extras.FFF
        );

        public static final Difficulty EVERYTHING_VANISHING = new Difficulty(
                12,
                15,
                30,
                15,
                3,
                Markers.TTTT,
                Treasure.HARD,
                Extras.FTF
        );

        public static final Difficulty EVERYTHING_FLIP = new Difficulty(
                12,
                15,
                30,
                15,
                3,
                Markers.TTTT,
                Treasure.HARD,
                Extras.TFF
        );

        public static final Difficulty EVERYTHING_FLIP_MOVING = new Difficulty(
                12,
                15,
                30,
                15,
                3,
                Markers.TTTT,
                Treasure.HARD,
                Extras.TFT
        );

        public static final Difficulty NON_STOP_ACTION = new Difficulty(
                15,
                18,
                30,
                0,
                10,
                Markers.TTFF,
                Treasure.HARD,
                Extras.FFF
        );

        public static final Difficulty NON_STOP_ACTION_VANISHING = new Difficulty(
                15,
                18,
                30,
                0,
                10,
                Markers.TTFF,
                Treasure.HARD,
                Extras.FTF
        );

        public static final Difficulty NON_STOP_ACTION_VANISHING_MOVING = new Difficulty(
                15,
                18,
                30,
                0,
                10,
                Markers.TTFF,
                Treasure.HARD,
                Extras.FTF
        );

        public Difficulty withTreasure(Treasure treasure) {
            return new Difficulty(this.speed, this.reward, this.rewardThin, this.penalty, this.decay, this.markers, treasure, this.extras);
        }

        public Difficulty withExtras(Extras extras) {
            return new Difficulty(this.speed, this.reward, this.rewardThin, this.penalty, this.decay, this.markers, this.treasure, extras);
        }

        public Difficulty withMarkers(Markers markers) {
            return new Difficulty(this.speed, this.reward, this.rewardThin, this.penalty, this.decay, markers, this.treasure, this.extras);
        }

        public static final Codec<Difficulty> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("speed").forGetter(Difficulty::speed),
                        Codec.INT.fieldOf("hitReward").forGetter(Difficulty::reward),
                        Codec.INT.fieldOf("reward_thin").forGetter(Difficulty::rewardThin),
                        Codec.INT.fieldOf("missPenalty").forGetter(Difficulty::penalty),
                        Codec.INT.fieldOf("decay").forGetter(Difficulty::decay),
                        Markers.CODEC.fieldOf("markers").forGetter(Difficulty::markers),
                        Treasure.CODEC.fieldOf("treasure").forGetter(Difficulty::treasure),
                        Extras.CODEC.fieldOf("extras").forGetter(Difficulty::extras)
                ).apply(instance, Difficulty::new));


        public static final StreamCodec<ByteBuf, Difficulty> STREAM_CODEC = ExtraComposites.composite(
                ByteBufCodecs.INT, Difficulty::speed,
                ByteBufCodecs.INT, Difficulty::reward,
                ByteBufCodecs.INT, Difficulty::rewardThin,
                ByteBufCodecs.INT, Difficulty::penalty,
                ByteBufCodecs.INT, Difficulty::decay,
                Markers.STREAM_CODEC, Difficulty::markers,
                Treasure.STREAM_CODEC, Difficulty::treasure,
                Extras.STREAM_CODEC, Difficulty::extras,
                Difficulty::new
        );
    }

    //endregion dif

    public record SizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage, float weightDeviation,
                                int goldenChance, int goldenIncrease) {
        public static final SizeAndWeight DEFAULT = new SizeAndWeight(41f, 21f, 2001f, 701f, 11, 21);

        public static final Codec<SizeAndWeight> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("average_size_cm").forGetter(SizeAndWeight::sizeAverage),
                        Codec.FLOAT.fieldOf("deviation_size_cm").forGetter(SizeAndWeight::sizeDeviation),
                        Codec.FLOAT.fieldOf("average_weight_grams").forGetter(SizeAndWeight::weightAverage),
                        Codec.FLOAT.fieldOf("deviation_weight_grams").forGetter(SizeAndWeight::weightDeviation),
                        Codec.INT.fieldOf("golden_chance_percentage").forGetter(SizeAndWeight::goldenChance),
                        Codec.INT.fieldOf("golden_state_increase").forGetter(SizeAndWeight::goldenIncrease)
                ).apply(instance, SizeAndWeight::new));

        public static final StreamCodec<ByteBuf, SizeAndWeight> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, SizeAndWeight::sizeAverage,
                ByteBufCodecs.FLOAT, SizeAndWeight::sizeDeviation,
                ByteBufCodecs.FLOAT, SizeAndWeight::weightAverage,
                ByteBufCodecs.FLOAT, SizeAndWeight::weightDeviation,
                ByteBufCodecs.INT, SizeAndWeight::goldenChance,
                ByteBufCodecs.INT, SizeAndWeight::goldenIncrease,
                SizeAndWeight::new
        );
    }


    public enum Rarity implements StringRepresentable {
        COMMON("common", 4),
        UNCOMMON("uncommon", 8),
        RARE("rare", 12),
        EPIC("epic", 20),
        LEGENDARY("legendary", 35);

        public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
        public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);
        private final String key;
        private final int xp;

        Rarity(String key, int xp) {
            this.key = key;
            this.xp = xp;
        }

        public String getSerializedName() {
            return this.key;
        }

        public int getId() {
            return this.ordinal();
        }

        public int getXp() {
            return xp;
        }
    }

    public enum Daytime implements StringRepresentable {
        ALL("all"),
        DAY("day"),
        NOON("noon"),
        NIGHT("night"),
        MIDNIGHT("midnight");

        public static final Codec<Daytime> CODEC = StringRepresentable.fromEnum(Daytime::values);
        public static final StreamCodec<FriendlyByteBuf, Daytime> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Daytime.class);
        private final String key;

        Daytime(String key) {
            this.key = key;
        }

        public String getSerializedName() {
            return this.key;
        }
    }

    public enum Weather implements StringRepresentable {
        ALL("all"),
        CLEAR("clear"),
        RAIN("rain"),
        THUNDER("thunder");

        public static final Codec<Weather> CODEC = StringRepresentable.fromEnum(Weather::values);
        public static final StreamCodec<FriendlyByteBuf, Weather> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Weather.class);
        private final String key;

        Weather(String key) {
            this.key = key;
        }

        public String getSerializedName() {
            return this.key;
        }
    }

    public static List<ResourceLocation> getBiomesAsList(FishProperties fp, Level level) {
        level.registryAccess().registry(Registries.BIOME);

        List<ResourceLocation> rls = new ArrayList<>();

        for (ResourceLocation rl : fp.wr.biomesTags) {
            TagKey<Biome> biomeBeingChecked = TagKey.create(Registries.BIOME, rl);

            Optional<HolderSet.Named<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(biomeBeingChecked);

            if (optional.isPresent()) {
                for (Holder<Biome> biomeHolder : optional.get()) {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for (ResourceLocation rl : fp.wr.biomes) {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if (optional.isPresent()) if (!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }

    public static List<ResourceLocation> getBiomesBlacklistAsList(FishProperties fp, Level level) {
        level.registryAccess().registry(Registries.BIOME);

        List<ResourceLocation> rls = new ArrayList<>();

        for (ResourceLocation rl : fp.wr.biomesBlacklistTags) {
            TagKey<Biome> biomeBeingChecked = TagKey.create(Registries.BIOME, rl);

            Optional<HolderSet.Named<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(biomeBeingChecked);

            if (optional.isPresent()) {
                for (Holder<Biome> biomeHolder : optional.get()) {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for (ResourceLocation rl : fp.wr.biomesBlacklist) {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if (optional.isPresent()) if (!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }

    public static List<FishProperties> getFPs(Level level) {
        return getFPs(level.registryAccess());
    }

    public static List<FishProperties> getFPs(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY).stream().toList();
    }

    public static int getChance(FishProperties fp, Entity entity, ItemStack rod) {

        Level level = entity.level();

        int chance = fp.baseChance();

        ItemStack bobber = rod.get(ModDataComponents.BOBBER).stack().copy();
        ItemStack bait = rod.get(ModDataComponents.BAIT).stack().copy();


        //Serene Seasons check
        if (ModList.get().isLoaded("sereneseasons")) {
            if (!SereneSeasonsCompat.canCatch(fp, level)) return 0;
        }

        //Ecliptic Seasons check
        if (ModList.get().isLoaded("eclipticseasons")) {
            if (!EclipticSeasonsCompat.canCatch(fp, level)) return 0;
        }

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
        if (fp.br().incorrectBaits().contains(BuiltInRegistries.ITEM.getKey(bait.getItem()))) {
            return 0;
        }

        //y level check
        if (entity.position().y > fp.wr.mustBeCaughtBelowY()) {
            return 0;
        }

        //y level check
        if (entity.position().y < fp.wr.mustBeCaughtAboveY()) {
            return 0;
        }

        //time check
        if (fp.daytime() != Daytime.ALL) {

            //TODO change 24000 to the fraction of level day cycle
            long time = level.getDayTime() % 24000;

            switch (fp.daytime()) {
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

        if (!bait.is(ModItems.METEOROLOGICAL_BAIT)) {
            //clear check
            if (fp.weather() == Weather.CLEAR && (level.getRainLevel(0) > 0.5 || level.getThunderLevel(0) > 0.5)) {
                return 0;
            }

            //rain check
            if (fp.weather() == Weather.RAIN && level.getRainLevel(0) < 0.5) {
                return 0;
            }

            //thunder check
            if (fp.weather() == Weather.THUNDER && level.getThunderLevel(0) < 0.5) {
                return 0;
            }
        }

        //correct bait check
        if (fp.br().mustHaveCorrectBait() && !fp.br().correctBait().contains(BuiltInRegistries.ITEM.getKey(bait.getItem()))) {
            return 0;
        }

        //correct bait chance bonus
        if (fp.br().correctBait().contains(BuiltInRegistries.ITEM.getKey(bait.getItem()))) {
            chance += fp.br().correctBaitChanceAdded();
        }

        //correct bobber check
        if (!fp.br().correctBobber().isEmpty() && !fp.br().correctBobber().contains(BuiltInRegistries.ITEM.getKey(bobber.getItem()))) {
            return 0;
        }

        return chance;
    }

    public static List<FishProperties> getFpsWithGuideEntryForArea(Entity entity) {
        List<FishProperties> list = new ArrayList<>();

        for (FishProperties fp : entity.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
            if (getChance(fp, entity, new ItemStack(ModItems.ROD.get())) > 0 && fp.hasGuideEntry)
                list.add(fp);

        return list;
    }

    public static Fluid getSource(Fluid fluid1) {
        if (fluid1 instanceof FlowingFluid fluid) {
            return fluid.getSource();
        }

        return fluid1;
    }

    public static SizeAndWeight sw(float s, float s1, float w, float w1, int g, int g1) {
        return new SizeAndWeight(s, s1, w, w1, g, g1);
    }

}
