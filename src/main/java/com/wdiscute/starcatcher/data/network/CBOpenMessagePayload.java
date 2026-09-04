package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.messageinabottle.message.MessageScreen;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;

public record CBOpenMessagePayload(Message message) implements CustomPacketPayload
{
    public static final Type<CBOpenMessagePayload> TYPE = new Type<>(Starcatcher.rl("open_message"), CBOpenMessagePayload.class);

    public static final StreamCodec<CBOpenMessagePayload> STREAM_CODEC = StreamCodec.composite(
            Message.STREAM_CODEC, CBOpenMessagePayload::message,
            CBOpenMessagePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> MessageScreen.openMessageScreen(message));
    }
}
