package com.wdiscute.starcatcher.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public class TrustedHolder extends Holder.Reference
{
    protected TrustedHolder(Type type, HolderOwner owner, @Nullable ResourceKey key, @Nullable Object value)
    {
        super(type, owner, key, value);
    }

    @Override
    public boolean canSerializeIn(HolderOwner owner)
    {
        return false;
    }
}
