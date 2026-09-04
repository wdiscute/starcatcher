package com.wdiscute.starcatcher.compat;

import com.wdiscute.utils.Utils;
import de.cadentem.quality_food.config.QualityConfig;
import de.cadentem.quality_food.config.ServerConfig;
import de.cadentem.quality_food.core.Modification;
import de.cadentem.quality_food.core.Quality;
import de.cadentem.quality_food.util.QualityUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class QualityFoodCompat
{
    public static void addQuality(ItemStack itemStack, Player player, boolean golden, boolean perfectCatch, float percentile)
    {
        percentile = percentile / 100;
        Quality selected = Quality.NONE;

        for (Quality quality : Quality.values())
        {
            if (quality.level() != 0 && quality.level() <= ServerConfig.MAX_NATURAL_HARVEST_QUALITY.get().level())
            {
                double chance = Utils.r.nextDouble();
                chance = Modification.luck(player).apply(chance);

                if (golden) chance = 999;

                chance = Math.max((1 - percentile), chance);

                //rolls for weight 3 (5 lol) more times
                if (perfectCatch)
                {
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                }

                if (chance > (double) 0.0F && chance >= Utils.r.nextFloat())
                    selected = quality;
            }
        }

        QualityUtils.applyQuality(itemStack, selected);
    }
}
