package com.wdiscute.starcatcher.data.network.tournament;


import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.Tournament;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.List;

public record CBFinishedTournamentsListPayload(List<Tournament> list) implements CustomPacketPayload
{
    public CBFinishedTournamentsListPayload
    {
        list = List.copyOf(list);
    }

    public static final Type<CBFinishedTournamentsListPayload> TYPE = new Type<>(Starcatcher.rl("cb_finished_tournaments_list"), CBFinishedTournamentsListPayload.class);

    public static final StreamCodec<CBFinishedTournamentsListPayload> STREAM_CODEC = StreamCodec.composite(
            Tournament.STREAM_CODEC.apply(ByteBufCodecs.list()), CBFinishedTournamentsListPayload::list,
            CBFinishedTournamentsListPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(IPayloadContext context)
    {
        StandScreen.finishedTournaments = list;
    }

}
