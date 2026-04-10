package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.compat.curios.CuriosCompat;
import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface SCMinigameModifiers
{
    DeferredRegister<Supplier<AbstractMinigameModifier>> REGISTRY =
            DeferredRegister.create(Starcatcher.MINIGAME_MODIFIERS_REGISTRY, Starcatcher.MOD_ID);

    //ice fishes
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> FREEZE_ON_MISS =
            registerMinigameModifier("freeze_on_miss", FreezeOnMissModifier::new);

    //hot fishes
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> BURN_ON_MISS =
            registerMinigameModifier("burn_on_miss", BurnOnMissModifier::new);

    //end fishes
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> TELEPORT =
            registerMinigameModifier("teleport", TeleportModifier::new);

    //base modifier
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> LOW_CHANCE_TREASURE_SPAWN =
            registerMinigameModifier("low_chance_treasure_spawn", LowChanceTreasureSpawnModifier::new);

    //shiny hook
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SPAWN_TREASURE_ON_THREE_HITS =
            registerMinigameModifier("spawn_treasure_on_three_hits", ShinyHookModifier::new);

    //heavy hook
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SLOWER_MOVING_SWEET_SPOTS =
            registerMinigameModifier("slower_moving_sweet_spots", () -> new HeavyHookModifier(2));

    //stone hook
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> STOP_DECAY_ON_HIT =
            registerMinigameModifier("stop_decay_on_hit", StoneHookModifier::new);

    //mossy hook
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> HARDER_WITH_TREASURE_ON_PERFECT =
            registerMinigameModifier("harder_with_treasure_on_perfect", MossyHookModifier::new);

    //steady bobber
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> BIGGER_GREEN_SWEET_SPOTS =
            registerMinigameModifier("bigger_green_sweet_spots", SteadyBobberModifier::new);

    //clear bobber
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SLOWER_VANISHING =
            registerMinigameModifier("slower_vanishing", ClearBobberModifier::new);

    //aqua bobber
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> ADD_AQUA_SWEET_SPOT =
            registerMinigameModifier("add_aqua_sweet_spot", () -> new AquaBobberModifier(-1, 1));

    //MultiLayer
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> NIKDO53_MODIFIER =
            registerMinigameModifier("nikdo53_modifier", Nikdo53Modifier::new);

    //base
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> BASE =
            registerMinigameModifier("base", BaseMinigameModifier::new);

    //flip on every hit
    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> FLIP_EVERY_HIT =
            registerMinigameModifier("flip_every_hit", () -> new FlipEveryHitModifier(-1));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> FROZEN_POINTER =
            registerMinigameModifier("frozen_pointer", () -> new FrozenPointerWhileActiveModifier(20, 10));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SPAWN_SWEET_SPOTS =
            registerMinigameModifier("spawn_sweet_spots", () -> new SpawnSweetSpotsModifier(SpawnSweetSpotsModifier.legacyFreeze()));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> FASTER_POINTER_SPEED =
            registerMinigameModifier("faster_handle_speed", () -> new ModifyBasePointerSpeedModifier(1.5F));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SLIGHTLY_FASTER_POINTER_SPEED =
            registerMinigameModifier("slightly_faster_handle_speed", () -> new ModifyBasePointerSpeedModifier(1.25f));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SLIGHTLY_SLOWER_POINTER_SPEED =
            registerMinigameModifier("slightly_slower_handle_speed", () -> new ModifyBasePointerSpeedModifier(0.75f));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> SLOWER_POINTER_SPEED =
            registerMinigameModifier("slower_handle_speed", () -> new ModifyBasePointerSpeedModifier(0.5f));

    DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> ADD_LEAF_MODIFIER =
            registerMinigameModifier("add_leaves_spots", () -> new AddLeavesSweetspotsModifier(0.025f));

    static DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> registerMinigameModifier(String name, Supplier<AbstractMinigameModifier> sup)
    {
        return REGISTRY.register(name, () -> sup);
    }

    static Supplier<AbstractMinigameModifier> getMinigameModifierSupplier(Level level, ResourceLocation resourceLocation)
    {
        Optional<Supplier<AbstractMinigameModifier>> optional = level.registryAccess().registryOrThrow(Starcatcher.MINIGAME_MODIFIERS).getOptional(resourceLocation);
        return optional.orElse(null);
    }

    static List<AbstractMinigameModifier> getMinigameModifiers(Player player)
    {
        List<ResourceLocation> rls = new ArrayList<>();

        //rod
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        if (main.is(SCTags.RODS))
        {
            rls.addAll(getMinigameModifiersRLs(main));
            SCDataComponents.getSlotsInRod(main).forEach(o -> rls.addAll(getMinigameModifiersRLs(o)));
        }
        else if (player.getOffhandItem().is(SCTags.RODS))
        {
            rls.addAll(getMinigameModifiersRLs(off));
            SCDataComponents.getSlotsInRod(off).forEach(o -> rls.addAll(getMinigameModifiersRLs(o)));
        }

        //armor
        player.getInventory().armor.forEach(o -> rls.addAll(getMinigameModifiersRLs(o)));

        //curios
        if (ModList.get().isLoaded("curios"))
        {
            CuriosCompat.getItems(player).forEach(o -> rls.addAll(getMinigameModifiersRLs(o)));
        }

        //get all AbstractMinigameModifier instances of registered RLs
        List<AbstractMinigameModifier> minigameModifiers = new ArrayList<>();
        rls.forEach(rl ->
        {
            Supplier<AbstractMinigameModifier> minigameModifierSupplier = getMinigameModifierSupplier(player.level(), rl);
            if (minigameModifierSupplier != null)
                minigameModifiers.add(minigameModifierSupplier.get());
            else
                LogUtils.getLogger().error("The modifier {} is not registered. Skipping.", rl);
        });
        return minigameModifiers;
    }

    static List<ResourceLocation> getMinigameModifiersRLs(ItemStack itemStack)
    {
        List<ResourceLocation> resourceLocations = SCDataComponents.get(itemStack, SCDataComponents.MINIGAME_MODIFIERS);
        if (resourceLocations == null) return List.of();
        return resourceLocations;
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
