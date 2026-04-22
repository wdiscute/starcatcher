package com.wdiscute.starcatcher.minigame;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;


public class PartialTickHelper
{
    public static final PartialTickHelper INSTANCE = new PartialTickHelper();

    private static final long TICK_NANOS = 50_000_000L; // 50 ms

    private long lastTickTime = System.nanoTime();
    private long lastGameTick = -1;


    // pure ChatGPT code
    public float getPartialTicks(Level level)
    {
        long gameTick = level.getGameTime();
        long now = System.nanoTime();

        // Detect a new game tick
        if (gameTick != lastGameTick)
        {
            lastGameTick = gameTick;
            lastTickTime = now;
            return 0.0f;
        }

        float partial = (now - lastTickTime) / (float) TICK_NANOS;
        return Mth.clamp(partial, 0.0f, 1.0f);
    }
}
