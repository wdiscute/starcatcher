package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.U;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class ArtifactsCompat
{
    private static final ResourceLocation ANGLERS_HAT = U.rl("artifacts", "anglers_hat");

    public static boolean isWearingAnglersHat(Player player)
    {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> !handler.findCurios(stack ->
                        BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(ANGLERS_HAT))
                        .isEmpty())
                .orElse(false);
    }

    public static int adjustLureMin(int current)
    {
        return Math.max(current - 20, 1);
    }

    public static int adjustLureMax(int current)
    {
        return Math.max(current - 60, 1);
    }

    public static float adjustLureChance(float current)
    {
        return Math.min(current + 0.01f, 1.0f);
    }
}
