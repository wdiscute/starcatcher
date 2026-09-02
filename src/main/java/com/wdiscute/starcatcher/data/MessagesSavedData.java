package com.wdiscute.starcatcher.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

public class MessagesSavedData extends SavedData
{
    public static final SavedDataType<MessagesSavedData> ID = new SavedDataType<>(
            Starcatcher.rl("starcaught_messages"),
            MessagesSavedData::new,
            _ ->
                    RecordCodecBuilder.create(instance -> instance.group(
                            Message.CODEC.listOf().fieldOf("messages").forGetter(o -> o.messages)
                    ).apply(instance, MessagesSavedData::new))
    );

    public static final Codec<List<Message>> CODEC = Message.CODEC.listOf();
    public static final String NAME = "starcaught_messages";

    private List<Message> messages = new ArrayList<>();

    public MessagesSavedData(List<Message> tournaments)
    {
        this.messages = tournaments;
        setDirty();
    }

    public MessagesSavedData(ServerLevel sl)
    {
        this.messages = new ArrayList<>();
        setDirty();
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }

    public void removeMessage(Message message)
    {
        messages.remove(message);
    }

    public static MessagesSavedData get(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(ID);
    }

    public List<Message> getMessages()
    {
        return messages;
    }
}
