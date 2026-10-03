package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.clam.ClamFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SCFeatures
{
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURES =
            DeferredRegister.create(BuiltInRegistries.FEATURE_TYPE, Starcatcher.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<? extends Feature>> CLAM_FEATURE = FEATURES.register("clam", () -> ClamFeature.CODEC);

    public static final ResourceKey<Feature> CLAM = ResourceKey.create(Registries.FEATURE, Starcatcher.rl("clam"));

    public static final ResourceKey<PlacedFeature> CLAMS =
            ResourceKey.create(
                    Registries.PLACED_FEATURE,
                    Starcatcher.rl("clams")
            );

    public static void register(IEventBus eventBus)
    {
        FEATURES.register(eventBus);
    }
}