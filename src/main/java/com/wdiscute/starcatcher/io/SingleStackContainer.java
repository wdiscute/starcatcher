package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.ArrayList;
import java.util.List;

//use stack() when obtaining the stack from the container to prevent accidental mutation of the itemstack
public record SingleStackContainer(@Deprecated ItemStack stackDoNotUse)
{

    public static final Codec<SingleStackContainer> CODEC = ItemStack.CODEC.xmap(SingleStackContainer::new, SingleStackContainer::stackDoNotUse);

    public static final Codec<List<SingleStackContainer>> LIST_CODEC = SingleStackContainer.CODEC.listOf();

    public static final StreamCodec<SingleStackContainer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.ITEM_STACK, SingleStackContainer::stackDoNotUse,
            SingleStackContainer::new
    );

    public static SingleStackContainer from(ItemStack itemStack)
    {
        return new SingleStackContainer(itemStack.copy());
    }

    public static final StreamCodec<List<SingleStackContainer>> STREAM_CODEC_LIST = STREAM_CODEC.apply(ByteBufCodecs.list());

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
        return ItemStack.matches(this.stackDoNotUse, other.stackDoNotUse);
    }

    public ItemStack stack()
    {
        return stackDoNotUse.copy();
    }

    public static SingleStackContainer empty(){return new SingleStackContainer(ItemStack.EMPTY.copy());}

    public static final List<SingleStackContainer> EMPTY_LIST = List.of();

    public boolean isEmpty()
    {
        return stack().isEmpty();
    }
}
