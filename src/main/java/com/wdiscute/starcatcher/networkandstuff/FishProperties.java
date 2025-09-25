package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function12;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherTags;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record FishProperties(
        ResourceLocation fish,
        int baseChance,

        String customName,
        WorldRestrictions wr,
        BaitRestrictions br,
        Difficulty dif,
        Daytime daytime,
        Weather weather,
        int mustBeCaughtBellowY,
        int mustBeCaughtAboveY,
        boolean skipMinigame,
        boolean hasGuideEntry
)
{

    public static final FishProperties DEFAULT = new FishProperties(
            Starcatcher.rl("none"),
            5,
            "",
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

    public FishProperties withFish(ResourceLocation fish)
    {
        return new FishProperties(fish, this.baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaseChance(int baseChance)
    {
        return new FishProperties(this.fish, baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withCustomName(String customName)
    {
        return new FishProperties(this.fish, this.baseChance, customName, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWorldRestrictions(WorldRestrictions wr)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withBaitRestrictions(BaitRestrictions br)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDifficulty(Difficulty dif)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withDaytime(Daytime daytime)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, this.dif, daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withWeather(Weather weather)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withMustBeCaughtBellowY(int mustBeCaughtBellowY)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, this.weather, mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withMustBeCaughtAboveY(int mustBeCaughtAboveY)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, mustBeCaughtAboveY, this.skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withSkipMinigame(boolean skipMinigame)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, skipMinigame, this.hasGuideEntry);
    }

    public FishProperties withHasGuideEntry(boolean hasGuideEntry)
    {
        return new FishProperties(this.fish, this.baseChance, this.customName, this.wr, this.br, this.dif, this.daytime, this.weather, this.mustBeCaughtBellowY, this.mustBeCaughtAboveY, this.skipMinigame, hasGuideEntry);
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
    }

    //endregion bait

    //region world
    public record WorldRestrictions(
            List<ResourceLocation> dims,
            List<ResourceLocation> dimsBlacklist,
            List<ResourceLocation> biomes,
            List<ResourceLocation> biomesTags,
            List<ResourceLocation> biomesBlacklist,
            List<ResourceLocation> biomesBlacklistTags
    )
    {
        public static final Codec<WorldRestrictions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("dimensions", List.of()).forGetter(WorldRestrictions::dims),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("dimensions_blacklist", List.of()).forGetter(WorldRestrictions::dimsBlacklist),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes", List.of()).forGetter(WorldRestrictions::biomes),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_tags", List.of()).forGetter(WorldRestrictions::biomesTags),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_blacklist", List.of()).forGetter(WorldRestrictions::biomesBlacklist),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_blacklist_tags", List.of()).forGetter(WorldRestrictions::biomesBlacklistTags)
                ).apply(instance, WorldRestrictions::new));


        public static final StreamCodec<ByteBuf, WorldRestrictions> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::dims,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::dimsBlacklist,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomes,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesTags,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesBlacklist,
                ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)), WorldRestrictions::biomesBlacklistTags,
                WorldRestrictions::new
        );

        public static final WorldRestrictions DEFAULT = new WorldRestrictions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());


        public static final WorldRestrictions OVERWORLD =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()));

        public static final WorldRestrictions OVERWORLD_LUSH_CAVES =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomes(List.of(Biomes.LUSH_CAVES.location()));

        public static final WorldRestrictions OVERWORLD_RIVER =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomesTags(List.of(StarcatcherTags.IS_RIVER));

        public static final WorldRestrictions OVERWORLD_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomesTags(List.of(StarcatcherTags.IS_OCEAN));

        public static final WorldRestrictions OVERWORLD_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomesBlacklistTags(List.of(StarcatcherTags.IS_OCEAN, StarcatcherTags.IS_RIVER));

        public static final WorldRestrictions OVERWORLD_ICY_RIVER =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomesTags(List.of(StarcatcherTags.IS_COLD_RIVER));

        public static final WorldRestrictions OVERWORLD_ICY_OCEAN =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomesTags(List.of(StarcatcherTags.IS_COLD_OCEAN));

        public static final WorldRestrictions OVERWORLD_ICY_LAKE =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.OVERWORLD.location()))
                        .withBiomesTags(List.of(StarcatcherTags.IS_COLD_LAKE));

        public static final WorldRestrictions NETHER =
                WorldRestrictions.DEFAULT
                        .withDims(List.of(Level.NETHER.location()));

        public WorldRestrictions withDims(List<ResourceLocation> dims)
        {
            return new WorldRestrictions(dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags);
        }

        public WorldRestrictions withDimsBlacklist(List<ResourceLocation> dimsBlacklist)
        {
            return new WorldRestrictions(this.dims, dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags);
        }

        public WorldRestrictions withBiomes(List<ResourceLocation> biomes)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, biomes, this.biomesTags, this.biomesBlacklist, this.biomesBlacklistTags);
        }

        public WorldRestrictions withBiomesTags(List<ResourceLocation> biomesTags)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, biomesTags, this.biomesBlacklist, this.biomesBlacklistTags);
        }

        public WorldRestrictions withBiomesBlacklist(List<ResourceLocation> biomesBlacklist)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, biomesBlacklist, this.biomesBlacklistTags);
        }

        public WorldRestrictions withBiomesBlacklistTags(List<ResourceLocation> biomesBlacklistTags)
        {
            return new WorldRestrictions(this.dims, this.dimsBlacklist, this.biomes, this.biomesTags, this.biomesBlacklist, biomesBlacklistTags);
        }

    }

    //endregion world

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
            boolean hasTreasure,
            boolean changeRotationOnEveryHit
    )
    {
        public static final Codec<Difficulty> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.optionalFieldOf("speed", 7).forGetter(Difficulty::speed),
                        Codec.INT.optionalFieldOf("reward", 15).forGetter(Difficulty::reward),
                        Codec.INT.optionalFieldOf("reward_thin", 45).forGetter(Difficulty::rewardThin),
                        Codec.INT.optionalFieldOf("penalty", 5).forGetter(Difficulty::penalty),
                        Codec.INT.optionalFieldOf("decay", 1).forGetter(Difficulty::decay),
                        Codec.BOOL.optionalFieldOf("has_first_marker", true).forGetter(Difficulty::hasFirstMarker),
                        Codec.BOOL.optionalFieldOf("has_second_marker", false).forGetter(Difficulty::hasSecondMarker),
                        Codec.BOOL.optionalFieldOf("has_first_thin_marker", true).forGetter(Difficulty::hasFirstThinMarker),
                        Codec.BOOL.optionalFieldOf("has_second_thin_marker", false).forGetter(Difficulty::hasSecondThinMarker),
                        Codec.BOOL.optionalFieldOf("has_treasure", true).forGetter(Difficulty::hasTreasure),
                        Codec.BOOL.optionalFieldOf("change_rotation_every_hit", false).forGetter(Difficulty::changeRotationOnEveryHit)
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
                ByteBufCodecs.BOOL, Difficulty::hasTreasure,
                ByteBufCodecs.BOOL, Difficulty::changeRotationOnEveryHit,
                Difficulty::new
        );

        public static final Difficulty DEFAULT = new Difficulty(
                9,
                15,
                30,
                5,
                1,
                true,
                true,
                false,
                false,
                true,
                false
        );
    }

    //endregion dif

    public enum Daytime implements StringRepresentable
    {
        ALL("all"),
        DAY("day"),
        NOON("noon"),
        NIGHT("night"),
        MIDNIGHT("midnight");

        public static final Codec<Daytime> CODEC = StringRepresentable.fromEnum(Daytime::values);
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

        ItemStack bobber = rod.get(ModDataComponents.BOBBER).copyOne();
        ItemStack bait = rod.get(ModDataComponents.BAIT).copyOne();

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


        //blacklisted baits
        if (fp.br().incorrectBaits().contains(BuiltInRegistries.ITEM.getKey(bait.getItem())))
        {
            return 0;
        }

        //y level check
        if (entity.position().y > fp.mustBeCaughtBellowY())
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

    //region codecs

    public static final Codec<FishProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    //mandatory
                    ResourceLocation.CODEC.fieldOf("fish").forGetter(FishProperties::fish),
                    Codec.INT.fieldOf("base_chance").forGetter(FishProperties::baseChance),
                    //optional
                    Codec.STRING.optionalFieldOf("customName", "").forGetter(FishProperties::customName),
                    WorldRestrictions.CODEC.optionalFieldOf("world_restrictions", WorldRestrictions.DEFAULT).forGetter(FishProperties::wr),
                    BaitRestrictions.CODEC.optionalFieldOf("bait_restrictions", BaitRestrictions.DEFAULT).forGetter(FishProperties::br),
                    Difficulty.CODEC.optionalFieldOf("difficulty", Difficulty.DEFAULT).forGetter(FishProperties::dif),
                    Daytime.CODEC.optionalFieldOf("daytime", Daytime.ALL).forGetter(FishProperties::daytime),
                    Weather.CODEC.optionalFieldOf("weather", Weather.ALL).forGetter(FishProperties::weather),
                    Codec.INT.optionalFieldOf("bellow_y", Integer.MAX_VALUE).forGetter(FishProperties::mustBeCaughtBellowY),
                    Codec.INT.optionalFieldOf("above_y", Integer.MIN_VALUE).forGetter(FishProperties::mustBeCaughtAboveY),
                    Codec.BOOL.optionalFieldOf("skips_minigame", false).forGetter(FishProperties::skipMinigame),
                    Codec.BOOL.optionalFieldOf("has_guide_entry", true).forGetter(FishProperties::hasGuideEntry)

            ).apply(instance, FishProperties::new)
    );

    public static final Codec<List<FishProperties>> LIST_CODEC = FishProperties.CODEC.listOf();

    public static final StreamCodec<ByteBuf, FishProperties> STREAM_CODEC = composite(
            ResourceLocation.STREAM_CODEC, FishProperties::fish,
            ByteBufCodecs.VAR_INT, FishProperties::baseChance,
            ByteBufCodecs.STRING_UTF8, FishProperties::customName,
            WorldRestrictions.STREAM_CODEC, FishProperties::wr,
            BaitRestrictions.STREAM_CODEC, FishProperties::br,
            Difficulty.STREAM_CODEC, FishProperties::dif,
            ByteBufCodecs.fromCodec(Daytime.CODEC), FishProperties::daytime,
            ByteBufCodecs.fromCodec(Weather.CODEC), FishProperties::weather,
            ByteBufCodecs.VAR_INT, FishProperties::mustBeCaughtBellowY,
            ByteBufCodecs.VAR_INT, FishProperties::mustBeCaughtAboveY,
            ByteBufCodecs.BOOL, FishProperties::skipMinigame,
            ByteBufCodecs.BOOL, FishProperties::hasGuideEntry,
            FishProperties::new
    );

    public static final StreamCodec<ByteBuf, List<FishProperties>> STREAM_CODEC_LIST = composite(
            ResourceLocation.STREAM_CODEC, FishProperties::fish,
            ByteBufCodecs.VAR_INT, FishProperties::baseChance,
            ByteBufCodecs.STRING_UTF8, FishProperties::customName,
            WorldRestrictions.STREAM_CODEC, FishProperties::wr,
            BaitRestrictions.STREAM_CODEC, FishProperties::br,
            Difficulty.STREAM_CODEC, FishProperties::dif,
            ByteBufCodecs.fromCodec(Daytime.CODEC), FishProperties::daytime,
            ByteBufCodecs.fromCodec(Weather.CODEC), FishProperties::weather,
            ByteBufCodecs.VAR_INT, FishProperties::mustBeCaughtBellowY,
            ByteBufCodecs.VAR_INT, FishProperties::mustBeCaughtAboveY,
            ByteBufCodecs.BOOL, FishProperties::skipMinigame,
            ByteBufCodecs.BOOL, FishProperties::hasGuideEntry,
            FishProperties::new
    ).apply(ByteBufCodecs.list());

    //endregion codecs

    //region composite

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
            public C decode(B p_330310_)
            {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                T9 t9 = codec9.decode(p_330310_);
                T10 t10 = codec10.decode(p_330310_);
                T11 t11 = codec11.decode(p_330310_);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
            }

            @Override
            public void encode(B p_332052_, C p_331912_)
            {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
                codec9.encode(p_332052_, getter9.apply(p_331912_));
                codec10.encode(p_332052_, getter10.apply(p_331912_));
                codec11.encode(p_332052_, getter11.apply(p_331912_));
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
            public C decode(B p_330310_)
            {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                T9 t9 = codec9.decode(p_330310_);
                T10 t10 = codec10.decode(p_330310_);
                T11 t11 = codec11.decode(p_330310_);
                T12 t12 = codec12.decode(p_330310_);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
            }

            @Override
            public void encode(B p_332052_, C p_331912_)
            {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
                codec9.encode(p_332052_, getter9.apply(p_331912_));
                codec10.encode(p_332052_, getter10.apply(p_331912_));
                codec11.encode(p_332052_, getter11.apply(p_331912_));
                codec12.encode(p_332052_, getter12.apply(p_331912_));
            }
        };
    }

    //endregion composite

}
