package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SCTags
{

    public static final ResourceLocation IS_COLD_LAKE = Starcatcher.rl("is_cold_lake");
    public static final ResourceLocation IS_COLD_RIVER = Starcatcher.rl("is_cold_river");
    public static final ResourceLocation IS_COLD_OCEAN = Starcatcher.rl("is_cold_ocean");

    public static final ResourceLocation IS_WARM_LAKE = Starcatcher.rl("is_warm_lake");
    public static final ResourceLocation IS_WARM_RIVER = Starcatcher.rl("is_warm_river");
    public static final ResourceLocation IS_WARM_OCEAN = Starcatcher.rl("is_warm_ocean");

    public static final ResourceLocation IS_OCEAN = Starcatcher.rl("is_ocean");
    public static final ResourceLocation IS_NORMAL_OCEAN = Starcatcher.rl("is_normal_ocean");
    public static final ResourceLocation IS_LUKEWARM_OCEAN = Starcatcher.rl("is_lukewarm_ocean");
    public static final ResourceLocation IS_DEEP_OCEAN = Starcatcher.rl("is_deep_ocean");
    public static final ResourceLocation IS_RIVER = Starcatcher.rl("is_river");

    public static final ResourceLocation IS_BEACH = Starcatcher.rl("is_beach");
    public static final ResourceLocation IS_SWAMP = Starcatcher.rl("is_swamp");
    public static final ResourceLocation IS_CHERRY_GROVE = Starcatcher.rl("is_cherry_grove");
    public static final ResourceLocation IS_MUSHROOM_FIELDS = Starcatcher.rl("is_mushroom_fields");
    public static final ResourceLocation IS_DARK_FOREST = Starcatcher.rl("is_dark_forest");
    public static final ResourceLocation IS_BIRCH_FOREST = Starcatcher.rl("is_birch_forest");

    public static final ResourceLocation IS_CRIMSON_FOREST = Starcatcher.rl("is_crimson_forest");
    public static final ResourceLocation IS_WARPED_FOREST = Starcatcher.rl("is_warped_forest");
    public static final ResourceLocation IS_SOUL_SAND_VALLEY = Starcatcher.rl("is_soul_sand_valley");
    public static final ResourceLocation IS_BASALT_DELTAS = Starcatcher.rl("is_basalt_deltas");

    public static final TagKey<Item> TACKLE_BOXES = ItemTags.create(Starcatcher.rl("tackle_boxes"));
    public static final TagKey<Item> HOOKS = ItemTags.create(Starcatcher.rl("hooks"));
    public static final TagKey<Item> BOBBERS = ItemTags.create(Starcatcher.rl("bobbers"));
    public static final TagKey<Item> BAITS = ItemTags.create(Starcatcher.rl("baits"));
    public static final TagKey<Item> TACKLE_SKINS = ItemTags.create(Starcatcher.rl("tackle_skins"));
    public static final TagKey<Item> TEMPLATES = ItemTags.create(Starcatcher.rl("templates"));
    public static final TagKey<Item> EQUIPMENTS = ItemTags.create(Starcatcher.rl("equipments"));
    public static final TagKey<Item> GADGETS = ItemTags.create(Starcatcher.rl("gadgets"));
    public static final TagKey<Item> HATS = ItemTags.create(Starcatcher.rl("hats"));

    public static final TagKey<Item> AQUARIUM_INTERACTIONS = ItemTags.create(Starcatcher.rl("aquarium_guide_display"));

    public static final TagKey<Item> PLACEABLE_IN_DISPLAY = ItemTags.create(Starcatcher.rl("placeable_in_display"));
    public static final TagKey<Item> PLACEABLE_IN_TACKLE_BOX = ItemTags.create(Starcatcher.rl("placeable_in_tackle_box"));
    public static final TagKey<Item> PLACEABLE_IN_TACKLE_BOX_FISH_SLOT = ItemTags.create(Starcatcher.rl("placeable_in_tackle_box_fish_slot"));
    public static final TagKey<Item> WORMS = ItemTags.create(Starcatcher.rl("worms"));

    public static final TagKey<Item> RODS = ItemTags.create(Starcatcher.rl("rods"));

    public static final TagKey<Item> BUCKETABLE_FISHES = ItemTags.create(Starcatcher.rl("bucketable_fishes"));
    public static final TagKey<Item> STARCAUGHT_FISHES = ItemTags.create(Starcatcher.rl("starcaught_fishes"));

    public static final TagKey<Item> TRASH = ItemTags.create(Starcatcher.rl("trash"));

    public static final TagKey<Item> COMMON_FISHES = ItemTags.create(Starcatcher.rl("common_fishes"));
    public static final TagKey<Item> UNCOMMON_FISHES = ItemTags.create(Starcatcher.rl("uncommon_fishes"));
    public static final TagKey<Item> RARE_FISHES = ItemTags.create(Starcatcher.rl("rare_fishes"));
    public static final TagKey<Item> EPIC_FISHES = ItemTags.create(Starcatcher.rl("epic_fishes"));
    public static final TagKey<Item> LEGENDARY_FISHES = ItemTags.create(Starcatcher.rl("legendary_fishes"));

    public static final TagKey<FishProperties> TRASH_FISHES_FP = TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl("trash_fishes"));
    public static final TagKey<FishProperties> COMMON_FISHES_FP = TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl("common_fishes"));
    public static final TagKey<FishProperties> UNCOMMON_FISHES_FP = TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl("uncommon_fishes"));
    public static final TagKey<FishProperties> RARE_FISHES_FP = TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl("rare_fishes"));
    public static final TagKey<FishProperties> EPIC_FISHES_FP = TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl("epic_fishes"));
    public static final TagKey<FishProperties> LEGENDARY_FISHES_FP = TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl("legendary_fishes"));


}
