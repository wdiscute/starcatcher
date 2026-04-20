package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class TackleSkinItem extends Item
{
    public TackleSkinItem(Identifier rl)
    {
        super(new Item.Properties()
                .component(SCDataComponents.TACKLE_SKIN, rl)
        );
    }
}
