package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TournamentSettings
        (
                float perfectCatchMultiplier,
                int missPenalty

        )
{

        public static final Codec<TournamentSettings> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.optionalFieldOf("perfect_catch_multiplier", 0.0f).forGetter(TournamentSettings::perfectCatchMultiplier),
                        Codec.INT.optionalFieldOf("miss_penalty", 0).forGetter(TournamentSettings::missPenalty)
                ).apply(instance, TournamentSettings::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, TournamentSettings> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, TournamentSettings::perfectCatchMultiplier,
                ByteBufCodecs.VAR_INT, TournamentSettings::missPenalty,
                TournamentSettings::new
        );
}
