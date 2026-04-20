package com.wdiscute.starcatcher.io.network.tournament;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record CBActiveTournamentUpdatePayload(List<NameAndId> listSignups, Tournament tour) implements CustomPacketPayload
{

    public static CBActiveTournamentUpdatePayload helper(Player player, Tournament tournament)
    {
        if (player.level().isClientSide()) throw new RuntimeException();
        List<NameAndId> list = new ArrayList<>();
        for (var entry : tournament.playerScores)
        {
            UserNameToIdResolver resolver = player.level().getServer().services().nameToIdCache();
            Optional<NameAndId> gameProfile = resolver.get(entry.playerUUID);
            gameProfile.ifPresent(list::add);
        }

        return new CBActiveTournamentUpdatePayload(list, tournament);
    }

    public static final StreamCodec<ByteBuf, NameAndId> NAME_AND_ID_STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, NameAndId::id,
            ByteBufCodecs.STRING_UTF8, NameAndId::name,
            NameAndId::new
    );

    public static final StreamCodec<ByteBuf, List<NameAndId>> NAME_AND_ID_STREAM_CODEC_LIST = NAME_AND_ID_STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final Type<CBActiveTournamentUpdatePayload> TYPE = new Type<>(Starcatcher.rl("cb_clear_tournament"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBActiveTournamentUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            NAME_AND_ID_STREAM_CODEC_LIST, CBActiveTournamentUpdatePayload::listSignups,
            Tournament.STREAM_CODEC, CBActiveTournamentUpdatePayload::tour,
            CBActiveTournamentUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(IPayloadContext context)
    {
        TournamentOverlay.onTournamentReceived(tour, listSignups);
    }

}
