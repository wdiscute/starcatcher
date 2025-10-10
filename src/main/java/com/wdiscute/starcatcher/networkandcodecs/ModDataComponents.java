package com.wdiscute.starcatcher.networkandcodecs;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.items.ColorfulBobber;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents
{
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Starcatcher.MOD_ID);


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> BOBBER = register(
            "bobber",
            builder -> builder.persistent(SingleStackContainer.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> BAIT = register(
            "bait",
            builder -> builder.persistent(SingleStackContainer.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> HOOK = register(
            "hook",
            builder -> builder.persistent(SingleStackContainer.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ColorfulBobber.BobberColor>> BOBBER_COLOR = register(
            "color",
            builder -> builder.persistent(ColorfulBobber.BobberColor.CODEC));


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator)
    {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus)
    {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
