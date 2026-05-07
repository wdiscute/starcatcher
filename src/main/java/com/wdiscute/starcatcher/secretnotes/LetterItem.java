package com.wdiscute.starcatcher.secretnotes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.StarcatcherClient;
import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LetterItem extends Item
{
    public LetterItem(Properties properties)
    {
        super(properties.stacksTo(1).component(SCDataComponents.MESSAGE, Message.empty()));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        Message message = SCDataComponents.getOrDefault(player.getItemInHand(usedHand), SCDataComponents.MESSAGE, Message.empty());
        if(level.isClientSide())
        {
            if (message.locked)
                StarcatcherClient.openMessageScreen(message);
            else
                StarcatcherClient.openMessageWriteScreen(message);
        }
        return InteractionResult.SUCCESS;
    }

    public record Message(
            UUID sender,
            String senderDisplayName,
            Identifier dimension,
            List<String> text,
            boolean locked
    )
    {
        public static Message empty()
        {
            return new Message(UUID.randomUUID(), "-Your Name", Starcatcher.rl(""), new ArrayList<>(List.of("[click to edit]")), false);
        }

        public static final Codec<Message> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        UUIDUtil.CODEC.fieldOf("sender").forGetter(Message::sender),
                        Codec.STRING.fieldOf("sender_display_name").forGetter(Message::senderDisplayName),
                        Identifier.CODEC.fieldOf("dimension").forGetter(Message::dimension),
                        Codec.STRING.listOf().fieldOf("text").forGetter(Message::text),
                        Codec.BOOL.fieldOf("locked").forGetter(Message::locked)
                ).apply(instance, Message::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Message> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, Message::sender,
                ByteBufCodecs.STRING_UTF8, Message::senderDisplayName,
                Identifier.STREAM_CODEC, Message::dimension,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), Message::text,
                ByteBufCodecs.BOOL, Message::locked,
                Message::new
        );

        public Message lock()
        {
            return new Message(this.sender, this.senderDisplayName, this.dimension, this.text, true);
        }
    }
}
