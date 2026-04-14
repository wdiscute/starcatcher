package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.Optional;
import java.util.function.Supplier;

public interface SCTackleSkins
{
    DeferredRegisterTyped<Supplier<AbstractTackleSkin>> REGISTRY =
            DeferredRegisterTyped.create(Starcatcher.TACKLE_SKIN, Starcatcher.MOD_ID);

    //base
    ResourceLocation BASE_TACKLE_SKIN = registerCatchModifier("base", BaseTackleSkin::new);

    //pearl
    ResourceLocation PEARL_TACKLE_SKIN = registerCatchModifier("pearl", PearlTackleSkin::new);

    //kimbe
    ResourceLocation KIMBE_TACKLE_SKIN = registerCatchModifier("kimbe", KimbeTackleSkin::new);

    //frog
    ResourceLocation FROG_TACKLE_SKIN = registerCatchModifier("frog", FrogTackleSkin::new);

    //colorful
    ResourceLocation COLORFUL_TACKLE_SKIN = registerCatchModifier("colorful", ColorfulTackleSkin::new);

    //clear
    ResourceLocation CLEAR_TACKLE_SKIN = registerCatchModifier("clear", ClearTackleSkin::new);

    //king
    ResourceLocation KING_TACKLE_SKIN = registerCatchModifier("king", KingTackleSkin::new);

    static ResourceLocation getTackleSkin(ItemStack stack)
    {
        //if skin on tackle not default, return that
        ResourceLocation onStack = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_SKIN, Starcatcher.rl("base"));
        if (!onStack.equals(Starcatcher.rl("base"))) return onStack;

        //return whatever is in the datamap or default
        return SCDataMaps.getOrDefault(stack, SCDataMaps.TACKLE_SKIN, Starcatcher.rl("base"));
    }

    static ResourceLocation registerCatchModifier(String name, Supplier<AbstractTackleSkin> sup)
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
            ResourceLocation rl = SCDataComponents.get(itemInHand, SCDataComponents.TACKLE_SKIN);

            Optional<Supplier<AbstractTackleSkin>> optional = level.registryAccess().registryOrThrow(Starcatcher.TACKLE_SKIN).getOptional(rl);
            if (optional.isPresent()) return optional.get().get();
        }
        return new BaseTackleSkin();
    }
}
