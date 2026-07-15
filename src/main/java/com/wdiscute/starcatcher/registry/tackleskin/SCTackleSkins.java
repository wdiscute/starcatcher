package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public interface SCTackleSkins
{
    DeferredRegister<AbstractTackleSkin> REGISTRY =
            DeferredRegister.create(Starcatcher.TACKLE_SKIN_REGISTRY, Starcatcher.MOD_ID);

    //rod
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> BASE_TACKLE_SKIN = registerCatchModifier("base", BaseTackleSkin::new);

    //pearl
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> PEARL_TACKLE_SKIN = registerCatchModifier("pearl", PearlTackleSkin::new);

    //kimbe
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> KIMBE_TACKLE_SKIN = registerCatchModifier("kimbe", KimbeTackleSkin::new);

    //frog
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> FROG_TACKLE_SKIN = registerCatchModifier("frog", FrogTackleSkin::new);

    //colorful
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> COLORFUL_TACKLE_SKIN = registerCatchModifier("colorful", ColorfulTackleSkin::new);

    //clear
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> CLEAR_TACKLE_SKIN = registerCatchModifier("clear", ClearTackleSkin::new);

    //king
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> KING_TACKLE_SKIN = registerCatchModifier("king", KingTackleSkin::new);

    //valley
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> VALLEY_TACKLE_SKIN = registerCatchModifier("valley", ValleyTackleSkin::new);

    //survivor
    DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> SURVIVOR_TACKLE_SKIN = registerCatchModifier("survivor", SurvivorTackleSkin::new);

    static DeferredHolder<AbstractTackleSkin, AbstractTackleSkin> registerCatchModifier(String name, Supplier<AbstractTackleSkin> sup)
    {
        return REGISTRY.register(name, sup);
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
