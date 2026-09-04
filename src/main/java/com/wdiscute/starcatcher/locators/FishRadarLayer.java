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
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

import java.util.ArrayList;
import java.util.List;

public class FishRadarLayer implements GuiLayer
{
    private static final ScreenUtils.Image BASE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/fish_radar/base.png"), 101, 160);
    private static final ScreenUtils.Image EXTRA_ROW = new ScreenUtils.Image(Starcatcher.rl("textures/gui/fish_radar/extra_row.png"), 101, 22);

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

        for (FishProperties fp : player.level().registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            if (fp.hasGuideEntry() && fp.calculateChance(player, player.level(), ItemStack.EMPTY, AbstractFishRestriction.Context.RADAR) > 0)
                fpsInArea.add(fp);

        fishesCaught.clear();

        FishingGuideAttachment.getFishesCaught(player).forEach((loc, counter) ->
        {
            fishesCaught.add(FishApi.getFP(level, loc));
        });
    }

    @Override
    public void render(GuiGraphicsExtractor g, DeltaTracker deltaTracker)
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
                             || Minecraft.getInstance().gui.screen() instanceof SettingsScreen;

        //if any armor slots has tag
        if (!shouldShow)
        {
            shouldShow = shouldShow || player.getItemBySlot(EquipmentSlot.HEAD).is(SCTags.HAS_RADAR_LAYER);
            shouldShow = shouldShow || player.getItemBySlot(EquipmentSlot.CHEST).is(SCTags.HAS_RADAR_LAYER);
            shouldShow = shouldShow || player.getItemBySlot(EquipmentSlot.LEGS).is(SCTags.HAS_RADAR_LAYER);
            shouldShow = shouldShow || player.getItemBySlot(EquipmentSlot.FEET).is(SCTags.HAS_RADAR_LAYER);
        }

        //if any of the curios has the tag
        if (!shouldShow)
            if (CuriosCompat.isLoaded())
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

        g.pose().pushMatrix();
        g.pose().scale(((float) SCConfig.RADAR_SCALE.getAsDouble()), ((float) SCConfig.RADAR_SCALE.getAsDouble()));
        g.pose().translate(((float)SCConfig.RADAR_X_OFFSET.getAsDouble()), (float) SCConfig.RADAR_Y_OFFSET.getAsDouble());

        g.pose().translate(-offScreen, 0);

        //rows of radar to render
        BASE.render(g, uiX, uiY);

        int rows = (fpsInArea.size() - 1) / 5;

        for (int i = 0; i < rows; i++)
            EXTRA_ROW.render(g, uiX, uiY + i * 18 + 66);

        int animationFrame = ((int) (System.currentTimeMillis() / 100 % 32 + 1));
        new ScreenUtils.Image(
                Starcatcher.rl("textures/gui/fish_radar/radar_animation" + animationFrame + ".png"),
                101, 160)
                .render(g, uiX, uiY);

        //recalculate every <config value>
        if (System.currentTimeMillis() > lastRefreshMS + SCConfig.OVERLAY_UPDATE_FREQUENCY.get())
            recalculate();

        for (int i = 0; i < fpsInArea.size(); i++)
        {
            ItemStack is = new ItemStack(SCItems.MISSINGNO.get());

            if (fishesCaught.contains(fpsInArea.get(i)))
                is = fpsInArea.get(i).catchInfo().fish().toStack();

            ScreenUtils.item(g, is,
                    uiX + 9 + i * 18 % 90,
                    uiY + 48 + i / 5 * 18);
        }

        g.pose().popMatrix();
    }
}
