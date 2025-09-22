package com.wdiscute.starcatcher.rod;

import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class FishTrackerLayer implements LayeredDraw.Layer
{

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/fishing/fish_tracker.png");

    int uiX;
    int uiY;

    float offScreen = -150;

    Font font;

    int imageWidth = 150;
    int imageHeight = 100;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        font = Minecraft.getInstance().font;
        uiX = Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth;
        uiY = Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight - 80;

        if (Minecraft.getInstance().level == null) return;
        if (Minecraft.getInstance().player == null) return;

        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;

        boolean shouldShow = false;

        if (player.getMainHandItem().is(ModItems.FISH_SPOTTER) || player.getOffhandItem().is(ModItems.FISH_SPOTTER))
        {
            shouldShow = true;
        }
        else
        {
            ItemContainerContents icc = player.getMainHandItem().get(ModDataComponents.BOBBER);
            if (icc != null)
            {
                ItemStack is = icc.copyOne();
                if (is.is(ModItems.FISH_SPOTTER)) shouldShow = true;
            }
        }

        FishProperties fp = player.getData(ModDataAttachments.FISH_SPOTTER);

        if (!shouldShow)
        {
            offScreen -= 15 * deltaTracker.getGameTimeDeltaTicks();
            if (offScreen < -150) offScreen = -150;
        }
        else
        {
            offScreen += 15 * deltaTracker.getGameTimeDeltaTicks();
            if (offScreen > 0) offScreen = 0;
        }


        ItemStack rod = new ItemStack(ModItems.STARCATCHER_FISHING_ROD.get());
        ItemStack bait = rod.get(ModDataComponents.BAIT).copyOne();

        int total = 0;

        for (FishProperties d : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            total += FishProperties.getChance(d, player, rod);
        }

        int specific = FishProperties.getChance(fp, player, rod);

        int chance = ((int) (((float) specific / total) * 100));

        ItemStack fishBeingTracked = new ItemStack(BuiltInRegistries.ITEM.get(fp.fish()));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-offScreen, 0, 0);

        renderImage(guiGraphics, BACKGROUND);

        guiGraphics.renderItem(fishBeingTracked, uiX + 50, uiY + 10);

        guiGraphics.drawString(
                Minecraft.getInstance().font, fishBeingTracked.getItem().getDescription(),
                uiX + 70, uiY + 15, 0, false);

        guiGraphics.drawString(
                Minecraft.getInstance().font, chance + "%",
                uiX + 27, uiY + 37, 0, false);

        guiGraphics.drawString(
                Minecraft.getInstance().font, "(" + specific + "/" + total + ")",
                uiX + 20, uiY + 47, 0, false);

        //planet
        {
            MutableComponent comp = Component.literal("Planet").withColor(0x00AA00);

            if (!fp.wr().dims().isEmpty() && !fp.wr().dims().contains(level.dimension().location()))
                comp.withColor(0xAA0000);
            if (fp.wr().dimsBlacklist().contains(level.dimension().location()))
                comp.withColor(0xAA0000);

            drawComp(guiGraphics, comp, 70, 30);
        }

        //biome
        {
            MutableComponent comp = Component.literal("Biome").withColor(0x00AA00);

            if (!fp.wr().biomes().isEmpty() && !fp.wr().biomes().contains(level.getBiome(player.blockPosition()).getKey().location()))
                comp.withColor(0xAA0000);
            if (fp.wr().biomesBlacklist().contains(level.getBiome(player.blockPosition()).getKey().location()))
                comp.withColor(0xAA0000);

            drawComp(guiGraphics, comp, 70, 40);
        }

        //bait
        {
            MutableComponent comp = Component.literal("Bait").withColor(0x00AA00);

            if (fp.br().mustHaveCorrectBait() && !fp.br().correctBait().contains(BuiltInRegistries.ITEM.getKey(bait.getItem())))
                comp.withColor(0xAA0000);

            drawComp(guiGraphics, comp, 70, 50);
        }

        //weather
        {
            MutableComponent comp = Component.literal("Weather").withColor(0x00AA00);

            if (fp.weather() == FishProperties.Weather.CLEAR && (level.getRainLevel(0) > 0.5 || level.getThunderLevel(0) > 0.5))
                comp.withColor(0xAA0000);
            if (fp.weather() == FishProperties.Weather.RAIN && level.getRainLevel(0) < 0.5)
                comp.withColor(0xAA0000);
            if (fp.weather() == FishProperties.Weather.THUNDER && level.getThunderLevel(0) < 0.5)
                comp.withColor(0xAA0000);

            drawComp(guiGraphics, comp, 70, 60);
        }

        //Daytime
        {
            MutableComponent comp = Component.literal("Daytime").withColor(0x00AA00);

            if (fp.daytime() != FishProperties.Daytime.ALL)
            {
                //TODO change 24000 to the fraction of level day cycle
                long time = level.getDayTime() % 24000;

                switch (fp.daytime())
                {
                    case FishProperties.Daytime.DAY:
                        if (!(time > 23000 || time < 12700)) comp.withColor(0xAA0000);
                        break;

                    case FishProperties.Daytime.NOON:
                        if (!(time > 3500 && time < 8500)) comp.withColor(0xAA0000);
                        break;

                    case FishProperties.Daytime.NIGHT:
                        if (!(time < 23000 && time > 12700)) comp.withColor(0xAA0000);
                        break;

                    case FishProperties.Daytime.MIDNIGHT:
                        if (!(time > 16500 && time < 19500)) comp.withColor(0xAA0000);
                        break;
                }
            }

            drawComp(guiGraphics, comp, 70, 70);
        }

        //mustBeCaughtAboveY
        {
            MutableComponent comp = Component.literal("Elevation").withColor(0x00AA00);

            if (fp.mustBeCaughtAboveY() != Integer.MIN_VALUE || fp.mustBeCaughtBellowY() != Integer.MAX_VALUE)
            {
                if (player.position().y > fp.mustBeCaughtBellowY())
                    comp.withColor(0xAA0000);

                if (player.position().y < fp.mustBeCaughtAboveY())
                    comp.withColor(0xAA0000);

                drawComp(guiGraphics, comp, 70, 80);
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
