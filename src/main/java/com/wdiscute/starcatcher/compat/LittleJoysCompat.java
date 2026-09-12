package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.blay09.mods.littlejoys.api.LittleJoysAPI;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.fml.ModList;

public class LittleJoysCompat
{
    public static boolean isLoaded()
    {
        return ModList.get().isLoaded("littlejoys");
    }

    public static boolean isOnFishingSpot(FishingBobEntity fbe)
    {
        if (fbe.level() instanceof ServerLevel sl)
        {
            return LittleJoysAPI
                    .findFishingSpot(sl, fbe.blockPosition())
                    .map(pos -> pos.distManhattan(fbe.blockPosition()) < 2)
                    .orElse(false);
        }
        return false;
    }
}
