package com.wdiscute.starcatcher.registry.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;

public class HatItem extends BlockItem
{
    public HatItem(Properties p, Block block)
    {
        super(block, p
                .stacksTo(1)
                .component(
                        DataComponents.EQUIPPABLE,
                        Equippable
                                .builder(EquipmentSlot.HEAD)
                                .build()
                ));
    }
}
