package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCSounds
{
    public static final DeferredRegisterTyped<SoundEvent> SOUND_EVENTS =
            DeferredRegisterTyped.create(BuiltInRegistries.SOUND_EVENT, Starcatcher.MOD_ID);


    public static final Supplier<SoundEvent> KING_HEHEHA = registerSoundEvent("king_heheha");
    public static final Supplier<SoundEvent> KING_CRY = registerSoundEvent("king_cry");
    public static final Supplier<SoundEvent> KING_GRR = registerSoundEvent("king_grr");



    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = Starcatcher.rl(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
