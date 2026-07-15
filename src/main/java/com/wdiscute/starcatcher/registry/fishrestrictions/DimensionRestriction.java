package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.registry.SCDataEntries;
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

import java.util.List;
import java.util.Map;

public class DimensionRestriction extends AbstractFishRestriction
{
    private final String dimensionEntry;

    public static final MapCodec<DimensionRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("dimension_entry").forGetter(o -> o.dimensionEntry),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, DimensionRestriction::new));

    public DimensionRestriction(String dimensions, String translationOverride)
    {
        super(translationOverride);
        this.dimensionEntry = dimensions;
    }

    @Override
    public int getSortPriority()
    {
        return -100;
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
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        Map<String, List<ResourceLocation>> stringListMap = SCDataEntries.DIMENSION_TAGS.get();
        List<ResourceLocation> allowedDimensions = stringListMap.getOrDefault(dimensionEntry, List.of());

        if(allowedDimensions.contains(level.dimension().location())) return 0;

        return allowedDimensions.isEmpty() ? 0 : -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.dimension.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.dimension.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.dimension");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<ResourceLocation> allowedDimensions = SCDataEntries.DIMENSION_TAGS.get().getOrDefault(dimensionEntry, List.of());

        //Dimensions: [No Dimensions]
        if (allowedDimensions.isEmpty())
            return Component.translatable("gui.guide.dimension.empty");

        //single dimension name / [hover]
        if (allowedDimensions.size() == 1)
            return Component.translatable("dimension." + allowedDimensions.getFirst().toLanguageKey());
        else
            return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<ResourceLocation> allowedDimensions = SCDataEntries.DIMENSION_TAGS.get().getOrDefault(dimensionEntry, List.of());

        if(allowedDimensions.isEmpty()) return List.of();
        if(allowedDimensions.size() == 1) return List.of();

        return allowedDimensions.stream().map(o -> (Component) Component.translatable("dimension." + o.toLanguageKey())).toList();
    }

    public static final DimensionRestriction OVERWORLD = new DimensionRestriction("overworld", "dimension.minecraft.overworld");
    public static final DimensionRestriction NETHER = new DimensionRestriction("the_nether", "dimension.minecraft.the_nether");
    public static final DimensionRestriction END = new DimensionRestriction("the_end", "dimension.minecraft.the_end");
}
