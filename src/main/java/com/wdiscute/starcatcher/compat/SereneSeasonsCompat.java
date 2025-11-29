package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.FishProperties.WorldRestrictions.Seasons;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SereneSeasonsCompat
{

    public static boolean canCatch(FishProperties fp, Level level)
    {
        if (!fp.wr().seasons().contains(FishProperties.WorldRestrictions.Seasons.ALL))
        {
            return fp.wr().seasons().contains(getSeason(level)) || fp.wr().seasons().contains(getSubSeason(level));
        }
        return true;
    }

    public static Seasons getSeason(Level level)
    {
        Season season = SeasonHelper.getSeasonState(level).getSeason();
        return switch (season)
        {
            case SPRING -> Seasons.SPRING;
            case SUMMER -> Seasons.SUMMER;
            case AUTUMN -> Seasons.AUTUMN;
            default -> Seasons.WINTER;
        };
    }

    public static Seasons getSubSeason(Level level)
    {
        Season.SubSeason season = SeasonHelper.getSeasonState(level).getSubSeason();
        return switch (season)
        {
            case EARLY_SPRING -> Seasons.EARLY_SPRING;
            case MID_SPRING -> Seasons.MID_SPRING;
            case LATE_SPRING -> Seasons.LATE_SPRING;

            case EARLY_SUMMER -> Seasons.EARLY_SUMMER;
            case MID_SUMMER -> Seasons.MID_SUMMER;
            case LATE_SUMMER -> Seasons.LATE_SUMMER;

            case EARLY_AUTUMN -> Seasons.EARLY_AUTUMN;
            case MID_AUTUMN -> Seasons.MID_AUTUMN;
            case LATE_AUTUMN -> Seasons.LATE_AUTUMN;

            case EARLY_WINTER -> Seasons.EARLY_WINTER;
            case MID_WINTER -> Seasons.MID_WINTER;
            default -> Seasons.LATE_WINTER;
        };
    }
}
