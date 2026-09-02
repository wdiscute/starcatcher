package com.wdiscute.starcatcher.locators;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FishTrackerLayer implements GuiLayer
{
    private static final ScreenUtils.Image BACKGROUND = new ScreenUtils.Image(Starcatcher.rl("textures/gui/fish_tracker/background.png"), 155, 135);
    private static final ScreenUtils.Image EMPTY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/fish_tracker/empty.png"), 34, 34);

    int uiX;
    int uiY;

    float offScreen = -150;

    Font font;

    int imageWidth = 155;
    int imageHeight = 135;

    long lastRefreshMS = 0;

    Player player;
    ClientLevel level;

    int cachedChance = 0;
    int cachedTotalChance = 0;
    boolean cachedCaughtFish = false;
    List<Component> cachedRestrictions = new ArrayList<>();

    FishProperties cachedFP = null;
    Identifier cachedRL = null;

    private void recalculate()
    {
        lastRefreshMS = System.currentTimeMillis();

        LocalPlayer player = Minecraft.getInstance().player;
        cachedRL = player.getData(SCDataAttachments.TRACKED_FISH);
        cachedCaughtFish = player.getData(SCDataAttachments.FISHING_GUIDE).fishesCaught.containsKey(cachedRL);

        //chances
        cachedChance = 0;
        cachedTotalChance = 0;
        List<FishProperties> fishes = FishApi.getFishes(level);

        for (FishProperties fish : fishes)
        {
            Identifier key = level.registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fish);

            int chance = fish.calculateChance(player, level, player.getMainHandItem().is(SCTags.RODS) ? player.getMainHandItem() : player.getOffhandItem(), AbstractFishRestriction.Context.TRACKER);

            //if fish being checked is registered
            if (key != null)
            {
                //if fish being checked is tracked fish
                if (key.equals(cachedRL))
                {
                    cachedFP = fish;
                    //if chance > 0 then add that chance to chanced and total chance pool
                    if (chance > 0)
                    {
                        cachedChance = chance;
                        cachedTotalChance += chance;
                    }
                }
                else
                //if not tracked fish
                {
                    //add chance to total if it's possible
                    if (chance > 0)
                        cachedTotalChance += chance;
                }
            }
        }

        //cache restrictions
        cachedRestrictions.clear();
        if (cachedFP != null)
            cachedFP.restrictions().stream().filter(AbstractFishRestriction::isEnabled)
                    .forEach(o -> cachedRestrictions
                            .addAll(o.getIndexHover(level, cachedFP, player, AbstractFishRestriction.Context.GUIDE_FISHES_HOVER)));
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

        boolean shouldShow = player.getMainHandItem().is(SCTags.HAS_TRACKER_LAYER) || player.getOffhandItem().is(SCTags.HAS_TRACKER_LAYER);

        Identifier trackedRL = Minecraft.getInstance().player.getData(SCDataAttachments.TRACKED_FISH);
        shouldShow = shouldShow && !trackedRL.equals(Starcatcher.MISSINGNO);

        if (Minecraft.getInstance().screen instanceof SettingsScreen)
        {
            shouldShow = true;
            cachedFP = FishProperties.empty();
        }

        //smoothly moves ui in and out of screen
        if (!shouldShow)
            if (offScreen > -250 + SCConfig.TRACKER_X_OFFSET.get())
                offScreen -= 15F * deltaTracker.getRealtimeDeltaTicks();
            else
            {
                offScreen = (float) (-250 + SCConfig.TRACKER_X_OFFSET.get());
                return;
            }
        else if (offScreen < 0)
            offScreen += 15F * deltaTracker.getRealtimeDeltaTicks();
        else
            offScreen = 0;

        //transform and scale from config
        g.pose().pushMatrix();
        g.pose().scale(((float) SCConfig.TRACKER_SCALE.getAsDouble()), ((float) SCConfig.TRACKER_SCALE.getAsDouble()));
        g.pose().translate(((float) SCConfig.TRACKER_X_OFFSET.getAsDouble()), (float) SCConfig.TRACKER_Y_OFFSET.getAsDouble());

        //translate offset animation
        g.pose().translate(-offScreen, 0);

        //recalculate every <config freq>
        if (System.currentTimeMillis() > lastRefreshMS + SCConfig.OVERLAY_UPDATE_FREQUENCY.get())
            recalculate();

        if (cachedFP == null || cachedRL == null)
        {
            g.pose().popMatrix();
            return;
        }

        //render base background
        BACKGROUND.render(g, uiX, uiY);

        //render fish + name
        if (cachedCaughtFish || !SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get())
        {
            ScreenUtils.text(g, font, font.plainSubstrByWidth(cachedFP.getDisplayName().getString(), 77),
                    uiX + 77,  uiY + 27, SCColors.GUIDE_TEXT_DARK, false);
            ScreenUtils.item(g, cachedFP.catchInfo().fish().toStack(), uiX + 42, uiY + 15, g.pose(), 2);
        }
        else
        {
            EMPTY.render(g, uiX + 34, uiY + 7);
        }

        //render weight
        double percentage = (double) cachedChance / cachedTotalChance * 100;
        ScreenUtils.centeredText(g, font, Component.literal(new DecimalFormat("0.#").format(percentage) + "%"),
                uiX + 34, uiY + 65, SCColors.GUIDE_TEXT_DARK, false);
        ScreenUtils.centeredText(g, font, Component.literal(cachedChance + "/" + cachedTotalChance),
                uiX + 34, uiY + 75, SCColors.GUIDE_TEXT_DARK, false);

        //render restrictions
        for (int i = 0; i < cachedRestrictions.size(); i++)
            ScreenUtils.text(g, font, cachedRestrictions.get(i), uiX + 70, uiY + 49 + i * 10, SCColors.GUIDE_TEXT_DARK, false);

        g.pose().popMatrix();
    }
}
