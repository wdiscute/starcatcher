package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.networkandcodecs.ExtraComposites;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import com.wdiscute.starcatcher.networkandcodecs.SingleStackContainer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.UUID;

public record TournamentPlayerScore
        (
                int score,
                int misses,
                UUID uuid,
                int common,
                int uncommon,
                int rare,
                int epic,
                int legendary
        )
{
        public static final Codec<TournamentPlayerScore> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.optionalFieldOf("score", 0).forGetter(TournamentPlayerScore::score),
                        Codec.INT.optionalFieldOf("misses", 0).forGetter(TournamentPlayerScore::misses),
                        UUIDUtil.CODEC.optionalFieldOf("uuid", UUID.fromString("0")).forGetter(TournamentPlayerScore::uuid),
                        Codec.INT.optionalFieldOf("common", 0).forGetter(TournamentPlayerScore::common),
                        Codec.INT.optionalFieldOf("uncommon", 0).forGetter(TournamentPlayerScore::uncommon),
                        Codec.INT.optionalFieldOf("rare", 0).forGetter(TournamentPlayerScore::rare),
                        Codec.INT.optionalFieldOf("epic", 0).forGetter(TournamentPlayerScore::epic),
                        Codec.INT.optionalFieldOf("legendary", 0).forGetter(TournamentPlayerScore::legendary)
                ).apply(instance, TournamentPlayerScore::new)
        );

        public static final Codec<List<TournamentPlayerScore>> LIST_CODEC = TournamentPlayerScore.CODEC.listOf();

        public static final StreamCodec<RegistryFriendlyByteBuf, TournamentPlayerScore> STREAM_CODEC = ExtraComposites.composite(
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::score,
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::misses,
                UUIDUtil.STREAM_CODEC, TournamentPlayerScore::uuid,
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::common,
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::uncommon,
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::rare,
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::epic,
                ByteBufCodecs.VAR_INT, TournamentPlayerScore::legendary,
                TournamentPlayerScore::new
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, List<TournamentPlayerScore>> STREAM_CODEC_LIST = STREAM_CODEC.apply(ByteBufCodecs.list());


}
