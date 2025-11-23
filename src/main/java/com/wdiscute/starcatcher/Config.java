package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue MINIGAME_GUI_SCALE = BUILDER
            .comment("//ALL THESE SETTINGS CAN ALSO BE ACCESSED")
            .comment("//THROUGH THE IN-GAME SETTING TAB INSIDE")
            .comment("//THE STARCATCHER'S GUIDE")
            .defineInRange("minigame_gui_scale", 3, 0, 6);

    public static final ForgeConfigSpec.DoubleValue HIT_DELAY = BUILDER
            .defineInRange("hit_delay", 0.0d, -20, 20);

    public static final ForgeConfigSpec.EnumValue<SettingsScreen.Units> UNIT = BUILDER
            .defineEnum("units", SettingsScreen.Units.IMPERIAL);

    public static final ForgeConfigSpec.EnumValue<FishingGuideScreen.Sort> SORT = BUILDER
            .defineEnum("sort", FishingGuideScreen.Sort.ALPHABETICAL_DOWN);

    static final ForgeConfigSpec SPEC = BUILDER.build();


    private static final ForgeConfigSpec.Builder BUILDER_SERVER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue SHOW_EXCLAMATION_MARK_PARTICLE = BUILDER_SERVER
            .define("show_exclamation_mark_particle", true);

    static final ForgeConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


}
