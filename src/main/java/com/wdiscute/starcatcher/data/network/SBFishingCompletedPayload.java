package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record SBFishingCompletedPayload(boolean completed, int time, boolean completedTreasure, boolean perfectCatch,
                                        int hits) implements CustomPacketPayload
{
    public static final Type<SBFishingCompletedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_completed"), SBFishingCompletedPayload.class);

    public static final StreamCodec<SBFishingCompletedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SBFishingCompletedPayload::completed,
            ByteBufCodecs.INT,
            SBFishingCompletedPayload::time,
            ByteBufCodecs.BOOL,
            SBFishingCompletedPayload::completedTreasure,
            ByteBufCodecs.BOOL,
            SBFishingCompletedPayload::perfectCatch,
            ByteBufCodecs.INT,
            SBFishingCompletedPayload::hits,
            SBFishingCompletedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork( () ->
                FishApi.spawnFishFromPlayerFishing(((ServerPlayer) context.player()), completed, time, completedTreasure, perfectCatch, hits));
    }
}
