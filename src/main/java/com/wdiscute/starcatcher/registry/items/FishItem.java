package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FishItem extends Item
{
    public FishItem(Item.Properties properties)
    {
        super(properties.usingConvertsTo(SCItems.FISH_BONES.asItem()));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        //cant eat if golden
        CaughtFishInfo cfi = SCDataComponents.get(player.getItemInHand(usedHand), SCDataComponents.CAUGHT_FISH_INFO);
        if (cfi != null && cfi.golden())
            return InteractionResult.FAIL;

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        if (context.getPlayer().isCreative())
        {
            FishEntity fe = new FishEntity(SCEntities.FISH.get(), context.getLevel());
            fe.setFish(context.getItemInHand().copyWithCount(1));
            fe.setPos(Vec3.atCenterOf(context.getClickedPos().relative(context.getClickedFace())));

            context.getLevel().addFreshEntity(fe);

            ItemStack is = context.getPlayer().getItemInHand(context.getHand());
            is.shrink(1);

            context.getPlayer().setItemInHand(context.getHand(), is);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
