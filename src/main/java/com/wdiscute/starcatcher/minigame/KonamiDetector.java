package com.wdiscute.starcatcher.minigame;

import com.wdiscute.starcatcher.SCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class KonamiDetector
{
    private static final int[] CODE = {
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_LEFT,
            GLFW.GLFW_KEY_RIGHT,
            GLFW.GLFW_KEY_LEFT,
            GLFW.GLFW_KEY_RIGHT,
            GLFW.GLFW_KEY_B,
            GLFW.GLFW_KEY_A
    };

    private static int progress = 0;

    public static void keyPressed(int key)
    {
        if (key == CODE[progress])
        {
            progress++;

            if (progress == CODE.length)
            {
                SCConfig.DEBUG_MINIGAME.set(!SCConfig.DEBUG_MINIGAME.get());
                SCConfig.DEBUG_MINIGAME.save();
                if (SCConfig.DEBUG_MINIGAME.get())
                    Minecraft.getInstance().player.sendOverlayMessage(Component.literal("hackermans mode activated"));
                else
                    Minecraft.getInstance().player.sendOverlayMessage(Component.literal("hackermans mode deactivated"));

                progress = 0;
            }
        }
        else
        {
            progress = 0;
        }
    }
}
