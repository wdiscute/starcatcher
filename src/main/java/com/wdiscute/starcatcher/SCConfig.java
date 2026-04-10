package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.resources.language.I18n;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SCConfig
{
    private static final ModConfigSpec.Builder BUILDER_CLIENT = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MINIGAME_RENDER_SCALE = BUILDER_CLIENT
            .push("minigame_window")
            .translation("starcatcher.configuration.minigame_scale")
            .defineInRange("minigame_scale", 1.5, 0.1, 6);

    public static final ModConfigSpec.IntValue MINIGAME_X_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.x_offset")
            .defineInRange("minigame_x_offset", 0, -10000, 10000);

    public static final ModConfigSpec.IntValue MINIGAME_Y_OFFSET = BUILDER_CLIENT
            .translation("starcatcher.configuration.y_offset")
            .defineInRange("minigame_y_offset", 0, -10000, 10000);

    public static final ModConfigSpec.DoubleValue HIT_DELAY = BUILDER_CLIENT
            .pop()
            .translation("starcatcher.configuration.hit_delay")
            .defineInRange("hit_delay", 0.0d, -20, 20);

    public static final ModConfigSpec.BooleanValue REMOVE_NOTIFICATION_ON_HOVER = BUILDER_CLIENT
            .translation("starcatcher.configuration.remove_notif_on_hover")
            .comment("If enabled the new fish notifications will be removed when hovering on the index, instead of only when visiting the specific fish entry")
            .define("remove_notif_on_hover", true);

    public static final ModConfigSpec.EnumValue<FishProperties.SizeAndWeight.Units> UNIT = BUILDER_CLIENT
            .translation("starcatcher.configuration.units")
            .defineEnum("units", FishProperties.SizeAndWeight.Units.METRIC);

    public static final ModConfigSpec.EnumValue<FishingGuideScreen.Sort> SORT = BUILDER_CLIENT
            .translation("starcatcher.configuration.sort")
            .defineEnum("sort", FishingGuideScreen.Sort.ALPHABETICAL_DOWN);

    public static final ModConfigSpec.BooleanValue ENABLE_VILLAGER_SOUND = BUILDER_CLIENT
            .translation("starcatcher.configuration.enable_villager_sound")
            .define("enable_villager_sound", true);

    public static final ModConfigSpec.BooleanValue ENABLE_HIT_SOUND = BUILDER_CLIENT
            .translation("starcatcher.configuration.enable_hit_sound")
            .define("enable_hit_sound", true);

    public static final ModConfigSpec.BooleanValue ENABLE_MISS_SOUND = BUILDER_CLIENT
            .translation("starcatcher.configuration.enable_miss_sound")
            .define("enable_miss_sound", true);

    public static final ModConfigSpec.BooleanValue VANILLA_PARTIAL_TICK = BUILDER_CLIENT
            .comment("Whether to use the vanilla partial ticks for minigame smoothing or a custom implementation from 1.20")
            .comment("Vanilla should look better for most people")
            .translation("starcatcher.configuration.vanilla_partial_ticks")
            .define("vanilla_partial_ticks", true);


    static final ModConfigSpec SPEC = BUILDER_CLIENT.build();


    private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue GIVE_GUIDE = BUILDER_SERVER
            .translation("starcatcher.configuration.give_guide")
            .comment("Award guide when joining the world/server once per player")
            .define("give_guide", true);

    public static final ModConfigSpec.BooleanValue SHOW_EXCLAMATION_MARK_PARTICLE = BUILDER_SERVER
            .translation("starcatcher.configuration.show_exclamation_mark_particle")
            .define("show_exclamation_mark_particle", false);

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
            .define("enable_ftb_team_sharing", false);

    public static final ModConfigSpec.BooleanValue ENABLE_SEASONS = BUILDER_SERVER
            .comment("Enables/disables fishes being restricted by seasons.")
            .comment("Useful if you want to play with a seasons mod but don't like the built-in restrictions.")
            .translation("starcatcher.configuration.enable_seasons")
            .define("enable_seasons", true);

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

    public static final ModConfigSpec.DoubleValue POINTER_SPEED_MULTIPLIER = BUILDER_SERVER
            .comment("Adjusts the pointer speed rate, useful if you want to adjust the fishes' difficulty globally.")
            .translation("starcatcher.configuration.pointer_speed_multiplier")
            .defineInRange("pointer_speed_multiplier", 1d, 0d, 100d);

    public static final ModConfigSpec.BooleanValue HIDE_ENTRIES_UNTIL_FOUND = BUILDER_SERVER
            .comment("Hides entries in the guide book until one has been caught.")
            .translation("starcatcher.configuration.hide_entries_until_found")
            .define("hide_entries_until_found", true);

    public static final ModConfigSpec.DoubleValue FISH_PLAYER_MESSAGES_CHANCE = BUILDER_SERVER
            .comment("Controls the chance of fishing up messages-in-a-bottle left by other players of the server. Does not affect built-in Secrets")
            .translation("starcatcher.configuration.fish_player_messages_chance")
            .defineInRange("fish_player_messages_chance", 0.05d, 0d, 1d);

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
            .comment("The base chance to fish every tick (out of 1)")
            .translation("starcatcher.configuration.base_chance")
            .defineInRange("base_chance", 0.005d, 0, 1);

    public static final ModConfigSpec.BooleanValue RESTRICT_TACKLE_BOX_TO_TAG = BUILDER_SERVER
            .comment("Restricts items placeable inside the tackle box to #starcatcher:placeable_in_tacle_box")
            .translation("starcatcher.configuration.restrict_tackle_box_to_tag")
            .define("restrict_tackle_box_to_tag", true);

    public static final ModConfigSpec.BooleanValue ENABLE_ROD_MENU = BUILDER_SERVER
            .translation("starcatcher.configuration.enable_rod_menu")
            .define("enable_rod_menu", false);

    public static final ModConfigSpec.DoubleValue FISH_MAX_SCALE = BUILDER_SERVER
            .comment("Controls the maximum scale of the fish model based on the size and weight percentile")
            .translation("starcatcher.configuration.fish_percentile_size_max_scale")
            .defineInRange("fish_percentile_size_max_scale", 1.5d, 0, 100);

    public static final ModConfigSpec.DoubleValue FISH_MIN_SCALE = BUILDER_SERVER
            .comment("Controls the minimum scale of the fish model based on the size and weight percentile")
            .translation("starcatcher.configuration.fish_percentile_size_min_scale")
            .defineInRange("fish_percentile_size_min_scale", 0.5d, 0, 100);

    //todo add base modifiers config
//    public static final ModConfigSpec.ListValueSpec BASE_MODIFIERS = BUILDER_SERVER
//            .comment("Adjusts the fish decay rate multiplier, useful if you want to adjust the fishes' difficulty globally.")
//            .define("pointer_speed_multiplier", List.of());

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();

}
