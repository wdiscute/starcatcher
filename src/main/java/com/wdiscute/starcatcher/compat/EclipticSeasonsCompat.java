package com.wdiscute.starcatcher.compat;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.wdiscute.starcatcher.registry.fishrestrictions.SeasonRestriction;
import net.minecraft.world.level.Level;

public class EclipticSeasonsCompat
{

    public static SeasonRestriction.Seasons getSeason(Level level)
    {
        SolarTerm season = EclipticUtil.getNowSolarTerm(level);
        return switch (season)
        {
            case NONE -> SeasonRestriction.Seasons.ALL;

            case BEGINNING_OF_SPRING, RAIN_WATER -> SeasonRestriction.Seasons.EARLY_SPRING;
            case INSECTS_AWAKENING, SPRING_EQUINOX -> SeasonRestriction.Seasons.MID_SPRING;
            case FRESH_GREEN, GRAIN_RAIN -> SeasonRestriction.Seasons.LATE_SPRING;

            case BEGINNING_OF_SUMMER, LESSER_FULLNESS -> SeasonRestriction.Seasons.EARLY_SUMMER;
            case GRAIN_IN_EAR, SUMMER_SOLSTICE -> SeasonRestriction.Seasons.MID_SUMMER;
            case LESSER_HEAT, GREATER_HEAT -> SeasonRestriction.Seasons.LATE_SUMMER;

            case BEGINNING_OF_AUTUMN, END_OF_HEAT -> SeasonRestriction.Seasons.EARLY_AUTUMN;
            case WHITE_DEW, AUTUMNAL_EQUINOX -> SeasonRestriction.Seasons.MID_AUTUMN;
            case COLD_DEW, FIRST_FROST -> SeasonRestriction.Seasons.LATE_AUTUMN;

            case BEGINNING_OF_WINTER, LIGHT_SNOW -> SeasonRestriction.Seasons.EARLY_WINTER;
            case HEAVY_SNOW, WINTER_SOLSTICE -> SeasonRestriction.Seasons.MID_WINTER;
            case LESSER_COLD, GREATER_COLD -> SeasonRestriction.Seasons.LATE_WINTER;
        };
    }

}
