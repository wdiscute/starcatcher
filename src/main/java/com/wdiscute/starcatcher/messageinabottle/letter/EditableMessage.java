package com.wdiscute.starcatcher.messageinabottle.letter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record EditableMessage(
        String sender,
        List<String> text
)
{
    public static final Codec<EditableMessage> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("sender").forGetter(EditableMessage::sender),
                    Codec.STRING.listOf().fieldOf("text").forGetter(EditableMessage::text)
            ).apply(instance, EditableMessage::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, EditableMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EditableMessage::sender,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), EditableMessage::text,
            EditableMessage::new
    );
}
