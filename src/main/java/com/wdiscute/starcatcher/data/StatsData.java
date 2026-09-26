package com.wdiscute.starcatcher.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StatsData(int timeSpent, int treasuresCaught, int fishMissed, int baitUsed)
{
    public static final Codec<StatsData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("time_spent").forGetter(StatsData::timeSpent),
                    Codec.INT.fieldOf("treasures_caught").forGetter(StatsData::treasuresCaught),
                    Codec.INT.fieldOf("fish_missed").forGetter(StatsData::fishMissed),
                    Codec.INT.fieldOf("bait_used").forGetter(StatsData::baitUsed)
            ).apply(instance, StatsData::new)
    );
}
