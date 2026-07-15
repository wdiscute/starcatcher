package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.data.SignedGuide;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessage;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface SCDataComponents
{
    DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Starcatcher.MOD_ID);

    //bucketed fish
    DeferredHolder<DataComponentType<?>, DataComponentType<MaybeStack>> BUCKETED_FISH = register(
            "bucketed_fish",
            builder -> builder.persistent(MaybeStack.CODEC));


    //signed book system
    DeferredHolder<DataComponentType<?>, DataComponentType<SignedGuide>> SIGNED_GUIDE = register(
            "signed_guide",
            builder -> builder.persistent(SignedGuide.CODEC));


    //rod menu
    DeferredHolder<DataComponentType<?>, DataComponentType<MaybeStack>> BOBBER = register(
            "bobber",
            builder -> builder.persistent(MaybeStack.CODEC));

    DeferredHolder<DataComponentType<?>, DataComponentType<MaybeStack>> BAIT = register(
            "bait", builder -> builder.persistent(MaybeStack.CODEC));

    DeferredHolder<DataComponentType<?>, DataComponentType<MaybeStack>> HOOK = register(
            "hook", builder -> builder.persistent(MaybeStack.CODEC));


    //storing data on itemstack
    DeferredHolder<DataComponentType<?>, DataComponentType<EditableMessage>> EDITABLE_MESSAGE = register(
            "editable_message", builder -> builder.persistent(EditableMessage.CODEC));

    DeferredHolder<DataComponentType<?>, DataComponentType<Message>> MESSAGE = register(
            "message", builder -> builder.persistent(Message.CODEC));

    DeferredHolder<DataComponentType<?>, DataComponentType<CaughtFishInfo>> CAUGHT_FISH_INFO = register(
            "caught_fish_info", builder -> builder.persistent(CaughtFishInfo.CODEC));


    //modifiers
    DeferredHolder<DataComponentType<?>, DataComponentType<List<Modifier>>> MODIFIERS = register(
            "modifiers",
            builder -> builder.persistent(Modifier.CODEC.listOf()));

    DeferredHolder<DataComponentType<?>, DataComponentType<AbstractTackleSkin>> TACKLE_SKIN = register(
            "tackle_skin",
            builder -> builder.persistent(Starcatcher.TACKLE_SKIN_REGISTRY.byNameCodec()));

    DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NETHERITE_UPGRADE = register(
            "netherite_upgraded",
            builder -> builder.persistent(Codec.BOOL));

    //tackle box
    DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemStack>>> TACKLE_BOX_FISHES = register(
            "tackle_box_fishes",
            builder -> builder.persistent(ItemStack.OPTIONAL_CODEC.listOf()));

    static <T> void set(ItemStack stack, Supplier<DataComponentType<T>> component, T data)
    {
        stack.set(component, data);
    }

    @Nullable
    static <T> T get(ItemStack stack, Supplier<DataComponentType<T>> component)
    {
        return stack.get(component);
    }

    static <T> boolean has(ItemStack stack, Supplier<DataComponentType<T>> component)
    {
        return stack.has(component);
    }

    static <T> void remove(ItemStack stack, Supplier<DataComponentType<T>> component)
    {
        stack.remove(component);
    }

    @Nonnull
    static <T> T getOrDefault(ItemStack stack, Supplier<DataComponentType<T>> component, T defaultValue)
    {
        return stack.getOrDefault(component, defaultValue);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator)
    {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    static void register(IEventBus eventBus)
    {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
