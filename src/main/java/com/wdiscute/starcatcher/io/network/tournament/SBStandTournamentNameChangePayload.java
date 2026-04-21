package com.wdiscute.starcatcher.io.network.tournament;


import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.StandMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.players.NameAndId;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.UUID;

public record SBStandTournamentNameChangePayload(UUID uuid, String name) implements CustomPacketPayload
{

    public static final StreamCodec<ByteBuf, NameAndId> GAME_PROFILE_STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, NameAndId::id,
            ByteBufCodecs.STRING_UTF8, NameAndId::name,
            NameAndId::new
    );

    public static final StreamCodec<ByteBuf, List<NameAndId>> GAME_PROFILE_STREAM_CODEC_LIST = GAME_PROFILE_STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final Type<SBStandTournamentNameChangePayload> TYPE = new Type<>(Starcatcher.rl("sb_stand_tournament_name"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SBStandTournamentNameChangePayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, SBStandTournamentNameChangePayload::uuid,
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
       context.enqueueWork(() -> {
           if(context.player().containerMenu instanceof StandMenu sm)
           {
               sm.sbe.tournament.name = name;
           }
       });
    }

}
