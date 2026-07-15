package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCParticles
{
    DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Starcatcher.MOD_ID);


    Supplier<SimpleParticleType> VALLEY_NOTIFICATION =
            PARTICLE_TYPES.register("valley_notification", () -> new SimpleParticleType(true));

    Supplier<SimpleParticleType> FISHING_BITING =
            PARTICLE_TYPES.register("fishing_biting", () -> new SimpleParticleType(true));

    Supplier<SimpleParticleType> FISHING_BITING_LAVA =
            PARTICLE_TYPES.register("fishing_biting_lava", () -> new SimpleParticleType(true));

    static void register(IEventBus eventBus)
    {
        PARTICLE_TYPES.register(eventBus);
    }

}
