package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class NBTCodecHelper
{

    public static <T> void encode(Codec<T> codec, T data, ValueOutput output, String name)
    {
        if (data == null)
        {
            return;
        }

        output.store(name, codec, data);
    }

    public static <T> T decode(Codec<T> codec, ValueInput input, String name, Supplier<T> orElse)
    {
        Optional<T> read = input.read(name, codec);

        return read.orElseGet(orElse);

    }

    public static <T> @Nullable T decode(Codec<T> codec, ValueInput input, String name)
    {

        return decode(codec, input, name, () -> null);
    }

}
