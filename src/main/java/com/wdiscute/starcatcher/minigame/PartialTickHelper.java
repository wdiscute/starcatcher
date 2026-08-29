package com.wdiscute.starcatcher.minigame;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class PartialTickHelper
{
    public static final PartialTickHelper INSTANCE = new PartialTickHelper();

    private static final long TICK_NANOS = 50_000_000L; // 50 ms

    private static final long MAX_DRIFT_NANOS = TICK_NANOS * 5;

    private long lastGameTickTime = System.nanoTime();
    private long lastGameTick = -1;

    public float getPartialTicks(Level level)
    {
        long now = System.nanoTime();
        long currentGameTick = level.getGameTime();

        if (lastGameTick == -1)
        {
            lastGameTick = currentGameTick;
            lastGameTickTime = now;
            return 0.0f;
        }

        long ticksElapsed = currentGameTick - lastGameTick;

        if (ticksElapsed > 0)
        {
            lastGameTickTime += ticksElapsed * TICK_NANOS;
            lastGameTick = currentGameTick;
        }
        else if (ticksElapsed < 0)
        {
            lastGameTick = currentGameTick;
            lastGameTickTime = now;
        }

        long nanoSinceLastTick = now - lastGameTickTime;

        if (nanoSinceLastTick < 0 || nanoSinceLastTick > TICK_NANOS)
        {
            lastGameTickTime = now;
            nanoSinceLastTick = 0;
        }

        double partial = nanoSinceLastTick / (double) TICK_NANOS;

        return (float) Mth.clamp(partial, 0.0f, 1.0f);
    }
}