package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FishCaughtCounter
{

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("rl").forGetter(FishCaughtCounter::getResourceLocation),
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::getCount)
            ).apply(instance, FishCaughtCounter::new)
    );


    public static final StreamCodec<ByteBuf, List<FishCaughtCounter>> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, FishCaughtCounter::getResourceLocation,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::getCount, FishCaughtCounter::new
    ).apply(ByteBufCodecs.list());



    public static final Codec<List<FishCaughtCounter>> LIST_CODEC = FishCaughtCounter.CODEC.listOf();


    private final ResourceLocation resourceLocation;
    private final Integer count;


    public FishCaughtCounter(ResourceLocation rl, Integer count)
    {
        this.resourceLocation = rl;
        this.count = count;
    }

    public ResourceLocation getResourceLocation()
    {
        return resourceLocation;
    }

    public Integer getCount()
    {
        return count;
    }
}
