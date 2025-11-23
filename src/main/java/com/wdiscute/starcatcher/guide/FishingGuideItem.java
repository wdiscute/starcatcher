package com.wdiscute.starcatcher.guide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FishingGuideItem extends Item
{
    public FishingGuideItem()
    {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(level.isClientSide) openScreen();
        level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen()
    {
        Minecraft.getInstance().setScreen(new FishingGuideScreen());
    }
}
