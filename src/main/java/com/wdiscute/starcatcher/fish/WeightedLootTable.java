package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record WeightedLootTable(Identifier resourceLocation, int weight)
{
    public WeightedLootTable(Identifier resourceLocation)
    {
        this(resourceLocation, 1);
    }

    public static final Codec<WeightedLootTable> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("loot_table").forGetter(WeightedLootTable::resourceLocation),
                    Codec.INT.optionalFieldOf("weight", 1).forGetter(WeightedLootTable::weight)
            ).apply(instance, WeightedLootTable::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WeightedLootTable> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, WeightedLootTable::resourceLocation,
            ByteBufCodecs.INT, WeightedLootTable::weight,
            WeightedLootTable::new
    );
}
