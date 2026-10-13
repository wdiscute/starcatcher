package com.wdiscute.starcatcher.compat;

import de.cadentem.quality_food.config.QualityConfig;
import de.cadentem.quality_food.core.Modification;
import de.cadentem.quality_food.core.Quality;
import de.cadentem.quality_food.util.QualityUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class QualityFoodCompat
{
    public static void addQuality(ItemStack itemStack, Player player, double chance)
    {
        Quality selected = Quality.NONE;

        for (Quality quality : Quality.values())
        {
            if (quality.level() > selected.level())
            {
                chance = Modification.luck(player).apply(chance);

                double qualityChance = (double) 1.0F - QualityConfig.getChance(quality);
                if (chance >= qualityChance)
                    selected = quality;
            }
        }

        if (selected != Quality.NONE)
        {
            QualityUtils.applyQuality(itemStack, selected);
        }
    }

    public static boolean isLoaded()
    {
        return ModList.get().isLoaded("quality_food");
    }
}
