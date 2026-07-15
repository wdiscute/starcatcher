package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TournamentOverlay implements LayeredDraw.Layer
{
    public static Tournament tournament;

    public static Tournament.PlayerScore firstPlace = null;
    public static Tournament.PlayerScore secondPlace = null;
    public static Tournament.PlayerScore thirdPlace = null;

    public static Tournament.PlayerScore playerScore = null;
    public static ExpandedType expandedType = ExpandedType.BIG;


    private static final ResourceLocation BACKGROUND_TINY = Starcatcher.rl("textures/gui/tournament/overlay_tiny.png");
    private static final ResourceLocation BACKGROUND_EXPANDED = Starcatcher.rl("textures/gui/tournament/overlay_expanded.png");
    private static final ResourceLocation FIRST_PLACE_FISH = Starcatcher.rl("textures/gui/tournament/first_place_fish.png");
    private static final ResourceLocation SECOND_PLACE_FISH = Starcatcher.rl("textures/gui/tournament/second_place_fish.png");
    private static final ResourceLocation THIRD_PLACE_FISH = Starcatcher.rl("textures/gui/tournament/third_place_fish.png");

    int uiX;
    int uiY;

    Font font;
    int imageWidth = 420;
    int imageHeight = 260;
    Player player;
    ClientLevel level;

    public static void clear()
    {
        tournament = null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        if (tournament == null) return;
        if (Minecraft.getInstance().level == null) return;
        else level = Minecraft.getInstance().level;
        if (Minecraft.getInstance().player == null) return;
        else player = Minecraft.getInstance().player;

        uiX = Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth;
        uiY = Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight - 80;
        font = Minecraft.getInstance().font;


        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(SCConfig.TOURNAMENT_X_OFFSET.get(), SCConfig.TOURNAMENT_Y_OFFSET.get(), 0);
        guiGraphics.pose().scale(((float) SCConfig.TOURNAMENT_SCALE.getAsDouble()), ((float) SCConfig.TOURNAMENT_SCALE.getAsDouble()), 1);

        //get fish for player position
        ResourceLocation fish = null;
        if (firstPlace != null && firstPlace.uuid.equals(playerScore.uuid)) fish = FIRST_PLACE_FISH;
        if (secondPlace != null && secondPlace.uuid.equals(playerScore.uuid)) fish = SECOND_PLACE_FISH;
        if (thirdPlace != null && thirdPlace.uuid.equals(playerScore.uuid)) fish = THIRD_PLACE_FISH;

        //if small
        if (expandedType.equals(ExpandedType.SMALL))
        {
            renderImage(guiGraphics, BACKGROUND_TINY);

            guiGraphics.drawString(this.font, tournament.name, 58, 35, 0x635040, false);

            if (playerScore != null)
            {
                renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(playerScore.name), 48, 140, 70);
                renderCenteredScrollingString(guiGraphics, this.font, Component.literal(String.format("%.1f", playerScore.score)),
                        160, 151, 178, 70);
            }

            guiGraphics.drawString(this.font, getDisplayTimeLeft(), 21, 35, -1, false);

            //render fish icon for first/second/third place
            if (fish != null)
                guiGraphics.blit(fish, 30, 72, 0, 0, 11, 6, 11, 6);
        }
        //if big
        else if (expandedType.equals(ExpandedType.BIG))

        {
            renderImage(guiGraphics, BACKGROUND_EXPANDED);

            guiGraphics.drawString(this.font, tournament.name, 58, 16, 0x635040, false);

            //render first place
            if (firstPlace != null)
                renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(firstPlace.name), 48, 140, 71);
            if (firstPlace != null)
                renderCenteredScrollingString(guiGraphics, this.font, Component.literal(String.format("%.1f", firstPlace.score)),
                        160, 151, 178, 71);

            //render second place
            if (secondPlace != null)
                renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(secondPlace.name), 48, 140, 92);
            if (secondPlace != null)
                renderCenteredScrollingString(guiGraphics, this.font, Component.literal(String.format("%.1f", secondPlace.score)),
                        160, 151, 178, 92);

            //render third place
            if (thirdPlace != null)
                renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(thirdPlace.name), 48, 140, 113);
            if (thirdPlace != null)
                renderCenteredScrollingString(guiGraphics, this.font, Component.literal(String.format("%.1f", thirdPlace.score)),
                        160, 151, 178, 113);

            //render player score
            if (playerScore != null)
                renderStringWithLimitedSpace(guiGraphics, this.font, Component.literal(playerScore.name), 48, 140, 141);
            if (playerScore != null)
                renderCenteredScrollingString(guiGraphics, this.font, Component.literal(String.format("%.1f", playerScore.score)),
                        160, 151, 178, 141);

            guiGraphics.drawString(this.font, getDisplayTimeLeft(), 12, 31, -1, false);

            //render fish icon for first/second/third place
            if (fish != null)
                guiGraphics.blit(fish, 30, 142, 0, 0, 11, 6, 11, 6);
        }

        guiGraphics.pose().popPose();
    }

    public static void renderStringWithLimitedSpace(GuiGraphics guiGraphics, Font font, Component comp, int minX, int maxX, int y)
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
            guiGraphics.drawString(font, comp, x, y, SCColors.WHITE, false);
            guiGraphics.disableScissor();
        }
        else
        {
            guiGraphics.drawString(font, comp, minX, y, SCColors.WHITE, false);
        }
    }

    public static void renderCenteredScrollingString(GuiGraphics guiGraphics, Font font, Component text, int centerX, int minX, int maxX, int y)
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
            guiGraphics.drawString(font, text, x, y, SCColors.WHITE, false);
            guiGraphics.disableScissor();
        }
        else
        {
            int i1 = Mth.clamp(centerX, minX + i / 2, maxX - i / 2);
            guiGraphics.drawString(font, text.getVisualOrderText(), i1 - font.width(text.getVisualOrderText()) / 2, y, SCColors.WHITE, false);
        }
    }

    public static String getDisplayTimeLeft()
    {
        long endTime = tournament.startTimeEpoch + tournament.durationInTicks * 50;
        long currentTime = System.currentTimeMillis();
        long remaining = endTime - currentTime;

        long ticksRemainingToCalculate = remaining / 1000;
        if (ticksRemainingToCalculate < 0) return "????";
        String finalString = "";

        //days
        if (ticksRemainingToCalculate > 86400)
        {
            finalString += ticksRemainingToCalculate / 86400 + "d";
            return finalString;
        }

        //hours
        if (ticksRemainingToCalculate > 3600)
        {
            finalString += String.format("%02d", ticksRemainingToCalculate / 3600) + ":";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 3600;

            finalString += String.format("%02d", ticksRemainingToCalculate / 60);

            return finalString;
        }

        //minutes
        finalString += String.format("%02d", ticksRemainingToCalculate / 60) + ":";
        ticksRemainingToCalculate = ticksRemainingToCalculate % 60;

        //seconds
        finalString += String.format("%02d", ticksRemainingToCalculate);
        return finalString;
    }

    public static void onTournamentReceived(Tournament t)
    {
        //assign with nulls so we can compare the score
        firstPlace = new Tournament.PlayerScore(null, null, 0);
        secondPlace = new Tournament.PlayerScore(null, null, 0);
        thirdPlace = new Tournament.PlayerScore(null, null, 0);

        //if tournament is active
        //get first
        for (Tournament.PlayerScore tps : t.playerScores)
        {
            if (tps.score > thirdPlace.score || thirdPlace.name == null)
            {
                thirdPlace = tps;
            }

            if (tps.score > secondPlace.score || secondPlace.name == null)
            {
                thirdPlace = secondPlace;
                secondPlace = tps;
            }

            if (tps.score > firstPlace.score || firstPlace.name == null)
            {
                secondPlace = firstPlace;
                firstPlace = tps;
            }
        }

        //reset back to null
        if (firstPlace.name == null) firstPlace = null;
        if (secondPlace.name == null) secondPlace = null;
        if (thirdPlace.name == null) thirdPlace = null;

        //set player place name & score
        Optional<Tournament.PlayerScore> optional = t.playerScores.stream().filter(p -> p.uuid.equals(Minecraft.getInstance().player.getUUID())).findFirst();
        optional.ifPresentOrElse(playerScore -> TournamentOverlay.playerScore = playerScore, () -> playerScore = new Tournament.PlayerScore(UUID.randomUUID(), "???", 0));

        tournament = t;
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, 0, 0, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    public enum ExpandedType
    {
        SMALL,
        BIG,
        HIDDEN;

        private static final ExpandedType[] vals = values();

        public ExpandedType previous()
        {
            if (this.ordinal() == 0) return vals[vals.length - 1];
            return vals[(this.ordinal() - 1) % vals.length];
        }

        public ExpandedType next()
        {
            return vals[(this.ordinal() + 1) % vals.length];
        }

    }
}
