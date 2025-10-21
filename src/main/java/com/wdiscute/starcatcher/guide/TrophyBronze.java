package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.networkandcodecs.FishCaughtCounter;
import com.wdiscute.starcatcher.networkandcodecs.ModDataAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TrophyBronze extends Item
{

    public TrophyBronze()
    {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!player.isCreative())
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        //reset fishes caught
        player.setData(ModDataAttachments.FISHES_CAUGHT, new ArrayList<>());
        player.setData(ModDataAttachments.FISHES_NOTIFICATION, new ArrayList<>());
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
