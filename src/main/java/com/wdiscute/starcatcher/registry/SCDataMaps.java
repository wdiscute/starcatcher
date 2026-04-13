package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.datamaps.DataMapType;

import java.util.List;

public class SCDataMaps
{
    public static final DataMapType<Item, AquariumBlock.Interaction> AQUARIUM_INTERACTION = DataMapType.builder(
            Starcatcher.rl("aquarium_interaction"), Registries.ITEM, AquariumBlock.Interaction.CODEC)
            .synced(AquariumBlock.Interaction.CODEC, true).build();

    public static final DataMapType<Item, List<ResourceLocation>> CATCH_MODIFIERS = DataMapType.builder(
            Starcatcher.rl("catch_modifiers"), Registries.ITEM, ResourceLocation.CODEC.listOf()
    ).synced(ResourceLocation.CODEC.listOf(), true).build();

    public static final DataMapType<Item, List<ResourceLocation>> MINIGAME_MODIFIERS = DataMapType.builder(
            Starcatcher.rl("minigame_modifiers"), Registries.ITEM, ResourceLocation.CODEC.listOf()
    ).synced(ResourceLocation.CODEC.listOf(), true).build();

    public static final DataMapType<Item, ResourceLocation> TACKLE_SKIN = DataMapType.builder(
            Starcatcher.rl("tackle_skin"), Registries.ITEM, ResourceLocation.CODEC
    ).synced(ResourceLocation.CODEC, true).build();

    public static final DataMapType<FishProperties, Treasure.TreasureInstance> TREASURE = DataMapType.builder(
            Starcatcher.rl("treasures"), Starcatcher.FISH_REGISTRY_KEY, Treasure.TREASURE_CODEC
    ).synced(Treasure.TREASURE_CODEC, true).build();




    public static <T> T getOrDefault(ItemStack stack, DataMapType<Item, T> dataMap, T d)
    {
        T data = stack.getItemHolder().getData(dataMap);
        if (data == null) return d;
        return data;
    }

}
