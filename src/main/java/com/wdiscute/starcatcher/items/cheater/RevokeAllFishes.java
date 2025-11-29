package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.io.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RevokeAllFishes extends Item
{
    public RevokeAllFishes()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //reset fishes caught
        player.removeData(ModDataAttachments.FISHES_CAUGHT);
        player.removeData(ModDataAttachments.FISHES_NOTIFICATION);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
