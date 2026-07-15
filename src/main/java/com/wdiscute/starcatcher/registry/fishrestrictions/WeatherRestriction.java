package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class WeatherRestriction extends AbstractFishRestriction
{
    private final Weather weather;

    public static final MapCodec<WeatherRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Weather.CODEC.fieldOf("weather").forGetter(o -> o.weather),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, WeatherRestriction::new));

    public enum Weather implements StringRepresentable
    {
        CLEAR("clear", (level) -> level.getRainLevel(0) < 0.2f && level.getThunderLevel(0) < 0.2f),
        RAIN("rain", (level) -> level.getRainLevel(0) > 0.2f),
        THUNDER("thunder", (level) -> level.getThunderLevel(0) > 0.2f),
        CLEAR_OR_RAIN("clear_or_rain", (level) -> CLEAR.isCorrect.test(level) || RAIN.isCorrect.test(level)),
        CLEAR_OR_THUNDER("clear_or_thunder", (level) -> CLEAR.isCorrect.test(level) || THUNDER.isCorrect.test(level)),
        RAIN_OR_THUNDER("rain_or_thunder", (level) -> RAIN.isCorrect.test(level) || THUNDER.isCorrect.test(level));

        public static final Codec<Weather> CODEC = StringRepresentable.fromEnum(Weather::values);

        private final String name;
        public final Predicate<Level> isCorrect;

        public String toLanguageKey()
        {
            return "gui.guide.weather." + name;
        }

        Weather(String name, Predicate<Level> isCorrect)
        {
            this.name = name;
            this.isCorrect = isCorrect;
        }

        @Override
        public String getSerializedName()
        {
            return name;
        }
    }


    public WeatherRestriction(Weather weather)
    {
        super("");
        this.weather = weather;
    }

    public WeatherRestriction(Weather weather, String translationOverride)
    {
        super(translationOverride);
        this.weather = weather;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.WEATHER;
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.weather");
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //fishes in area for guidebook ignores this restriction
        if(context.equals(Context.RADAR)) return 0;
        if (context.equals(Context.GUIDE_FISHES_IN_AREA)) return 0;
        if (context.equals(Context.TRACKER)) return weather.isCorrect.test(level) ? 0 : -9999;

        //skip if any modifiers have skipsWeatherRestriction interface
        if (context.equals(Context.FISHING) && entity instanceof FishingBobEntity bob && bob.player != null)
        {
            if (Modifier.getCatchModifiers(bob.player).stream().anyMatch(
                    o -> o instanceof SkipsWeatherRestriction sp && sp.shouldSkipWeather(level)))
                return 0;

            if (Modifier.getModifiers(bob.player).stream().anyMatch(
                    o -> o instanceof SkipsWeatherRestriction sp && sp.shouldSkipWeather(level)))
                return 0;
        }

        return weather.isCorrect.test(level) ? 0 : -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.weather.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.weather.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.translatable(weather.toLanguageKey());
    }

    public static final WeatherRestriction CLEAR = new WeatherRestriction(Weather.CLEAR);
    public static final WeatherRestriction RAIN = new WeatherRestriction(Weather.RAIN);
    public static final WeatherRestriction THUNDER = new WeatherRestriction(Weather.THUNDER);

    public interface SkipsWeatherRestriction
    {
        boolean shouldSkipWeather(Level level);
    }
}
