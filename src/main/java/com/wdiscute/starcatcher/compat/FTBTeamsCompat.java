package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class FTBTeamsCompat
{
    public static void awardToTeam(Player player, FishProperties fp, Identifier rl, int ticks)
    {
//        TeamManagerImpl teamManager = TeamManagerImpl.INSTANCE;
//        Optional<Team> teamByID = teamManager.getTeamForPlayerID(player.getUUID());
//
//        if (teamByID.isPresent())
//        {
//            Set<UUID> members = teamByID.get().getMembers();
//
//            for (UUID uuid : members)
//            {
//                Player playerByUUID = player.level().getServer().getPlayerList().getPlayer(uuid);
//                if (playerByUUID != null && playerByUUID.getUUID().equals(player.getUUID()))
//                {
//                    FishCaughtCounter.awardFishCaughtCounter(fp, rl, playerByUUID,
//                            ticks, 100, false, false, false, false);
//                }
//            }
//        }
    }
}

