package com.wdiscute.starcatcher;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents
{
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE ,Starcatcher.MOD_ID);


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> BOBBER = register("bobber",
            builder -> builder.persistent(ItemContainerContents.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> BAIT = register("bait",
            builder -> builder.persistent(ItemContainerContents.CODEC));

//    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CAST = register("cast",
//            builder -> builder.persistent(Codec.BOOL));


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
