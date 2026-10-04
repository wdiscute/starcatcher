package com.wdiscute.starcatcher.blocks.tacklebox;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class TackleBoxBlockItem extends BlockItem
{
    public TackleBoxBlockItem(Block block, Properties properties)
    {
        super(block, properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new TackleBoxTooltip(stack));
    }

    public record TackleBoxTooltip(ItemStack box) implements TooltipComponent
    {
    }
}
