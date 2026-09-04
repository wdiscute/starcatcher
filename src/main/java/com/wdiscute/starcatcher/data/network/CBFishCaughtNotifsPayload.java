package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record CBFishCaughtNotifsPayload(FishProperties fp, boolean displayToast, float percentile, boolean golden) implements CustomPacketPayload
{

    public static final Type<CBFishCaughtNotifsPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught_toast"), CBFishCaughtNotifsPayload.class);

    public static final StreamCodec<CBFishCaughtNotifsPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC,
            CBFishCaughtNotifsPayload::fp,
            ByteBufCodecs.BOOL,
            CBFishCaughtNotifsPayload::displayToast,
            ByteBufCodecs.FLOAT,
            CBFishCaughtNotifsPayload::percentile,
            ByteBufCodecs.BOOL,
            CBFishCaughtNotifsPayload::golden,
            CBFishCaughtNotifsPayload::new
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
                FishCaughtToast.newFish(fp(), displayToast, percentile, golden);
        });
    }
}
