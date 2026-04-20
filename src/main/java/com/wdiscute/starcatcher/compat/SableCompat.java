package com.wdiscute.starcatcher.compat;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

public class SableCompat {
    public static boolean isLoaded() {
        return ModList.get().isLoaded("sable");
    }


    public static double getPlayerX(Player player, BlockPos blockPos) {
        final SubLevel subLevel = Sable.HELPER.getContaining(player.level(), blockPos);

        if (subLevel != null) {
            return subLevel.logicalPose().transformPositionInverse(player.getEyePosition()).x();
        }

        return player.getX();
    }

    public static double getPlayerZ(Player player, BlockPos blockPos) {
        final SubLevel subLevel = Sable.HELPER.getContaining(player.level(), blockPos);

        if (subLevel != null) {
            return subLevel.logicalPose().transformPositionInverse(player.getEyePosition()).z();
        }

        return player.getZ();
    }
}
