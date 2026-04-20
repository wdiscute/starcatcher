package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.curios.CuriosCompat;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface SCCatchModifiers
{
    DeferredRegister<Supplier<AbstractCatchModifier>> REGISTRY =
            DeferredRegister.create(Starcatcher.CATCH_MODIFIERS_REGISTRY, Starcatcher.MOD_ID);

    //todo built-in modifiers to skip minigame for low rarity or something, using AbstractCatchModifier#forceSkipMinigame

    //every bait
    Pair<Identifier, Supplier<AbstractCatchModifier>> DECREASES_LURE_TIME = registerCatchModifier(
            "decrease_lure_time",
            () -> new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f));

    //every bait
    Pair<Identifier, Supplier<AbstractCatchModifier>> BIG_DECREASES_LURE_TIME = registerCatchModifier(
            "big_decrease_lure_time",
            () -> new AdjustLureTimeModifier(0.4f, 0.6f, 1.3f));

    Pair<Identifier, Supplier<AbstractCatchModifier>> INCREASE_LURE_TIME = registerCatchModifier(
            "increase_lure_time",
            () -> new AdjustLureTimeModifier(1.5f, 1.6f, 1));

    //vanilla bobber
    Pair<Identifier, Supplier<AbstractCatchModifier>> VANILLA_LOOT =
            registerCatchModifier("vanilla_loot", VanillaLootModifier::new);
    Pair<Identifier, Supplier<AbstractCatchModifier>> SKIP_MINIGAME_IF_VANILLA_LOOT =
            registerCatchModifier("skip_minigame_if_vanilla_loot", SkipMinigameIfVanillaLoot::new);

    //almighty worm
    Pair<Identifier, Supplier<AbstractCatchModifier>> FISH_ENTITY =
            registerCatchModifier("fish_entity", ForceFishEntityModifier::new);

    //seeking worm
    Pair<Identifier, Supplier<AbstractCatchModifier>> GUARANTEE_NEW_FISH_ALWAYS =
            registerCatchModifier("guarantee_new_fish_always", () -> new GuaranteeNewFishModifier(101));
    Pair<Identifier, Supplier<AbstractCatchModifier>> GUARANTEE_NEW_FISH_HALF =
            registerCatchModifier("guarantee_new_fish_half", () -> new GuaranteeNewFishModifier(50));

    //gold hook
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_EXP_BASED_ON_PERFORMANCE =
            registerCatchModifier("extra_exp_based_on_performance", ExtraExpBasedOnPerformanceModifier::new);

    //split hook
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_ITEM = registerCatchModifier("extra_item", () -> new ExtraItemsModifier(1));
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_TWO_ITEMS = registerCatchModifier("extra_two_item", () -> new ExtraItemsModifier(2));
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_THREE_ITEMS = registerCatchModifier("extra_three_item", () -> new ExtraItemsModifier(3));
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_FIVE_ITEMS = registerCatchModifier("extra_five_item", () -> new ExtraItemsModifier(5));
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_TEN_ITEMS = registerCatchModifier("extra_ten_item", () -> new ExtraItemsModifier(10));
    Pair<Identifier, Supplier<AbstractCatchModifier>> EXTRA_THIRTY_TWO_ITEMS = registerCatchModifier("extra_thirty_two_item", () -> new ExtraItemsModifier(32));

    //meteorological bait
    Pair<Identifier, Supplier<AbstractCatchModifier>> IGNORE_DAYTIME_AND_WEATHER_RESTRICTIONS = registerCatchModifier("ignore_daytime_and_weather_restrictions", IgnoreDaytimeWeatherRestrictions::new);

    //survives lava - crystal hook
    Pair<Identifier, Supplier<AbstractCatchModifier>> SURVIVES_LAVA = registerCatchModifier("survives_lava", SurvivesLavaModifier::new);

    //add creeper
    Pair<Identifier, Supplier<AbstractCatchModifier>> ADD_CREEPER = registerCatchModifier("add_creeper", AddCreeperModifier::new);

    //golden
    Pair<Identifier, Supplier<AbstractCatchModifier>> GUARANTEE_GOLDEN = registerCatchModifier("guarantee_golden", GuaranteeGolden::new);
    Pair<Identifier, Supplier<AbstractCatchModifier>> ADD_5_GOLDEN_CHANCE = registerCatchModifier("add_5_golden_chance", () -> new IncreaseGoldenChance(0.05f));
    Pair<Identifier, Supplier<AbstractCatchModifier>> ADD_50_GOLDEN_CHANCE = registerCatchModifier("add_50_golden_chance", () -> new IncreaseGoldenChance(0.5f));
    Pair<Identifier, Supplier<AbstractCatchModifier>> CANCEL_GOLDEN = registerCatchModifier("cancel_golden", CancelGolden::new);

    //hide catch
    Pair<Identifier, Supplier<AbstractCatchModifier>> HIDE_CATCH = registerCatchModifier("hide_catch", HideCatchModifier::new);

    //angler's hat (artifacts / reliquified artifacts compat)
    Pair<Identifier, Supplier<AbstractCatchModifier>> ANGLERS_HAT = registerCatchModifier("anglers_hat", AnglersHatModifier::new);


    static Pair<Identifier, Supplier<AbstractCatchModifier>> registerCatchModifier(String name, Supplier<AbstractCatchModifier> sup)
    {
        REGISTRY.register(name, () -> sup);
        return Pair.of(Starcatcher.rl(name), sup);
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }

    static Supplier<AbstractCatchModifier> getCatchModifierSupplier(Level level, Identifier resourceLocation)
    {
        Optional<Supplier<AbstractCatchModifier>> optional = level.registryAccess().getOrThrow(Starcatcher.CATCH_MODIFIERS).value().getOptional(resourceLocation);
        return optional.orElse(null);
    }

    static List<AbstractCatchModifier> getCatchModifiers(Player player)
    {
        List<Identifier> rls = new ArrayList<>();

        //rod
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        if (main.is(SCTags.RODS))
        {
            rls.addAll(getCatchModifiersRLs(main));
        }
        else if (player.getOffhandItem().is(SCTags.RODS))
        {
            rls.addAll(getCatchModifiersRLs(off));
        }

        //armor
        player.getInventory().getNonEquipmentItems().forEach(o -> rls.addAll(getCatchModifiersRLs(o)));

        //curios
        if (ModList.get().isLoaded("curios"))
        {
            CuriosCompat.getItems(player).forEach(o -> rls.addAll(getCatchModifiersRLs(o)));
        }

        List<AbstractCatchModifier> catchModifiers = new ArrayList<>();
        rls.forEach(o -> catchModifiers.add(getCatchModifierSupplier(player.level(), o).get()));
        return catchModifiers;
    }

    static List<Identifier> getCatchModifiersRLs(ItemStack itemStack)
    {
        List<Identifier> rls = new ArrayList<>(SCDataComponents.getOrDefault(itemStack, SCDataComponents.CATCH_MODIFIERS, List.of()));

        rls.addAll(SCDataMaps.getOrDefault(itemStack, SCDataMaps.CATCH_MODIFIERS, List.of()));

        if(!itemStack.is(SCTags.RODS)) return rls;

        //if not a rod, add hook bobber and bait slot modifiers too
        var hook = SCDataComponents.getOrDefault(itemStack, SCDataComponents.HOOK, SingleStackContainer.empty()).stack();
        var bait = SCDataComponents.getOrDefault(itemStack, SCDataComponents.BAIT, SingleStackContainer.empty()).stack();
        var bobber = SCDataComponents.getOrDefault(itemStack, SCDataComponents.BOBBER, SingleStackContainer.empty()).stack();

        rls.addAll(SCDataMaps.getOrDefault(hook, SCDataMaps.CATCH_MODIFIERS, List.of()));
        rls.addAll(SCDataMaps.getOrDefault(bait, SCDataMaps.CATCH_MODIFIERS, List.of()));
        rls.addAll(SCDataMaps.getOrDefault(bobber, SCDataMaps.CATCH_MODIFIERS, List.of()));

        rls.addAll(SCDataComponents.getOrDefault(hook, SCDataComponents.CATCH_MODIFIERS, List.of()));
        rls.addAll(SCDataComponents.getOrDefault(bait, SCDataComponents.CATCH_MODIFIERS, List.of()));
        rls.addAll(SCDataComponents.getOrDefault(bobber, SCDataComponents.CATCH_MODIFIERS, List.of()));

        return rls;
    }
}

