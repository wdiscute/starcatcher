package com.wdiscute.starcatcher.tournament;

import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.networkandcodecs.SingleStackContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.system.windows.TOUCHINPUT;

import java.util.*;

public class StandScreen extends AbstractContainerScreen<StandMenu>
{
    public static Tournament tournamentCache;
    public static Map<UUID, String> gameProfilesCache;
    private final StandMenu standMenu;

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/tournament/background.png");

    int uiX;
    int uiY;

    @Override
    protected void init()
    {
        super.init();
        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1)
    {
        this.renderBlurredBackground(i);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        if (tournamentCache == null) return;
        if (gameProfilesCache == null) return;

        renderImage(guiGraphics, BACKGROUND);

        guiGraphics.drawString(this.font, tournamentCache.name, uiX + 53, uiY + 36, 0x635040, false);

        //organizer
        guiGraphics.drawString(this.font, getPlayerFromUUID(tournamentCache.owner), uiX + 55, uiY + 56, 0x635040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.organizer"), uiX + 55, uiY + 68, 0x9c897c, false);

        //prize pool
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.prize_pool"), uiX + 130, uiY + 53, 0x9c897c, false);


        //status
        guiGraphics.drawString(this.font, Component.translatable(tournamentCache.status.getSerializedName()), uiX + 55, uiY + 88, 0x635040, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.status"), uiX + 55, uiY + 100, 0x9c897c, false);

        //signup button
        int color = tournamentCache.settings.canSignUp(minecraft.player) ? 0x40752c : 0xa34536;
        guiGraphics.drawString(this.font, Component.translatable("gui.starcatcher.tournament.sign_up"), uiX + 51, uiY + 120, color, false);

        if (x > 48 && x < 98 && y > 117 && y < 127 && !tournamentCache.settings.entryCost.isEmpty())
        {
            List<Component> signUpCostList = new ArrayList<>();
            signUpCostList.add(Component.literal("Sign Up Fee:"));

            for (SingleStackContainer ssc : tournamentCache.settings.entryCost)
            {
                signUpCostList.add(Component.literal(ssc.stack().getCount() + "x ").append(Component.translatable(ssc.stack().getItem().getDescriptionId())));
            }

            guiGraphics.renderTooltip(this.font, signUpCostList, Optional.empty(), mouseX, mouseY);
        }


        //list of players
        int count = 0;
        int xOffset = 53;
        int yOffset = 132;
        boolean drawOthers = false;
        List<Component> others = new ArrayList<>();
        others.add(Component.translatable("gui.starcatcher.tournament.other"));
        for (var entry : tournamentCache.getPlayerScores().entrySet())
        {
            if (count == 11)
            {
                drawOthers = true;
                others.add(Component.literal(getPlayerFromUUID(entry.getKey())));
                continue;
            }
            guiGraphics.drawString(this.font, getPlayerFromUUID(entry.getKey()), uiX + xOffset, uiY + yOffset, 0x635040, false);
            count++;
            yOffset += 12;
            if (count == 6)
            {
                xOffset += 77;
                yOffset = 132;
            }
        }

        if (drawOthers)
        {
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.hover"), uiX + xOffset, uiY + yOffset, 0x635040, false);
            if (x > 125 && x < 190 && y > 188 && y < 202)
                guiGraphics.renderTooltip(
                        this.font,
                        others,
                        Optional.empty(), mouseX, mouseY);
        }

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        System.out.println("clicked relative x:" + x);
        System.out.println("clicked relative y:" + y);

        if (x > 48 && x < 98 && y > 117 && y < 127)
        {
            minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 67);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public static String getPlayerFromUUID(UUID uuid)
    {
        if (gameProfilesCache != null)
        {
            if (gameProfilesCache.containsKey(uuid))
            {
                return gameProfilesCache.get(uuid);
            }
        }
        return "Unknown";
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 420, 260, 420, 260);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int xOffset, int yOffset)
    {
        guiGraphics.blit(rl, uiX + xOffset, uiY + yOffset, 0, 0, 420, 260, 420, 260);
    }

    public StandScreen(StandMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        standMenu = menu;
        imageWidth = 420;
        imageHeight = 260;
    }
}
