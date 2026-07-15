package com.wdiscute.starcatcher.messageinabottle.letter;

import com.wdiscute.starcatcher.data.network.CBOpenEditableMessagePayload;
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

public class LetterItem extends Item
{
    public LetterItem()
    {
        super(new Properties().stacksTo(1).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);

        EditableMessage editableMessage = SCDataComponents.get(stack, SCDataComponents.EDITABLE_MESSAGE);

        // if no message, set default
        if (editableMessage == null)
            editableMessage = new EditableMessage(player.getName().getString(), List.of());

        PacketDistributor.sendToPlayer((ServerPlayer) player, new CBOpenEditableMessagePayload(editableMessage));

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
