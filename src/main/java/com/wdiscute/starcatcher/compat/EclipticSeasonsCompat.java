package com.wdiscute.starcatcher.compat;

import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.FishProperties.WorldRestrictions.Seasons;
import net.minecraft.world.level.Level;

public class EclipticSeasonsCompat
{

    public static boolean canCatch(FishProperties fp, Level level)
    {
        if (!fp.wr().seasons().contains(Seasons.ALL))
        {
            return fp.wr().seasons().contains(getSeason(level)) || fp.wr().seasons().contains(getSubSeason(level));
        }
        return true;
    }

    public static Seasons getSeason(Level level)
    {
        Season season =  EclipticUtil.getNowSolarTerm(level).getSeason();
        return switch (season)
        {
            case NONE -> Seasons.ALL;

            case SPRING -> Seasons.SPRING;
            case SUMMER -> Seasons.SUMMER;
            case AUTUMN -> Seasons.AUTUMN;
            default -> Seasons.WINTER;
        };
    }


    public static Seasons getSubSeason(Level level)
    {
        SolarTerm season = EclipticUtil.getNowSolarTerm(level);
        return switch (season)
        {
            case NONE -> Seasons.ALL;

            case BEGINNING_OF_SPRING, RAIN_WATER -> Seasons.EARLY_SPRING;
            case INSECTS_AWAKENING, SPRING_EQUINOX -> Seasons.MID_SPRING;
            case FRESH_GREEN, GRAIN_RAIN -> Seasons.LATE_SPRING;

            case BEGINNING_OF_SUMMER, LESSER_FULLNESS -> Seasons.EARLY_SUMMER;
            case GRAIN_IN_EAR, SUMMER_SOLSTICE -> Seasons.MID_SUMMER;
            case LESSER_HEAT, GREATER_HEAT -> Seasons.LATE_SUMMER;

            case BEGINNING_OF_AUTUMN, END_OF_HEAT -> Seasons.EARLY_AUTUMN;
            case WHITE_DEW, AUTUMNAL_EQUINOX -> Seasons.MID_AUTUMN;
            case COLD_DEW, FIRST_FROST -> Seasons.LATE_AUTUMN;

            case BEGINNING_OF_WINTER, LIGHT_SNOW -> Seasons.EARLY_WINTER;
            case HEAVY_SNOW, WINTER_SOLSTICE -> Seasons.MID_WINTER;
            case LESSER_COLD, GREATER_COLD -> Seasons.LATE_WINTER;
        };
    }

}
