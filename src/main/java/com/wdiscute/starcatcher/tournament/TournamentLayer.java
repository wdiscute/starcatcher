package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.nikdo53.neobackports.screen.LayeredDraw;

import java.text.DecimalFormat;
import java.util.*;

public class TournamentLayer implements LayeredDraw.Layer
{
    public static Tournament tournament;

    public static Tournament.PlayerScore firstPlace = null;
    public static Tournament.PlayerScore secondPlace = null;
    public static Tournament.PlayerScore thirdPlace = null;

    public static Tournament.PlayerScore playerScore = null;
    public static ExpandedType expandedType = ExpandedType.BIG;


    private static final ScreenUtils.Image BACKGROUND_TINY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/overlay_tiny.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_EXPANDED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/overlay_expanded.png"), 420, 260);
    private static final ScreenUtils.Image FIRST_PLACE_FISH = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/first_place_fish.png"), 11, 6);
    private static final ScreenUtils.Image SECOND_PLACE_FISH = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/second_place_fish.png"), 11, 6);
    private static final ScreenUtils.Image THIRD_PLACE_FISH = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tournament/third_place_fish.png"), 11, 6);

    public static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    Font font;
    int imageWidth = 420;
    int imageHeight = 260;

    public static void clear()
    {
        tournament = null;
    }

    @Override
    public void render(GuiGraphics g, float v)
    {
        if (tournament == null) return;
        if (Minecraft.getInstance().level == null) return;
        if (Minecraft.getInstance().player == null) return;

        font = Minecraft.getInstance().font;

        g.pose().pushPose();
        g.pose().translate(SCConfig.TOURNAMENT_X_OFFSET.get(), SCConfig.TOURNAMENT_Y_OFFSET.get(), 0);
        g.pose().scale(((float) (double)SCConfig.TOURNAMENT_SCALE.get()), ((float) (double) SCConfig.TOURNAMENT_SCALE.get()), 1);

        //get fish for player position
        ScreenUtils.Image fish = null;
        if (firstPlace != null && firstPlace.uuid.equals(playerScore.uuid)) fish = FIRST_PLACE_FISH;
        if (secondPlace != null && secondPlace.uuid.equals(playerScore.uuid)) fish = SECOND_PLACE_FISH;
        if (thirdPlace != null && thirdPlace.uuid.equals(playerScore.uuid)) fish = THIRD_PLACE_FISH;

        //if small
        if (expandedType.equals(ExpandedType.SMALL))
        {
            BACKGROUND_TINY.render(g);

            ScreenUtils.text(g, this.font, tournament.name, 58, 35, SCColors.GUIDE_TEXT_DARK, false);

            if (playerScore != null)
            {
                ScreenUtils.text(g, this.font, font.plainSubstrByWidth(playerScore.name, 95), 48, 70, SCColors.WHITE, false);
                ScreenUtils.centeredText(g, this.font, Component.literal(FORMAT.format(playerScore.score)),
                        160, 70, SCColors.WHITE, false);
            }

            ScreenUtils.text(g, this.font, getDisplayTimeLeft(), 21, 35, -1, false);

            //render fish icon for first/second/third place
            if (fish != null)
                fish.render(g, 30, 72);
        }
        //if big
        else if (expandedType.equals(ExpandedType.BIG))
        {
            BACKGROUND_EXPANDED.render(g);

            ScreenUtils.text(g, this.font, tournament.name, 58, 16, 0x635040, false);

            //render first place
            if (firstPlace != null)
                ScreenUtils.text(g, this.font, font.plainSubstrByWidth(firstPlace.name, 95), 48, 71, SCColors.WHITE, false);
            if (firstPlace != null)
                ScreenUtils.centeredText(g, this.font, Component.literal(FORMAT.format(firstPlace.score)),
                        165, 71, SCColors.WHITE, false);

            //render second place
            if (secondPlace != null)
                ScreenUtils.text(g, this.font, font.plainSubstrByWidth(secondPlace.name, 95), 48, 92, SCColors.WHITE, false);
            if (secondPlace != null)
                ScreenUtils.centeredText(g, this.font, Component.literal(FORMAT.format(secondPlace.score)),
                        165, 92, SCColors.WHITE, false);

            //render third place
            if (thirdPlace != null)
                ScreenUtils.text(g, this.font, font.plainSubstrByWidth(thirdPlace.name, 95), 48, 113, SCColors.WHITE, false);
            if (thirdPlace != null)
                ScreenUtils.centeredText(g, this.font, Component.literal(FORMAT.format(thirdPlace.score)),
                        165, 113, SCColors.WHITE, false);

            //render player score
            if (playerScore != null)
                ScreenUtils.text(g, this.font, font.plainSubstrByWidth(playerScore.name + "wadawdadwadwaawdawdwad", 95), 48, 141, SCColors.WHITE, false);
            if (playerScore != null)
                ScreenUtils.centeredText(g, this.font, Component.literal(FORMAT.format(playerScore.score)),
                        160, 141, SCColors.WHITE, false);

            ScreenUtils.text(g, this.font, getDisplayTimeLeft(), 12, 31, -1, false);

            //render fish icon for first/second/third place
            if (fish != null)
                fish.render(g, 30, 142);
        }

        g.pose().popPose();
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
        optional.ifPresentOrElse(playerScore -> TournamentLayer.playerScore = playerScore, () -> playerScore = new Tournament.PlayerScore(UUID.randomUUID(), "???", 0));

        tournament = t;
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
