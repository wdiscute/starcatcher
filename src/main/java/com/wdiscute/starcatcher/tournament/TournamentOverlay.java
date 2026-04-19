package com.wdiscute.starcatcher.tournament;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TournamentOverlay implements GuiLayer
{
    private static final Logger log = LoggerFactory.getLogger(TournamentOverlay.class);
    public static Tournament tournament;
    public static Map<UUID, String> gameProfilesCache = new HashMap<>();

    public static Pair<Component, Integer> firstPlace = Pair.of(Component.literal("[empty]"), 0);
    public static Pair<Component, Integer> secondPlace = Pair.of(Component.literal("[empty]"), 0);
    public static Pair<Component, Integer> thirdPlace = Pair.of(Component.literal("[empty]"), 0);

    public static Pair<Component, Integer> playerPlace = Pair.of(Component.empty(), 0);
    public static ExpandedType expandedType = ExpandedType.BIG;
    static int playerRank = 0;


    private static final Identifier BACKGROUND_TINY = Starcatcher.rl("textures/gui/tournament/overlay_tiny.png");
    private static final Identifier BACKGROUND_EXPANDED = Starcatcher.rl("textures/gui/tournament/overlay_expanded.png");
    private static final Identifier FIRST_PLACE_FISH = Starcatcher.rl("textures/gui/tournament/first_place_fish.png");
    private static final Identifier SECOND_PLACE_FISH = Starcatcher.rl("textures/gui/tournament/second_place_fish.png");
    private static final Identifier THIRD_PLACE_FISH = Starcatcher.rl("textures/gui/tournament/third_place_fish.png");

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
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker)
    {
        if (tournament == null) return;
        if (Minecraft.getInstance().level == null) return;
        else level = Minecraft.getInstance().level;
        if (Minecraft.getInstance().player == null) return;
        else player = Minecraft.getInstance().player;

        uiX = Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth;
        uiY = Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight - 80;
        font = Minecraft.getInstance().font;


        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
        //add scale with config like minigame

        //if small
        if (expandedType.equals(ExpandedType.SMALL))
        {
            renderImage(guiGraphics, BACKGROUND_TINY);

            guiGraphics.text(this.font, tournament.name, 58, 35, 0x635040, false);

            guiGraphics.text(this.font, playerPlace.getFirst(), 48, 70, -1, false);
            guiGraphics.text(this.font, playerPlace.getSecond() + "", 160, 70, -1, false);

            guiGraphics.text(this.font, getDisplayTimeLeft(tournament.lastsUntilEpoch - System.currentTimeMillis()), 21, 35, -1, false);
            switch (playerRank)
            {
                case 1:
                    guiGraphics.blit(FIRST_PLACE_FISH, 30, 72, 0, 0, 11, 6, 11, 6);
                case 2:
                    guiGraphics.blit(SECOND_PLACE_FISH, 30, 72, 0, 0, 11, 6, 11, 6);
                case 3:
                    guiGraphics.blit(THIRD_PLACE_FISH, 30, 72, 0, 0, 11, 6, 11, 6);
            }
        }
        //if big
        else if(expandedType.equals(ExpandedType.BIG))
        {
            renderImage(guiGraphics, BACKGROUND_EXPANDED);

            guiGraphics.text(this.font, tournament.name, 58, 16, 0x635040, false);

            //render first/second/third player + scores
            if(firstPlace.getSecond() != 0)guiGraphics.text(this.font, firstPlace.getFirst(), 48, 71, -1, false);
            if(firstPlace.getSecond() != 0)guiGraphics.text(this.font, firstPlace.getSecond() + "", 154, 71, -1, false);
            if(secondPlace.getSecond() != 0)guiGraphics.text(this.font, secondPlace.getFirst(), 48, 92, -1, false);
            if(secondPlace.getSecond() != 0)guiGraphics.text(this.font, secondPlace.getSecond() + "", 154, 92, -1, false);
            if(thirdPlace.getSecond() != 0)guiGraphics.text(this.font, thirdPlace.getFirst(), 48, 113, -1, false);
            if(thirdPlace.getSecond() != 0) guiGraphics.text(this.font, thirdPlace.getSecond() + "", 154, 113, -1, false);

            guiGraphics.text(this.font, playerPlace.getFirst(), 48, 141, -1, false);
            guiGraphics.text(this.font, playerPlace.getSecond() + "", 154, 141, -1, false);

            guiGraphics.text(this.font, getDisplayTimeLeft(tournament.lastsUntilEpoch - System.currentTimeMillis()), 12, 31, -1, false);

            //render fish icon for first/second/third place
            if (playerRank != 0)
                guiGraphics.blit(
                        switch (playerRank)
                        {
                            case 1:
                                yield FIRST_PLACE_FISH;
                            case 2:
                                yield SECOND_PLACE_FISH;
                            default:
                                yield THIRD_PLACE_FISH;
                        },
                        30, 142, 0, 0, 11, 6, 11, 6);
        }

        guiGraphics.pose().popMatrix();
    }


    public static String getDisplayTimeLeft(long ticks)
    {
        long ticksRemainingToCalculate = ticks / 1000;
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

    public static void onTournamentReceived(Tournament t, List<GameProfile> list)
    {
        //add entries to cached game profile
        list.forEach(e -> gameProfilesCache.put(e.id(), e.name()));

        firstPlace = Pair.of(Component.literal(""), 0);
        secondPlace = Pair.of(Component.literal(""), 0);
        thirdPlace = Pair.of(Component.literal(""), 0);

        if (t.status.equals(Tournament.Status.ACTIVE))
        {
            for (TournamentPlayerScore tps : t.playerScores)
            {
                if (tps.score > thirdPlace.getSecond())
                {
                    thirdPlace = Pair.of(
                            Component.literal(gameProfilesCache.get(tps.playerUUID)),
                            tps.score
                    );
                }

                if (tps.score > secondPlace.getSecond())
                {
                    thirdPlace = secondPlace;
                    secondPlace = Pair.of(
                            Component.literal(gameProfilesCache.get(tps.playerUUID)),
                            tps.score
                    );
                }

                if (tps.score > firstPlace.getSecond())
                {
                    secondPlace = firstPlace;
                    firstPlace = Pair.of(
                            Component.literal(gameProfilesCache.get(tps.playerUUID)),
                            tps.score
                    );
                }


            }

            //set player place name & score
            Optional<TournamentPlayerScore> optional = t.playerScores.stream().filter(p -> p.playerUUID.equals(Minecraft.getInstance().player.getUUID())).findFirst();
            optional.ifPresent(playerScore -> playerPlace = Pair.of(
                    Minecraft.getInstance().player.getName(),
                    playerScore.score));

            //set playerRank
            if (firstPlace.getFirst().equals(playerPlace.getFirst())) playerRank = 1;
            else if (secondPlace.getFirst().equals(playerPlace.getFirst())) playerRank = 2;
            else if (thirdPlace.getFirst().equals(playerPlace.getFirst())) playerRank = 3;
            else playerRank = 0;
        }
        tournament = t;
    }

    private void renderImage(GuiGraphicsExtractor guiGraphics, Identifier rl)
    {
        guiGraphics.blit(RenderPipelines.GUI, rl, 0, 0, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
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
