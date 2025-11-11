package com.wdiscute.starcatcher;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MINIGAME_GUI_SCALE = BUILDER
            .defineInRange("minigame_gui_scale", 3, 0, 6);

    public static final ModConfigSpec.DoubleValue HIT_DELAY = BUILDER
            .defineInRange("hit_delay", 0.0d, 0.0d, Double.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();


    private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_EXCLAMATION_MARK_PARTICLE = BUILDER_SERVER
            .define("show_exclamation_mark_particle", true);

    static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


}
