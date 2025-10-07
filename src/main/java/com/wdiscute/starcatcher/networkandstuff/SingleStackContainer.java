package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.ModItems;
import net.minecraft.world.item.ItemStack;

public record SingleStackContainer(ItemStack stack) {

    public static final Codec<SingleStackContainer> CODEC = ItemStack.OPTIONAL_CODEC.xmap(SingleStackContainer::new, SingleStackContainer::stack);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SingleStackContainer other = (SingleStackContainer) o;
        return ItemStack.matches(this.stack, other.stack);
    }

    public static final  SingleStackContainer EMPTY = new SingleStackContainer(ItemStack.EMPTY);
}
