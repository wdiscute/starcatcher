package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.fish.FishProperties;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FTBTeamsCompat
{
    private static final Logger log = LoggerFactory.getLogger(FTBTeamsCompat.class);

    public static void awardToTeam(Player player, FishProperties fp, ResourceLocation rl, int ticks)
    {
        TeamManagerImpl teamManager = TeamManagerImpl.INSTANCE;
        Optional<Team> teamByID = teamManager.getTeamForPlayerID(player.getUUID());

        if (teamByID.isPresent())
        {
            Set<UUID> members = teamByID.get().getMembers();

            for (UUID uuid : members)
            {
                Player playerByUUID = player.getServer().getPlayerList().getPlayer(uuid);
                if (playerByUUID != null && playerByUUID.getUUID().equals(player.getUUID()))
                {
                    FishCaughtCounter.awardFishCaughtCounter(fp, rl, playerByUUID,
                            ticks, 100, false, false, false, false);
                }
            }
        }
    }
}

