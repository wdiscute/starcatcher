package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.compat.ReliquifiedArtifactsCompat;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.List;

public class AnglersHatModifier extends AbstractCatchModifier
{
    @Override
    public boolean forceAwardTreasure(com.wdiscute.starcatcher.bobberentity.FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        if (completedTreasure) return false;
        if (ModList.get().isLoaded("reliquified_artifacts"))
        {
            return ReliquifiedArtifactsCompat.shouldAwardBonusTreasure(instance.player);
        }
        return false;
    }

    @Override
    public List<ItemStack> addToFishedItems(int time, boolean perfectCatch, int hits, boolean completedTreasure, Player player)
    {
        if (instance.fpToFish.catchInfo().alwaysSpawnEntity() ||
                instance.modifiers.stream().anyMatch(AbstractCatchModifier::forceSpawnEntity) ||
                !instance.fpToFish.hasGuideEntry() || instance.fpToFish.catchInfo().fishEntryType()
                .equals(FishProperties.CatchInfo.FishEntryType.FISH)) return List.of();

        if (ModList.get().isLoaded("reliquified_artifacts"))
        {
            return ReliquifiedArtifactsCompat.getBonusCatchItems(player, instance);
        }
        return List.of();
    }

    @Override
    public void onSuccessfulMinigameCompletion(ServerPlayer player, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        if (ModList.get().isLoaded("reliquified_artifacts"))
        {
            ReliquifiedArtifactsCompat.awardRelicXP(player, completedTreasure);
        }
    }

    //todo tf was tyson trying to do??????
//    @Override
//    public int adjustMinTicksToFish(int minTicksToFish)
//    {
//        if (!ModList.get().isLoaded("reliquified_artifacts"))
//        {
//            return (int) (minTicksToFish * 0.8f);
//        }
//        return minTicksToFish;
//    }
//
//    @Override
//    public int adjustMaxTicksToFish(int maxTicksToFish)
//    {
//        if (!ModList.get().isLoaded("reliquified_artifacts"))
//        {
//            return (int) (maxTicksToFish * 0.8f);
//        }
//        return maxTicksToFish;
//    }
//
//    @Override
//    public float adjustChanceToFishEachTick(float chanceToFishEachTick)
//    {
//        if (!ModList.get().isLoaded("reliquified_artifacts"))
//        {
//            return chanceToFishEachTick * 1.2f;
//        }
//        return chanceToFishEachTick;
//    }
}
