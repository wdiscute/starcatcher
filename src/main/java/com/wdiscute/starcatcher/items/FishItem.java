package com.wdiscute.starcatcher.items;

import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class FishItem extends Item
{
    public FishItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        //Twitch chat didn't force me to do write this
        FishEntity fe = new FishEntity(ModEntities.FISH.get(), context.getLevel());
        fe.setFish(context.getItemInHand().copyWithCount(1));
        fe.setPos(new Vec3(
                context.getClickedPos().relative(context.getClickedFace()).getX() + 0.5f,
                context.getClickedPos().relative(context.getClickedFace()).getY() + 0.5f,
                context.getClickedPos().relative(context.getClickedFace()).getZ() + 0.5f
        ));

        context.getLevel().addFreshEntity(fe);

        ItemStack is = context.getPlayer().getItemInHand(context.getHand());
        is.shrink(1);

        context.getPlayer().setItemInHand(context.getHand(), is);

        return InteractionResult.CONSUME;
    }
}
