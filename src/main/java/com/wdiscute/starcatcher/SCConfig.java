package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SCConfig
{
    private static final ModConfigSpec.Builder BUILDER_CLIENT = new ModConfigSpec.Builder();

    //minigame & overlays positioning
    public static final ModConfigSpec.DoubleValue MINIGAME_X_OFFSET = BUILDER_CLIENT
            .push("ui_settings")
            .translation("starcatcher.configuration.minigame_x_offset")
            .defineInRange("minigame_x_offset", 0d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue MINIGAME_Y_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.minigame_y_offset")
            .defineInRange("minigame_y_offset", 0d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue MINIGAME_RENDER_SCALE = BUILDER_CLIENT
            .translation("starcatcher.configuration.minigame_scale")
            .defineInRange("minigame_scale", 1.5, 0.1, 6);

    public static final ModConfigSpec.DoubleValue TOURNAMENT_X_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.tournament_x_offset")
            .defineInRange("tournament_x_offset", 0d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue TOURNAMENT_Y_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.tournament_y_offset")
            .defineInRange("tournament_y_offset", 0d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue TOURNAMENT_SCALE = BUILDER_CLIENT
            .translation("starcatcher.configuration.tournament_scale")
            .defineInRange("tournament_scale", 1d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue RADAR_X_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.radar_x_offset")
            .defineInRange("radar_x_offset", 0d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue RADAR_Y_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.radar_y_offset")
            .defineInRange("radar_y_offset", 80d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue RADAR_SCALE = BUILDER_CLIENT
            .translation("starcatcher.configuration.radar_scale")
            .defineInRange("radar_scale", 1d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue TRACKER_X_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.tracker_x_offset")
            .defineInRange("tracker_x_offset", 0d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue TRACKER_Y_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.tracker_y_offset")
            .defineInRange("tracker_y_offset", -50d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue TRACKER_SCALE = BUILDER_CLIENT
            .translation("starcatcher.configuration.tracker_scale")
            .defineInRange("tracker_scale", 1d, -10000, 10000);

    public static final ModConfigSpec.DoubleValue HIT_DELAY = BUILDER_CLIENT
            .pop()
            .translation("starcatcher.configuration.hit_delay")
            .defineInRange("hit_delay", 0.0d, -20, 20);

    public static final ModConfigSpec.BooleanValue DEBUG_MINIGAME = BUILDER_CLIENT
            .comment("Shows a bunch of extra text info during the minigame for debugging")
            .translation("starcatcher.configuration.debug")
            .define("debug", false);

    //non minigame
    public static final ModConfigSpec.IntValue OVERLAY_UPDATE_FREQUENCY = BUILDER_CLIENT
            .translation("starcatcher.configuration.overlay_update_frequency")
            .defineInRange("overlay_update_frequency", 1000, 100, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue REMOVE_NOTIFICATION_ON_HOVER = BUILDER_CLIENT
            .translation("starcatcher.configuration.remove_notif_on_hover")
            .comment("If enabled the new fish notifications will be removed when hovering on the index, instead of only when visiting the specific fish entry")
            .define("remove_notif_on_hover", true);

    public static final ModConfigSpec.EnumValue<SizeAndWeight.Units> UNIT = BUILDER_CLIENT
            .translation("starcatcher.configuration.units")
            .defineEnum("units", SizeAndWeight.Units.METRIC);

    public static final ModConfigSpec.EnumValue<FishingGuideScreen.Sort> SORT = BUILDER_CLIENT
            .translation("starcatcher.configuration.sort")
            .defineEnum("sort", FishingGuideScreen.Sort.ALPHABETICAL_DOWN);

    public static final ModConfigSpec.BooleanValue ENABLE_TACKLE_SOUNDS = BUILDER_CLIENT
            .comment("Controls whether sounds for reeling, failing minigame, completing minigame etc should play")
            .translation("starcatcher.configuration.enable_tackle_sounds")
            .define("enable_tackle_sound", true);

    public static final ModConfigSpec.BooleanValue ENABLE_HIT_SOUNDS = BUILDER_CLIENT
            .comment("Should sweet-spots play the hit sounds")
            .translation("starcatcher.configuration.enable_hit_sounds")
            .define("enable_hit_sounds", true);

    public static final ModConfigSpec.BooleanValue ENABLE_MISS_SOUND = BUILDER_CLIENT
            .comment("Should play the hit sound")
            .translation("starcatcher.configuration.enable_miss_sound")
            .define("enable_miss_sound", true);


    static final ModConfigSpec SPEC = BUILDER_CLIENT.build();


    private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue GIVE_GUIDE = BUILDER_SERVER
            .translation("starcatcher.configuration.give_guide")
            .comment("Award guide when joining the world/server once per player")
            .define("give_guide", true);

    public static final ModConfigSpec.BooleanValue GIVE_ROD = BUILDER_SERVER
            .translation("starcatcher.configuration.give_rod")
            .comment("Award rod & guide the first time a player fishes with a vanilla fishing rod")
            .define("give_rod", true);

    public static final ModConfigSpec.BooleanValue ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS = BUILDER_SERVER
            .translation("starcatcher.configuration.enable_worms")
            .comment("Enables/disables the ability to bonemeal farmland for worms.")
            .define("enable_worms", true);

    public static final ModConfigSpec.BooleanValue ENABLE_MINIGAME = BUILDER_SERVER
            .translation("starcatcher.configuration.enable_minigame")
            .define("enable_minigame", true);

    public static final ModConfigSpec.BooleanValue ENABLE_FTB_TEAM_SHARING = BUILDER_SERVER
            .comment("Enables/disables fishes caught being unlocked for all online team members.")
            .comment("Offline players won't be awarded the entry.")
            .translation("starcatcher.configuration.enable_ftb_team_sharing")
            .define("enable_ftb_team_sharing", true);

    public static final ModConfigSpec.DoubleValue VANISHING_RATE_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the vanishing rate, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.vanishing_rate_multiplier")
            .defineInRange("vanishing_rate_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.DoubleValue MOVING_SPEED_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the moving sweet-spots speed, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.moving_speed_multiplier")
            .defineInRange("moving_speed_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.DoubleValue PENALTY_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the penalty for missing rate, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.penalty_multiplier")
            .defineInRange("penalty_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.DoubleValue DECAY_RATE_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the fish decay rate, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.decay_multiplier")
            .defineInRange("decay_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.DoubleValue HP_RATE_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the fish's HP, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.hp_multiplier")
            .defineInRange("hp_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.DoubleValue HANDLE_SPEED_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the handle speed, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.handle_speed_multiplier")
            .defineInRange("handle_speed_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.BooleanValue HIDE_ENTRIES_UNTIL_FOUND = BUILDER_SERVER
            .comment("Hides entries in the guide book until one has been caught.")
            .translation("starcatcher.configuration.hide_entries_until_found")
            .define("hide_entries_until_found", true);

    public static final ModConfigSpec.BooleanValue SAVE_DATA_TO_ITEMS = BUILDER_SERVER
            .comment("If enabled the size, weight, percentile, rarity & golden values will be saved to the item for displaying")
            .translation("starcatcher.configuration.save_data_to_items")
            .define("save_data_to_items", true);

    public static final ModConfigSpec.IntValue BASE_MIN_TICKS_TO_FISH = BUILDER_SERVER
            .comment("How many ticks should the bobber bobble for before being allowed to catch a fish (excluding modifiers)")
            .translation("starcatcher.configuration.base_min_ticks")
            .defineInRange("base_min_ticks", 100, 0, 999999);

    public static final ModConfigSpec.IntValue BASE_MAX_TICKS_TO_FISH = BUILDER_SERVER
            .comment("How many ticks at the maximum should the bobber bob for (excluding modifiers)")
            .translation("starcatcher.configuration.base_max_ticks")
            .defineInRange("base_max_ticks", 300, 0, 999999);

    public static final ModConfigSpec.DoubleValue BASE_CHANCE_TO_FISH = BUILDER_SERVER
            .comment("The rod weight to fish every tick (out of 1)")
            .translation("starcatcher.configuration.base_chance")
            .defineInRange("base_chance", 0.005d, 0, 1);

    public static final ModConfigSpec.BooleanValue RESTRICT_TACKLE_BOX_TO_TAG = BUILDER_SERVER
            .comment("Restricts items placeable inside the tackle box to #starcatcher:placeable_in_tacle_box")
            .translation("starcatcher.configuration.restrict_tackle_box_to_tag")
            .define("restrict_tackle_box_to_tag", true);

    public static final ModConfigSpec.DoubleValue FISH_MAX_SCALE = BUILDER_SERVER
            .comment("Controls the maximum scale of the fish model based on the size and weight percentile")
            .translation("starcatcher.configuration.fish_percentile_size_max_scale")
            .defineInRange("fish_percentile_size_max_scale", 1.5d, 0, 100);

    public static final ModConfigSpec.DoubleValue FISH_MIN_SCALE = BUILDER_SERVER
            .comment("Controls the minimum scale of the fish model based on the size and weight percentile")
            .translation("starcatcher.configuration.fish_percentile_size_min_scale")
            .defineInRange("fish_percentile_size_min_scale", 0.5d, 0, 100);


    public static final ModConfigSpec.IntValue MAX_TACKLE_BOX_FISH_STORAGE = BUILDER_SERVER
            .comment("Sets the maximum number of fishes the tackle box can store in its 'infinite slot'")
            .translation("starcatcher.configuration.max_tackle_box_fish_storage")
            .defineInRange("max_tackle_box_fish_storage", 900, 0, 999);

    public static final ModConfigSpec.BooleanValue HIDE_TREASURES = BUILDER_SERVER
            .comment("Hides the treasure item during minigame")
            .translation("starcatcher.configuration.hide_treasures")
            .define("hide_treasures", false);

    public static final ModConfigSpec.BooleanValue HIDE_CATCHES = BUILDER_SERVER
            .comment("Hides the catch during minigame")
            .translation("starcatcher.configuration.hide_catches")
            .define("hide_catches", false);

    public static final ModConfigSpec.BooleanValue ENABLE_ROD_DURABILITY = BUILDER_SERVER
            .comment("Enable Starcatcher's fishing rod durability")
            .translation("starcatcher.configuration.enable_rod_durability")
            .define("enable_rod_durability", false);

    public static final ModConfigSpec.BooleanValue ALLOW_GUIDE_KEYBIND = BUILDER_SERVER
            .comment("Allows players to use a keybind to open the guide without having the item")
            .translation("starcatcher.configuration.allow_guide_keybind")
            .define("allow_guide_keybind", true);

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();

}
