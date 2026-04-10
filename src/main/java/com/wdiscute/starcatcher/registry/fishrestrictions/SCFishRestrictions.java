package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.wdiscute.starcatcher.Starcatcher;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCFishRestrictions
{
    DeferredRegister<AbstractFishRestriction> REGISTRY =
            DeferredRegister.create(Starcatcher.FISH_RESTRICTIONS_REGISTRY, Starcatcher.MOD_ID);


    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> EMPTY =
            registerFishRestriction("empty", EmptyRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> DIMENSION =
            registerFishRestriction("dimension", DimensionRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> BIOME =
            registerFishRestriction("biome", BiomeRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> BAIT =
            registerFishRestriction("bait", BaitRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> FLUID =
            registerFishRestriction("fluid", FluidRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> ELEVATION_RESTRICTION =
            registerFishRestriction("elevation_restriction", ElevationRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> ELEVATION_BIAS =
            registerFishRestriction("elevation_bias", ElevationBias::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> WEATHER =
            registerFishRestriction("weather_restriction", WeatherRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> DAYTIME_RESTRICTION =
            registerFishRestriction("daytime_restriction", DaytimeRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> DAYTIME_BIAS =
            registerFishRestriction("daytime_bias", DaytimeBias::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> MOON_PHASE =
            registerFishRestriction("moon_phase", EmptyRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> SEASON =
            registerFishRestriction("season", SeasonRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> MAX_CATCH_LIMIT =
            registerFishRestriction("caught_limit", CaughtLimitRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> RARITY_COUNT_RESTRICTION =
            registerFishRestriction("rarity_count", RarityCountRestriction::new);

    DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> CHANCE_RESTRICTION =
            registerFishRestriction("percentage_chance", ChancePercentageRestriction::new);

    class Extra
    {
        public static DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> TFC_HUMIDITY = null;

        static void registerOptionals()
        {
            if (ModList.get().isLoaded("tfc"))
                Extra.TFC_HUMIDITY = registerFishRestriction("tfc_huminity", EmptyRestriction::new);
        }
    }

    static DeferredHolder<AbstractFishRestriction, AbstractFishRestriction>
    registerFishRestriction(String name, Supplier<AbstractFishRestriction> sup)
    {
        return REGISTRY.register(name, sup);
    }

    static void register(IEventBus eventBus)
    {
        Extra.registerOptionals();
        REGISTRY.register(eventBus);
    }
}
