package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SignedGuide;
import io.netty.buffer.ByteBuf;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record SignGuidePayload(String signature) implements CustomPacketPayload
{
    public static final Type<SignGuidePayload> TYPE = new Type<>(Starcatcher.rl("sign_guide"), SignGuidePayload.class);

    public static final StreamCodec<SignGuidePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING,
            SignGuidePayload::signature,
            SignGuidePayload::new
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
            SignedGuide.SignGuide(signature, context.player());
        });
    }
}
