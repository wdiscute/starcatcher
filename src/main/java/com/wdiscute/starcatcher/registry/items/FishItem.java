package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.StarcatcherClient;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.fishentity.SCArmPoses;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class FishItem extends Item
{
    public FishItem(Item.Properties properties)
    {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving)
    {
        ItemStack itemstack = super.finishUsingItem(stack, level, entityLiving);
        return entityLiving instanceof Player && ((Player) entityLiving).getAbilities().instabuild ? itemstack : new ItemStack(SCItems.FISH_BONES.asItem());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(StarcatcherClient.HOLD_ABOVE_HEAD);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //cant eat if golden
        CaughtFishInfo cfi = SCDataComponents.get(player.getItemInHand(usedHand), SCDataComponents.CAUGHT_FISH_INFO);
        if (cfi != null && cfi.golden())
            return InteractionResultHolder.fail(player.getItemInHand(usedHand));

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        if (context.getPlayer().isCreative())
        {
            FishEntity fe = new FishEntity(SCEntities.FISH.get(), context.getLevel());
            fe.setFish(context.getItemInHand().copyWithCount(1));
            fe.setPos(context.getClickedPos().relative(context.getClickedFace()).getCenter());

            context.getLevel().addFreshEntity(fe);

            ItemStack is = context.getPlayer().getItemInHand(context.getHand());
            is.shrink(1);

            context.getPlayer().setItemInHand(context.getHand(), is);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
