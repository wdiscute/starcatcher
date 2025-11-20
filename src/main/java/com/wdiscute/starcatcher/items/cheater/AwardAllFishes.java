package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.networkandcodecs.FishCaughtCounter;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import com.wdiscute.starcatcher.networkandcodecs.ModDataAttachments;
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
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!player.isCreative())
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        //sets all fps on fishes caught to 1
        List<FishCaughtCounter> fishCounter = new ArrayList<>();
        List<FishProperties> fishes = new ArrayList<>();

        FishProperties awd;

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            if(fp.hasGuideEntry())
                fishCounter.add(new FishCaughtCounter(fp, 999999, 0, 0, 0, 0, false));
        }


        player.setData(ModDataAttachments.FISHES_CAUGHT, fishCounter);

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            fishes.add(fp);
        }

        player.setData(ModDataAttachments.FISHES_NOTIFICATION, fishes);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


}
