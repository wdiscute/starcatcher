package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MossyBootItem extends Item
{
    public MossyBootItem(Properties p)
    {
        super(p.craftRemainder(SCItems.BOOT.asItem()));
    }
}
