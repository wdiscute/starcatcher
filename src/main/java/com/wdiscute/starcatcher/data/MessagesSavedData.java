package com.wdiscute.starcatcher.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.letter.LetterItem;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class MessagesSavedData extends SavedData
{
    public static final Codec<List<Message>> CODEC = Message.CODEC.listOf();
    public static final String NAME = "starcaught_messages";

    private List<Message> messages = new ArrayList<>();

    public MessagesSavedData(List<Message> tournaments)
    {
        this.messages = tournaments;
    }

    public MessagesSavedData()
    {

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
        return level.getDataStorage().computeIfAbsent(MessagesSavedData::load, MessagesSavedData::new, NAME);
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public static MessagesSavedData load(CompoundTag compoundTag)
    {
        Tag tag = compoundTag.get(NAME);

        List<Message> messagesNew = CODEC.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .map(Pair::getFirst)
                .orElseGet(ArrayList::new);

        return new MessagesSavedData(messagesNew);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        CODEC.encodeStart(NbtOps.INSTANCE, messages)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .ifPresent(tag -> compoundTag.put(NAME, tag));

        return compoundTag;
    }
}
