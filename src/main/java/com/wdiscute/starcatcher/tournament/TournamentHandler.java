package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.io.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TournamentHandler
{
    private static final List<Tournament> finishedTournaments = new ArrayList<>();
    private static final List<Tournament> activeTournaments = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(TournamentHandler.class);

    public static Tournament getTournamentOrNull(UUID uuid)
    {
        for (Tournament t : activeTournaments)
        {
            if (t.tournamentUUID.equals(uuid)) return t;
        }

        for (Tournament t : finishedTournaments)
        {
            if (t.tournamentUUID.equals(uuid)) return t;
        }
        return null;
    }

    public static void sendActiveTournamentUpdateToClient(ServerPlayer sp, Tournament tournament)
    {
        if (sp == null || tournament == null) return;
        PacketDistributor.sendToPlayer(sp, CBActiveTournamentUpdatePayload.helper(sp, tournament));
    }

    public static void clearTournamentToClient(ServerPlayer sp)
    {
        if (sp == null) return;
        PacketDistributor.sendToPlayer(sp, new CBClearTournamentPayload(":)"));
    }

    public static void startTournament(Player playerWhoStartedTheTournament, Tournament tournament)
    {
        activeTournaments.add(tournament);
        tournament.status = Tournament.Status.ACTIVE;
        tournament.lastsUntilEpoch = System.currentTimeMillis() + tournament.settings.durationInTicks / 20 * 1000;

        Level level = playerWhoStartedTheTournament.level();
        for (TournamentPlayerScore playerScore : tournament.playerScores)
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(playerScore.playerUUID);
            sendActiveTournamentUpdateToClient(player, tournament);

            if (player != null)
            {
                player.sendSystemMessage(Component.literal(tournament.name).append(Component.translatable("gui.starcatcher.tournament.started")));
                player.sendSystemMessage(Component.translatable("gui.starcatcher.tournament.press_tab").withColor(CommonColors.LIGHT_GRAY));
            }
        }
    }

    public static void cancelTournament(Level level, Tournament tournament)
    {
        for (var entry : tournament.playerScores)
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.playerUUID);
            sendActiveTournamentUpdateToClient(player, tournament);
            clearTournamentToClient(player);
        }

        activeTournaments.remove(tournament);
        finishedTournaments.add(tournament);
        tournament.status = Tournament.Status.CANCELLED;
    }

    public static void addScore(Player playerToAwardScoreTo, FishProperties fp, boolean perfectCatch, int size, int weight, float percentile)
    {
        if (playerToAwardScoreTo.level().isClientSide()) return;
        for (Tournament t : activeTournaments)
        {
            t.playerScores.forEach(p ->
                    {
                        if (p.playerUUID.equals(playerToAwardScoreTo.getUUID()))
                        {
                            //simple scoring
                            if (t.settings.scoring.equals(TournamentSettings.Scoring.SIMPLE))
                            {
                                p.addScore(1);
                            }

                            Level level = playerToAwardScoreTo.level();
                            for (var entry : t.playerScores)
                            {
                                ServerPlayer sp = level.getServer().getPlayerList().getPlayer(entry.playerUUID);
                                sendActiveTournamentUpdateToClient(sp, t);
                            }
                        }
                    }
            );
        }
    }

    public static void tick(ServerTickEvent.Post event)
    {
        MinecraftServer server = event.getServer();
        long levelTicks = server.getTickCount();
        if (levelTicks % 20 != 0) return;

        List<Tournament> finished = new ArrayList<>();
        for (Tournament t : activeTournaments)
        {
            if (System.currentTimeMillis() >= t.lastsUntilEpoch)
            {
                finished.add(t);
                t.status = Tournament.Status.FINISHED;
                UUID winner = null;
                int bestScore = 0;

                for (TournamentPlayerScore playerscore : t.playerScores)
                {
                    if (playerscore.score > bestScore)
                    {
                        bestScore = playerscore.score;
                        winner = playerscore.playerUUID;
                    }
                }

                String winnerString = "???";

                if (winner != null)
                    winnerString = server.services().nameToIdCache().get(winner).get().name();

                for (var playerScore : t.playerScores)
                {
                    ServerPlayer player = server.getPlayerList().getPlayer(playerScore.playerUUID);
                    if (player != null)
                    {
                        TournamentHandler.clearTournamentToClient(player);
                        player.sendSystemMessage(Component.literal(t.name + " has ended! The winner is " + winnerString + "!"));
                    }
                }

            }
        }

        finishedTournaments.addAll(finished);
        activeTournaments.removeAll(finished);
    }

    public static List<Tournament> getAll()
    {
        List<Tournament> t = new ArrayList<>();
        t.addAll(activeTournaments);
        t.addAll(finishedTournaments);
        return t;
    }

    public static void setAll(List<Tournament> tournaments)
    {
        activeTournaments.clear();
        activeTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.ACTIVE)).toList());

        finishedTournaments.clear();
        finishedTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.FINISHED)).toList());
        finishedTournaments.addAll(tournaments.stream().filter(t -> t.status.equals(Tournament.Status.CANCELLED)).toList());
    }

    public static Tournament getTournamentForPlayer(Player player)
    {
        AtomicReference<Tournament> tToReturn = new AtomicReference<>();
        activeTournaments.forEach(t ->
        {
            Stream<TournamentPlayerScore> tournamentPlayerScoreStream = t.playerScores.stream().filter(p -> p.playerUUID.equals(player.getUUID()));
            if (tournamentPlayerScoreStream.findFirst().isPresent()) tToReturn.set(t);
        });

        return tToReturn.get();
    }
}
