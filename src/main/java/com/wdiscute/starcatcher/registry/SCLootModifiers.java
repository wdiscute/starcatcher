package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.DGSCLootModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SCLootModifiers
{
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Starcatcher.MOD_ID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> DGSCLootModifiers.AddItemModifier.CODEC);

    public static void register(IEventBus eventBus)
    {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
