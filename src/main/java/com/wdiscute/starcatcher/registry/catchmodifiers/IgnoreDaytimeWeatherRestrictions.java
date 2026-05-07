package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.registry.fishrestrictions.DaytimeRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.WeatherRestriction;
import net.minecraft.world.level.Level;

public class IgnoreDaytimeWeatherRestrictions
        extends AbstractCatchModifier
        implements WeatherRestriction.SkipsWeatherRestriction, DaytimeRestriction.SkipsDaytimeRestriction
{

    @Override
    public boolean shouldSkipWeather(Level level)
    {
        return true;
    }

    @Override
    public boolean shouldSkipDaytime(Level level)
    {
        return true;
    }
}
