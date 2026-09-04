package com.wdiscute.starcatcher.tournament;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.network.tournament.SBStandTournamentNameChangePayload;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.network.PacketDistributor;
import net.nikdo53.neobackports.io.networking.PacketDistributorNeo;

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

    private static final ScreenUtils.Image BACKGROUND = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/background.png"), 420, 260);

    private static final ScreenUtils.Image LEFT_ARROW = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/arrow_left.png"), 12, 9);
    private static final ScreenUtils.Image LEFT_ARROW_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/arrow_left_highlight.png"), 12, 9);
    private static final ScreenUtils.Image RIGHT_ARROW = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/arrow_right.png"), 12, 9);
    private static final ScreenUtils.Image RIGHT_ARROW_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/arrow_right_highlight.png"), 12, 9);

    private static final ScreenUtils.Image INDEX_ARROW = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/arrow_index.png"), 24, 15);
    private static final ScreenUtils.Image INDEX_ARROW_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/arrow_index_highlight.png"), 24, 15);

    private static final ScreenUtils.Image TINY_ARROW_RIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/tiny_arrow_right.png"), 6, 5);
    private static final ScreenUtils.Image TINY_ARROW_LEFT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/tiny_arrow_left.png"), 6, 5);

    private static final ScreenUtils.Image BUTTON = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/button.png"), 83, 15);
    private static final ScreenUtils.Image BUTTON_PRESSED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/button_pressed.png"), 83, 15);
    private static final ScreenUtils.Image BUTTON_DISABLED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/stand/button_disabled.png"), 83, 15);

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
        nameEditBox.setTextColor(SCColors.GUIDE_TEXT_DARK);
        nameEditBox.setBordered(false);
        nameEditBox.setMaxLength(20);
        nameEditBox.setTextShadow(false);
        nameEditBox.setEditable(false);
        addWidget(this.nameEditBox);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {

    }

    private void onFocusNameEditBox()
    {
        nameEditBox.setValue(currentTournament.name);
    }

    private void onUnfocusNameEditBox()
    {
        //send packet
        PacketDistributorNeo.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick)
    {
        super.render(g, mouseX, mouseY, partialTick);

        //render background
        BACKGROUND.render(g, uiX, uiY);

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
        if (currentTournament.status.equals(Tournament.Status.FINISHED)) nameEditBox.setValue(currentTournament.name);
        nameEditBox.render(g, mouseX, mouseY, partialTick);


        //organizer
        ScreenUtils.scrollingText(g, this.font, Component.literal(currentTournament.ownerName),
                uiX + 55, uiX + 116, uiY + 56, SCColors.GUIDE_TEXT_DARK, false);

        ScreenUtils.text(g, this.font, translatable("gui.starcatcher.tournament.organizer"), uiX + 55, uiY + 68, 0x9c897c, false);

        //status
        ScreenUtils.text(g, this.font, translatable(currentTournament.status.getSerializedName()), uiX + 130, uiY + 56, 0x635040, false);
        ScreenUtils.text(g, this.font, translatable("gui.starcatcher.tournament.status"), uiX + 130, uiY + 68, 0x9c897c, false);

        //duration
        ScreenUtils.text(g, this.font, Utils.calculateRealLifeTimeFromTicks(currentTournament.durationInTicks), uiX + 55, uiY + 88, 0x635040, false);
        if (isOwner && currentTournament.status.equals(Tournament.Status.PREPARING))
        {
            TINY_ARROW_LEFT.render(g, uiX + 54, uiY + 101);
            TINY_ARROW_RIGHT.render(g, uiX + 109, uiY + 101);
            ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.duration"),
                    uiX + 85, uiY + 100, SCColors.GUIDE_TEXT, false);
        }
        else
        {
            ScreenUtils.text(g, this.font, translatable("gui.starcatcher.tournament.duration"),
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
            ScreenUtils.scrollingText(g, this.font, Component.literal(startDate),
                    uiX + 130, uiX + 190, uiY + 88, SCColors.GUIDE_TEXT_DARK, false);
        else
            ScreenUtils.text(g, this.font, Component.literal("---"),
                    uiX + 130, uiY + 88, SCColors.GUIDE_TEXT, false);
        ScreenUtils.text(g, this.font, translatable("gui.starcatcher.tournament.start_date"),
                uiX + 130, uiY + 100, SCColors.GUIDE_TEXT, false);


        //scoring rules
        ScreenUtils.text(g, this.font, translatable("gui.starcatcher.tournament.scoring"),
                uiX + 56, uiY + 114, SCColors.GUIDE_TEXT, false);

        //trash
        ScreenUtils.scrollingText(g, this.font,
                translatable("gui.guide.rarity.trash"),
                uiX + 56, uiX + 104, uiY + 126, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.trashScore),
                uiX + 107, uiY + 126, SCColors.GUIDE_TEXT_DARK, false);

        //common
        ScreenUtils.scrollingText(g, this.font, translatable("gui.guide.rarity.common"),
                uiX + 124, uiX + 172, uiY + 126, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.commonScore),
                uiX + 174, uiY + 126, SCColors.GUIDE_TEXT_DARK, false);

        //uncommon
        ScreenUtils.scrollingText(g, this.font, translatable("gui.guide.rarity.uncommon"),
                uiX + 56, uiX + 104, uiY + 138, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.uncommonScore),
                uiX + 107, uiY + 138, SCColors.GUIDE_TEXT_DARK, false);

        //rare
        ScreenUtils.scrollingText(g, this.font, translatable("gui.guide.rarity.rare"),
                uiX + 124, uiX + 172, uiY + 138, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.rareScore),
                uiX + 174, uiY + 138, SCColors.GUIDE_TEXT_DARK, false);

        //epic
        ScreenUtils.scrollingText(g, this.font, translatable("gui.guide.rarity.epic"),
                uiX + 56, uiX + 104, uiY + 150, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.epicScore),
                uiX + 107, uiY + 150, SCColors.GUIDE_TEXT_DARK, false);

        //legendary
        ScreenUtils.scrollingText(g, this.font, translatable("gui.guide.rarity.legendary"),
                uiX + 124, uiX + 172, uiY + 150, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.legendaryScore),
                uiX + 174, uiY + 150, SCColors.GUIDE_TEXT_DARK, false);

        //percentile
        ScreenUtils.scrollingText(g, this.font, translatable("gui.starcatcher.tournament.scoring.percentile"),
                uiX + 56, uiX + 172, uiY + 162, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.percentileMultiplier),
                uiX + 174, uiY + 162, SCColors.GUIDE_TEXT_DARK, false);

        //perfect catch
        ScreenUtils.scrollingText(g, this.font, translatable("gui.starcatcher.tournament.scoring.perfect"),
                uiX + 56, uiX + 172, uiY + 174, SCColors.GUIDE_TEXT, false, 3000);

        ScreenUtils.text(g, this.font, String.format("%.1f", currentTournament.scoreSettings.perfectCatchMultiplier),
                uiX + 174, uiY + 174, SCColors.GUIDE_TEXT_DARK, false);

        //sign up button
        boolean isHovering = x > 79 && x < 163 && y > 192 && y < 208;
        //if owner
        if (isOwner)
        {
            if (currentTournament.status.equals(Tournament.Status.PREPARING))
            {
                if (mouseDown)
                    BUTTON_PRESSED.render(g, uiX + 80, uiY + 193);
                else
                    BUTTON.render(g, uiX + 80, uiY + 193);

                MutableComponent comp = translatable("gui.starcatcher.tournament.button.start");
                if (isHovering)
                {
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 196, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 198, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 119, uiX + 82, uiX + 159, uiY + 197, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 121, uiX + 84, uiX + 161, uiY + 197, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK, false);

                    ScreenUtils.outline(g, uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                }
                else
                    ScreenUtils.centeredScrollingText(g, this.font, comp,
                            uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK, false
                    );
            }

            if (currentTournament.status.equals(Tournament.Status.ACTIVE))
            {
                if (mouseDown)
                    BUTTON_PRESSED.render(g, uiX + 80, uiY + 193);
                else
                    BUTTON.render(g, uiX + 80, uiY + 193);

                MutableComponent comp = translatable("gui.starcatcher.tournament.button.cancel");
                if (isHovering)
                {
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 196, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 198, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 119, uiX + 82, uiX + 159, uiY + 197, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 121, uiX + 84, uiX + 161, uiY + 197, SCColors.WHITE, false);
                    ScreenUtils.centeredScrollingText(g, this.font, comp, uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK, false);

                    ScreenUtils.outline(g, uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                }
                else
                    ScreenUtils.centeredScrollingText(g, this.font, comp,
                            uiX + 120, uiX + 83, uiX + 160, uiY + 197, SCColors.GUIDE_TEXT_DARK, false
                    );
            }

        }
        //if not owner
        else
        {
            if (!viewingPastTournament)
            {
                //render button
                if (currentTournament.status.equals(Tournament.Status.PREPARING))
                {
                    if (mouseDown)
                        BUTTON_PRESSED.render(g, uiX + 80, uiY + 193);
                    else
                        BUTTON.render(g, uiX + 80, uiY + 193);
                }
                else
                    BUTTON_DISABLED.render(g, uiX + 80, uiY + 193);

                //if is signed up
                if (currentTournament.isPlayerSignedUp(Minecraft.getInstance().player))
                {
                    //if hovering button
                    if (x > 79 && x < 163 && y > 192 && y < 208 && currentTournament.status.equals(Tournament.Status.PREPARING))
                    {
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 121, uiY + 197, SCColors.WHITE, false);
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 119, uiY + 197, SCColors.WHITE, false);
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 120, uiY + 196, SCColors.WHITE, false);
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.unsign"), uiX + 120, uiY + 198, SCColors.WHITE, false);
                        ScreenUtils.outline(g,uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                    }

                    ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.unsign"),
                            uiX + 120, uiY + 197, SCColors.GUIDE_TEXT_DARK, false);
                }
                //if not signed up
                else
                {
                    //if hovering button
                    if (x > 79 && x < 163 && y > 192 && y < 208 && currentTournament.status.equals(Tournament.Status.PREPARING))
                    {
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 121, uiY + 197, SCColors.WHITE, false);
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 119, uiY + 197, SCColors.WHITE, false);
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 120, uiY + 196, SCColors.WHITE, false);
                        ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.sign_up"), uiX + 120, uiY + 198, SCColors.WHITE, false);
                        ScreenUtils.outline(g, uiX + 79, uiY + 192, 85, 17, SCColors.WHITE);
                    }

                    ScreenUtils.centeredText(g, this.font, translatable("gui.starcatcher.tournament.button.sign_up"),
                            uiX + 120, uiY + 197, SCColors.GUIDE_TEXT_DARK, false);
                }
            }
        }

        //player list
        //signed up
        ScreenUtils.text(g, this.font, currentTournament.status.equals(Tournament.Status.PREPARING) ?
                        translatable("gui.starcatcher.tournament.signed_up") : translatable("gui.starcatcher.tournament.scores"),
                uiX + 220, uiY + 45, SCColors.GUIDE_TEXT, false);

        //render names with offset based on page selected
        for (int i = playerListPage * 8; i < Math.min(currentTournament.playerScores.size(), playerListPage * 8 + 8); i++)
            ScreenUtils.scrollingText(g, this.font, Component.literal(currentTournament.playerScores.get(i).name),
                    uiX + 220, uiX + 323, uiY + 57 + i * 12 - playerListPage * 8 * 12, SCColors.GUIDE_TEXT, false);

        //render scores
        for (int i = playerListPage * 8; i < Math.min(currentTournament.playerScores.size(), playerListPage * 8 + 8); i++)
            ScreenUtils.scrollingText(g, this.font, Component.literal(String.format("%.1f", currentTournament.playerScores.get(i).score)),
                    uiX + 319, uiX + 345, uiY + 57 + i * 12 - playerListPage * 8 * 12, SCColors.GUIDE_TEXT, false);

        //previous arrow
        if (playerListPage > 0)
        {
            if (x > 215 && x < 230 && y > 151 && y < 163)
                LEFT_ARROW_HIGHLIGHT.render(g, uiX + 218, uiY + 153);
            else
                LEFT_ARROW.render(g, uiX + 218, uiY + 153);
        }

        //next arrow
        if (playerListPage < (currentTournament.playerScores.size() - 1) / 8)
        {
            if (x > 330 && x < 347 && y > 151 && y < 163)
                RIGHT_ARROW_HIGHLIGHT.render(g, uiX + 333, uiY + 153);
            else
                RIGHT_ARROW.render(g, uiX + 333, uiY + 153);
        }

        //page count
        ScreenUtils.centeredText(g, this.font, Component.literal(playerListPage + 1 + "/" + (((currentTournament.playerScores.size() - 1) / 8) + 1)),
                uiX + 280, uiY + 153, SCColors.GUIDE_TEXT, false);


        //previous tournaments
        ScreenUtils.text(g, this.font, translatable("gui.starcatcher.tournament.previous_tournaments"),
                uiX + 220, uiY + 175, SCColors.GUIDE_TEXT, false);

        if (viewingPastTournament)
        {
            if (x > 164 && x < 188 && y > 193 && y < 208)
            {
                INDEX_ARROW_HIGHLIGHT.render(g, uiX + 164, uiY + 193);
                tooltips.add(Component.literal("Back To Current Tournament"));
            }
            else
                INDEX_ARROW.render(g, uiX + 164, uiY + 193);
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
                    LEFT_ARROW_HIGHLIGHT.render(g, uiX + 218, uiY + 198);
                else
                    LEFT_ARROW.render(g, uiX + 218, uiY + 198);
            }

            //next arrow
            if (finishedTournaments.size() > tournamentPage + 1)
            {
                if (x > 330 && x < 347 && y > 196 && y < 208)
                    RIGHT_ARROW_HIGHLIGHT.render(g, uiX + 333, uiY + 198);
                else
                    RIGHT_ARROW.render(g, uiX + 333, uiY + 198);
            }

            //previous tournament name
            ScreenUtils.text(g, this.font, Component.literal(finishedTournaments.get(tournamentPage).name),
                    uiX + 220, uiY + 187, SCColors.GUIDE_TEXT_DARK, false);
        }

        //render tooltip
        ScreenUtils.Tooltip.set(tooltips);
        ScreenUtils.Tooltip.render(g, this.font, mouseX, mouseY);
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
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //System.out.println(x);
        //System.out.println(y);

        if (isOwner)
        {
            //duration decrease, shift does x10
            if (x > 53 && x < 117 && y > 88 && y < 107 && delta < -0.5f)
            {
                if (!hasShiftDown())
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 101);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 102);
                return true;
            }

            //duration increase, shift does x10
            if (x > 53 && x < 117 && y > 88 && y < 107 && delta > 0.5f)
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
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 200);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 201);
                return true;
            }

            //common
            if (x > 122 && x < 187 && y > 124 && y < 134)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 210);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 211);
                return true;
            }

            //uncommon
            if (x > 54 && x < 120 && y > 136 && y < 146)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 220);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 221);
                return true;
            }

            //rare
            if (x > 122 && x < 187 && y > 136 && y < 146)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 230);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 231);
                return true;
            }

            //epic
            if (x > 54 && x < 120 && y > 148 && y < 158)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 240);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 241);
                return true;
            }

            //legendary
            if (x > 122 && x < 187 && y > 148 && y < 158)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 250);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 251);
                return true;
            }

            //percentile
            if (x > 54 && x < 187 && y > 160 && y < 170)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 260);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 261);
                return true;
            }

            //perfect catch
            if (x > 54 && x < 187 && y > 172 && y < 182)
            {
                if (delta < 0.5f)
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 270);
                else
                    minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 271);
                return true;
            }

        }

        return super.mouseScrolled(mouseX, mouseY, delta);
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

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        boolean b = super.charTyped(codePoint, modifiers);
        PacketDistributorNeo.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
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
            PacketDistributorNeo.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
        return editbox || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        if (!nameEditBox.getValue().isEmpty())
            PacketDistributorNeo.sendToServer(new SBStandTournamentNameChangePayload(currentTournament.tournamentUUID, nameEditBox.getValue()));
    }

    public StandScreen(StandMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        currentTournament = menu.sbe.tournament;
        imageWidth = 420;
        imageHeight = 260;
    }

    public MutableComponent translatable(String key)
    {
        return Tooltips.resolveTagsToComponentFromTranslationKey(key);
    }
}
