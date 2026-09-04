package com.wdiscute.starcatcher.data.network.tournament;


import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentLayer;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;

public record CBActiveTournamentUpdatePayload(Tournament tournament) implements CustomPacketPayload
{

    public static final Type<CBActiveTournamentUpdatePayload> TYPE = new Type<>(Starcatcher.rl("cb_clear_tournament"), CBActiveTournamentUpdatePayload.class);

    public static final StreamCodec<CBActiveTournamentUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            Tournament.STREAM_CODEC, CBActiveTournamentUpdatePayload::tournament,
            CBActiveTournamentUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(IPayloadContext context)
    {
        TournamentLayer.onTournamentReceived(tournament);
    }

}
