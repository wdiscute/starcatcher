package com.wdiscute.starcatcher.io;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.items.ColorfulBobber;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
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

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TrophyProperties>> TROPHY = register(
            "trophy",
            builder -> builder.persistent(TrophyProperties.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FishProperties>> FISH_PROPERTIES = register(
            "fish_properties",
            builder -> builder.persistent(FishProperties.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SecretNote.Note>> SECRET_NOTE = register(
            "secret_note",
            builder -> builder.persistent(SecretNote.Note.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SizeAndWeight>> SIZE_AND_WEIGHT = register(
            "size_and_weight",
            builder -> builder.persistent(SizeAndWeight.CODEC));

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
