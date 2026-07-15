package com.wdiscute.starcatcher.data.network.tournament;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record CBClearTournamentPayload(String text) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<CBClearTournamentPayload> TYPE = new CustomPacketPayload.Type<>(Starcatcher.rl("cb_active_tournament_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBClearTournamentPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CBClearTournamentPayload::text,
            CBClearTournamentPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        TournamentOverlay.clear();
    }
}
