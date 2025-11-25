package com.wdiscute.starcatcher.items;

import com.wdiscute.starcatcher.blocks.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

public class ModBrushItem extends Item
{
    public ModBrushItem()
    {
        super(new Item.Properties().durability(12));
    }


    private static final Map<Block, Block> Registro = Map.of(
            Blocks.OAK_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.BIRCH_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.SPRUCE_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.CHERRY_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.JUNGLE_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.DARK_OAK_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.CRIMSON_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.WARPED_PLANKS, ModBlocks.TROPHY_GOLD.get(),
            Blocks.ACACIA_PLANKS, ModBlocks.TROPHY_GOLD.get()
    );

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        pTooltipComponents.add(Component.translatable("tooltip.tf2.brush.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {

        Level level = context.getLevel();
        Block Clickedblock = level.getBlockState(context.getClickedPos()).getBlock();
        ItemStack itemstack = context.getItemInHand();

        if (Registro.containsKey(Clickedblock))
        {
            if (!level.isClientSide)
            {
                level.setBlockAndUpdate(context.getClickedPos(), Registro.get(Clickedblock).defaultBlockState());
                context.getItemInHand().hurtAndBreak(1, context.getPlayer(),
                        player -> player.broadcastBreakEvent(player.getUsedItemHand()));

                if (itemstack.isEmpty())
                {
                    ItemStack itemstack1 = new ItemStack(Items.FISHING_ROD);
                    itemstack1.setTag(itemstack.getTag());
                    context.getPlayer().setItemInHand(context.getHand(), itemstack1);
                    return InteractionResult.SUCCESS;
                }

                //level.playSound(null, p_41427_.getClickedPos(), ModSounds.SPLASH.get(), SoundSource.BLOCKS, 1.0F, 5.0F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }


}
