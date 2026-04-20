package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
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
                    LetterItem.Message.CODEC.listOf().fieldOf("messages").forGetter(o -> o.messages)
            ).apply(instance, MessagesSavedData::new))
    );

    public List<LetterItem.Message> messages;

    public MessagesSavedData(List<LetterItem.Message> messages)
    {
        this.messages = new ArrayList<>(messages);
        setDirty();
    }

    public MessagesSavedData(ServerLevel sl)
    {
        this.messages = new ArrayList<>();
        setDirty();
    }

    public void addMessage(LetterItem.Message message)
    {
        messages.add(message);
        setDirty();
    }

    public void removeMessage(LetterItem.Message message)
    {
        messages.remove(message);
        setDirty();
    }

    public static MessagesSavedData get(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(ID);
    }

    public List<LetterItem.Message> getMessages()
    {
        return messages;
    }
}
