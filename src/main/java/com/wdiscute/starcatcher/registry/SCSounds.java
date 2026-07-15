package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCSounds
{
    DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Starcatcher.MOD_ID);


    Supplier<SoundEvent> KING_HEHEHA = registerSoundEvent("king_heheha");
    Supplier<SoundEvent> KING_CRY = registerSoundEvent("king_cry");
    Supplier<SoundEvent> KING_GRR = registerSoundEvent("king_grr");

    Supplier<SoundEvent> VALLEY_BITING = registerSoundEvent("valley_biting");
    Supplier<SoundEvent> VALLEY_CAST = registerSoundEvent("valley_cast");
    Supplier<SoundEvent> VALLEY_FAILED = registerSoundEvent("valley_failed");
    Supplier<SoundEvent> VALLEY_MISSED = registerSoundEvent("valley_missed");
    Supplier<SoundEvent> VALLEY_BOOP = registerSoundEvent("valley_boop");
    Supplier<SoundEvent> VALLEY_REEL = registerSoundEvent("valley_reel");
    Supplier<SoundEvent> VALLEY_MINIGAME_STARTS = registerSoundEvent("valley_minigame_starts");

    Supplier<SoundEvent> SURVIVOR_BITING = registerSoundEvent("survivor_biting");
    Supplier<SoundEvent> SURVIVOR_MINIGAME_STARTS = registerSoundEvent("survivor_minigame_starts");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = Starcatcher.rl(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
