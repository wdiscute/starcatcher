package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.networkandcodecs.ModDataAttachments;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RevokeAllSecrets extends Item
{
    public RevokeAllSecrets()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //revoke all secrets
        List<TrophyProperties> list = new ArrayList<>(player.getData(ModDataAttachments.TROPHIES_CAUGHT));

        player.getData(ModDataAttachments.TROPHIES_CAUGHT).forEach(tp ->
        {
            if(tp.trophyType() == TrophyProperties.TrophyType.SECRET) list.remove(tp);
        });

        player.setData(ModDataAttachments.TROPHIES_CAUGHT, list);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
