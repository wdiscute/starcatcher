package com.wdiscute.starcatcher.modifiers;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class EmptyModifier implements Modifier
{
    public static final MapCodec<EmptyModifier> CODEC = MapCodec.unit(EmptyModifier::new);

    public EmptyModifier()
    {
    }

    @Override
    public List<Component> getDescription(boolean shift)
    {
        return List.of();
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("empty");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
