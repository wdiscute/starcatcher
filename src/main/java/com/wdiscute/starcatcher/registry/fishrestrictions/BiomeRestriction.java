package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.EntryOrTag;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiomeRestriction extends AbstractFishRestriction
{
    private final List<EntryOrTag<Biome>> biomes;
    private final List<EntryOrTag<Biome>> blacklist;
    private final String hover;

    public static final MapCodec<BiomeRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EntryOrTag.codec(Registries.BIOME).listOf()
                            .fieldOf("biomes")
                            .forGetter(o -> o.biomes),
                    EntryOrTag.codec(Registries.BIOME).listOf()
                            .fieldOf("blacklist")
                            .forGetter(o -> o.blacklist),
                    Codec.STRING.optionalFieldOf("hover_translation", "").forGetter(o -> o.hover),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BiomeRestriction::new));

    public BiomeRestriction(List<EntryOrTag<Biome>> biomes, List<EntryOrTag<Biome>> blacklist, String hover, String translation)
    {
        super(translation);
        this.biomes = biomes;
        this.blacklist = blacklist;
        this.hover = hover;
    }

    @Override
    public int getSortPriority()
    {
        return -50;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.BIOME;
    }

    private static final BlockPos[] BPS = {
            new BlockPos(5, 0, 0),
            new BlockPos(-5, 0, 0),
            new BlockPos(0, 0, 5),
            new BlockPos(0, 0, -5)
    };

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        Registry<Biome> registry = level.registryAccess().registryOrThrow(Registries.BIOME);

        //check biomes around the bobber
        for (int i = 0; i < 4; i++)
        {
            BlockPos offset = BPS[i];
            Holder<Biome> biome = level.getBiome(entity.blockPosition().offset(offset.getX(), offset.getY(), offset.getZ()));

            //if biomes contains biome and blacklist doesn't contain biome
            if ((biomes.isEmpty() || biomes.stream().anyMatch(o -> o.matches(biome, registry)))
                && blacklist.stream().noneMatch(o -> o.matches(biome, registry)))
                return 0;
        }

        return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.hover.biome.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.hover.biome.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.biome");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        //Biomes: ------
        if (biomes.isEmpty())
            return Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.biomes.empty");

        //
        if (biomes.size() == 1)
        {
            //single biome name / biome tag name / [hover]
            return Tooltips.resolveTagsToComponentFromTranslationKey(biomes.get(0).getTranslation("biome"));
        }
        else
        {
            return Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.hover");
        }
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (!this.hover.isEmpty()) return List.of(Tooltips.resolveTagsToComponentFromTranslationKey(this.hover));

        List<Component> list = new ArrayList<>();

        List<Component> tags = new ArrayList<>();
        List<Component> biomes = new ArrayList<>();

        tags.add(Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.biome_tags").withStyle(ChatFormatting.BOLD));
        biomes.add(Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.biome").withStyle(ChatFormatting.BOLD));

        for (EntryOrTag<Biome> eot : this.biomes)
        {
            if (eot instanceof EntryOrTag.Tag<Biome> tag)
            {
                tags.add(Tooltips.resolveTagsToComponentFromTranslationKey(tag.getTranslation("biome")));

                //adds all biomes from tag into biomes list
                Optional<HolderSet.Named<Biome>> optional = level.registryAccess().registryOrThrow(Registries.BIOME).getTag(tag.tag());
                optional.ifPresent(holders ->
                        holders.forEach(o ->
                                biomes.add(Tooltips.resolveTagsToComponentFromTranslationKey("biome." + o.getRegisteredName().replace(":", ".")))));

            }

            if (eot instanceof EntryOrTag.Entry<Biome> entry)
                biomes.add(Tooltips.resolveTagsToComponentFromTranslationKey(entry.getTranslation("biome")));
        }

        if (tags.size() > 1)
            list.addAll(tags);

        if (biomes.size() > 1)
        {
            //add empty line if there are tags
            if (!list.isEmpty())
                list.add(Component.empty());

            list.addAll(biomes);
        }

        return list;
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> list = new ArrayList<>();

        List<Component> tags = new ArrayList<>();
        List<Component> biomes = new ArrayList<>();

        tags.add(Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.blacklisted_biome_tags").withStyle(ChatFormatting.BOLD));
        biomes.add(Tooltips.resolveTagsToComponentFromTranslationKey("gui.guide.blacklisted_biomes").withStyle(ChatFormatting.BOLD));

        for (EntryOrTag<Biome> eot : blacklist)
        {
            if (eot instanceof EntryOrTag.Tag<Biome> tag)
                tags.add(Tooltips.resolveTagsToComponentFromTranslationKey(tag.getTranslation("biome")));

            if (eot instanceof EntryOrTag.Entry<Biome> entry)
                biomes.add(Tooltips.resolveTagsToComponentFromTranslationKey(entry.getTranslation("biome")));
        }

        if (tags.size() > 1)
            list.addAll(tags);

        if (biomes.size() > 1)
            list.addAll(biomes);

        return list;
    }

    public BiomeRestriction biome(ResourceLocation biome)
    {
        biomes.add(new EntryOrTag.Entry<>(ResourceKey.create(Registries.BIOME, biome)));
        return this;
    }

    public BiomeRestriction tag(ResourceLocation tag)
    {
        biomes.add(new EntryOrTag.Tag<>(TagKey.create(Registries.BIOME, tag)));
        return this;
    }

    public BiomeRestriction blacklisted(ResourceLocation biome)
    {
        blacklist.add(new EntryOrTag.Entry<>(ResourceKey.create(Registries.BIOME, biome)));
        return this;
    }

    public BiomeRestriction blacklistedTag(ResourceLocation tag)
    {
        blacklist.add(new EntryOrTag.Tag<>(TagKey.create(Registries.BIOME, tag)));
        return this;
    }

    public BiomeRestriction hover(String hover)
    {
        return new BiomeRestriction(biomes, blacklist, hover, translationOverride);
    }

    public BiomeRestriction translation(String translation)
    {
        return new BiomeRestriction(biomes, blacklist, hover, translation);
    }

    public static BiomeRestriction empty()
    {
        return new BiomeRestriction(new ArrayList<>(), new ArrayList<>(), "", "");
    }

    public static final BiomeRestriction LAKES = empty()
            .blacklistedTag(SCTags.IS_OCEAN)
            .blacklistedTag(SCTags.IS_RIVER)
            .blacklistedTag(SCTags.IS_MUSHROOM_FIELDS)
            .blacklistedTag(SCTags.IS_COLD_LAKE)
            .blacklistedTag(SCTags.IS_WARM_LAKE)
            .blacklistedTag(SCTags.IS_CHERRY_GROVE)
            .hover("gui.guide.lakes.hover")
            .translation("gui.guide.lakes");

    public static final BiomeRestriction WARM_LAKES = empty()
            .tag(SCTags.IS_WARM_LAKE);

    public static final BiomeRestriction COLD_LAKES = empty()
            .tag(SCTags.IS_COLD_LAKE);

    public static final BiomeRestriction ICE_SPIKES = empty()
            .biome(Biomes.ICE_SPIKES.location());

    public static final BiomeRestriction FLOWER_FOREST = empty()
            .biome(Biomes.FLOWER_FOREST.location());

    public static final BiomeRestriction SUNFLOWER_PLAINS = empty()
            .biome(Biomes.SUNFLOWER_PLAINS.location());

    public static final BiomeRestriction SWAMP_ONLY = empty()
            .biome(Biomes.SWAMP.location());

    public static final BiomeRestriction BAMBOO_JUNGLE = empty()
                .biome(Biomes.BAMBOO_JUNGLE.location());

    public static final BiomeRestriction LUSH_CAVES = empty()
                .biome(Biomes.LUSH_CAVES.location());

    public static final BiomeRestriction UNDERGROUND = empty()
                .blacklisted(Biomes.DRIPSTONE_CAVES.location())
                .blacklisted(Biomes.LUSH_CAVES.location())
                .blacklisted(Biomes.DEEP_DARK.location())
                .translation("gui.guide.caves")
                ;

    public static final BiomeRestriction DRIPSTONE_CAVES = empty()
                .biome(Biomes.DRIPSTONE_CAVES.location());

    public static final BiomeRestriction DEEP_DARK = empty()
                .biome(Biomes.DEEP_DARK.location());

    public static final BiomeRestriction FROZEN_OCEAN = empty()
                .tag(SCTags.IS_FROZEN_OCEAN);

    public static final BiomeRestriction ALL_OCEANS = empty()
                .tag(SCTags.IS_OCEAN);

    public static final BiomeRestriction NORMAL_OCEANS = empty()
                .tag(SCTags.IS_NORMAL_OCEAN);

    public static final BiomeRestriction LUKEWARM_OCEAN = empty()
                .tag(SCTags.IS_LUKEWARM_OCEAN);

    public static final BiomeRestriction COLD_AND_LUKEWARM_OCEAN = empty()
                .tag(SCTags.IS_LUKEWARM_OCEAN)
                .tag(SCTags.IS_FROZEN_OCEAN);

    public static final BiomeRestriction WARM_OCEANS = empty()
                .tag(SCTags.IS_WARM_OCEAN);

    public static final BiomeRestriction DEEP_OCEANS = empty()
                .tag(SCTags.IS_DEEP_OCEAN);

    public static final BiomeRestriction COLD_RIVERS = empty()
                .tag(SCTags.IS_COLD_RIVER);

    public static final BiomeRestriction RIVERS = empty()
                .tag(SCTags.IS_RIVER)
                .blacklistedTag(SCTags.IS_COLD_RIVER);

    public static final BiomeRestriction SAVANNAS = empty()
                .tag(BiomeTags.IS_SAVANNA.location());

    public static final BiomeRestriction BEACHES = empty()
                .tag(SCTags.IS_BEACH);

    public static final BiomeRestriction MUSHROOM_FIELDS = empty()
                .tag(SCTags.IS_MUSHROOM_FIELDS);

    public static final BiomeRestriction JUNGLES = empty()
                .tag(BiomeTags.IS_JUNGLE.location());

    public static final BiomeRestriction TAIGAS = empty()
                .tag(BiomeTags.IS_TAIGA.location());

    public static final BiomeRestriction CHERRY_GROVES = empty()
                .tag(SCTags.IS_CHERRY_GROVE);

    public static final BiomeRestriction JUNGLES_AND_SWAMPS = empty()
                .tag(BiomeTags.IS_JUNGLE.location())
                .tag(SCTags.IS_SWAMP);

    public static final BiomeRestriction SWAMPS = empty()
                .tag(SCTags.IS_SWAMP);

    public static final BiomeRestriction MANGROVE_SWAMP = empty()
                .biome(Biomes.MANGROVE_SWAMP.location());

    public static final BiomeRestriction DARK_FOREST = empty()
                .tag(SCTags.IS_DARK_FOREST);

    public static final BiomeRestriction FOREST = empty()
                .tag(BiomeTags.IS_FOREST.location());

    public static final BiomeRestriction LUSH_CAVES_AND_JUNGLES = empty()
                .biome(Biomes.LUSH_CAVES.location())
                .tag(BiomeTags.IS_JUNGLE.location());

    public static final BiomeRestriction CRIMSON_FOREST = empty()
                .tag(SCTags.IS_CRIMSON_FOREST);

    public static final BiomeRestriction WARPED_FOREST = empty()
                .tag(SCTags.IS_WARPED_FOREST);

    public static final BiomeRestriction SOUL_SAND_VALLEY = empty()
                .tag(SCTags.IS_SOUL_SAND_VALLEY);

    public static final BiomeRestriction BASALT_DELTAS = empty()
                .tag(SCTags.IS_BASALT_DELTAS);

    public static final BiomeRestriction OUTER_ISLANDS = empty()
                .tag(BiomeTags.IS_END.location())
                .blacklisted(Biomes.THE_END.location())
                .translation("gui.guide.outer_end_islands");

}
