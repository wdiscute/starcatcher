package com.wdiscute.starcatcher.tournament;

import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.UUID;

public record Tournament
        (
                String name,
                BlockPos TournamentTable,
                List<TournamentPlayerScore> players,
                TournamentSettings tournamentSettings
        )
{
}
