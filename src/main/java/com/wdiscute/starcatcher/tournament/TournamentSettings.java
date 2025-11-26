package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record TournamentSettings
        (
                Type type,
                float perfectCatchMultiplier,
                int missPenalty

        )
{
    public enum Type implements StringRepresentable
    {
        SIMPLE("simple"),
        SCORE_BASED("score_based");

        Type(String name)
        {
            this.key = name;
        }

        public String toString()
        {
            return this.key;
        }

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        public static final StreamCodec<RegistryFriendlyByteBuf, Type> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Type.class);
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }
        }

    public static final Codec<TournamentSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Type.CODEC.optionalFieldOf("type", Type.SIMPLE).forGetter(TournamentSettings::type),
                    Codec.FLOAT.optionalFieldOf("perfect_catch_multiplier", 0.0f).forGetter(TournamentSettings::perfectCatchMultiplier),
                    Codec.INT.optionalFieldOf("miss_penalty", 0).forGetter(TournamentSettings::missPenalty)
            ).apply(instance, TournamentSettings::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TournamentSettings> STREAM_CODEC = StreamCodec.composite(
            Type.STREAM_CODEC, TournamentSettings::type,
            ByteBufCodecs.FLOAT, TournamentSettings::perfectCatchMultiplier,
            ByteBufCodecs.VAR_INT, TournamentSettings::missPenalty,
            TournamentSettings::new
    );
}
