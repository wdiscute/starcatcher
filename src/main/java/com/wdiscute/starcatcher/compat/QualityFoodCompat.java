package com.wdiscute.starcatcher.compat;

import com.wdiscute.utils.Utils;
import de.cadentem.quality_food.core.Modification;
import de.cadentem.quality_food.core.codecs.QualityType;
import de.cadentem.quality_food.registry.QFComponents;
import de.cadentem.quality_food.util.QualityUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class QualityFoodCompat
{
    public static void addQuality(ItemStack itemStack, Player player, boolean golden, boolean perfectCatch, float percentile)
    {
        percentile = percentile / 100;
        Holder<QualityType> selected = null;

        for (Holder<QualityType> type : player.level().registryAccess().registryOrThrow(QFComponents.QUALITY_TYPE_REGISTRY).holders().toList())
        {
            if (selected == null || type.value().level() > selected.value().level())
            {
                double chance = Utils.r.nextDouble();
                chance = Modification.luck(player).apply(chance);

                if(golden) chance = 999;

                chance = Math.max((1 - percentile), chance);

                //rolls for weight 3 more times
                if(perfectCatch)
                {
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                    chance = Math.max(Utils.r.nextDouble(), chance);
                }

                if (chance >= (double) 1.0F - type.value().chance())
                    selected = type;
            }
        }

        if (selected != null)
        {
            QualityUtils.applyQuality(itemStack, selected);
        }
    }
}
