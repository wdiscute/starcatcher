package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DimensionRestriction extends AbstractFishRestriction
{

    private final List<ResourceLocation> dimensions;
    private final List<ResourceLocation> dimensionsBlacklist;
    private final String translationOverride;

    public static final MapCodec<DimensionRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("dimensions").forGetter(DimensionRestriction::getDimensions),
                    ResourceLocation.CODEC.listOf().fieldOf("dimensions_blacklist").forGetter(DimensionRestriction::getDimensionsBlacklist),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(DimensionRestriction::getTranslationOverride)
            ).apply(instance, DimensionRestriction::new));

    public DimensionRestriction()
    {
        this.dimensions = List.of();
        this.dimensionsBlacklist = List.of();
        this.translationOverride = "";
    }

    public DimensionRestriction(List<ResourceLocation> dimensions, List<ResourceLocation> dimensionsBlacklist, String translationOverride)
    {
        this.dimensions = dimensions;
        this.dimensionsBlacklist = dimensionsBlacklist;
        this.translationOverride = translationOverride;
    }

    public List<ResourceLocation> getDimensions()
    {
        return dimensions;
    }

    public List<ResourceLocation> getDimensionsBlacklist()
    {
        return dimensionsBlacklist;
    }

    public String getTranslationOverride()
    {
        return translationOverride;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.DIMENSION;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        ResourceLocation dim = level.dimension().location();
        if (!dimensions.isEmpty() && !dimensions.contains(dim)) return -9999;
        if (dimensionsBlacklist.contains(dim)) return -9999;
        return 0;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (getFishChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.dimension.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.dimension.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @Nullable Player player, Context context)
    {

        MutableComponent comp;
        List<Component> hover = new ArrayList<>();

        if (dimensions.isEmpty())
            return Component.translatable("gui.guide.dimension").append(Component.translatable("gui.guide.no_restriction"));


        //if there's only one dimension
        if (dimensions.size() == 1)
        {
            comp = Component.translatable("dimension." + dimensions.getFirst().toLanguageKey());
        }
        else
        {
            comp = Component.translatable("gui.guide.hover");

            //show tooltip while hovering
            List<Component> c = new ArrayList<>();

            c.add(Component.translatable("gui.guide.dimensions"));

            for (ResourceLocation dimension : dimensions)
                hover.add(Component.translatable("dimension." + dimension.toLanguageKey()));
        }

        if (dimensions.contains(level.dimension().location()))
            comp.withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN));
        else
            comp.withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED));

        return Component.translatable("gui.guide.dimension").append(comp);
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> blacklist = new ArrayList<>();

        if (!dimensionsBlacklist.isEmpty())
        {
            //show tooltip while hovering
            blacklist.add(Component.translatable("gui.guide.blacklisted_dimensions"));

            for (ResourceLocation resourceLocation : dimensionsBlacklist)
                blacklist.add(Component.literal(resourceLocation.toString()));
        }

        return blacklist;
    }

    public static final DimensionRestriction OVERWORLD = new DimensionRestriction(List.of(Level.OVERWORLD.location()), List.of(), "");
    public static final DimensionRestriction NETHER = new DimensionRestriction(List.of(Level.NETHER.location()), List.of(), "");
    public static final DimensionRestriction END = new DimensionRestriction(List.of(Level.END.location()), List.of(), "");
}
