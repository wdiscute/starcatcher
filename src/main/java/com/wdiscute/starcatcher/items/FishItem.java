package com.wdiscute.starcatcher.items;

import com.wdiscute.starcatcher.ModEntities;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MoverType;
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
        FishEntity fe = new FishEntity(ModEntities.FISH.get(), context.getLevel());
        fe.setFish(new ItemStack(this.asItem()));
        fe.setPos(new Vec3(context.getClickedPos().above().getX(), context.getClickedPos().above().getY(), context.getClickedPos().above().getZ()));

        context.getLevel().addFreshEntity(fe);



        return InteractionResult.CONSUME;
    }
}
