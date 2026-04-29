package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DaytimeRestriction extends AbstractFishRestriction
{
    private final List<Duo> ranges;
    private final String translationOverride;

    public static final MapCodec<DaytimeRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Duo.CODEC.listOf().fieldOf("ranges").forGetter(DaytimeRestriction::getRanges),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(DaytimeRestriction::getTranslationOverride)
            ).apply(instance, DaytimeRestriction::new));

    public record Duo(int first, int second)
    {
        public static final Codec<Duo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("first").forGetter(Duo::first),
                        Codec.INT.fieldOf("second").forGetter(Duo::second)
                ).apply(instance, Duo::new));
    }

    public DaytimeRestriction()
    {
        this.ranges = List.of();
        this.translationOverride = "";
    }

    public DaytimeRestriction(List<Duo> ranges, String translationOverride)
    {
        this.ranges = ranges;
        this.translationOverride = translationOverride;
    }

    public List<Duo> getRanges()
    {
        return ranges;
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
        return SCFishRestrictions.DAYTIME_RESTRICTION;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //skip if any modifiers have SkipsDaytimeRestriction interface
        if (context.equals(Context.FISHING) && entity instanceof FishingBobEntity bob && bob.player != null)
        {
            if (SCCatchModifiers.getCatchModifiers(bob.player).stream().anyMatch(
                    o -> o instanceof SkipsDaytimeRestriction sp && sp.shouldSkipDaytime(level)))
                return 0;

            if (SCMinigameModifiers.getMinigameModifiers(bob.player).stream().anyMatch(
                    o -> o instanceof SkipsDaytimeRestriction sp && sp.shouldSkipDaytime(level)))
                return 0;
        }


        float daytime = level.dayTime() % 24000;

        if (ranges.stream().anyMatch(range -> daytime > range.first && daytime < range.second))
            return 0;

        return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (getFishChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.daytime.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.daytime.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int color = getFishChance(0, level, fp, player, ItemStack.EMPTY, context) >= 0 ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;

        return Component.translatable("gui.guide.daytime").copy().append(
                translationOverride.isEmpty() ?
                        Component.translatable("gui.guide.hover").withStyle(Style.EMPTY.withColor(color)) :
                        Component.translatable(translationOverride).withStyle(Style.EMPTY.withColor(color))
        );
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();

        for (Duo range : ranges)
            hover.add(Component.literal(" > " + range.first + " & < " + range.second));

        return hover;
    }

    public static final DaytimeRestriction DAY = new DaytimeRestriction(List.of(new Duo(0, 12700)), "gui.guide.day");
    public static final DaytimeRestriction NOON = new DaytimeRestriction(List.of(new Duo(4500, 7500)), "gui.guide.noon");
    public static final DaytimeRestriction MIDNIGHT = new DaytimeRestriction(List.of(new Duo(16500, 19500)), "gui.guide.midnight");
    public static final DaytimeRestriction NIGHT = new DaytimeRestriction(List.of(new Duo(12700, 24000)), "gui.guide.night");

    public interface SkipsDaytimeRestriction
    {
        boolean shouldSkipDaytime(Level level);
    }
}
