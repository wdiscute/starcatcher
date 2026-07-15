package com.wdiscute.starcatcher.tournament;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.network.tournament.SBStandTournamentNameChangePayload;
import com.wdiscute.utils.Utils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StandScreen extends AbstractContainerScreen<StandMenu>
{
    public Tournament currentTournament;
    public Tournament tournamentCached;
    public static List<Tournament> finishedTournaments;

    public boolean viewingPastTournament;
    public int tournamentPage;

    private EditBox nameEditBox;
    private boolean nameWasFocused;

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/tournament/stand/background.png");

    private static final ResourceLocation LEFT_ARROW = Starcatcher.rl("textures/gui/tournament/stand/arrow_left.png");
    private static final ResourceLocation LEFT_ARROW_HIGHLIGHT = Starcatcher.rl("textures/gui/tournament/stand/arrow_left_highlight.png");
    private static final ResourceLocation RIGHT_ARROW = Starcatcher.rl("textures/gui/tournament/stand/arrow_right.png");
    private static final ResourceLocation RIGHT_ARROW_HIGHLIGHT = Starcatcher.rl("textures/gui/tournament/stand/arrow_right_highlight.png");

    private static final ResourceLocation INDEX_ARROW = Starcatcher.rl("textures/gui/tournament/stand/arrow_index.png");
    private static final ResourceLocation INDEX_ARROW_HIGHLIGHT = Starcatcher.rl("textures/gui/tournament/stand/arrow_index_highlight.png");

    private static final ResourceLocation TINY_ARROW_RIGHT = Starcatcher.rl("textures/gui/tournament/stand/tiny_arrow_right.png");
    private static final ResourceLocation TINY_ARROW_LEFT = Starcatcher.rl("textures/gui/tournament/stand/tiny_arrow_left.png");

    private static final ResourceLocation BUTTON = Starcatcher.rl("textures/gui/tournament/stand/button.png");
    private static final ResourceLocation BUTTON_PRESSED = Starcatcher.rl("textures/gui/tournament/stand/button_pressed.png");
    private static final ResourceLocation BUTTON_DISABLED = Starcatcher.rl("textures/gui/tournament/stand/button_disabled.png");

    int uiX;
    int uiY;
    boolean isOwner = false;

    boolean mouseDown = false;

    int playerListPage = 0;

    @Override
    protected void init()
    {
        super.init();
        currentTournament = menu.sbe.tournament;
        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;
        subInit();
    }

    protected void subInit()
    {
        nameEditBox = new EditBox(this.font, uiX + 53, uiY + 36, 210, 12, Component.empty());
        nameEditBox.setCanLoseFocus(true);
        nameEditBox.setTextColor(0xff635040);
        nameEditBox.setBordered(false);
        nameEditBox.setMaxLength(20);
        nameEditBox.setTextShadow(false);
        nameEditBox.setEditable(false);
        addWidget(this.nameEditBox);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1)
    {
        this.renderBlurredBackground(i);
    }

    private void onFocusNameEditBox()
    {
        nameEditBox.setValue(currentTournament.name);
    }

    private void onUnfocusNameEditBox()
    {
        //send packet
        PacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        //render background
        renderImage(guiGraphics, BACKGROUND);

        //update every frame for when server sends a new tournament
        if (currentTournament != menu.sbe.tournament) onTournamentReceived();
        if (currentTournament == null) return;

        double x = mouseX - uiX;
        double y = mouseY - uiY;
        List<Component> tooltips = new ArrayList<>();

        //handle Name editbox focusing
        if (nameWasFocused != nameEditBox.isFocused())
        {
            if (nameEditBox.isFocused())
                onFocusNameEditBox();
            else
                onUnfocusNameEditBox();
        }

        nameWasFocused = nameEditBox.isFocused();

        //render tournament name
        if(currentTournament.status.equals(Tournament.Status.FINISHED)) nameEditBox.setValue(currentTournament.name);
        nameEditBox.render(guiGraphics, mouseX, mouseY, partialTick);


        //organizer
        renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(currentTournament.ownerName),
                uiX + 55, uiX + 116, uiY + 56, mouseX, mouseY);
        guiGraphics.drawString(this.font, translatable("gui.starcatcher.tournament.organizer"), uiX + 55, uiY + 68, 0x9c897c, false);

        //status
        guiGraphics.drawString(this.font, translatable(currentTournament.status.getSerializedName()), uiX + 130, uiY + 56, 0x635040, false);
        guiGraphics.drawString(this.font, translatable("gui.starcatcher.tournament.status"), uiX + 130, uiY + 68, 0x9c897c, false);

        //duration
        guiGraphics.drawString(this.font, Utils.calculateRealLifeTimeFromTicks(currentTournament.durationInTicks), uiX + 55, uiY + 88, 0x635040, false);
        if (isOwner && currentTournament.status.equals(Tournament.Status.PREPARING))
        {
            renderImage(guiGraphics, TINY_ARROW_LEFT, 54, 101, 6, 5);
            renderImage(guiGraphics, TINY_ARROW_RIGHT, 109, 101, 6, 5);
            renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.duration"),
                    uiX + 85, uiY + 100, SCColors.GUIDE_TEXT);
        }
        else
        {
            guiGraphics.drawString(this.font, translatable("gui.starcatcher.tournament.duration"),
                    uiX + 55, uiY + 100, 0x9c897c, false);
        }

        //duration hover
        if (x > 52 && x < 116 && y > 85 && y < 98)
        {
            tooltips.add(Component.literal(currentTournament.durationInTicks + " ticks"));

            MutableComponent durationComp = Component.literal(String.format("%.2f ", (float) currentTournament.durationInTicks / 24000));
            if (currentTournament.durationInTicks % 24000 == 0)
                durationComp = Component.literal(currentTournament.durationInTicks / 24000 + " ");
            tooltips.add(durationComp.append(translatable("gui.starcatcher.tournament.duration.days")));
        }

        String startDate = Instant.ofEpochMilli(currentTournament.startTimeEpoch)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss"));

        //start date
        if (currentTournament.startTimeEpoch != 0)
            renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(startDate),
                    uiX + 130, uiX + 190, uiY + 88, true);
        else
            guiGraphics.drawString(this.font, Component.literal("---"),
                    uiX + 130, uiY + 88, SCColors.GUIDE_TEXT, false);
        guiGraphics.drawString(this.font, translatable("gui.starcatcher.tournament.start_date"),
                uiX + 130, uiY + 100, SCColors.GUIDE_TEXT, false);


        //scoring rules
        guiGraphics.drawString(this.font, translatable("gui.starcatcher.tournament.scoring"),
                uiX + 56, uiY + 114, SCColors.GUIDE_TEXT, false);

        //trash
        renderStringWithLimitedSpace(guiGraphics, this.font, 
                translatable("gui.guide.rarity.trash"),
                uiX + 56, uiX + 104, uiY + 126, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.trashScore),
                uiX + 107, uiY + 126, SCColors.GUIDE_TEXT_DARK, false);

        //common
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.guide.rarity.common"),
                uiX + 124, uiX + 172, uiY + 126, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.commonScore),
                uiX + 174, uiY + 126, SCColors.GUIDE_TEXT_DARK, false);

        //uncommon
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.guide.rarity.uncommon"),
                uiX + 56, uiX + 104, uiY + 138, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.uncommonScore),
                uiX + 107, uiY + 138, SCColors.GUIDE_TEXT_DARK, false);

        //rare
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.guide.rarity.rare"),
                uiX + 124, uiX + 172, uiY + 138, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.rareScore),
                uiX + 174, uiY + 138, SCColors.GUIDE_TEXT_DARK, false);

        //epic
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.guide.rarity.epic"),
                uiX + 56, uiX + 104, uiY + 150, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.epicScore),
                uiX + 107, uiY + 150, SCColors.GUIDE_TEXT_DARK, false);

        //legendary
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.guide.rarity.legendary"),
                uiX + 124, uiX + 172, uiY + 150, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.legendaryScore),
                uiX + 174, uiY + 150, SCColors.GUIDE_TEXT_DARK, false);

        //percentile
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.starcatcher.tournament.scoring.percentile"),
                uiX + 56, uiX + 172, uiY + 162, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.percentileMultiplier),
                uiX + 174, uiY + 162, SCColors.GUIDE_TEXT_DARK, false);

        //perfect catch
        renderStringWithLimitedSpace(guiGraphics, this.font, translatable("gui.starcatcher.tournament.scoring.perfect"),
                uiX + 56, uiX + 172, uiY + 174, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", currentTournament.scoreSettings.perfectCatchMultiplier),
                uiX + 174, uiY + 174, SCColors.GUIDE_TEXT_DARK, false);

        //sign up button
        boolean isHovering = x > 79 && x < 163 && y > 192 && y < 208;
        //if owner
        if (isOwner)
        {
            if (currentTournament.status.equals(Tournament.Status.PREPARING))
            {
                if (mouseDown)
                    renderImage(guiGraphics, BUTTON_PRESSED, 80, 193, 83, 15);
                else
                    renderImage(guiGraphics, BUTTON, 80, 193, 83, 15);

                MutableComponent comp = translatable("gui.starcatcher.tournament.button.start");
                if (isHovering)
                {
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 196, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 198, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 119, uiX + 82, uiX + 159, uiY + 197, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 121, uiX + 84, uiX + 161, uiY + 197, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK);

                    guiGraphics.renderOutline(uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                }
                else
                    renderCenteredScrollingString(guiGraphics, this.font, comp,
                            uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK
                    );
            }

            if (currentTournament.status.equals(Tournament.Status.ACTIVE))
            {
                if (mouseDown)
                    renderImage(guiGraphics, BUTTON_PRESSED, 80, 193, 83, 15);
                else
                    renderImage(guiGraphics, BUTTON, 80, 193, 83, 15);

                MutableComponent comp = translatable("gui.starcatcher.tournament.button.cancel");
                if (isHovering)
                {
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 196, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 198, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 119, uiX + 82, uiX + 159, uiY + 197, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 121, uiX + 84, uiX + 161, uiY + 197, SCColors.WHITE);
                    renderCenteredScrollingString(guiGraphics, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK);

                    guiGraphics.renderOutline(uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                }
                else
                    renderCenteredScrollingString(guiGraphics, this.font, comp,
                            uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK
                    );
            }

        }
        //if not owner
        else
        {
            if(!viewingPastTournament)
            {
                //render button
                if (currentTournament.status.equals(Tournament.Status.PREPARING))
                {
                    if (mouseDown)
                        renderImage(guiGraphics, BUTTON_PRESSED, 80, 193, 83, 15);
                    else
                        renderImage(guiGraphics, BUTTON, 80, 193, 83, 15);
                }
                else
                    renderImage(guiGraphics, BUTTON_DISABLED, 80, 193, 83, 15);

                //if is signed up
                if (currentTournament.isPlayerSignedUp(Minecraft.getInstance().player))
                {
                    //if hovering button
                    if (x > 79 && x < 163 && y > 192 && y < 208 && currentTournament.status.equals(Tournament.Status.PREPARING))
                    {
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 121, uiY + 197, SCColors.WHITE);
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 119, uiY + 197, SCColors.WHITE);
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 120, uiY + 196, SCColors.WHITE);
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 120, uiY + 198, SCColors.WHITE);
                        guiGraphics.renderOutline(uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                    }

                    renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.unsign"),
                            uiX + 120, uiY + 197, SCColors.GUIDE_TEXT_DARK);
                }
                //if not signed up
                else
                {
                    //if hovering button
                    if (x > 79 && x < 163 && y > 192 && y < 208 && currentTournament.status.equals(Tournament.Status.PREPARING))
                    {
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 121, uiY + 197, SCColors.WHITE);
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 119, uiY + 197, SCColors.WHITE);
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 120, uiY + 196, SCColors.WHITE);
                        renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 120, uiY + 198, SCColors.WHITE);
                        guiGraphics.renderOutline(uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                    }

                    renderCenteredString(guiGraphics, this.font, translatable("gui.starcatcher.tournament.button.sign_up"),
                            uiX + 120, uiY + 197, SCColors.GUIDE_TEXT_DARK);
                }
            }
        }

        //player list
        //signed up
        guiGraphics.drawString(this.font, currentTournament.status.equals(Tournament.Status.PREPARING) ?
                        translatable("gui.starcatcher.tournament.signed_up") :  translatable("gui.starcatcher.tournament.scores"),
                uiX + 220, uiY + 45, SCColors.GUIDE_TEXT, false);

        //render names with offset based on page selected
        for (int i = playerListPage * 8; i < Math.min(currentTournament.playerScores.size(), playerListPage * 8 + 8); i++)
            renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(currentTournament.playerScores.get(i).name),
                    uiX + 220, uiX + 323, uiY + 57 + i * 12 - playerListPage * 8 * 12, mouseX, mouseY);

        //render scores
        for (int i = playerListPage * 8; i < Math.min(currentTournament.playerScores.size(), playerListPage * 8 + 8); i++)
            renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(String.format("%.1f", currentTournament.playerScores.get(i).score)),
                    uiX + 319, uiX + 345, uiY + 57 + i * 12 - playerListPage * 8 * 12, mouseX, mouseY);

        //previous arrow
        if (playerListPage > 0)
        {
            if (x > 215 && x < 230 && y > 151 && y < 163)
                renderImage(guiGraphics, LEFT_ARROW_HIGHLIGHT, 218, 153, 12, 9);
            else
                renderImage(guiGraphics, LEFT_ARROW, 218, 153, 12, 9);
        }

        //next arrow
        if (playerListPage < (currentTournament.playerScores.size() - 1) / 8)
        {
            if (x > 330 && x < 347 && y > 151 && y < 163)
                renderImage(guiGraphics, RIGHT_ARROW_HIGHLIGHT, 333, 153, 12, 9);
            else
                renderImage(guiGraphics, RIGHT_ARROW, 333, 153, 12, 9);
        }

        //page count
        renderCenteredString(guiGraphics, this.font, Component.literal(playerListPage + 1 + "/" + (((currentTournament.playerScores.size() - 1) / 8) + 1)),
                uiX + 280, uiY + 153, SCColors.GUIDE_TEXT);


        //previous tournaments
        guiGraphics.drawString(this.font, translatable("gui.starcatcher.tournament.previous_tournaments"),
                uiX + 220, uiY + 175, SCColors.GUIDE_TEXT, false);

        if (viewingPastTournament)
        {
            if (x > 164 && x < 188 && y > 193 && y < 208)
            {
                renderImage(guiGraphics, INDEX_ARROW_HIGHLIGHT, 164, 193, 24, 15);
                tooltips.add(Component.literal("Back To Current Tournament"));
            }
            else
                renderImage(guiGraphics, INDEX_ARROW, 164, 193, 24, 15);
        }

        if (!finishedTournaments.isEmpty())
        {
            //click to view
            if (x > 214 && x < 347 && y > 184 && y < 196)
            {
                tooltips.add(Component.literal("Click To View"));
                tooltips.add(Component.literal(""));
                tooltips.add(Component.literal(finishedTournaments.get(tournamentPage).name));
                tooltips.add(Component.literal("By: " + finishedTournaments.get(tournamentPage).ownerName));

                String startDatePreviousTournament = Instant.ofEpochMilli(finishedTournaments.get(tournamentPage).startTimeEpoch)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss"));
                tooltips.add(Component.literal(startDatePreviousTournament));
            }

            //previous arrow
            if (tournamentPage > 0)
            {
                if (x > 215 && x < 230 && y > 196 && y < 208)
                    renderImage(guiGraphics, LEFT_ARROW_HIGHLIGHT, 218, 198, 12, 9);
                else
                    renderImage(guiGraphics, LEFT_ARROW, 218, 198, 12, 9);
            }

            //next arrow
            if (finishedTournaments.size() > tournamentPage + 1)
            {
                if (x > 330 && x < 347 && y > 196 && y < 208)
                    renderImage(guiGraphics, RIGHT_ARROW_HIGHLIGHT, 333, 198, 12, 9);
                else
                    renderImage(guiGraphics, RIGHT_ARROW, 333, 198, 12, 9);
            }

            //previous tournament name
            guiGraphics.drawString(this.font, Component.literal(finishedTournaments.get(tournamentPage).name),
                    uiX + 220, uiY + 187, SCColors.GUIDE_TEXT_DARK, false);
        }


        //render tooltip
        guiGraphics.renderTooltip(this.font, tooltips, Optional.empty(), mouseX, mouseY);
    }

    public void onTournamentReceived()
    {
        if (!viewingPastTournament)
        {
            currentTournament = menu.sbe.tournament;
            isOwner = this.currentTournament.owner.equals(Minecraft.getInstance().player.getUUID());
            nameEditBox.setEditable(isOwner);
            if (!isOwner || !nameEditBox.isFocused())
            {
                nameEditBox.setValue(currentTournament.name);
            }
        }
        else
        {
            tournamentCached = menu.sbe.tournament;
        }

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //System.out.println(x);
        //System.out.println(y);

        if (isOwner)
        {
            //duration decrease, shift does x10
            if (x > 53 && x < 117 && y > 88 && y < 107 && scrollY < -0.5f)
            {
                if (!hasShiftDown())
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 101);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 102);
                return true;
            }

            //duration increase, shift does x10
            if (x > 53 && x < 117 && y > 88 && y < 107 && scrollY > 0.5f)
            {
                if (!hasShiftDown())
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 103);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 104);
                return true;
            }

            //trash
            if (x > 54 && x < 120 && y > 124 && y < 134)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 200);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 201);
                return true;
            }

            //common
            if (x > 122 && x < 187 && y > 124 && y < 134)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 210);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 211);
                return true;
            }

            //uncommon
            if (x > 54 && x < 120 && y > 136 && y < 146)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 220);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 221);
                return true;
            }

            //rare
            if (x > 122 && x < 187 && y > 136 && y < 146)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 230);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 231);
                return true;
            }

            //epic
            if (x > 54 && x < 120 && y > 148 && y < 158)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 240);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 241);
                return true;
            }

            //legendary
            if (x > 122 && x < 187 && y > 148 && y < 158)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 250);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 251);
                return true;
            }

            //percentile
            if (x > 54 && x < 187 && y > 160 && y < 170)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 260);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 261);
                return true;
            }

            //perfect catch
            if (x > 54 && x < 187 && y > 172 && y < 182)
            {
                if (scrollY < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 270);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 271);
                return true;
            }

        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (button == 0)
        {
            double x = mouseX - uiX;
            double y = mouseY - uiY;
            mouseDown = false;

            //gold button click
            if (x > 79 && x < 163 && y > 192 && y < 208)
            {
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 67);
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;
        assert minecraft != null;
        assert minecraft.gameMode != null;
        if (button != 0) super.mouseClicked(mouseX, mouseY, button);

        //System.out.println("clicked relative x: " + x);
        //System.out.println("clicked relative y: " + y);

        //gold button click
        if (x > 79 && x < 163 && y > 192 && y < 208)
        {
            mouseDown = true;
        }

        //index button
        if (x > 164 && x < 188 && y > 193 && y < 208 && viewingPastTournament)
        {
            viewingPastTournament = false;
            currentTournament = tournamentCached;
            nameEditBox.setValue(currentTournament.name);
        }

        //previous tournaments next arrow
        if (finishedTournaments.size() > tournamentPage + 1 && x > 330 && x < 347 && y > 196 && y < 208)
        {
            tournamentPage++;
            return true;
        }

        //previous tournaments previous arrow
        if (tournamentPage > 0 && x > 215 && x < 230 && y > 196 && y < 208)
        {
            tournamentPage--;
            return true;
        }

        if (x > 214 && x < 347 && y > 184 && y < 196)
        {
            currentTournament = finishedTournaments.get(tournamentPage);
            viewingPastTournament = true;
        }

        //player list page previous
        if (playerListPage > 0 && x > 215 && x < 230 && y > 151 && y < 163)
        {
            playerListPage--;
            return true;
        }

        //player list page next
        if (playerListPage < (currentTournament.playerScores.size() - 1) / 8 && x > 330 && x < 347 && y > 151 && y < 163)
        {
            playerListPage++;
            return true;
        }


        //medal
        if (x > 349 && x < 396 && y > 48 && y < 95)
        {
            minecraft.player.playSound(SoundEvents.NOTE_BLOCK_CHIME.value(), 0.1f, 1.2f);
            minecraft.player.playSound(SoundEvents.GLASS_STEP, 0.4f, 1.2f);
            return true;
        }

        //duration decrease, shift does x10
        if (x > 50 && x < 60 && y > 98 && y < 107 && isOwner)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 101);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 102);
            return true;
        }

        //duration increase, shift does x10
        if (x > 109 && x < 119 && y > 99 && y < 109 && isOwner)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 103);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 104);
            return true;
        }

        nameEditBox.setFocused(false);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 420, 260, 420, 260);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int xOffset, int yOffset, int width, int height)
    {
        guiGraphics.blit(rl, uiX + xOffset, uiY + yOffset, 0, 0, width, height, width, height);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        boolean b = super.charTyped(codePoint, modifiers);
        PacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
        return b;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == 256)
        {
            this.minecraft.player.closeContainer();
        }

        boolean editbox = this.nameEditBox.keyPressed(keyCode, scanCode, modifiers) || this.nameEditBox.canConsumeInput();

        if (editbox)
            PacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
        return editbox || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        if (!nameEditBox.getValue().isEmpty())
            PacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
    }

    public StandScreen(StandMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        currentTournament = menu.sbe.tournament;
        imageWidth = 420;
        imageHeight = 260;
    }

    public static void renderCenteredScrollingString(GuiGraphics guiGraphics, Font font, Component text, int centerX, int minX, int maxX, int y, int color)
    {
        int i = font.width(text);
        int k = maxX - minX;
        if (i > k)
        {
            int l = i - k;
            double d0 = (double) Util.getMillis() / (double) 300.0F;
            double d1 = Math.max((double) l * (double) 0.5F, 3.0F);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / (double) 2.0F + (double) 0.5F;
            double d3 = Mth.lerp(d2, 0.0F, l);
            guiGraphics.enableScissor(minX, y - 10, maxX, y + 10);
            int x = minX - (int) d3;
            guiGraphics.drawString(font, text, x, y, color, false);
            guiGraphics.disableScissor();
        }
        else
        {
            int i1 = Mth.clamp(centerX, minX + i / 2, maxX - i / 2);
            guiGraphics.drawString(font, text.getVisualOrderText(), i1 - font.width(text.getVisualOrderText()) / 2, y, color, false);
        }
    }

    public static void renderStringWithLimitedSpace(GuiGraphics guiGraphics, Font font, Component comp, int minX, int maxX, int y, double mouseX, double mouseY)
    {
        boolean hovering = mouseX > minX && mouseX < maxX && mouseY > y - 3 && mouseY < y + 9;
        renderStringWithLimitedSpace(guiGraphics, font, comp, minX, maxX, y, hovering);
    }

    public static void renderStringWithLimitedSpace(GuiGraphics guiGraphics, Font font, Component comp, int minX, int maxX, int y, boolean hovering)
    {
        int width = font.width(comp);
        int sizeAvailable = maxX - minX;
        if (width > sizeAvailable)
        {
            int l = width - sizeAvailable;
            double d0 = (double) Util.getMillis() / (double) 300.0F;
            double d1 = Math.max((double) l * (double) 0.5F, 3.0F);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / (double) 2.0F + (double) 0.5F;
            double d3 = Mth.lerp(d2, 0.0F, l);
            guiGraphics.enableScissor(minX, y - 20, maxX, y + 20);
            int x = minX - (int) d3;
            if (hovering)
                guiGraphics.drawString(font, comp, x, y, SCColors.GUIDE_TEXT_DARK, false);
            else
                guiGraphics.drawString(font, comp, minX, y, SCColors.GUIDE_TEXT_DARK, false);
            guiGraphics.disableScissor();
        }
        else
        {
            guiGraphics.drawString(font, comp, minX, y, SCColors.GUIDE_TEXT_DARK, false);
        }
    }

    public MutableComponent translatable(String key)
    {
        return Tooltips.resolveTagsToComponentFromTranslationKey(key);
    }

    public void renderCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color)
    {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.drawString(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, false);
    }
}
