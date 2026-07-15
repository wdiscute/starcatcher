package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlock;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Treasure;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.List;

public interface SCDataMaps
{
    DataMapType<Item, AquariumBlock.Interaction> AQUARIUM_INTERACTION = DataMapType.builder(
            Starcatcher.rl("aquarium_interaction"), Registries.ITEM, AquariumBlock.Interaction.CODEC)
            .synced(AquariumBlock.Interaction.CODEC, true).build();

    DataMapType<Item, List<Modifier>> ITEM_MODIFIERS = DataMapType.builder(
            Starcatcher.rl("modifiers"), Registries.ITEM, Modifier.CODEC.listOf()
    ).synced(Modifier.CODEC.listOf(), true).build();

    DataMapType<MobEffect, List<Modifier>> EFFECT_MODIFIERS = DataMapType.builder(
            Starcatcher.rl("modifiers"), Registries.MOB_EFFECT, Modifier.CODEC.listOf()
    ).synced(Modifier.CODEC.listOf(), true).build();

    DataMapType<Enchantment, List<Modifier>> ENCHANTMENT_MODIFIERS = DataMapType.builder(
            Starcatcher.rl("modifiers"), Registries.ENCHANTMENT, Modifier.CODEC.listOf()
    ).synced(Modifier.CODEC.listOf(), true).build();

    DataMapType<Item, AbstractTackleSkin> TACKLE_SKIN = DataMapType.builder(
            Starcatcher.rl("tackle_skin"), Registries.ITEM, Starcatcher.TACKLE_SKIN_REGISTRY.byNameCodec()
    ).synced(Starcatcher.TACKLE_SKIN_REGISTRY.byNameCodec(), true).build();

    DataMapType<FishProperties, Treasure> TREASURE = DataMapType.builder(
            Starcatcher.rl("treasures"), Starcatcher.FISH_REGISTRY_KEY, Treasure.CODEC
    ).synced(Treasure.CODEC, true).build();

    DataMapType<LevelStem, ResourceLocation> MESSAGE_BACKGROUND = DataMapType.builder(
            Starcatcher.rl("message_background"), Registries.LEVEL_STEM, ResourceLocation.CODEC
    ).build();

    static <T> T getOrDefault(ItemStack stack, DataMapType<Item, T> dataMap, T d)
    {
        T data = stack.getItemHolder().getData(dataMap);
        if (data == null) return d;
        return data;
    }

}
