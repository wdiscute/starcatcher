package com.wdiscute.starcatcher.data.network.tournament;


import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CBActiveTournamentUpdatePayload(Tournament tournament) implements CustomPacketPayload
{

    public static final Type<CBActiveTournamentUpdatePayload> TYPE = new Type<>(Starcatcher.rl("cb_clear_tournament"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBActiveTournamentUpdatePayload> STREAM_CODEC = StreamCodec.composite(
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
        TournamentOverlay.onTournamentReceived(tournament);
    }

}
