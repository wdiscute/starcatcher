package com.wdiscute.starcatcher.registry.items.helper;

import net.minecraft.world.item.Item;

public class SingleStackBasicItem extends Item
{
    public SingleStackBasicItem(Properties properties)
    {
        super(properties.stacksTo(1));
    }
}
