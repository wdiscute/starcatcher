package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessage;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessageScreen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CBOpenEditableMessagePayload(EditableMessage message) implements CustomPacketPayload
{
    public static final Type<CBOpenEditableMessagePayload> TYPE = new Type<>(Starcatcher.rl("open_editable_message"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBOpenEditableMessagePayload> STREAM_CODEC = StreamCodec.composite(
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
