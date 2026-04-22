package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.SCItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

//use create() when obtaining the stack
public record SingleStackContainer(Optional<ItemStackTemplate> stackDoNotUse)
{

    public static final Codec<SingleStackContainer> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.lenientOptionalFieldOf("stack").forGetter(SingleStackContainer::stackDoNotUse)
            ).apply(instance, SingleStackContainer::new)
    );

    public static SingleStackContainer from(ItemStack itemStack)
    {
        if (itemStack.isEmpty()) return new SingleStackContainer(Optional.empty());
        return new SingleStackContainer(Optional.of(ItemStackTemplate.fromNonEmptyStack(itemStack)));
    }

    public static SingleStackContainer from(ItemStackTemplate template)
    {
        return new SingleStackContainer(Optional.of(template));
    }

    public static SingleStackContainer from(Holder<Item> itemHolder)
    {
        return new SingleStackContainer(Optional.of(new ItemStackTemplate(itemHolder)));
    }

    public static SingleStackContainer from(Item item)
    {
        return new SingleStackContainer(Optional.of(new ItemStackTemplate(item)));
    }

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
        return stackDoNotUse.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
    }

    public static SingleStackContainer empty()
    {
        return new SingleStackContainer(Optional.empty());
    }

    public static final List<SingleStackContainer> EMPTY_LIST = List.of();

    public boolean isEmpty()
    {
        return create().isEmpty();
    }
}
