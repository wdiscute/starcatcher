package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import it.hurts.sskirillss.relics.api.relics.IRelicItem;
import it.hurts.sskirillss.relics.api.relics.data.AbilityData;
import it.hurts.sskirillss.relics.api.relics.data.RelicData;
import it.hurts.sskirillss.relics.utils.EntityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ReliquifiedArtifactsCompat
{
    private static Item getHatItem()
    {
        return BuiltInRegistries.ITEM.get(U.rl("artifacts", "anglers_hat"));
    }

    public static ItemStack getEquippedHatStack(Player player)
    {
        ItemStack stack = EntityUtils.findEquippedCurio(player, getHatItem());
        if (stack.isEmpty() || !(stack.getItem() instanceof IRelicItem)) return ItemStack.EMPTY;
        return stack;
    }

    public static boolean isWearingAnglersHat(Player player)
    {
        return !getEquippedHatStack(player).isEmpty();
    }

    private static AbilityData getCatchAbility(Player player, ItemStack stack)
    {
        IRelicItem relic = (IRelicItem) stack.getItem();
        RelicData relicData = relic.getRelicData(player, stack);
        return relicData.getAbilitiesData().getAbilityData("catch");
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

    public static void attemptBonusCatch(ServerPlayer player, FishingBobEntity fbe, FishProperties originalCatch)
    {
        ItemStack stack = getEquippedHatStack(player);
        if (stack.isEmpty()) return;

        AbilityData ability = getCatchAbility(player, stack);
        double chance = ability.getStatData("chance").getValue();
        int maxCasts = (int) Math.round(ability.getStatData("max_casts").getValue());

        int bonusCount = MathUtils.multicast(player.getRandom(), chance, maxCasts);
        if (bonusCount <= 0) return;

        ServerLevel level = (ServerLevel) player.level();

        //build the same weighted pool as reel()
        List<FishProperties> available = new ArrayList<>();
        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            int ch = fp.calculateChance(fbe, level, fbe.rod, AbstractFishRestriction.Context.FISHING);
            for (int i = 0; i < ch; i++)
            {
                available.add(fp);
            }
        }

        if (available.isEmpty()) return;

        for (int b = 0; b < bonusCount; b++)
        {
            //pick a random bonus fish
            FishProperties bonusFp = available.get(U.r.nextInt(available.size()));

            //generate size and weight
            float percentile = U.r.nextFloat(100);
            int size = FishProperties.SizeAndWeight.getRandomSize(bonusFp, percentile);
            int weight = FishProperties.SizeAndWeight.getRandomWeight(bonusFp, percentile);

            //award fish counter
            FishCaughtCounter.awardFishCaughtCounter(bonusFp, player, 0, size, weight, percentile, false, true, false);

            //award starcatcher xp
            player.giveExperiencePoints(bonusFp.rarity().getXp());

            //spawn bonus fish item using the same method as the main catch
            ItemStack is = FishProperties.makeItemStack(fbe.rod, bonusFp, size, weight, percentile, false, player, false);

            ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, is);
            double x = Math.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
            double y = Math.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
            double z = Math.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
            Vec3 vec3 = new Vec3(x, 0.7 + y, z);
            itemFished.setDeltaMovement(vec3);
            level.addFreshEntity(itemFished);
        }

        //award relic xp for bonus catches
        IRelicItem relic = (IRelicItem) stack.getItem();
        relic.getRelicData(player, stack).getLevelingData().addExperience("catch", "catch", bonusCount);
    }

    public static void awardRelicXP(Player player, boolean gotTreasure)
    {
        ItemStack stack = getEquippedHatStack(player);
        if (stack.isEmpty()) return;

        IRelicItem relic = (IRelicItem) stack.getItem();
        RelicData relicData = relic.getRelicData(player, stack);

        //primary catch xp
        relicData.getLevelingData().addExperience("catch", "catch", 1.0);

        //treasure catch xp (only if hat has treasure rank unlocked)
        if (gotTreasure)
        {
            AbilityData ability = relicData.getAbilitiesData().getAbilityData("catch");
            if (ability.isRankModifierUnlocked("treasure"))
            {
                relicData.getLevelingData().addExperience("catch", "treasure_catch", 1.0);
            }
        }
    }
}
