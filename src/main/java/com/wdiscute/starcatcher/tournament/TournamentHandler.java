package com.wdiscute.starcatcher.tournament;

import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class TournamentHandler
{

    public static List<Tournament> tournaments = new ArrayList<>();


    public static void tick(ServerTickEvent.Post event)
    {
        event.getServer();
    }

}
