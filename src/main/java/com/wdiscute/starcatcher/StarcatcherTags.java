package com.wdiscute.starcatcher;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface StarcatcherTags
{
    ResourceLocation IS_COLD_LAKE = Starcatcher.rl("is_cold_lake");
    ResourceLocation IS_COLD_RIVER = Starcatcher.rl("is_cold_river");
    ResourceLocation IS_COLD_OCEAN = Starcatcher.rl("is_cold_ocean");

    ResourceLocation IS_WARM_LAKE = Starcatcher.rl("is_warm");
    ResourceLocation IS_WARM_RIVER = Starcatcher.rl("is_warm_river");
    ResourceLocation IS_WARM_OCEAN = Starcatcher.rl("is_warm_river");

    ResourceLocation IS_OCEAN = Starcatcher.rl("is_ocean");
    ResourceLocation IS_DEEP_OCEAN = Starcatcher.rl("is_deep_ocean");
    ResourceLocation IS_RIVER = Starcatcher.rl("is_river");

    ResourceLocation IS_BEACH = Starcatcher.rl("is_beach");
    ResourceLocation IS_SWAMP = Starcatcher.rl("is_swamp");
    ResourceLocation IS_CHERRY_GROVE = Starcatcher.rl("is_cherry_grove");
    ResourceLocation IS_MUSHROOM_FIELDS = Starcatcher.rl("is_mushroom_fields");
    ResourceLocation IS_DARK_FOREST = Starcatcher.rl("is_dark_forest");
    ResourceLocation IS_BIRCH_FOREST = Starcatcher.rl("is_birch_forest");

    TagKey<Item> HOOKS = ItemTags.create(Starcatcher.rl("hooks"));
    TagKey<Item> BOBBERS = ItemTags.create(Starcatcher.rl("bobbers"));
    TagKey<Item> HOOKS_SURVIVE_FIRE = ItemTags.create(Starcatcher.rl("hooks_survive_fire"));

    TagKey<Item> CURATED_TREASURE = ItemTags.create(Starcatcher.rl("curated_treasure"));
}
