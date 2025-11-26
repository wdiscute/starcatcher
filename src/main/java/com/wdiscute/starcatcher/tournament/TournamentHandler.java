package com.wdiscute.starcatcher.tournament;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class TournamentHandler
{

    private static final List<Tournament> tournaments = new ArrayList<>();

    public static List<Tournament> getAllTournaments()
    {
        return tournaments;
    }

    public static Tournament getTournament(int id)
    {
        if(id > tournaments.size()) return null;
        return tournaments.get(id);
    }

    public static int addTournament(Tournament tournament)
    {
        tournaments.add(tournament);
        return tournaments.size();
    }

    public static void tick(LevelTickEvent.Post event)
    {

    }

}
