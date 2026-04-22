package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.datagen.DGSCLootModifiers;
import net.minecraft.world.entity.animal.Cod;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;

import java.util.function.Supplier;

public class SCLootModifiers
{
    public static final DeferredRegisterTyped<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegisterTyped.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Starcatcher.MOD_ID);

    public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> DGSCLootModifiers.AddItemModifier.CODEC);

    public static void register(IEventBus eventBus)
    {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
