package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.networkandcodecs.FishCaughtCounter;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import com.wdiscute.starcatcher.networkandcodecs.DataAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class RevokeAllFishes extends Item
{
    public RevokeAllFishes()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!(level instanceof ServerLevel)) return InteractionResultHolder.success(player.getItemInHand(usedHand));

        //reset fishes caught
        DataAttachments.get(player).setFishesCaught(List.of(new FishCaughtCounter(FishProperties.DEFAULT, 0, 0,0,0,0, false)));
        DataAttachments.get(player).setFishNotifications(List.of(FishProperties.DEFAULT));
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
