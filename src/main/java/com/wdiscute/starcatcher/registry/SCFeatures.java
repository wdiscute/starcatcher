package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.clam.ClamFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCFeatures
{
    public static final DeferredRegisterTyped<Feature<?>> FEATURES =
            DeferredRegisterTyped.create(BuiltInRegistries.FEATURE, Starcatcher.MOD_ID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> CLAM_FEATURE = FEATURES.register("clam",
            () -> new ClamFeature(NoneFeatureConfiguration.CODEC));



    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}