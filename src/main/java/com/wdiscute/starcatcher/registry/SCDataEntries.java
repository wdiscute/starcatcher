package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import java.util.List;
import java.util.Map;

public interface SCDataEntries
{
    static void register(IEventBus eventBus){}

    //todo tag-like loader system for this
    DataEntry<Map<String, List<ResourceLocation>>> DIMENSION_TAGS = DataEntry.register(Starcatcher.rl("dimension_tags"),
            Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC.listOf()),
            Map.of());

    //todo void fishing with this
    DataEntry<Map<String, Integer>> DIMENSION_VOID_LEVEL = DataEntry.register(Starcatcher.rl("dimension_void_level"),
            Codec.unboundedMap(Codec.STRING, Codec.INT),
            Map.of());

    //this should be empty as defaults come from datapack!
    DataEntry<List<Modifier>> DEFAULT_CATCH_MODIFIERS = DataEntry.register(Starcatcher.rl("default_catch_modifiers"), Modifier.CODEC.listOf(),
            List.of());

    //this should be empty as defaults come from datapack!
    DataEntry<List<Modifier>> DEFAULT_MINIGAME_MODIFIERS = DataEntry.register(Starcatcher.rl("default_minigame_modifiers"), Modifier.CODEC.listOf(),
            List.of());

    DataEntry<List<Utils.Duo<ItemStack, Integer>>> FARMLAND_BONEMEAL_DROPS = DataEntry.register(Starcatcher.rl("farmland_bonemeal_drops"),
            Utils.Duo.codec(ItemStack.CODEC, "item", Codec.INT, "weight").listOf(),
            List.of());
}
