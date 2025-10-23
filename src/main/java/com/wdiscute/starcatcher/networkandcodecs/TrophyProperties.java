package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TrophyProperties
        (
                FishProperties fp,
                TrophyType trophyType,
                String customName,
                int uniqueFishCount,
                int totalCaughtCount,
                int chanceToCatch
        )
{

    public static final TrophyProperties DEFAULT = new TrophyProperties(
            FishProperties.DEFAULT.withFish(ModItems.MISSINGNO),
            TrophyType.NONE,
            "Missingno Trophy",
            Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE
    );

    public static final Codec<TrophyProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.optionalFieldOf("fish_properties", DEFAULT.fp).forGetter(TrophyProperties::fp),
                    TrophyType.CODEC.optionalFieldOf("trophy_type", DEFAULT.trophyType).forGetter(TrophyProperties::trophyType),
                    Codec.STRING.optionalFieldOf("custom_name", DEFAULT.customName).forGetter(TrophyProperties::customName),
                    Codec.INT.optionalFieldOf("unique_fishes", DEFAULT.uniqueFishCount).forGetter(TrophyProperties::uniqueFishCount),
                    Codec.INT.optionalFieldOf("total_fishes", DEFAULT.totalCaughtCount).forGetter(TrophyProperties::totalCaughtCount),
                    Codec.INT.optionalFieldOf("chance_to_catch", DEFAULT.chanceToCatch).forGetter(TrophyProperties::chanceToCatch)
            ).apply(instance, TrophyProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TrophyProperties> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, TrophyProperties::fp,
            TrophyType.STREAM_CODEC, TrophyProperties::trophyType,
            ByteBufCodecs.STRING_UTF8, TrophyProperties::customName,
            ByteBufCodecs.VAR_INT, TrophyProperties::uniqueFishCount,
            ByteBufCodecs.VAR_INT, TrophyProperties::totalCaughtCount,
            ByteBufCodecs.VAR_INT, TrophyProperties::chanceToCatch,
            TrophyProperties::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<TrophyProperties>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final Codec<List<TrophyProperties>> LIST_CODEC = TrophyProperties.CODEC.listOf();

    public enum TrophyType implements StringRepresentable
    {
        TROPHY("trophy"),
        SECRET("secret"),
        NONE("none");

        public static final Codec<TrophyType> CODEC = StringRepresentable.fromEnum(TrophyType::values);
        public static final StreamCodec<FriendlyByteBuf, TrophyType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(TrophyType.class);
        private final String key;

        TrophyType(String key)
        {
            this.key = key;
        }

        public @NotNull String getSerializedName()
        {
            return this.key;
        }
    }

}
