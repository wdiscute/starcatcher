package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.FishProperties;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FTBTeamsCompat
{
    private static final Logger log = LoggerFactory.getLogger(FTBTeamsCompat.class);

    public static void awardToTeam(Player player, FishProperties fp)
    {
        TeamManagerImpl teamManager = TeamManagerImpl.INSTANCE;
        Optional<Team> teamByID = teamManager.getTeamForPlayerID(player.getUUID());

        if (teamByID.isPresent())
        {
            Set<UUID> members = teamByID.get().getMembers();

            for (UUID uuid : members)
            {
                Player playerByUUID = ((ServerPlayer) player).level().getServer().getPlayerList().getPlayer(uuid);
                if (playerByUUID != null)
                {
                    FishCaughtCounter.awardFishCaughtCounter(fp, playerByUUID, 0, 0, 0, 0, false, false, false);
                }
            }
        }
    }
}

