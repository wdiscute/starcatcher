package com.wdiscute.starcatcher.secretnotes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LetterItem extends Item
{
    public LetterItem()
    {
        super(new Properties().stacksTo(1).component(SCDataComponents.MESSAGE.get(), Message.empty()));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        if(SCDataComponents.has(stack, SCDataComponents.MESSAGE) && tooltipFlag.isAdvanced())
        {
            Message wd = SCDataComponents.get(stack, SCDataComponents.MESSAGE);
            tooltipComponents.add(Component.literal("written by uuid: " + wd.sender).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
        }
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        Message message = SCDataComponents.getOrDefault(player.getItemInHand(usedHand), SCDataComponents.MESSAGE, Message.empty());
        if(level.isClientSide)
        {
            if (message.locked)
                openMessageScreen(message);
            else
                openMessageWriteScreen(message);
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openMessageScreen(Message message)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new MessageScreen(message));
    }

    @OnlyIn(Dist.CLIENT)
    private void openMessageWriteScreen(Message message)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new MessageWriteScreen(message));
    }

    public record Message(
            UUID sender,
            String senderDisplayName,
            ResourceLocation dimension,
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
                        ResourceLocation.CODEC.fieldOf("dimension").forGetter(Message::dimension),
                        Codec.STRING.listOf().fieldOf("text").forGetter(Message::text),
                        Codec.BOOL.fieldOf("locked").forGetter(Message::locked)
                ).apply(instance, Message::new));

        public static final StreamCodec<Message> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.UUID, Message::sender,
                ByteBufCodecs.STRING, Message::senderDisplayName,
                ByteBufCodecs.RESOURCE_LOCATION, Message::dimension,
                ByteBufCodecs.STRING.apply(ByteBufCodecs.list()), Message::text,
                ByteBufCodecs.BOOL, Message::locked,
                Message::new
        );

        public Message lock()
        {
            return new Message(this.sender, this.senderDisplayName, this.dimension, this.text, true);
        }
    }
}
