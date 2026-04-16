package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.U;
import de.cadentem.quality_food.core.Modification;
import de.cadentem.quality_food.core.Quality;
import de.cadentem.quality_food.util.QualityUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class QualityFoodCompat
{
    public static void addQuality(ItemStack itemStack, Player player, Level level, boolean golden, boolean perfectCatch, float percentile)
    {
        percentile = percentile / 100;
        Quality selected = null;

        for (Quality type : Quality.values())
        {
            if (selected == null || type.level() > selected.level())
            {
                double chance = U.r.nextDouble();
                chance = Modification.luck(player).apply(chance);

                if(golden) chance = 999;

                chance = Math.max((1 - percentile), chance);

                //rolls for chance 3 more times
                if(perfectCatch)
                {
                    chance = Math.max(U.r.nextDouble(), chance);
                    chance = Math.max(U.r.nextDouble(), chance);
                    chance = Math.max(U.r.nextDouble(), chance);
                    chance = Math.max(U.r.nextDouble(), chance);
                    chance = Math.max(U.r.nextDouble(), chance);
                }

                //Todo: fix this
              //  if (chance >= (double) 1.0F - type.chance())
              //      selected = type;
            }
        }

        if (selected != null)
        {
            QualityUtils.applyQuality(itemStack, selected);
        }
    }
}
