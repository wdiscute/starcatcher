package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record FishingCompletedPayload(int time, boolean completedTreasure, boolean perfectCatch,
                                      int hits) implements CustomPacketPayload
{
    public static final Type<FishingCompletedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_completed"), FishingCompletedPayload.class);

    public static final StreamCodec<FishingCompletedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FishingCompletedPayload::time,
            ByteBufCodecs.BOOL,
            FishingCompletedPayload::completedTreasure,
            ByteBufCodecs.BOOL,
            FishingCompletedPayload::perfectCatch,
            ByteBufCodecs.INT,
            FishingCompletedPayload::hits,
            FishingCompletedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork( () -> {
            FishProperties.spawnFishFromPlayerFishing(((ServerPlayer) context.player()), time, completedTreasure, perfectCatch, hits);
        });
    }
}
