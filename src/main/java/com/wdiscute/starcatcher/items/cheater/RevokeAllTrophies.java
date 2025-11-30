package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.TrophyProperties;
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
        //revoke all trophies
        List<TrophyProperties> list = new ArrayList<>(U.getTpsFromRls(level, player.getData(ModDataAttachments.TROPHIES_CAUGHT)));

        player.getData(ModDataAttachments.TROPHIES_CAUGHT).forEach(tp ->
        {
            if(U.getTpFromRl(level, tp).trophyType() == TrophyProperties.TrophyType.TROPHY) list.remove(tp);
        });

        player.setData(ModDataAttachments.TROPHIES_CAUGHT, U.getRlsFromTps(level, list));
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
