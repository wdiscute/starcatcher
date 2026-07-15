package com.wdiscute.starcatcher.locators;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.compat.curios.CuriosCompat;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.fish.FishProperties;
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
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class FishRadarLayer implements LayeredDraw.Layer
{

    private static final ResourceLocation BASE = Starcatcher.rl("textures/gui/fish_radar/base.png");
    private static final ResourceLocation EXTRA_ROW = Starcatcher.rl("textures/gui/fish_radar/extra_row.png");

    int uiX;
    int uiY;

    float offScreen = -150;

    Font font;

    int imageWidth = 101;
    int imageHeight = 160;

    long lastRefreshMS = 0;

    Player player;
    ClientLevel level;

    List<FishProperties> fpsInArea = new ArrayList<>();
    List<FishProperties> fishesCaught = new ArrayList<>();

    private void recalculate()
    {
        fpsInArea.clear();

        lastRefreshMS = System.currentTimeMillis();

        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            if (fp.hasGuideEntry() && fp.calculateChance(player, player.level(), ItemStack.EMPTY, AbstractFishRestriction.Context.RADAR) > 0)
                fpsInArea.add(fp);

        fishesCaught.clear();

        FishingGuideAttachment.getFishesCaught(player).forEach((loc, counter) ->
        {
            fishesCaught.add(FishApi.getFP(level, loc));
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

        //if is holding item with tag to show radar in hand or is in settings screen
        boolean shouldShow = player.getMainHandItem().is(SCTags.HAS_RADAR_LAYER)
                             || player.getOffhandItem().is(SCTags.HAS_RADAR_LAYER)
                             || Minecraft.getInstance().screen instanceof SettingsScreen;

        //if any armor slots has tag
        if (!shouldShow)
            shouldShow = player.getInventory().armor.stream().anyMatch(o -> o.is(SCTags.HAS_RADAR_LAYER));

        //if any of the curios has the tag
        if (!shouldShow)
            if (ModList.get().isLoaded("curios"))
                shouldShow = CuriosCompat.getItems(player).stream().anyMatch(o -> o.is(SCTags.HAS_RADAR_LAYER));

        //smoothly moves ui in and out of screen
        if (!shouldShow)
            if (offScreen > -150 + SCConfig.RADAR_X_OFFSET.get())
                offScreen -= 15 * deltaTracker.getGameTimeDeltaTicks();
            else
            {
                offScreen = (float) (-150 + SCConfig.RADAR_X_OFFSET.get());
                return;
            }
        else if (offScreen < 0)
            offScreen += 15 * deltaTracker.getGameTimeDeltaTicks();
        else
            offScreen = 0;


        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(((float) SCConfig.RADAR_SCALE.getAsDouble()), ((float) SCConfig.RADAR_SCALE.getAsDouble()), 1);
        guiGraphics.pose().translate(SCConfig.RADAR_X_OFFSET.get(), SCConfig.RADAR_Y_OFFSET.get(), 0);

        guiGraphics.pose().translate(-offScreen, 0, 0);

        //rows of radar to render
        renderImage(guiGraphics, BASE);

        int rows = (fpsInArea.size() -1) / 5;

        for (int i = 0; i < rows; i++)
        {
            guiGraphics.blit(EXTRA_ROW, uiX, uiY + i * 18 + 66, 0, 0, 101, 22, 101, 22);
        }


        int animationFrame = ((int) (level.getGameTime() / 2 % 32 + 1));
        renderImage(guiGraphics, Starcatcher.rl("textures/gui/fish_radar/radar_animation" + animationFrame + ".png"));

        //recalculate every <config value>
        if (System.currentTimeMillis() > lastRefreshMS + SCConfig.OVERLAY_UPDATE_FREQUENCY.get())
            recalculate();

        for (int i = 0; i < fpsInArea.size(); i++)
        {
            ItemStack is = new ItemStack(SCItems.MISSINGNO.get());

            if (fishesCaught.contains(fpsInArea.get(i)))
                is = fpsInArea.get(i).catchInfo().fish().toStack();

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
