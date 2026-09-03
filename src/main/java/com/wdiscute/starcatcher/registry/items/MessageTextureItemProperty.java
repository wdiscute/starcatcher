package com.wdiscute.starcatcher.registry.items;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record MessageTextureItemProperty() implements RangeSelectItemModelProperty
{
    public static final MapCodec<MessageTextureItemProperty> MAP_CODEC = MapCodec.unit(MessageTextureItemProperty::new);

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed)
    {
        if (itemStack == null) return 0.0f;

        Message message = SCDataComponents.get(itemStack, SCDataComponents.MESSAGE);
        if (message == null) return 0;

        if (message.background().equals(Message.BACKGROUND_NETHER))
            return 1;

        if (message.background().equals(Message.BACKGROUND_END))
            return 2;

        return 0f;
    }

    @Override
    public MapCodec<MessageTextureItemProperty> type()
    {
        return MAP_CODEC;
    }
}

