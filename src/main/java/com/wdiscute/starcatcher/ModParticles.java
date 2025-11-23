package com.wdiscute.starcatcher;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModParticles
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Starcatcher.MOD_ID);


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
