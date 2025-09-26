package com.wdiscute.starcatcher.items;

import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class TrophyBronze extends Item
{

    public TrophyBronze(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //reset fishes caught
        player.setData(ModDataAttachments.FISHES_CAUGHT, new ArrayList<>());
        player.setData(ModDataAttachments.FISHES_NOTIFICATION, new ArrayList<>());
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
