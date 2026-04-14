package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.FishProperties;
import it.hurts.sskirillss.relics.api.relics.IRelicItem;
import it.hurts.sskirillss.relics.api.relics.data.AbilityData;
import it.hurts.sskirillss.relics.api.relics.data.RelicData;
import it.hurts.sskirillss.relics.api.relics.data.RelicMetricData;
import it.hurts.sskirillss.relics.api.relics.data.RelicStatisticData;
import it.hurts.sskirillss.relics.utils.EntityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReliquifiedArtifactsCompat
{
    private static Item getHatItem()
    {
        return BuiltInRegistries.ITEM.get(U.rl("artifacts", "anglers_hat"));
    }

    private static ItemStack getEquippedHatStack(Player player)
    {
        ItemStack stack = EntityUtils.findEquippedCurio(player, getHatItem());
        if (stack.isEmpty() || !(stack.getItem() instanceof IRelicItem)) return ItemStack.EMPTY;
        return stack;
    }

    private static AbilityData getCatchAbility(Player player, ItemStack stack)
    {
        IRelicItem relic = (IRelicItem) stack.getItem();
        return relic.getRelicData(player, stack).getAbilitiesData().getAbilityData("catch");
    }

    public static boolean shouldAwardBonusTreasure(Player player)
    {
        ItemStack stack = getEquippedHatStack(player);
        if (stack.isEmpty()) return false;

        AbilityData ability = getCatchAbility(player, stack);
        if (!ability.isRankModifierUnlocked("treasure")) return false;

        double treasureChance = ability.getStatData("treasure_chance").getValue();
        return U.r.nextFloat() < treasureChance;
    }

    public static List<ItemStack> getBonusCatchItems(Player player, FishingBobEntity fbe)
    {
        ItemStack stack = getEquippedHatStack(player);
        if (stack.isEmpty()) return List.of();

        AbilityData ability = getCatchAbility(player, stack);
        double chance = ability.getStatData("chance").getValue();
        int maxCasts = (int) Math.round(ability.getStatData("max_casts").getValue());

        int bonusCount = MathUtils.multicast(player.getRandom(), chance, maxCasts);
        if (bonusCount <= 0) return List.of();

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < bonusCount; i++)
        {
            float percentile = U.r.nextFloat(100);
            int size = FishProperties.SizeAndWeight.getRandomSize(fbe.fpToFish, percentile);
            int weight = FishProperties.SizeAndWeight.getRandomWeight(fbe.fpToFish, percentile);
            ItemStack is = FishProperties.makeItemStack(ItemStack.EMPTY, fbe.fpToFish, size, weight, percentile, false, player, false);
            items.add(is);
        }

        //award relic xp for bonus catches
        IRelicItem relic = (IRelicItem) stack.getItem();
        relic.getRelicData(player, stack).getLevelingData().addExperience("catch", "catch", bonusCount);

        return items;
    }

    public static void awardRelicXP(Player player, boolean gotTreasure)
    {
        ItemStack stack = getEquippedHatStack(player);
        if (stack.isEmpty()) return;

        IRelicItem relic = (IRelicItem) stack.getItem();
        RelicData relicData = relic.getRelicData(player, stack);

        relicData.getLevelingData().addExperience("catch", "catch", 1.0);
        AbilityData ability = relicData.getAbilitiesData().getAbilityData("catch");
        ability.getStatisticData().getMetricData("fish_caught").addValue(1);

        if (gotTreasure)
        {
            if (ability.isRankModifierUnlocked("treasure"))
            {
                relicData.getLevelingData().addExperience("catch", "treasure_catch", 1.0);
                ability.getStatisticData().getMetricData("treasures_caught").addValue(1);
            }
        }
    }
}
