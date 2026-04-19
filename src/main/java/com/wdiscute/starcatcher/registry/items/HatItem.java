package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HatItem extends BlockItem
// implements Equipable
// waiting on curios port
{

    public HatItem(Block block, String name)
    {
        super(block, new Properties()
                .stacksTo(1)
                .setId(ResourceKey.create(Registries.ITEM, Starcatcher.rl(name)))
        );
    }

//    @Override
//    public @NotNull EquipmentSlot getEquipmentSlot()
//    {
//        return EquipmentSlot.HEAD;
//    }
}
