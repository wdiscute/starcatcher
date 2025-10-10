package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record SingleStackContainer(ItemStack stack)
{

    public static final Codec<SingleStackContainer> CODEC = ItemStack.OPTIONAL_CODEC.xmap(SingleStackContainer::new, SingleStackContainer::stack);

    public static final StreamCodec<RegistryFriendlyByteBuf, SingleStackContainer> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, SingleStackContainer::stack,
            SingleStackContainer::new
    );

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        SingleStackContainer other = (SingleStackContainer) o;
        return ItemStack.matches(this.stack, other.stack);
    }

    public static final SingleStackContainer EMPTY = new SingleStackContainer(ItemStack.EMPTY);
}
