package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.BonemealInteractionEntry;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.DataEntry;
import com.wdiscute.utils.Utils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import java.util.List;
import java.util.Map;

public interface SCDataEntries
{
    static void register(IEventBus eventBus){}

    DataEntry.MultiEntry<Utils.Duo<ResourceLocation, String>> DIMENSION_TAGS = DataEntry.MultiEntry.register(Starcatcher.rl("dimension_entries"),
            Utils.Duo.codec(ResourceLocation.CODEC, "dimension", Codec.STRING, "looks_like"))
            .sync(Utils.Duo.streamCodec(ResourceLocation.STREAM_CODEC, ByteBufCodecs.STRING_UTF8));

    //todo void fishing with this
    DataEntry<Map<String, Integer>> DIMENSION_VOID_LEVEL = DataEntry.register(Starcatcher.rl("dimension_void_level"),
            Codec.unboundedMap(Codec.STRING, Codec.INT),
            Map.of());

    //this should be empty as defaults come from datapack!
    DataEntry.MultiEntry<Modifier> DEFAULT_CATCH_MODIFIERS = DataEntry.MultiEntry.register(Starcatcher.rl("default_catch_modifiers"), Modifier.CODEC);

    //this should be empty as defaults come from datapack!
    DataEntry.MultiEntry<Modifier> DEFAULT_MINIGAME_MODIFIERS = DataEntry.MultiEntry.register(Starcatcher.rl("default_minigame_modifiers"), Modifier.CODEC);

    DataEntry.MultiEntry<BonemealInteractionEntry> BONEMEAL_INTERACTION_ENTRY = DataEntry.MultiEntry.register(Starcatcher.rl("bonemeal_interaction_drops"), BonemealInteractionEntry.CODEC);
}
