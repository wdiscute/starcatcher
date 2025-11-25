package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TournamentSettings
        (
                boolean idk
        )
{

        public static final Codec<TournamentSettings> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.optionalFieldOf("score", true).forGetter(TournamentSettings::idk)
                ).apply(instance, TournamentSettings::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, TournamentSettings> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, TournamentSettings::idk,
                TournamentSettings::new
        );
}
