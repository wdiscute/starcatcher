package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessage;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessageScreen;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;

public record CBOpenEditableMessagePayload(EditableMessage message) implements CustomPacketPayload
{
    public static final Type<CBOpenEditableMessagePayload> TYPE = new Type<>(Starcatcher.rl("open_editable_message"), CBOpenEditableMessagePayload.class);

    public static final StreamCodec<CBOpenEditableMessagePayload> STREAM_CODEC = StreamCodec.composite(
            EditableMessage.STREAM_CODEC, CBOpenEditableMessagePayload::message,
            CBOpenEditableMessagePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> EditableMessageScreen.openEditableMessageScreen(message));
    }
}
