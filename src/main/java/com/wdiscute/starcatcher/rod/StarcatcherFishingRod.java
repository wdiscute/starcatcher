package com.wdiscute.starcatcher.rod;

import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarcatcherFishingRod extends Item implements MenuProvider
{
    public StarcatcherFishingRod()
    {
        super(new Item.Properties()
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .component(ModDataComponents.BOBBER.get(), SingleStackContainer.EMPTY)
                .component(ModDataComponents.BAIT.get(), SingleStackContainer.EMPTY)
                .component(ModDataComponents.HOOK.get(), new SingleStackContainer(new ItemStack(ModItems.HOOK.get())))
        );
    }


    //comment - kuko010
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if (!player.getItemInHand(hand).is(ModItems.ROD))
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        if (player.isCrouching())
        {
            player.openMenu(this);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        if (level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(hand));


        if (player.getData(ModDataAttachments.FISHING.get()).isEmpty())
        {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

            if (level instanceof ServerLevel)
            {
                //TODO ADD CUSTOM STAT FOR NUMBER OF FISHES CAUGHT TOTAL ON STAT SCREEN

                Entity entity = new FishingBobEntity(level, player, player.getItemInHand(hand));
                level.addFreshEntity(entity);

                player.setData(ModDataAttachments.FISHING.get(), entity.getStringUUID());
                entity.setData(ModDataAttachments.BOBBER.get(), player.getItemInHand(hand).get(ModDataComponents.BOBBER));
            }
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
                        player.setData(ModDataAttachments.FISHING.get(), "");
                    }
                }
            }

        }


        return InteractionResultHolder.success(player.getItemInHand(hand));
    }


    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack)
    {
        return itemStack.copy();
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("Starcatcher's rod");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        if (player.getMainHandItem().is(ModItems.ROD))
            return new FishingRodMenu(i, inventory, player.getMainHandItem());
        else
            return new FishingRodMenu(i, inventory, player.getOffhandItem());
    }
}

