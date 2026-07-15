package com.wdiscute.starcatcher.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class NBTCodecHelper {

    public static <T> void encode(Codec<T> codec, T data, CompoundTag compoundTag, String name){
        if (data == null){
            return;
        }

        codec.encodeStart(NbtOps.INSTANCE, data)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .ifPresent(tag -> compoundTag.put(name, tag));
    }

    public static <T> T decode(Codec<T> codec, CompoundTag compoundTag, String name, Supplier<T> orElse){
        Tag tag = compoundTag.get(name);

        return codec.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .map(Pair::getFirst).orElseGet(orElse);
    }

    public static  <T> @Nullable T decode(Codec<T> codec, CompoundTag compoundTag, String name){
        if (!compoundTag.contains(name)){
            return null;
        }

        return decode(codec, compoundTag, name, () -> null);
    }

}
