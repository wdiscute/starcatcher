package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record WeightedLootTable(ResourceLocation resourceLocation, int weight)
{
    public WeightedLootTable(ResourceLocation resourceLocation)
    {
        this(resourceLocation, 1);
    }

    public static final Codec<WeightedLootTable> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("loot_table").forGetter(WeightedLootTable::resourceLocation),
                    Codec.INT.optionalFieldOf("weight", 1).forGetter(WeightedLootTable::weight)
            ).apply(instance, WeightedLootTable::new));

    public static final StreamCodec<WeightedLootTable> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.RESOURCE_LOCATION, WeightedLootTable::resourceLocation,
            ByteBufCodecs.INT, WeightedLootTable::weight,
            WeightedLootTable::new
    );
}
