package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class SCKeymappings
{
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Starcatcher.rl("starcatcher"));
    public static final KeyMapping EXPAND_TOURNAMENT = new KeyMapping("key.starcatcher.expand_tournament", GLFW.GLFW_KEY_TAB, CATEGORY);
    public static final KeyMapping MINIGAME_HIT = new KeyMapping("key.starcatcher.minigame_hit", GLFW.GLFW_KEY_SPACE, CATEGORY);
}
