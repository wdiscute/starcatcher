package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record WeightedStack(MaybeStack stack, int weight)
{
    public static final Codec<WeightedStack> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    MaybeStack.CODEC.fieldOf("stack").forGetter(WeightedStack::stack),
                    Codec.INT.optionalFieldOf("weight", 1).forGetter(WeightedStack::weight)
            ).apply(instance, WeightedStack::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, WeightedStack> STREAM_CODEC = StreamCodec.composite(
            MaybeStack.STREAM_CODEC, WeightedStack::stack,
            ByteBufCodecs.INT, WeightedStack::weight,
            WeightedStack::new
    );
}
