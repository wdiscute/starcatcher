package com.wdiscute.starcatcher.fishspotter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FishTrackerLayer implements LayeredDraw.Layer
{

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/fish_tracker.png");

    int uiX;
    int uiY;

    float offScreen = -150;

    Font font;

    int imageWidth = 150;
    int imageHeight = 100;

    float counterSinceLastRefresh = 999;

    Player player;
    ClientLevel level;

    List<FishProperties> fpsInArea = new ArrayList<>();
    List<FishProperties> fishesCaught;

    private void recalculate()
    {
        fpsInArea = FishProperties.getFpsWithGuideEntryForArea(player);
        fishesCaught = new ArrayList<>();
        for (FishCaughtCounter fishes : player.getData(ModDataAttachments.FISHES_CAUGHT)) fishesCaught.add(U.getFpFromRl(level, fishes.fp()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        font = Minecraft.getInstance().font;
        uiX = Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth;
        uiY = Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight - 80;

        if (Minecraft.getInstance().level == null) return;
        else level = Minecraft.getInstance().level;
        if (Minecraft.getInstance().player == null) return;
        else player = Minecraft.getInstance().player;

        boolean shouldShow = player.getMainHandItem().is(ModItems.FISH_SPOTTER) || player.getOffhandItem().is(ModItems.FISH_SPOTTER);

        //smoothly moves ui in and out of screen
        if (!shouldShow)
            if (offScreen > -150)
                offScreen -= 15 * deltaTracker.getGameTimeDeltaTicks();
            else
            {
                offScreen = -150;
                return;
            }
        else if (offScreen < 0)
            offScreen += 15 * deltaTracker.getGameTimeDeltaTicks();
        else
            offScreen = 0;


        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-offScreen, 0, 0);

        RenderSystem.enableBlend();

        renderImage(guiGraphics, BACKGROUND);

        RenderSystem.disableBlend();

        //recalculate every 100 ticks?
        counterSinceLastRefresh += 1 * deltaTracker.getGameTimeDeltaTicks();
        if (counterSinceLastRefresh > 100) recalculate();

        for (int i = 0; i < fpsInArea.size(); i++)
        {
            ItemStack is = new ItemStack(ModItems.MISSINGNO.get());

            if (fishesCaught.contains(fpsInArea.get(i)))
            {
                is = new ItemStack(fpsInArea.get(i).fish());
            }

            guiGraphics.renderItem(
                    is,
                    uiX + 50 + i * 20 % 100,
                    uiY + 10 + i / 5 * 20);

            if(i > 8)
            {
                break;
            }
        }


        guiGraphics.pose().popPose();

    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    private void drawComp(GuiGraphics guiGraphics, Component comp, int xOffset, int yOffset)
    {
        guiGraphics.drawString(font, comp, uiX + xOffset, uiY + yOffset, 0, false);
    }
}
