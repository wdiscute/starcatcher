package com.wdiscute.starcatcher.io;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
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
    public static final Codec<List<LetterItem.Message>> CODEC = LetterItem.Message.CODEC.listOf();
    public static final String NAME = "starcaught_messages";

    private List<LetterItem.Message> messages = new ArrayList<>();

    public MessagesSavedData(List<LetterItem.Message> tournaments)
    {
        this.messages = tournaments;
    }

    public MessagesSavedData()
    {

    }

    public void addMessage(LetterItem.Message message)
    {
        messages.add(message);
    }

    public void removeMessage(LetterItem.Message message)
    {
        messages.remove(message);
    }

    public static MessagesSavedData get(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(MessagesSavedData::load, MessagesSavedData::new, NAME);
    }

    public List<LetterItem.Message> getMessages()
    {
        return messages;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        CODEC.encodeStart(NbtOps.INSTANCE, messages)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .ifPresent(tag -> compoundTag.put(NAME, tag));

        return compoundTag;
    }

    public static MessagesSavedData load(CompoundTag compoundTag)
    {
        Tag tag = compoundTag.get(NAME);

        List<LetterItem.Message> messagesNew = CODEC.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .map(Pair::getFirst)
                .orElseGet(ArrayList::new);

        return new MessagesSavedData(messagesNew);
    }
}
