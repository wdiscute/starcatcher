package com.wdiscute.starcatcher.tournament;

import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.data.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.data.network.tournament.CBFinishedTournamentsListPayload;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.data.network.tournament.CBActiveTournamentUpdatePayload;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TournamentHandler
{
    private static final List<Tournament> finishedTournaments = new ArrayList<>();
    private static final List<Tournament> activeTournaments = new ArrayList<>();

    public static Tournament getActiveTournamentOrNull(UUID uuid)
    {
        for (Tournament t : activeTournaments)
            if (t.tournamentUUID.equals(uuid)) return t;

        return null;
    }

    public static List<Tournament> getFinishedTournaments()
    {
        return finishedTournaments;
    }

    public static void sendActiveTournamentUpdateToClient(ServerPlayer sp, Tournament tournament)
    {
        if (sp == null || tournament == null) return;
        PacketDistributor.sendToPlayer(sp, new CBActiveTournamentUpdatePayload(tournament));
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
        tournament.startTimeEpoch = System.currentTimeMillis();

        Level level = playerWhoStartedTheTournament.level();
        for (Tournament.PlayerScore playerScore : tournament.playerScores)
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(playerScore.uuid);
            sendActiveTournamentUpdateToClient(player, tournament);

            if (player != null)
            {
                player.sendSystemMessage(Component.translatable("gui.starcatcher.tournament.started", tournament.name));
                player.sendSystemMessage(Component.translatable("gui.starcatcher.tournament.press_tab").withColor(CommonColors.LIGHT_GRAY));
            }
        }
    }

    public static void cancelTournament(Level level, Tournament tournament)
    {
        for (var entry : tournament.playerScores)
        {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.uuid);
            sendActiveTournamentUpdateToClient(player, tournament);
            clearTournamentToClient(player);
        }

        tournament.status = Tournament.Status.FINISHED;
        activeTournaments.remove(tournament);
        finishedTournaments.add(0, tournament);

        //update finished tournaments to client
        if (level instanceof ServerLevel serverLevel)
        {
            for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers())
            {
                PacketDistributor.sendToPlayer(player, new CBFinishedTournamentsListPayload(TournamentHandler.getFinishedTournaments()));
            }
        }
    }

    public static void addScore(Player player, FishProperties fp, boolean perfectCatch, float percentile)
    {
        //only score fishes
        if (!fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH)) return;


        for (Tournament t : activeTournaments)
        {
            List<Tournament.PlayerScore> list = t.playerScores.stream().filter(o -> o.uuid.equals(player.getUUID())).toList();

            if (list.isEmpty()) continue;

            Tournament.PlayerScore first = list.getFirst();

            float baseScore = switch (fp.rarity())
            {
                case TRASH -> t.scoreSettings.trashScore;
                case COMMON -> t.scoreSettings.commonScore;
                case UNCOMMON -> t.scoreSettings.uncommonScore;
                case RARE -> t.scoreSettings.rareScore;
                case EPIC -> t.scoreSettings.epicScore;
                case LEGENDARY -> t.scoreSettings.legendaryScore;
                default -> 0;
            };

            float extraAwardPercentile = baseScore * ((100 - percentile) / 100) * t.scoreSettings.percentileMultiplier;
            float extraAwardPerfectCatch = 0;
            if (perfectCatch)
                extraAwardPercentile = baseScore * t.scoreSettings.perfectCatchMultiplier;

            first.score += baseScore + extraAwardPercentile + extraAwardPerfectCatch;

            List<Tournament.PlayerScore> mutableList = new ArrayList<>(){{
                this.addAll(t.playerScores);
            }};

            mutableList.sort((o, o2) -> Float.compare(o2.score, o.score));

            t.playerScores = mutableList;

            //send blockstate update to every opened stand
            for (ServerPlayer sp : player.getServer().getPlayerList().getPlayers())
            {
                if (sp.containerMenu instanceof StandMenu sm)
                {
                    if (sm.sbe != null) sm.sbe.sync();
                }
            }

            //update to logged in players
            for (Tournament.PlayerScore playerScore : t.playerScores)
            {
                Player playerByUUID = player.level().getPlayerByUUID(playerScore.uuid);
                if (playerByUUID != null)
                    PacketDistributor.sendToPlayer((ServerPlayer) playerByUUID, new CBActiveTournamentUpdatePayload(t));
            }
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
            //if tournament time has past
            if (System.currentTimeMillis() >= t.startTimeEpoch + t.durationInTicks * 50)
            {
                finished.add(t);
                t.status = Tournament.Status.FINISHED;
                Tournament.PlayerScore nobody = new Tournament.PlayerScore(UUID.randomUUID(), "Nobody :( ", 0);
                Tournament.PlayerScore winner = nobody;
                int bestScore = 0;

                //find highest score player
                for (Tournament.PlayerScore playerscore : t.playerScores)
                {
                    if (playerscore.score > bestScore)
                    {
                        winner = playerscore;
                    }
                }

                //send blockstate update to every opened stand
                for (ServerPlayer player : server.getPlayerList().getPlayers())
                {
                    if (player.containerMenu instanceof StandMenu sm)
                    {
                        if (sm.sbe != null) sm.sbe.sync();
                    }
                }

                //send message and packet to every player playing
                for (var playerScore : t.playerScores)
                {
                    ServerPlayer player = server.getPlayerList().getPlayer(playerScore.uuid);
                    if (player != null)
                    {
                        TournamentHandler.clearTournamentToClient(player);
                        if (winner == nobody)
                            player.sendSystemMessage(Component.translatable("gui.starcatcher.tournament.ended.no_winner", t.name));
                        else
                            player.sendSystemMessage(Component.translatable("gui.starcatcher.tournament.ended",
                                    t.name, winner.name, String.format("%.1f", winner.score)));
                    }
                }

            }
        }

        finishedTournaments.addAll(0, finished);
        activeTournaments.removeAll(finished);

        //update finished tournaments to client
        for (ServerPlayer player : server.getPlayerList().getPlayers())
        {
            PacketDistributor.sendToPlayer(player, new CBFinishedTournamentsListPayload(TournamentHandler.getFinishedTournaments()));
        }
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
    }

    public static Tournament getTournamentForPlayer(Player player)
    {
        AtomicReference<Tournament> tToReturn = new AtomicReference<>();
        activeTournaments.forEach(t ->
        {
            Stream<Tournament.PlayerScore> tournamentPlayerScoreStream = t.playerScores.stream().filter(playerScore -> playerScore.uuid.equals(player.getUUID()));
            if (tournamentPlayerScoreStream.findFirst().isPresent()) tToReturn.set(t);
        });

        return tToReturn.get();
    }
}
