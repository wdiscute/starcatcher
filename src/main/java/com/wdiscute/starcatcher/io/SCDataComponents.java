package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.SignedGuide;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.registry.DeferredHolder;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SCDataComponents
{
    public static final DeferredRegisterTyped.DataComponents DATA_COMPONENT_TYPES =
            DeferredRegisterTyped.createDataComponents(Starcatcher.MOD_ID);

    //bucketed fish
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> BUCKETED_FISH = register(
            "bucketed_fish",
            builder -> builder.persistent(SingleStackContainer.CODEC));

    //signed book system
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SignedGuide>> SIGNED_GUIDE = register(
            "signed_guide",
            builder -> builder.persistent(SignedGuide.CODEC));

    //rod menu
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> BOBBER = register(
            "bobber",
            builder -> builder.persistent(SingleStackContainer.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> BAIT = register(
            "bait", builder -> builder.persistent(SingleStackContainer.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SingleStackContainer>> HOOK = register(
            "hook", builder -> builder.persistent(SingleStackContainer.CODEC));

    //storing data on itemstack
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SecretNote.Note>> SECRET_NOTE = register(
            "secret_note", builder -> builder.persistent(SecretNote.Note.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LetterItem.Message>> MESSAGE = register(
            "message", builder -> builder.persistent(LetterItem.Message.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CaughtFishInfo>> CAUGHT_FISH_INFO = register(
            "caught_fish_info", builder -> builder.persistent(CaughtFishInfo.CODEC));


    //modifiers
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceLocation>>> MINIGAME_MODIFIERS = register(
            "minigame_modifiers",
            builder -> builder.persistent(ResourceLocation.CODEC.listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceLocation>>> CATCH_MODIFIERS = register(
            "catch_modifiers",
            builder -> builder.persistent(ResourceLocation.CODEC.listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> TACKLE_SKIN = register(
            "tackle_skin",
            builder -> builder.persistent(ResourceLocation.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NETHERITE_UPGRADE = register(
            "netherite_upgraded",
            builder -> builder.persistent(Codec.BOOL));

    public static <T> void set(ItemStack stack, Supplier<DataComponentType<T>> component, T data)
    {
        stack.set(component.get(), data);
    }

    public static List<ItemStack> getSlotsInRod(ItemStack itemStack)
    {
        List<ItemStack> list = new ArrayList<>();
        list.add(SCDataComponents.getOrDefault(itemStack, SCDataComponents.HOOK, SingleStackContainer.empty()).stack());
        list.add(SCDataComponents.getOrDefault(itemStack, SCDataComponents.BAIT, SingleStackContainer.empty()).stack());
        list.add(SCDataComponents.getOrDefault(itemStack, SCDataComponents.BOBBER, SingleStackContainer.empty()).stack());
        return list;
    }

    @Nullable
    public static <T> T get(ItemStack stack, Supplier<DataComponentType<T>> component)
    {
        return stack.get(component.get());
    }

    public static <T> boolean has(ItemStack stack, Supplier<DataComponentType<T>> component)
    {
        return stack.has(component.get());
    }

    public static <T> void remove(ItemStack stack, Supplier<DataComponentType<T>> component)
    {
        stack.remove(component.get());
    }

    @Nonnull
    public static <T> T getOrDefault(ItemStack stack, Supplier<DataComponentType<T>> component, T defaultValue)
    {
        return stack.getOrDefault(component.get(), defaultValue);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator)
    {
        return DATA_COMPONENT_TYPES.registerComponentType(name, builderOperator);
    }

    public static void register(IEventBus eventBus)
    {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
