package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BiomeRestriction extends AbstractFishRestriction
{
    private final List<Identifier> biomes;
    private final List<Identifier> biomesTags;
    private final List<Identifier> biomesBlacklist;
    private final List<Identifier> biomesBlacklistTags;
    private final String hover;
    private final String translationOverride;

    public static final MapCodec<BiomeRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.listOf().fieldOf("biomes").forGetter(o -> o.biomes),
                    Identifier.CODEC.listOf().fieldOf("biomes_tags").forGetter(o -> o.biomesTags),
                    Identifier.CODEC.listOf().fieldOf("biomes_blacklist").forGetter(o -> o.biomesBlacklist),
                    Identifier.CODEC.listOf().fieldOf("biomes_blacklist_tags").forGetter(o -> o.biomesBlacklistTags),
                    Codec.STRING.optionalFieldOf("hover_translation", "").forGetter(o -> o.hover),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BiomeRestriction::new));

    public BiomeRestriction()
    {
        this.biomes = List.of();
        this.biomesTags = List.of();
        this.biomesBlacklist = List.of();
        this.biomesBlacklistTags = List.of();
        this.translationOverride = "";
        this.hover = "";
    }

    public BiomeRestriction(Identifier biome, String translationOverride)
    {
        this.biomes = List.of(biome);
        this.biomesTags = List.of();
        this.biomesBlacklist = List.of();
        this.biomesBlacklistTags = List.of();
        this.translationOverride = translationOverride;
        this.hover = "";
    }

    public BiomeRestriction(List<Identifier> biomes, List<Identifier> biomesTags, List<Identifier> biomesBlacklist, List<Identifier> biomesBlacklistTags, String translationOverride)
    {
        this.biomes = biomes;
        this.biomesTags = biomesTags;
        this.biomesBlacklist = biomesBlacklist;
        this.biomesBlacklistTags = biomesBlacklistTags;
        this.hover = "";
        this.translationOverride = translationOverride;
    }

    public BiomeRestriction(List<Identifier> biomes, List<Identifier> biomesTags, List<Identifier> biomesBlacklist, List<Identifier> biomesBlacklistTags, String hover, String translationOverride)
    {
        this.biomes = biomes;
        this.biomesTags = biomesTags;
        this.biomesBlacklist = biomesBlacklist;
        this.biomesBlacklistTags = biomesBlacklistTags;
        this.hover = hover;
        this.translationOverride = translationOverride;
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

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        Holder<Biome> biome = level.getBiome(entity.blockPosition());
        Identifier biomeRL = biome.getKey().identifier();

        //if biomes or biomesTags then check if biome is in any of them
        if (!biomes.isEmpty() || !biomesTags.isEmpty())
        {
            boolean safe = false;

            if (biomes.contains(biomeRL)) safe = true;

            if (biomesTags.stream().anyMatch(rl -> biome.is(TagKey.create(Registries.BIOME, rl))))
                safe = true;

            if (!safe) return -9999;
        }

        //return if biome is in blacklist
        if (biomesBlacklist.contains(biomeRL)) return -9999;

        //return if biome is part of blacklist tags
        for (Identifier rl : biomesBlacklistTags)
            if (biome.is(TagKey.create(Registries.BIOME, rl)))
                return -9999;

        return 0;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (getFishChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.biome.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.biome.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @Nullable Player player, Context context)
    {
        int color = getFishChance(0, level, fp, player, ItemStack.EMPTY, context) >= 0 ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;

        if (!translationOverride.isEmpty())
            return Component.translatable("gui.guide.biome").append(Component.translatable(translationOverride).withStyle(Style.EMPTY.withColor(color)));

        MutableComponent comp;

        List<Identifier> biomesList = FishProperties.getBiomesAsListFromTags(biomes, biomesTags, level);

        //Biomes: ------
        if (biomesList.isEmpty())
            return Component.translatable("gui.guide.biome").append(Component.translatable("gui.guide.no_restriction"));

        //single biome name / biome tag name / [hover]
        if (biomesList.size() == 1)
            comp = Component.translatable("biome." + biomesList.getFirst().toLanguageKey());
        else if (biomesTags.size() == 1)
            comp = Component.translatable("tag." + biomesTags.getFirst().toLanguageKey());
        else
            comp = Component.translatable("gui.guide.hover");

        return Component.translatable("gui.guide.biome").append(comp.withStyle(Style.EMPTY.withColor(color)));
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();
        List<Identifier> biomesList = FishProperties.getBiomesAsListFromTags(biomes, biomesTags, level);

        if(!this.hover.isEmpty()) return List.of(Component.translatable(this.hover));

        if (!biomesList.isEmpty())
        {
            if (!biomesTags.isEmpty())
            {
                hover.add(Component.translatable("gui.guide.biome_tags").withStyle(Style.EMPTY.withBold(true)));

                for (Identifier rl : biomesTags)
                    hover.add(Component.translatable("tag." + rl.toLanguageKey()));
                hover.add(Component.empty());
            }

            hover.add(Component.translatable("gui.guide.biomes").withStyle(Style.EMPTY.withBold(true)));
            if (biomesList.isEmpty())
                hover.add(Component.translatable("gui.guide.biomes.empty"));

            for (Identifier rl : biomesList)
                hover.add(Component.translatable("biome." + rl.toLanguageKey()));
        }

        return hover;
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> blacklist = new ArrayList<>();
        List<Identifier> biomesBlacklistList = FishProperties.getBiomesBlacklistAsList(biomesBlacklist, biomesBlacklistTags, level);

        if (!biomesBlacklistTags.isEmpty())
        {
            blacklist.add(Component.translatable("gui.guide.blacklisted_biome_tags").withStyle(Style.EMPTY.withBold(true)));

            for (Identifier rl : biomesBlacklistTags)
                blacklist.add(Component.translatable("tag." + rl.toLanguageKey()));
            blacklist.add(Component.empty());
        }

        if (!biomesBlacklistList.isEmpty())
        {
            blacklist.add(Component.translatable("gui.guide.blacklisted_biomes").withStyle(Style.EMPTY.withBold(true)));

            for (Identifier rl : biomesBlacklistList)
                blacklist.add(Component.translatable("biome." + rl.toLanguageKey()));
        }

        return blacklist;
    }

    //Vanilla
    public static final BiomeRestriction LUSH_CAVES = new BiomeRestriction(Biomes.LUSH_CAVES.identifier(), "");
    public static final BiomeRestriction DRIPSTONE_CAVES = new BiomeRestriction(Biomes.DRIPSTONE_CAVES.identifier(), "");
    public static final BiomeRestriction DEEP_DARK = new BiomeRestriction(Biomes.DEEP_DARK.identifier(), "");
    public static final BiomeRestriction SWAMP_ONLY = new BiomeRestriction(Biomes.SWAMP.identifier(), "");
    public static final BiomeRestriction BAMBOO_JUNGLE = new BiomeRestriction(Biomes.BAMBOO_JUNGLE.identifier(), "");
    public static final BiomeRestriction RIVERS = new BiomeRestriction(List.of(), List.of(SCTags.IS_RIVER), List.of(), List.of(), "");
    public static final BiomeRestriction ALL_OCEANS = new BiomeRestriction(List.of(), List.of(SCTags.IS_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction NORMAL_OCEANS = new BiomeRestriction(List.of(), List.of(SCTags.IS_NORMAL_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction LUKEWARM_OCEAN = new BiomeRestriction(List.of(), List.of(SCTags.IS_LUKEWARM_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction COLD_AND_LUKEWARM_OCEAN = new BiomeRestriction(List.of(), List.of(SCTags.IS_LUKEWARM_OCEAN, SCTags.IS_COLD_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction WARM_OCEANS = new BiomeRestriction(List.of(), List.of(SCTags.IS_WARM_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction DEEP_OCEANS = new BiomeRestriction(List.of(), List.of(SCTags.IS_DEEP_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction LAKES = new BiomeRestriction(List.of(), List.of(), List.of(), List.of(SCTags.IS_OCEAN, SCTags.IS_RIVER, SCTags.IS_MUSHROOM_FIELDS),
            "gui.guide.lakes.hover", "gui.guide.lakes");
    public static final BiomeRestriction WARM_LAKES = new BiomeRestriction(List.of(), List.of(SCTags.IS_WARM_LAKE), List.of(), List.of(), "");
    public static final BiomeRestriction COLD_RIVERS = new BiomeRestriction(List.of(), List.of(SCTags.IS_COLD_RIVER), List.of(), List.of(), "");
    public static final BiomeRestriction COLD_OCEANS = new BiomeRestriction(List.of(), List.of(SCTags.IS_COLD_OCEAN), List.of(), List.of(), "");
    public static final BiomeRestriction COLD_LAKES = new BiomeRestriction(List.of(), List.of(SCTags.IS_COLD_LAKE), List.of(), List.of(), "");
    public static final BiomeRestriction SAVANNAS = new BiomeRestriction(List.of(), List.of(BiomeTags.IS_SAVANNA.location()), List.of(), List.of(), "");
    public static final BiomeRestriction BEACHES = new BiomeRestriction(List.of(), List.of(SCTags.IS_BEACH), List.of(), List.of(), "");
    public static final BiomeRestriction MUSHROOM_FIELDS = new BiomeRestriction(List.of(), List.of(SCTags.IS_MUSHROOM_FIELDS), List.of(), List.of(), "");
    public static final BiomeRestriction JUNGLES = new BiomeRestriction(List.of(), List.of(BiomeTags.IS_JUNGLE.location()), List.of(), List.of(), "");
    public static final BiomeRestriction TAIGAS = new BiomeRestriction(List.of(), List.of(BiomeTags.IS_TAIGA.location()), List.of(), List.of(), "");
    public static final BiomeRestriction CHERRY_GROVES = new BiomeRestriction(List.of(), List.of(SCTags.IS_CHERRY_GROVE), List.of(), List.of(), "");
    public static final BiomeRestriction JUNGLES_AND_SWAMPS = new BiomeRestriction(List.of(), List.of(BiomeTags.IS_JUNGLE.location(), SCTags.IS_SWAMP), List.of(), List.of(), "");
    public static final BiomeRestriction SWAMPS = new BiomeRestriction(List.of(), List.of(SCTags.IS_SWAMP), List.of(), List.of(), "");
    public static final BiomeRestriction MANGROVE_SWAMP = new BiomeRestriction(List.of(Biomes.MANGROVE_SWAMP.identifier()), List.of(), List.of(), List.of(), "");
    public static final BiomeRestriction DARK_FOREST = new BiomeRestriction(List.of(), List.of(SCTags.IS_DARK_FOREST), List.of(), List.of(), "");
    public static final BiomeRestriction FOREST = new BiomeRestriction(List.of(), List.of(BiomeTags.IS_FOREST.location()), List.of(), List.of(), "");
    public static final BiomeRestriction LUSH_CAVES_AND_JUNGLES = new BiomeRestriction(List.of(Biomes.LUSH_CAVES.identifier()), List.of(BiomeTags.IS_JUNGLE.location()), List.of(), List.of(), "tag.starcatcher.lush_and_jungle");
    public static final BiomeRestriction CRIMSON_FOREST = new BiomeRestriction(List.of(), List.of(SCTags.IS_CRIMSON_FOREST), List.of(), List.of(), "");
    public static final BiomeRestriction WARPED_FOREST = new BiomeRestriction(List.of(), List.of(SCTags.IS_WARPED_FOREST), List.of(), List.of(), "");
    public static final BiomeRestriction SOUL_SAND_VALLEY = new BiomeRestriction(List.of(), List.of(SCTags.IS_SOUL_SAND_VALLEY), List.of(), List.of(), "");
    public static final BiomeRestriction BASALT_DELTAS = new BiomeRestriction(List.of(), List.of(SCTags.IS_BASALT_DELTAS), List.of(), List.of(), "");
    public static final BiomeRestriction OUTER_ISLANDS = new BiomeRestriction(List.of(), List.of(BiomeTags.IS_END.location()), List.of(Biomes.THE_END.identifier()), List.of(), "");
}
