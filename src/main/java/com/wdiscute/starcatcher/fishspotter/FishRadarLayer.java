package com.wdiscute.starcatcher.fishspotter;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.FishProperties;
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

public class FishRadarLayer implements LayeredDraw.Layer
{

    private static final ResourceLocation ONE_ROW = Starcatcher.rl("textures/gui/fish_radar/1_row.png");
    private static final ResourceLocation TWO_ROWS = Starcatcher.rl("textures/gui/fish_radar/2_row.png");
    private static final ResourceLocation THREE_ROWS = Starcatcher.rl("textures/gui/fish_radar/3_row.png");
    private static final ResourceLocation FOUR_ROWS = Starcatcher.rl("textures/gui/fish_radar/4_row.png");
    private static final ResourceLocation FIVE_ROWS = Starcatcher.rl("textures/gui/fish_radar/5_row.png");
    private static final ResourceLocation SIX_ROWS = Starcatcher.rl("textures/gui/fish_radar/6_row.png");

    int uiX;
    int uiY;

    float offScreen = -150;

    Font font;

    int imageWidth = 101;
    int imageHeight = 160;

    float counterSinceLastRefresh = 999;

    Player player;
    ClientLevel level;

    List<FishProperties> fpsInArea = new ArrayList<>();
    List<FishProperties> fishesCaught = new ArrayList<>();

    private void recalculate()
    {
        fpsInArea.clear();

        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            if (fp.hasGuideEntry() && fp.calculateChance(player, player.level(), ItemStack.EMPTY, AbstractFishRestriction.Context.GUIDE_FISHES_HOVER) > 0)
                fpsInArea.add(fp);

        fishesCaught.clear();

        FishingGuideAttachment.getFishesCaught(player).forEach((loc, counter) ->
        {
            fishesCaught.add(U.getFpFromRl(level, loc));
        });
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

        boolean shouldShow = player.getMainHandItem().is(SCItems.FISH_RADAR) || player.getOffhandItem().is(SCItems.FISH_RADAR);

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

        switch (fpsInArea.size())
        {
            case 0, 1, 2, 3, 4, 5:
                renderImage(guiGraphics, ONE_ROW);
                break;

            case 6, 7, 8, 9, 10:
                renderImage(guiGraphics, TWO_ROWS);
                break;

            case 11, 12, 13, 14, 15:
                renderImage(guiGraphics, THREE_ROWS);
                break;

            case 16, 17, 18, 19, 20:
                renderImage(guiGraphics, FOUR_ROWS);
                break;

            case 21, 22, 23, 24, 25:
                renderImage(guiGraphics, FIVE_ROWS);
                break;

            default:
                renderImage(guiGraphics, SIX_ROWS);
        }

        int animationFrame = ((int) (level.getGameTime() / 2 % 32 + 1));
        renderImage(guiGraphics, Starcatcher.rl("textures/gui/fish_radar/radar_animation" + animationFrame + ".png"));

        //recalculate every 100 ticks?
        counterSinceLastRefresh += 1 * deltaTracker.getGameTimeDeltaTicks();
        if (counterSinceLastRefresh > 100) recalculate();

        for (int i = 0; i < fpsInArea.size(); i++)
        {
            ItemStack is = new ItemStack(SCItems.MISSINGNO.get());

            if (fishesCaught.contains(fpsInArea.get(i)))
            {
                is = new ItemStack(fpsInArea.get(i).catchInfo().fish());
            }

            guiGraphics.renderItem(
                    is,
                    uiX + 9 + i * 18 % 90,
                    uiY + 48 + i / 5 * 18);
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
