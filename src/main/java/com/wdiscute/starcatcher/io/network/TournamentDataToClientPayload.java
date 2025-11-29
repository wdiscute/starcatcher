package com.wdiscute.starcatcher.io.network;


import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.Tournament;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.List;

public record TournamentDataToClientPayload(List<GameProfile> listSignups,
                                            Tournament tour) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, GameProfile> GAME_PROFILE_STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, GameProfile::getId,
            ByteBufCodecs.STRING_UTF8, GameProfile::getName,
            GameProfile::new
    );

    public static final StreamCodec<ByteBuf, List<GameProfile>> GAME_PROFILE_STREAM_CODEC_LIST = GAME_PROFILE_STREAM_CODEC.apply(ByteBufCodecs.list());


    public static final Type<TournamentDataToClientPayload> TYPE = new Type<>(Starcatcher.rl("tour"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TournamentDataToClientPayload> STREAM_CODEC = StreamCodec.composite(
            GAME_PROFILE_STREAM_CODEC_LIST, TournamentDataToClientPayload::listSignups,
            Tournament.STREAM_CODEC, TournamentDataToClientPayload::tour,
            TournamentDataToClientPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public void handle(IPayloadContext context) {
        clientReceiveTournamentData(this, context);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientReceiveTournamentData(TournamentDataToClientPayload data, final IPayloadContext context) {

        StandScreen.getTournamentCache(data.tour());
        StandScreen.gameProfilesCache = new HashMap<>();
        data.listSignups().forEach(e -> StandScreen.gameProfilesCache.put(e.getId(), e.getName()));
    }
}
