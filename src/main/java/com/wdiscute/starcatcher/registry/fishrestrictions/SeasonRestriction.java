package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.compat.EclipticSeasonsCompat;
import com.wdiscute.starcatcher.compat.SereneSeasonsCompat;
import com.wdiscute.starcatcher.compat.TerraFirmaCraftSeasonsCompat;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasonRestriction extends AbstractFishRestriction
{
    private final Map<Seasons, Integer> seasons;

    public static final MapCodec<SeasonRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(Seasons.CODEC, Codec.INT).fieldOf("season_extra_chance").forGetter(o -> o.seasons),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SeasonRestriction::new));

    public SeasonRestriction(Map<Seasons, Integer> seasons, String translationOverride)
    {
        super(translationOverride);
        this.seasons = seasons;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.SEASON;
    }

    @Override
    public boolean isEnabled()
    {
        return ModList.get().isLoaded("sereneseasons") || ModList.get().isLoaded("eclipticseasons");
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        Seasons currentSeason = Seasons.ALL;

        //Serene Seasons check
        if (ModList.get().isLoaded("sereneseasons"))
        {
            currentSeason = SereneSeasonsCompat.getSeason(level);
        }

        //Ecliptic Seasons check
        if (ModList.get().isLoaded("eclipticseasons"))
        {
            currentSeason = EclipticSeasonsCompat.getSeason(level);
        }

        //TerraFirmaCraft Seasons check
        if (ModList.get().isLoaded("tfc"))
        {
            currentSeason = TerraFirmaCraftSeasonsCompat.getSeason(level);
        }

        if (currentSeason.equals(Seasons.ALL)) return 0;

        if (seasons.containsKey(currentSeason)) return seasons.get(currentSeason);

        //not in season
        return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int color = adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0 ?
                SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;

        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.seasons.in_season").withStyle(Style.EMPTY.withColor(color)));
        else
            return List.of(Component.translatable("gui.guide.seasons.not_in_season").withStyle(Style.EMPTY.withColor(color)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.season");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (seasons.entrySet().size() == 1)
            return Component.translatable(seasons.entrySet().stream().findFirst().get().getKey().toTranslationKey());
        else
            return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();

        hover.add(Component.translatable("gui.guide.seasons"));

        for (Map.Entry<Seasons, Integer> entry : seasons.entrySet())
            hover.add(Component.translatable("gui.guide.seasons." + entry.getKey().getSerializedName()));

        return hover;
    }


    public enum Seasons implements StringRepresentable
    {
        ALL("all"),

        EARLY_SPRING("early_spring"),
        MID_SPRING("mid_spring"),
        LATE_SPRING("late_spring"),

        EARLY_SUMMER("early_summer"),
        MID_SUMMER("mid_summer"),
        LATE_SUMMER("late_summer"),

        EARLY_AUTUMN("early_autumn"),
        MID_AUTUMN("mid_autumn"),
        LATE_AUTUMN("late_autumn"),

        EARLY_WINTER("early_winter"),
        MID_WINTER("mid_winter"),
        LATE_WINTER("late_winter");

        public static final Codec<Seasons> CODEC = StringRepresentable.fromEnum(Seasons::values);
        private final String name;

        Seasons(String name)
        {
            this.name = name;
        }

        public String toTranslationKey()
        {
            return "gui.guide.seasons." + name;
        }


        public String getSerializedName()
        {
            return this.name;
        }
    }

    public static final SeasonRestriction SPRING = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0
    ), "gui.guide.seasons.spring");

    public static final SeasonRestriction SUMMER = new SeasonRestriction(Map.of(
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0
    ), "gui.guide.seasons.summer");

    public static final SeasonRestriction AUTUMN = new SeasonRestriction(Map.of(
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0
    ), "gui.guide.seasons.autumn");

    public static final SeasonRestriction WINTER = new SeasonRestriction(Map.of(
            Seasons.EARLY_WINTER, 0,
            Seasons.MID_WINTER, 0,
            Seasons.LATE_WINTER, 0
    ), "gui.guide.seasons.winter");

    public static final SeasonRestriction NOT_SPRING = new SeasonRestriction(Map.of(
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0,
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0,
            Seasons.EARLY_WINTER, 0,
            Seasons.MID_WINTER, 0,
            Seasons.LATE_WINTER, 0
    ), "gui.guide.seasons.not_spring");

    public static final SeasonRestriction NOT_SUMMER = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0,
            Seasons.EARLY_WINTER, 0,
            Seasons.MID_WINTER, 0,
            Seasons.LATE_WINTER, 0
    ), "gui.guide.seasons.not_summer");

    public static final SeasonRestriction NOT_AUTUMN = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0,
            Seasons.EARLY_WINTER, 0,
            Seasons.MID_WINTER, 0,
            Seasons.LATE_WINTER, 0
    ), "gui.guide.seasons.not_autumn");

    public static final SeasonRestriction NOT_WINTER = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0,
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0
    ), "gui.guide.seasons.not_winter");

    public static final SeasonRestriction SUMMER_AUTUMN = new SeasonRestriction(Map.of(
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0,
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0
    ), "gui.guide.seasons.summer_autumn");

    public static final SeasonRestriction SPRING_WINTER = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_WINTER, 0,
            Seasons.MID_WINTER, 0,
            Seasons.LATE_WINTER, 0
    ), "gui.guide.seasons.spring_winter");

    public static final SeasonRestriction SPRING_AUTUMN = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0
    ), "gui.guide.seasons.spring_winter");

    public static final SeasonRestriction AUTUMN_WINTER = new SeasonRestriction(Map.of(
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0,
            Seasons.EARLY_WINTER, 0,
            Seasons.MID_WINTER, 0,
            Seasons.LATE_WINTER, 0
    ), "gui.guide.seasons.autumn_winter");

    public static final SeasonRestriction SPRING_SUMMER = new SeasonRestriction(Map.of(
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0
    ), "gui.guide.seasons.spring_summer");

    public static final SeasonRestriction AROUND_SPRING = new SeasonRestriction(Map.of(
            Seasons.LATE_WINTER, 0,
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_SUMMER, 0
    ), "gui.guide.seasons.around_spring");

    public static final SeasonRestriction AROUND_SUMMER = new SeasonRestriction(Map.of(
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_SUMMER, 0,
            Seasons.MID_SUMMER, 0,
            Seasons.LATE_SUMMER, 0,
            Seasons.EARLY_AUTUMN, 0
    ), "gui.guide.seasons.around_spring");

    public static final SeasonRestriction AROUND_AUTUMN = new SeasonRestriction(Map.of(
            Seasons.LATE_SUMMER, 0,
            Seasons.EARLY_AUTUMN, 0,
            Seasons.MID_AUTUMN, 0,
            Seasons.LATE_AUTUMN, 0,
            Seasons.EARLY_WINTER, 0
    ), "gui.guide.seasons.around_spring");

    public static final SeasonRestriction AROUND_WINTER = new SeasonRestriction(Map.of(
            Seasons.LATE_WINTER, 0,
            Seasons.EARLY_SPRING, 0,
            Seasons.MID_SPRING, 0,
            Seasons.LATE_SPRING, 0,
            Seasons.EARLY_SUMMER, 0
    ), "gui.guide.seasons.around_spring");
}
