package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SizeAndWeight(
        int sizeInCentimeters,
        int weightInGrams
)
{

    public static final Codec<SizeAndWeight> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("size").forGetter(SizeAndWeight::sizeInCentimeters),
                    Codec.INT.fieldOf("weight").forGetter(SizeAndWeight::weightInGrams)
            ).apply(instance, SizeAndWeight::new));

    public static final SizeAndWeight DEFAULT = new SizeAndWeight(0, 0);

}
