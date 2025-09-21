package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record FishProperties(
        ResourceLocation fish,
        int baseChance,

        WorldRestrictions wr,
        BaitRestrictions br,
        Daytime daytime,
        Weather weather,
        int mustBeCaughtBellowY,
        int mustBeCaughtAboveY,
        boolean skipMinigame,
        boolean hasGuideEntry
)
{
    public static final Codec<FishProperties> RECORD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    //mandatory
                    ResourceLocation.CODEC.fieldOf("fish").forGetter(FishProperties::fish),
                    Codec.INT.fieldOf("base_chance").forGetter(FishProperties::baseChance),
                    //optional
                    WorldRestrictions.CODEC.optionalFieldOf("world_restrictions", WorldRestrictions.DEFAULT).forGetter(FishProperties::wr),
                    BaitRestrictions.CODEC.optionalFieldOf("bait_restrictions", BaitRestrictions.DEFAULT).forGetter(FishProperties::br),
                    Daytime.CODEC.optionalFieldOf("daytime", Daytime.ALL).forGetter(FishProperties::daytime),
                    Weather.CODEC.optionalFieldOf("weather", Weather.ALL).forGetter(FishProperties::weather),
                    Codec.INT.optionalFieldOf("bellow_y", Integer.MAX_VALUE).forGetter(FishProperties::mustBeCaughtBellowY),
                    Codec.INT.optionalFieldOf("above_y", Integer.MIN_VALUE).forGetter(FishProperties::mustBeCaughtAboveY),
                    Codec.BOOL.optionalFieldOf("skips_minigame", false).forGetter(FishProperties::skipMinigame),
                    Codec.BOOL.optionalFieldOf("has_guide_entry", true).forGetter(FishProperties::hasGuideEntry)

            ).apply(instance, FishProperties::new)
    );

    public static final FishProperties DEFAULT = new FishProperties(
            Starcatcher.rl("none"),
            0,
            WorldRestrictions.DEFAULT,
            BaitRestrictions.DEFAULT,
            Daytime.ALL,
            Weather.ALL,
            0,
            0,
            false,
            false
            );


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

        public static final BaitRestrictions DEFAULT = new BaitRestrictions(
                List.of(),
                List.of(),
                true,
                0,
                List.of(),
                false);
    }

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
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes",List.of()).forGetter(WorldRestrictions::biomes),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_tags", List.of()).forGetter(WorldRestrictions::biomesTags),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_blacklist", List.of()).forGetter(WorldRestrictions::biomesBlacklist),
                        Codec.list(ResourceLocation.CODEC).optionalFieldOf("biomes_blacklist_tags", List.of()).forGetter(WorldRestrictions::biomesBlacklistTags)
                ).apply(instance, WorldRestrictions::new));

        public static final WorldRestrictions DEFAULT = new WorldRestrictions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());


    }

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

            if(optional.isPresent())
            {
                for (Holder<Biome> biomeHolder : optional.get())
                {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for(ResourceLocation rl : fp.wr.biomes)
        {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if(optional.isPresent()) if(!rls.contains(rl)) rls.add(rl);
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

            if(optional.isPresent())
            {
                for (Holder<Biome> biomeHolder : optional.get())
                {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for(ResourceLocation rl : fp.wr.biomesBlacklist)
        {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if(optional.isPresent()) if(!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }


    public static FishProperties getFishProperties(RegistryAccess registry, ItemStack is)
    {
        return getFishProperties(registry, is.getItem());
    }

    public static FishProperties getFishProperties(RegistryAccess registry, Item item)
    {
        return getFishProperties(registry, BuiltInRegistries.ITEM.getKey(item));
    }

    public static FishProperties getFishProperties(RegistryAccess registry, ResourceLocation item)
    {
        for (FishProperties fp : registry.registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            if (fp.fish == item) return fp;
        }
        return null;
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

}
