package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ExtraItemsModifier extends AbstractCatchModifier
{
    final int count;

    public ExtraItemsModifier(int count)
    {
        this.count = count;
    }

    @Override
    public List<ItemStack> addToFishedItems(int time, boolean perfectCatch, int hits, boolean completedTreasure, Player player)
    {
        if(!instance.fpToFish.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH)) return List.of();
        if (instance.fpToFish.catchInfo().alwaysSpawnEntity() ||
                ModList.get().isLoaded("fishingreal") ||
                instance.modifiers.stream().anyMatch(AbstractCatchModifier::forceSpawnEntity) ||
                !instance.fpToFish.hasGuideEntry()) return List.of();

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            //pick size, weight and golden
            float percentile = U.r.nextFloat(100);
            int size = FishProperties.SizeAndWeight.getRandomSize(instance.fpToFish, percentile);
            int weight = FishProperties.SizeAndWeight.getRandomWeight(instance.fpToFish, percentile);
            ItemStack stack = FishProperties.makeItemStack(ItemStack.EMPTY, instance.fpToFish, size, weight, percentile, false, player, perfectCatch);
            items.add(stack);
        }
        return items;
    }
}
