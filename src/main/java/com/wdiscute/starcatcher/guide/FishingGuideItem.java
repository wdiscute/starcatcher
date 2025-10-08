package com.wdiscute.starcatcher.guide;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FishingGuideItem extends Item
{
    public FishingGuideItem()
    {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(usedHand.equals(InteractionHand.OFF_HAND)) return super.use(level, player, usedHand);
        if(level.isClientSide) openScreen();
        return super.use(level, player, usedHand);
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen()
    {
        Minecraft.getInstance().setScreen(new FishingGuideScreen());
        //Minecraft.getInstance().setScreen(new FishingMinigameScreen(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, 1));
    }


}
