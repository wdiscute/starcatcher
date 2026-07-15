package com.wdiscute.starcatcher.modifiers;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.*;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.curios.CuriosCompat;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.*;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.*;
import com.wdiscute.utils.MaybeStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Modifier
{
    Map<ResourceLocation, MapCodec<? extends Modifier>> MODIFIERS = new HashMap<>();

    static List<AbstractMinigameModifier> getDefaultMinigameModifiers()
    {
        List<Modifier> data = SCDataEntries.DEFAULT_MINIGAME_MODIFIERS.get();

        if(data == null) data = List.of();

        return data.stream()
            .filter(AbstractMinigameModifier.class::isInstance)
            .map(o -> (AbstractMinigameModifier) o).toList();
    }

    static List<AbstractCatchModifier> getDefaultCatchModifiers()
    {
        List<Modifier> data = SCDataEntries.DEFAULT_CATCH_MODIFIERS.get();

        if(data == null) data = List.of();

        return data.stream()
                .filter(AbstractCatchModifier.class::isInstance)
                .map(o -> (AbstractCatchModifier) o).toList();
    }

    List<Component> getDescription(boolean shift);

    ResourceLocation getIdentifier();

    MapCodec<? extends Modifier> getCodec();

    Codec<Modifier> CODEC = ResourceLocation.CODEC
            .dispatch(
                    Modifier::getIdentifier,
                    rl ->
                    {
                        if (MODIFIERS.containsKey(rl))
                            return MODIFIERS.get(rl);

                        if (FMLEnvironment.dist.isClient())
                            LogUtils.getLogger().warn("Modifier [{}] not found. Using empty modifier instead.", rl);
                        return EmptyModifier.CODEC;
                    }
            );

    static List<AbstractCatchModifier> getCatchModifiers(Player player)
    {
        return getModifiers(player).stream().filter(o -> o instanceof AbstractCatchModifier).map(o -> (AbstractCatchModifier) o).toList();
    }

    static List<AbstractMinigameModifier> getMinigameModifiers(Player player)
    {
        return getModifiers(player).stream().filter(o -> o instanceof AbstractMinigameModifier).map(o -> (AbstractMinigameModifier) o).toList();
    }

    static List<AbstractCatchModifier> getCatchModifiers(ItemStack itemStack)
    {
        return getModifiers(itemStack).stream().filter(o -> o instanceof AbstractCatchModifier).map(o -> (AbstractCatchModifier) o).toList();
    }

    static List<AbstractMinigameModifier> getMinigameModifiers(ItemStack itemStack)
    {
        return getModifiers(itemStack).stream()
                .filter(o -> o instanceof AbstractMinigameModifier)
                .map(o -> (AbstractMinigameModifier) o)
                .toList()
                ;
    }

    static List<Modifier> getModifiers(Player player)
    {
        List<Modifier> modifiers = new ArrayList<>();

        //rod
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        if (main.is(SCTags.RODS))
            modifiers.addAll(getModifiers(main));
        else if (player.getOffhandItem().is(SCTags.RODS))
            modifiers.addAll(getModifiers(off));

        //armor
        player.getInventory().armor.forEach(o -> modifiers.addAll(getModifiers(o)));

        //curios
        if (ModList.get().isLoaded("curios"))
            CuriosCompat.getItems(player).forEach(o -> modifiers.addAll(getModifiers(o)));

        //data attachments
        modifiers.addAll(SCDataAttachments.get(player, SCDataAttachments.MODIFIERS));

        //potion effects
        for (MobEffectInstance activeEffect : player.getActiveEffects())
        {
            List<Modifier> list = activeEffect.getEffect().getData(SCDataMaps.EFFECT_MODIFIERS);

            if(list != null && !list.isEmpty())
                for (int i = 0; i < activeEffect.getAmplifier(); i++)
                    modifiers.addAll(list);
        }

        return modifiers;
    }

    static List<Modifier> getModifiers(ItemStack itemStack)
    {
        List<Modifier> modifiers = new ArrayList<>();

        //add all modifiers stored directly in the item, in the data component
        modifiers.addAll(SCDataComponents.getOrDefault(itemStack, SCDataComponents.MODIFIERS, List.of()));

        //add all modifiers from rod data map
        modifiers.addAll(SCDataMaps.getOrDefault(itemStack, SCDataMaps.ITEM_MODIFIERS, List.of()));

        //enchants
        if(itemStack.isEnchanted())
        {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemStack.getTagEnchantments().entrySet())
            {
                List<Modifier> list = entry.getKey().getData(SCDataMaps.ENCHANTMENT_MODIFIERS);
                if(list != null && !list.isEmpty())
                {
                    for (int i = 0; i < entry.getIntValue(); i++)
                    {
                        modifiers.addAll(list);
                    }
                }
            }
        }

        //if not rod, end pipeline
        if (!itemStack.is(SCTags.RODS)) return modifiers;

        //if rod, add hook bobber and bait slot modifiers too
        var hook = SCDataComponents.getOrDefault(itemStack, SCDataComponents.HOOK, MaybeStack.EMPTY).toStack();
        var bait = SCDataComponents.getOrDefault(itemStack, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();
        var bobber = SCDataComponents.getOrDefault(itemStack, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack();

        modifiers.addAll(getModifiers(hook));
        modifiers.addAll(getModifiers(bait));
        modifiers.addAll(getModifiers(bobber));

        return modifiers;
    }

    static void registerCatch()
    {
        //catch modifier for codecs
        Modifier.MODIFIERS.put(Starcatcher.rl("empty"), EmptyModifier.CODEC);

        //defaults
        Modifier.MODIFIERS.put(Starcatcher.rl("fish_messages"), FishMessagesModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("luck_attribute"), LuckAttributeModifier.CODEC);

        //others
        Modifier.MODIFIERS.put(Starcatcher.rl("adjust_lure_time"), AdjustLureTimeModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("remove_base_fished_item"), RemoveBaseFishedItemModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("vanilla_fishing_loot"), VanillaFishingLootModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("trigger_skip_minigame"), TriggersSkipMinigameModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("skip_minigame_if_trigger_found"), SkipMinigameIfTriggerFoundModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("new_catch_increase"), NewCatchIncreaseModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("force_fish_entity"), ForceFishEntityModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("extra_base_catch"), ExtraBaseCatchModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("extra_exp_based_on_performance"), ExtraExpBasedOnPerformanceModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("ignore_daytime_restrictions"), IgnoreDaytimeRestrictionsModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("ignore_weather_restrictions"), IgnoreWeatherRestrictionsModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("skip_minigame"), SkipMinigameModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("survives_lava"), SurvivesLavaModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("extra_golden_chance"), ExtraGoldenChanceModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("cancel_golden"), CancelGoldenModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("hide_catch"), HideCatchModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("anglers_hat"), AnglersHatModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("award_treasure_on_perfect_catch"), AwardTreasureOnPerfectCatch.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("no_gravity"), NoGravityModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("boost_throw_speed"), BoostThrowSpeedModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("force_select_fish_from_entry"), ForceSelectFishFromEntryModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("force_select_fish"), ForceSelectFishModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("roll_percentile"), RollPercentileModifier.CODEC);
    }

    static void registerMinigame()
    {
        //defaults
        Modifier.MODIFIERS.put(Starcatcher.rl("kimbe_marker"), KimbeMarkerModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("spawn_treasure"), SpawnTreasureModifier.CODEC);

        //minigame modifier
        Modifier.MODIFIERS.put(Starcatcher.rl("freeze_on_miss"), FreezeOnMissModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("burn_on_miss"), BurnOnMissModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("teleport"), TeleportModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("spawn_treasure_on_hit_x"), SpawnTreasureOnHitX.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("stop_decay_on_hit"), StoneHookModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("bigger_green_sweetspots"), SteadyBobberModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("multi_layer"), Nikdo53Modifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("flip_every_hit"), FlipEveryHitModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("spawn_sweetspots"), SpawnSweetSpotsModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("add_leaves_spots"), AddLeavesSweetspotsModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("bounce_back"), BounceBackModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("never_lose"), NeverLoseModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("prevent_frozen"), PreventFrozenModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("deep_dark"), DeepDarkModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("add_basic_sweetspot"), AddBasicSweetSpotModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("pull_down"), PullDownModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("adjust_moving_sweetspots"), AdjustMovingModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("adjust_vanishing_rate"), AdjustVanishingRate.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("adjust_handle_speed"), AdjustBaseHandleSpeedModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("adjust_decay_rate"), AdjustDecayRateModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("adjust_penalty_rate"), AdjustPenaltyModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("disable_miss_sounds"), DisableMissSoundsModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("disable_hit_sounds"), DisableHitSoundsModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("move_sweetspots_on_miss"), MoveSweetSpotsOnMissModifier.CODEC);
        Modifier.MODIFIERS.put(Starcatcher.rl("flip_sweetspots_on_miss"), FlipSweetSpotsOnMissModifier.CODEC);
    }
}