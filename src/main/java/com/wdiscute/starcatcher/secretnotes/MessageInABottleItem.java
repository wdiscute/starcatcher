package com.wdiscute.starcatcher.secretnotes;

import com.wdiscute.starcatcher.io.SCDataComponents;
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
    public MessageInABottleItem(Properties properties)
    {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        if(SCDataComponents.has(itemInHand, SCDataComponents.MESSAGE))
        {
            //give note
            ItemStack is = new ItemStack(SCItems.MESSAGE.get());
            LetterItem.Message message = SCDataComponents.get(itemInHand, SCDataComponents.MESSAGE);
            SCDataComponents.set(is, SCDataComponents.MESSAGE, message.lock());
            player.addItem(is);

            player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);

            //replace with broken bottle
            player.setItemInHand(usedHand, new ItemStack(SCItems.BROKEN_BOTTLE.get()));
        }

        return InteractionResult.SUCCESS;
    }
}
