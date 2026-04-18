package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public interface SCTackleSkins
{
    DeferredRegister<Supplier<AbstractTackleSkin>> REGISTRY =
            DeferredRegister.create(Starcatcher.TACKLE_SKIN_REGISTRY, Starcatcher.MOD_ID);

    //base
    Identifier BASE_TACKLE_SKIN = registerCatchModifier("base", BaseTackleSkin::new);

    //pearl
    Identifier PEARL_TACKLE_SKIN = registerCatchModifier("pearl", PearlTackleSkin::new);

    //kimbe
    Identifier KIMBE_TACKLE_SKIN = registerCatchModifier("kimbe", KimbeTackleSkin::new);

    //frog
    Identifier FROG_TACKLE_SKIN = registerCatchModifier("frog", FrogTackleSkin::new);

    //colorful
    Identifier COLORFUL_TACKLE_SKIN = registerCatchModifier("colorful", ColorfulTackleSkin::new);

    //clear
    Identifier CLEAR_TACKLE_SKIN = registerCatchModifier("clear", ClearTackleSkin::new);

    //king
    Identifier KING_TACKLE_SKIN = registerCatchModifier("king", KingTackleSkin::new);

    static Identifier getTackleSkin(ItemStack stack)
    {
        //if skin on tackle not default, return that
        Identifier onStack = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_SKIN, Starcatcher.rl("base"));
        if (!onStack.equals(Starcatcher.rl("base"))) return onStack;

        //return whatever is in the datamap or default
        return SCDataMaps.getOrDefault(stack, SCDataMaps.TACKLE_SKIN, Starcatcher.rl("base"));
    }

    static Identifier registerCatchModifier(String name, Supplier<AbstractTackleSkin> sup)
    {
        REGISTRY.register(name, () -> sup);
        return Starcatcher.rl(name);
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }

    static AbstractTackleSkin get(Level level, ItemStack itemInHand)
    {
        if (SCDataComponents.has(itemInHand, SCDataComponents.TACKLE_SKIN))
        {
            Identifier rl = SCDataComponents.get(itemInHand, SCDataComponents.TACKLE_SKIN);

            Optional<Supplier<AbstractTackleSkin>> optional = level.registryAccess().getOrThrow(Starcatcher.TACKLE_SKIN).value().getOptional(rl);
            if (optional.isPresent()) return optional.get().get();
        }
        return new BaseTackleSkin();
    }
}
