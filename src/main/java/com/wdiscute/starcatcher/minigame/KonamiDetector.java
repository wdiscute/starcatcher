package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.SCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class KonamiDetector
{
    private static final int[] CODE = {
            InputConstants.KEY_UP,
            InputConstants.KEY_UP,
            InputConstants.KEY_DOWN,
            InputConstants.KEY_DOWN,
            InputConstants.KEY_LEFT,
            InputConstants.KEY_RIGHT,
            InputConstants.KEY_LEFT,
            InputConstants.KEY_RIGHT,
            InputConstants.KEY_B,
            InputConstants.KEY_A
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
