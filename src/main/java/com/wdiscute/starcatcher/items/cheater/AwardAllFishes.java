package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.ModDataAttachments;
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

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            if(fp.hasGuideEntry())
                fishCounter.add(new FishCaughtCounter(U.getRlFromFp(level, fp), 999999, 0, 0, 0, 0, false, false));
        }


        player.setData(ModDataAttachments.FISHES_CAUGHT, fishCounter);

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            fishes.add(fp);
        }

        player.setData(ModDataAttachments.FISHES_NOTIFICATION, U.getRlsFromFps(level, fishes));

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


}
