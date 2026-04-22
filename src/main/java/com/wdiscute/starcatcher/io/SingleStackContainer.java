package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

//use create() when obtaining the stack from the container to prevent crashing with empty stacks
public record SingleStackContainer(@Deprecated ItemStackTemplate stackDoNotUse)
{

    public static final Codec<SingleStackContainer> CODEC = ItemStackTemplate.CODEC.xmap(SingleStackContainer::new, SingleStackContainer::stackDoNotUse);

    public static final Codec<List<SingleStackContainer>> LIST_CODEC = SingleStackContainer.CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, SingleStackContainer> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, SingleStackContainer::stackDoNotUse,
            SingleStackContainer::new
    );

    public static SingleStackContainer from(ItemStack itemStack)
    {
        if(itemStack.isEmpty()) return new SingleStackContainer(null);
        return new SingleStackContainer(ItemStackTemplate.fromNonEmptyStack(itemStack));
    }

    public static SingleStackContainer from(ItemStackTemplate template)
    {
        return new SingleStackContainer(template);
    }

    public static SingleStackContainer from(Holder<Item> itemHolder)
    {
        return new SingleStackContainer(new ItemStackTemplate(itemHolder));
    }

    public static SingleStackContainer from(Item item)
    {
        return new SingleStackContainer(new ItemStackTemplate(item));
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, List<SingleStackContainer>> STREAM_CODEC_LIST = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static List<ItemStack> toItemStackList(List<SingleStackContainer> list)
    {
        List<ItemStack> is = new ArrayList<>();
        list.forEach(o -> is.add(o.create()));
        return is;
    }

    public static List<SingleStackContainer> fromItemStackList(List<ItemStack> list)
    {
        List<SingleStackContainer> ssc = new ArrayList<>();
        list.forEach(o -> ssc.add(from(o)));
        return ssc;
    }

    public ItemStack create()
    {
        return stackDoNotUse == null ? ItemStack.EMPTY : stackDoNotUse.create();
    }

    public static SingleStackContainer empty()
    {
        return new SingleStackContainer(null);
    }

    public static final List<SingleStackContainer> EMPTY_LIST = List.of();

    public boolean isEmpty()
    {
        return create().isEmpty();
    }
}
