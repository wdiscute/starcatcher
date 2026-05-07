package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.StarcatcherClient;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.blocks.display.DisplayBlock;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SignedGuide;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FishingGuideItem extends Item
{
    public FishingGuideItem(Properties properties)
    {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        //if clicked on empty lectern
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(clickedPos);
        if (blockState.is(Blocks.LECTERN))
        {
            if (!blockState.getValue(LecternBlock.HAS_BOOK))
            {
                level.setBlockAndUpdate(clickedPos, SCBlocks.DISPLAY.get().defaultBlockState()
                        .setValue(DisplayBlock.HAS_ITEM, true)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
                );

                if (level.getBlockEntity(clickedPos) instanceof DisplayBlockEntity dbe)
                {
                    dbe.setItem(context.getItemInHand().consumeAndReturn(1, context.getPlayer()));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        if (level.isClientSide())
        {
            if (SCDataComponents.has(player.getItemInHand(usedHand), SCDataComponents.SIGNED_GUIDE))
                StarcatcherClient.openSignedGuideScreen(SCDataComponents.get(player.getItemInHand(usedHand), SCDataComponents.SIGNED_GUIDE));
            else
                StarcatcherClient.openGuideScreen();
        }

        level.playSound(player, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS);
        return InteractionResult.SUCCESS;
    }
}
