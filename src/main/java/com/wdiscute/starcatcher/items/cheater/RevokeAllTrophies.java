package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.networkandcodecs.DataAttachments;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RevokeAllTrophies extends Item
{
    public RevokeAllTrophies()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(!(level instanceof ServerLevel)) return InteractionResultHolder.success(player.getItemInHand(usedHand));

        //revoke all trophies
        List<TrophyProperties> trpsCaught = new ArrayList<>(DataAttachments.get(player).trophiesCaught());

        DataAttachments.get(player).trophiesCaught().forEach(tp ->
        {
            if(tp.trophyType() == TrophyProperties.TrophyType.TROPHY) trpsCaught.remove(tp);
        });

        DataAttachments.get(player).setTrophiesCaught(trpsCaught);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
