package com.wdiscute.starcatcher.io.network.tournament;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;


public record CBClearTournamentPayload(String text) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<CBClearTournamentPayload> TYPE = new CustomPacketPayload.Type<>(Starcatcher.rl("cb_active_tournament_update"), CBClearTournamentPayload.class);

    public static final StreamCodec< CBClearTournamentPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING, CBClearTournamentPayload::text,
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
