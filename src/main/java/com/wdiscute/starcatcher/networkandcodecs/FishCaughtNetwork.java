package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record FishCaughtNetwork(
        ResourceLocation rl,
        int count,
        int fastestTicks,
        float averageTicks,
        int size,
        int weight,
        boolean golden
)
{
    public static final Codec<FishCaughtNetwork> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("rl", Starcatcher.rl("missingno")).forGetter(FishCaughtNetwork::rl),
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtNetwork::count),
                    Codec.INT.optionalFieldOf("fastest_ticks", 0).forGetter(FishCaughtNetwork::fastestTicks),
                    Codec.FLOAT.optionalFieldOf("average_ticks", 0.0f).forGetter(FishCaughtNetwork::averageTicks),
                    Codec.INT.optionalFieldOf("size", 0).forGetter(FishCaughtNetwork::size),
                    Codec.INT.optionalFieldOf("weight", 0).forGetter(FishCaughtNetwork::weight),
                    Codec.BOOL.optionalFieldOf("golden", false).forGetter(FishCaughtNetwork::golden)
            ).apply(instance, FishCaughtNetwork::new)
    );

    public static final Codec<List<FishCaughtNetwork>> LIST_CODEC = FishCaughtNetwork.CODEC.listOf();

}
