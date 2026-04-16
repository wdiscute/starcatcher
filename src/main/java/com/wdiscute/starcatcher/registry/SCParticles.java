package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCParticles
{
    public static final DeferredRegisterTyped<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegisterTyped.create(BuiltInRegistries.PARTICLE_TYPE, Starcatcher.MOD_ID);


    public static final Supplier<SimpleParticleType> FISHING_NOTIFICATION =
            PARTICLE_TYPES.register("fishing_notification", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> FISHING_BITING =
            PARTICLE_TYPES.register("fishing_biting", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> FISHING_BITING_LAVA =
            PARTICLE_TYPES.register("fishing_biting_lava", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }

}
