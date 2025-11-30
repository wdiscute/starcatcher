package com.wdiscute.starcatcher.items.cheater;

import com.wdiscute.starcatcher.Starcatcher;
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

public class AwardAllSecrets extends Item
{
    public AwardAllSecrets()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //awards all secrets
        List<TrophyProperties> trophies = new ArrayList<>(U.getTpsFromRls(level, player.getData(ModDataAttachments.TROPHIES_CAUGHT)));

        level.registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY).forEach(
                tp ->
                {
                    if(tp.trophyType() == TrophyProperties.TrophyType.SECRET && !trophies.contains(tp))
                        trophies.add(tp);
                });

        player.setData(ModDataAttachments.TROPHIES_CAUGHT, U.getRlsFromTps(level, trophies));
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
