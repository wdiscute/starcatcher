package com.wdiscute.starcatcher.rod;

import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.fishingbob.FishingBobEntity;
import com.wdiscute.starcatcher.fishingbob.ModItems;
import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarcatcherFishingRod extends Item implements MenuProvider
{
    public StarcatcherFishingRod(Properties properties)
    {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected)
    {
        if (entity instanceof ServerPlayer player)
        {
            if (stack != player.getMainHandItem() && Boolean.TRUE.equals(stack.get(ModDataComponents.CAST)))
                stack.set(ModDataComponents.CAST, false);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {

        ItemStack itemstack = player.getItemInHand(hand).copy();

        if (player.isCrouching() && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().is(ModItems.STARCATCHER_FISHING_ROD.get()))
        {
            player.openMenu(this);
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }


        if (player.getData(ModDataAttachments.FISHING.get()).isEmpty())
        {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

            if (level instanceof ServerLevel)
            {
                ItemStack bobber;
                ItemStack bait;

                if (itemstack.get(DataComponents.CONTAINER) == null)
                {
                    bobber = ItemStack.EMPTY;
                    bait = ItemStack.EMPTY;
                }
                else
                {
                    bobber = itemstack.get(ModDataComponents.BOBBER.get()).copyOne();
                    bait = itemstack.get(ModDataComponents.BAIT.get()).copyOne();
                }

                Entity entity = new FishingBobEntity(level, player, bobber, bait);
                level.addFreshEntity(entity);
                if (!level.isClientSide) player.setData(ModDataAttachments.FISHING.get(), entity.getStringUUID());
                if (player.getMainHandItem().is(ModItems.STARCATCHER_FISHING_ROD))
                    player.getMainHandItem().set(ModDataComponents.CAST, true);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }
        else
        {

            List<Entity> entities = level.getEntities(null, new AABB(-25, -65, -25, 25, 65, 25).move(player.position()));

            for (Entity entity : entities)
            {
                if (entity.getUUID().toString().equals(player.getData(ModDataAttachments.FISHING.get())))
                {
                    if (entity instanceof FishingBobEntity fbe && !fbe.checkBiting())
                    {
                        fbe.kill();
                        if (!level.isClientSide) player.setData(ModDataAttachments.FISHING.get(), "");
                        if (player.getMainHandItem().is(ModItems.STARCATCHER_FISHING_ROD))
                            player.getMainHandItem().set(ModDataComponents.CAST, false);
                    }
                }
            }

        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {


        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("test fishing rod menu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        return new FishingRodMenu(i, inventory, player.getMainHandItem());

    }
}

