package com.wdiscute.starcatcher.messageinabottle.message;

import com.wdiscute.starcatcher.data.network.CBOpenEditableMessagePayload;
import com.wdiscute.starcatcher.data.network.CBOpenMessagePayload;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessage;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class MessageItem extends Item
{
    public MessageItem()
    {
        super(new Properties().stacksTo(1).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);

        //server only
        Message message = SCDataComponents.get(stack, SCDataComponents.MESSAGE);

        //if item has message, open message screen
        if (message != null)
            PacketDistributor.sendToPlayer((ServerPlayer) player, new CBOpenMessagePayload(message));
        else
            PacketDistributor.sendToPlayer((ServerPlayer) player, new CBOpenMessagePayload(Message.DEFAULT));

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
