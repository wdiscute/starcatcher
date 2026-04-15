package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import io.netty.buffer.ByteBuf;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record FishCaughtPayload(FishProperties fp, boolean newFish, int size, int weight,
                                float percentile) implements CustomPacketPayload
{

    public static final Type<FishCaughtPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught"), FishCaughtPayload.class);

    public static final StreamCodec< FishCaughtPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC,
            FishCaughtPayload::fp,
            ByteBufCodecs.BOOL,
            FishCaughtPayload::newFish,
            ByteBufCodecs.INT,
            FishCaughtPayload::size,
            ByteBufCodecs.INT,
            FishCaughtPayload::weight,
            ByteBufCodecs.FLOAT,
            FishCaughtPayload::percentile,
            FishCaughtPayload::new
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
            if (fp.hasGuideEntry())
                Starcatcher.fishCaughtToast(fp(), newFish(), size(), weight());
        });
    }
}
