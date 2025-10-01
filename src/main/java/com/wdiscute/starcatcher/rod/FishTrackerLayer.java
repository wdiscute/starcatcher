package com.wdiscute.starcatcher.rod;

import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandstuff.FishCaughtCounter;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

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

        FishProperties fpTracked = player.getData(ModDataAttachments.FISH_SPOTTER);

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

        ItemStack rod = new ItemStack(ModItems.ROD.get());
        ItemStack bait = rod.get(ModDataComponents.BAIT).copyOne();

        int total = 0;

        for (FishProperties d : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            total += FishProperties.getChance(d, player, rod);
        }

        int specific = FishProperties.getChance(fpTracked, player, rod);

        int chance = ((int) (((float) specific / total) * 100));

        ItemStack fishBeingTracked = new ItemStack(BuiltInRegistries.ITEM.get(fpTracked.fish()));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-offScreen, 0, 0);

        renderImage(guiGraphics, BACKGROUND);

        guiGraphics.renderItem(fishBeingTracked, uiX + 50, uiY + 10);

        List<FishProperties> fpsInArea = FishProperties.getFpsForArea(player);
        List<FishCaughtCounter> fishesCaughtCounter = player.getData(ModDataAttachments.FISHES_CAUGHT);
        List<FishProperties> fishesCaught = new ArrayList<>();

        for (FishCaughtCounter fishes : fishesCaughtCounter)
        {
            fishesCaught.add(fishes.fp());
        }


        for (int i = 0; i < fpsInArea.size(); i++)
        {
            ItemStack is = new ItemStack(ModItems.MISSINGNO.get());

            if(fishesCaught.contains(fpsInArea.get(i)))
            {
                is = new ItemStack(BuiltInRegistries.ITEM.get(fpsInArea.get(i).fish()));
            }

            guiGraphics.renderItem(is,
                    uiX + 50 + i * 20 % 100,
                    uiY + 10 + i / 5 * 20);
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
