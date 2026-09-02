package com.wdiscute.starcatcher.messageinabottle.message;

import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MessageInABottleItem extends Item
{
    public MessageInABottleItem(Properties p)
    {
        super(p.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack itemInHand = player.getItemInHand(usedHand);

        Message message = SCDataComponents.getOrDefault(itemInHand, SCDataComponents.MESSAGE, Message.DEFAULT);

        //give note
        ItemStack is = new ItemStack(SCItems.MESSAGE.get());
        SCDataComponents.set(is, SCDataComponents.MESSAGE, message);
        player.addItem(is);

        player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);

        //replace with broken bottle
        player.setItemInHand(usedHand, new ItemStack(SCItems.BROKEN_BOTTLE.get()));

        return InteractionResult.SUCCESS;
    }
}
