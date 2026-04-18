package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCSweetSpotsBehaviour
{
    DeferredRegister<Supplier<? extends AbstractSweetSpotBehaviour>> REGISTRY =
            DeferredRegister.create(Starcatcher.SWEET_SPOT_BEHAVIOUR_REGISTRY, Starcatcher.MOD_ID);

    Identifier NORMAL = registerSweetspot("normal", () -> NormalSweetSpotBehaviour::new);
    Identifier FROZEN = registerSweetspot("freeze", () -> FreezeSweetSpotBehaviour::new);
    Identifier TREASURE = registerSweetspot("treasure", () -> TreasureSweetSpotBehaviour::new);
    Identifier TNT = registerSweetspot("tnt", () -> TntSweetSpotBehaviour::new);
    Identifier AQUA = registerSweetspot("aqua", () -> AquaSweetSpot::new);
    Identifier LEAF = registerSweetspot("leaf", () -> LeafSweetSpot::new);


    static Identifier registerSweetspot(String name, Supplier<Supplier<? extends AbstractSweetSpotBehaviour>> supplier)
    {
        REGISTRY.register(name, supplier);
        return Starcatcher.rl(name);
    }


    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
