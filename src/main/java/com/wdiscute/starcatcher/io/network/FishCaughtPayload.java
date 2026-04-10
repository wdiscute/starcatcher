package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FishCaughtPayload(FishProperties fp, boolean newFish, int size, int weight,
                                float percentile) implements CustomPacketPayload
{

    public static final Type<FishCaughtPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FishCaughtPayload> STREAM_CODEC = StreamCodec.composite(
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
