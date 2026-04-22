package com.wdiscute.starcatcher.compat.curios;

import com.wdiscute.starcatcher.registry.items.HatItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CurioHatItem extends HatItem implements ICurioItem {
    public CurioHatItem(Block block, ResourceLocation... modifiers) {
        super(block, modifiers);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("head");
    }
}
