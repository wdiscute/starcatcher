package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public record SingleStackContainer(ItemStack stack)
{

    public static final Codec<SingleStackContainer> CODEC = ItemStack.OPTIONAL_CODEC.xmap(SingleStackContainer::new, SingleStackContainer::stack);

    public static final Codec<List<SingleStackContainer>> LIST_CODEC = SingleStackContainer.CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, SingleStackContainer> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, SingleStackContainer::stack,
            SingleStackContainer::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<SingleStackContainer>> STREAM_CODEC_LIST = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static List<SingleStackContainer> fromItemStackHandler(ItemStackHandler prizePool)
    {
        List<SingleStackContainer> list = new ArrayList<>();

        for (int i = 0; i < prizePool.getSlots(); i++)
        {
            list.add(new SingleStackContainer(prizePool.getStackInSlot(i)));
        }

        return list;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        SingleStackContainer other = (SingleStackContainer) o;
        return ItemStack.matches(this.stack, other.stack);
    }

    public static final SingleStackContainer EMPTY = new SingleStackContainer(ItemStack.EMPTY);
    public static final List<SingleStackContainer> EMPTY_LIST = List.of();
}
