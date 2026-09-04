package com.wdiscute.starcatcher.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.fish.Rarity;

public record CaughtFishInfo(
        float size,
        float weight,
        float percentile,
        Rarity rarity
)
{
    public static final CaughtFishInfo GOLDEN = new CaughtFishInfo(0, 0, 0, Rarity.GOLDEN);
    public static final CaughtFishInfo AVERAGE = new CaughtFishInfo(0, 0, 50, Rarity.COMMON);

    public static final Codec<CaughtFishInfo> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("size", 0f).forGetter(CaughtFishInfo::size),
                    Codec.FLOAT.optionalFieldOf("weight", 0f).forGetter(CaughtFishInfo::weight),
                    Codec.FLOAT.optionalFieldOf("percentile", 0f).forGetter(CaughtFishInfo::percentile),
                    Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter(CaughtFishInfo::rarity)
            ).apply(instance, CaughtFishInfo::new));

    public float getScale()
    {
        float maxScale = ((float) (double) SCConfig.FISH_MAX_SCALE.get());
        float minScale = ((float) (double) SCConfig.FISH_MIN_SCALE.get());

        if (rarity.equals(Rarity.GOLDEN)) return maxScale;
        return maxScale - (percentile / 100f) * (maxScale - minScale);
    }

    public boolean golden()
    {
        return rarity.equals(Rarity.GOLDEN);
    }
}
