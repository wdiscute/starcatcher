package com.wdiscute.starcatcher.compat;

import de.cadentem.quality_food.core.Modification;
import de.cadentem.quality_food.core.codecs.QualityType;
import de.cadentem.quality_food.registry.QFComponents;
import de.cadentem.quality_food.util.QualityUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.List;

public class QualityFoodCompat
{
    public static void addQuality(ItemStack itemStack, Player player, double chance)
    {
        Holder<QualityType> selected = null;

        for (Holder<QualityType> type : player.level().registryAccess().registryOrThrow(QFComponents.QUALITY_TYPE_REGISTRY).holders().toList())
        {
            if (selected == null || type.value().level() > selected.value().level())
            {
                chance = Modification.luck(player).apply(chance);

                double qualityChance = (double) 1.0F - type.value().chance();
                if (chance >= qualityChance)
                    selected = type;
            }
        }

        if (selected != null)
        {
            QualityUtils.applyQuality(itemStack, selected);
        }
    }

    public static boolean isLoaded()
    {
        return ModList.get().isLoaded("quality_food");
    }

    public static void removeQuality(List<ItemStack> items)
    {
        for (ItemStack item : items)
        {
            QualityUtils.hasQuality(item);
                item.remove(QFComponents.QUALITY_DATA_COMPONENT);
        }
    }
}
