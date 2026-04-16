package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.registry.fishrestrictions.SeasonRestriction;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.world.level.Level;

public class TerraFirmaCraftSeasonsCompat
{

    public static SeasonRestriction.Seasons getSeason(Level level)
    {
        Month month;
        if (level.isClientSide)
            month = Calendars.CLIENT.getCalendarMonthOfYear();
        else
            month = Calendars.SERVER.getCalendarMonthOfYear();

        return switch (month)
        {
            case JANUARY -> SeasonRestriction.Seasons.MID_WINTER;
            case FEBRUARY -> SeasonRestriction.Seasons.LATE_WINTER;
            case MARCH -> SeasonRestriction.Seasons.EARLY_SPRING;
            case APRIL -> SeasonRestriction.Seasons.MID_SPRING;
            case MAY -> SeasonRestriction.Seasons.LATE_SPRING;
            case JUNE -> SeasonRestriction.Seasons.EARLY_SUMMER;
            case JULY -> SeasonRestriction.Seasons.MID_SUMMER;
            case AUGUST -> SeasonRestriction.Seasons.LATE_SUMMER;
            case SEPTEMBER -> SeasonRestriction.Seasons.EARLY_AUTUMN;
            case OCTOBER -> SeasonRestriction.Seasons.MID_AUTUMN;
            case NOVEMBER -> SeasonRestriction.Seasons.LATE_AUTUMN;
            case DECEMBER -> SeasonRestriction.Seasons.EARLY_WINTER;
        };
    }

}
