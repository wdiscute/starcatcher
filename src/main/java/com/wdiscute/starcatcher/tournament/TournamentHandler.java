package com.wdiscute.starcatcher.tournament;

import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

public class TournamentHandler
{

    private static final List<Tournament> activeTournaments = new ArrayList<>();

    public static List<Tournament> getAllTournaments()
    {
        return activeTournaments;
    }

    public static Tournament getTournament(int id)
    {
        if (id > activeTournaments.size()) return null;
        return activeTournaments.get(id);
    }

    public static int addTournament(Tournament tournament)
    {
        activeTournaments.add(tournament);
        System.out.println("tournament: " + tournament.name + " has started");
        return activeTournaments.size();
    }

    public static void addScore(Player player, FishProperties fp, boolean perfectCatch)
    {
        for (Tournament t : activeTournaments)
        {
            if (t.playerScores.containsKey(player.getUUID()))
            {
                if (t.settings.type.equals(TournamentSettings.Type.SIMPLE))
                {
                    t.playerScores.get(player.getUUID()).addScore(1);
                }
            }
        }
    }

    public static void tick(ServerTickEvent.Post event)
    {
        long levelTicks = event.getServer().getTickCount();
        if (levelTicks % 20 != 0) return;

        List<Tournament> finishedTournaments = new ArrayList<>();
        for (Tournament t : activeTournaments)
        {
            if (levelTicks >= t.lastsUntil)
            {
                finishedTournaments.add(t);
                System.out.println("tournament: " + t.name + " has ended");

                UUID winner = null;
                int bestScore = 0;

                for (Map.Entry<UUID, TournamentPlayerScore> entry : t.playerScores.entrySet())
                {
                    if (entry.getValue().score > bestScore)
                    {
                        bestScore = entry.getValue().score;
                        winner = entry.getKey();
                    }
                }

                Level level = null;

                if (winner == null)
                {
                    System.out.println("no one won :(");
                }
                else
                {
                    GameProfileCache profileCache = event.getServer().getProfileCache();

                    if (profileCache == null)
                    {
                        System.out.println("unknown player won looooooool who tf is that " + winner);
                    }
                    else
                    {
                        Optional<GameProfile> gameProfile = event.getServer().getProfileCache().get(winner);

                        if (gameProfile.isPresent())
                        {
                            System.out.println("Winner is " + gameProfile.get().getName());
                        }
                        else
                        {
                            System.out.println("unknown player won looooooool who tf is that " + winner);
                        }
                    }


                }


            }
        }

        activeTournaments.removeAll(finishedTournaments);
    }

}
