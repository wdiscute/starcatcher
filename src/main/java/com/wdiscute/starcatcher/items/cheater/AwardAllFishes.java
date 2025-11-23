package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.networkandcodecs.DataAttachments;
import com.wdiscute.starcatcher.networkandcodecs.FishCaughtCounter;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AwardAllFishes extends Item
{
    public AwardAllFishes()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!player.isCreative())
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        if(!(level instanceof ServerLevel)) return InteractionResultHolder.success(player.getItemInHand(usedHand));

        //sets all fps on fishes caught to 1
        List<FishCaughtCounter> fishCounter = new ArrayList<>();
        List<FishProperties> fishes = new ArrayList<>();

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            fishCounter.add(new FishCaughtCounter(fp, 999999, 0, 0, 0, 0, false));
        }

        DataAttachments.get(player).setFishesCaught(fishCounter);

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            fishes.add(fp);
        }

        DataAttachments.get(player).setFishNotifications(fishes);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


}
