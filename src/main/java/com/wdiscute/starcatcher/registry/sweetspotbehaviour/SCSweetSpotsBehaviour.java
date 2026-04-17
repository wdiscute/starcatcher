package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public interface SCSweetSpotsBehaviour
{
    DeferredRegisterTyped<Supplier<? extends AbstractSweetSpotBehaviour>> REGISTRY =
            DeferredRegisterTyped.create(Starcatcher.SWEET_SPOT_BEHAVIOUR, Starcatcher.MOD_ID);

    ResourceLocation NORMAL = registerSweetspot("normal", () -> NormalSweetSpotBehaviour::new);
    ResourceLocation FROZEN = registerSweetspot("freeze", () -> FreezeSweetSpotBehaviour::new);
    ResourceLocation TREASURE = registerSweetspot("treasure", () -> TreasureSweetSpotBehaviour::new);
    ResourceLocation TNT = registerSweetspot("tnt", () -> TntSweetSpotBehaviour::new);
    ResourceLocation AQUA = registerSweetspot("aqua", () -> AquaSweetSpot::new);
    ResourceLocation LEAF = registerSweetspot("leaf", () -> LeafSweetSpot::new);


    static ResourceLocation registerSweetspot(String name, Supplier<Supplier<? extends AbstractSweetSpotBehaviour>> supplier)
    {
        REGISTRY.register(name, supplier);
        return Starcatcher.rl(name);
    }


    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
