package com.wdiscute.starcatcher.data.network.tournament;


import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.StandMenu;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.UUID;

public record SBStandTournamentNameChangePayload(UUID uuid, String name) implements CustomPacketPayload
{
    public static final Type<SBStandTournamentNameChangePayload> TYPE = new Type<>(Starcatcher.rl("sb_stand_tournament_name"), SBStandTournamentNameChangePayload.class);

    public static final StreamCodec<SBStandTournamentNameChangePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.UUID, SBStandTournamentNameChangePayload::uuid,
            ByteBufCodecs.STRING_UTF8, SBStandTournamentNameChangePayload::name,
            SBStandTournamentNameChangePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            if (context.player().containerMenu instanceof StandMenu sm)
            {
                sm.sbe.tournament.name = name;
                sm.sbe.sync();
            }
        });
    }

}
