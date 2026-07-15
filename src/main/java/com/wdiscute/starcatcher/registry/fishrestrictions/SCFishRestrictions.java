package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.wdiscute.starcatcher.Starcatcher;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface SCFishRestrictions
{
    DeferredRegister<AbstractFishRestriction> REGISTRY =
            DeferredRegister.create(Starcatcher.FISH_RESTRICTIONS_REGISTRY, Starcatcher.MOD_ID);


    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> EMPTY =
            registerFishRestriction("empty", () -> new EmptyRestriction(""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> DIMENSION =
            registerFishRestriction("dimension", () -> new DimensionRestriction("", ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> BIOME =
            registerFishRestriction("biome", () -> new BiomeRestriction(List.of(), List.of(), "", ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> BAIT =
            registerFishRestriction("bait", () -> new BaitRestriction(Map.of(), ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> FLUID =
            registerFishRestriction("fluid", FluidRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> ELEVATION_RESTRICTION =
            registerFishRestriction("elevation_restriction", ElevationRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> ELEVATION_BIAS =
            registerFishRestriction("elevation_bias", ElevationBias::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> WEATHER =
            registerFishRestriction("weather_restriction", () -> new WeatherRestriction(WeatherRestriction.Weather.RAIN, ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> DAYTIME_RESTRICTION =
            registerFishRestriction("daytime_restriction", () -> new DaytimeRestriction(List.of(), ""));

    //todo add fishes with this
    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> DAYTIME_BIAS =
            registerFishRestriction("daytime_bias", DaytimeBias::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> MOON_PHASE =
            registerFishRestriction("moon_phase", () -> new MoonPhaseRestriction(List.of(), ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> LUCK_RESTRICTION =
            registerFishRestriction("luck_restriction", () -> new LuckRestriction(0, ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> SEASON =
            registerFishRestriction("season", () -> new SeasonRestriction(Map.of(), ""));

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> MAX_CATCH_LIMIT =
            registerFishRestriction("caught_limit", CaughtLimitRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> RARITY_COUNT_RESTRICTION =
            registerFishRestriction("rarity_count", RarityCountRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> CHANCE_RESTRICTION =
            registerFishRestriction("percentage_chance", ChancePercentageRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> STRUCTURE_RESTRICTION =
            registerFishRestriction("structure_restriction", () -> new StructureRestriction(List.of(), ""));

    static DeferredHolder<AbstractFishRestriction, AbstractFishRestriction>
    registerFishRestriction(String name, Supplier<AbstractFishRestriction> sup)
    {
        return REGISTRY.register(name, sup);
    }

    static void register(IEventBus eventBus)
    {
        //TFCCompat.registerOptionals();
        REGISTRY.register(eventBus);
    }
}
