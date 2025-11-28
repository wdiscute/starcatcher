package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.tournament.StandScreen;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MINIGAME_GUI_SCALE = BUILDER
            .comment("//ALL THESE SETTINGS CAN ALSO BE ACCESSED")
            .comment("//THROUGH THE IN-GAME SETTING TAB INSIDE")
            .comment("//THE STARCATCHER'S GUIDE")
            .defineInRange("minigame_gui_scale", 3, 0, 6);

    public static final ModConfigSpec.DoubleValue HIT_DELAY = BUILDER
            .defineInRange("hit_delay", 0.0d, -20, 20);

    public static final ModConfigSpec.EnumValue<SettingsScreen.Units> UNIT = BUILDER
            .defineEnum("units", SettingsScreen.Units.IMPERIAL);

    public static final ModConfigSpec.EnumValue<FishingGuideScreen.Sort> SORT = BUILDER
            .defineEnum("sort", FishingGuideScreen.Sort.ALPHABETICAL_DOWN);

    public static final ModConfigSpec.EnumValue<StandScreen.DurationDisplay> DURATION = BUILDER
            .defineEnum("duration_display", StandScreen.DurationDisplay.MINUTES);

    static final ModConfigSpec SPEC = BUILDER.build();


    private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_EXCLAMATION_MARK_PARTICLE = BUILDER_SERVER
            .define("show_exclamation_mark_particle", true);

    public static final ModConfigSpec.BooleanValue ENABLE_MINIGAME = BUILDER_SERVER
            .define("enable_minigame", true);

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


}
