package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CBFishCaughtNotifs(FishProperties fp, boolean displayToast, float percentile, boolean golden) implements CustomPacketPayload
{

    public static final Type<CBFishCaughtNotifs> TYPE = new Type<>(Starcatcher.rl("fish_caught_toast"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBFishCaughtNotifs> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC,
            CBFishCaughtNotifs::fp,
            ByteBufCodecs.BOOL,
            CBFishCaughtNotifs::displayToast,
            ByteBufCodecs.FLOAT,
            CBFishCaughtNotifs::percentile,
            ByteBufCodecs.BOOL,
            CBFishCaughtNotifs::golden,
            CBFishCaughtNotifs::new
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
