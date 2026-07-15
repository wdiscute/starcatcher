package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.WeightedStack;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class AbstractCatchModifier implements Modifier
{
    final String translationOverride;

    protected AbstractCatchModifier(String translationOverride)
    {
        this.translationOverride = translationOverride;
    }

    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        return List.of(Component.translatable("tooltip.modifier." + getIdentifier().toLanguageKey()));
    }

    public final List<Component> getDescription(boolean shift)
    {
        if(translationOverride.equals("hide")) return List.of();

        if(translationOverride.isEmpty())
        {
            return getNonOverriddenDescription(shift);
        }

        return List.of(Component.translatable(translationOverride));
    }

    public void onAdd(FishingBobEntity fishingBobEntity)
    {
    }

    public int adjustMinTicksToFish(FishingBobEntity fishingBobEntity, int minTicksToFish)
    {
        return minTicksToFish;
    }

    public int adjustMaxTicksToFish(FishingBobEntity fishingBobEntity, int maxTicksToFish)
    {
        return maxTicksToFish;
    }

    public float adjustChanceToFishEachTick(FishingBobEntity fishingBobEntity, float chanceToFishEachTick)
    {
        return chanceToFishEachTick;
    }

    public boolean survivesLava(FishingBobEntity fbe)
    {
        return false;
    }

    public void onReelStart(FishingBobEntity fbe)
    {

    }

    public List<FishProperties> modifyAvailablePool(FishingBobEntity fbe, List<FishProperties> available)
    {
        return available;
    }

    public boolean forceSkipMinigame(FishingBobEntity fbe)
    {
        return false;
    }

    public boolean shouldStopFishing(FishingBobEntity fbe)
    {
        return false;
    }

    public boolean forceSpawnEntity(FishingBobEntity fbe)
    {
        return false;
    }

    public void onFailedMinigame(FishingBobEntity fbe)
    {

    }

    public void onSuccessfulMinigameCompletion(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
    }

    public boolean forceAwardTreasure(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        return false;
    }

    public boolean shouldBeGolden(FishingBobEntity fbe, int time, boolean treasure, boolean perfect, int hits)
    {
        return false;
    }

    public boolean cancelGolden(FishingBobEntity fbe)
    {
        return false;
    }

    public boolean shouldSkipAddingBaseItem(FishingBobEntity fbe, ItemStack is)
    {
        return false;
    }

    public List<ItemStack> addToFishedItems(FishingBobEntity fbe, FishProperties fp, int time, boolean perfectCatch, int hits, boolean completedTreasure)
    {
        return List.of();
    }

    public ItemStack modifyTreasure(FishingBobEntity fbe, ItemStack originalTreasure, FishProperties fp)
    {
        return originalTreasure;
    }

    public boolean shouldHideCatch(FishingBobEntity fbe)
    {
        return false;
    }

    public float modifyPercentile(FishingBobEntity fbe, float percentile)
    {
        return percentile;
    }

    public boolean noGravity(FishingBobEntity fbe)
    {
        return false;
    }

    public Vec3 modifyThrowVec(FishingBobEntity fbe, Vec3 vec3)
    {
        return vec3;
    }

    public Pair<FishProperties, ResourceLocation> forceSelectFish(FishingBobEntity fbe)
    {
        return null;
    }

    public Pair<FishProperties, ResourceLocation> forceSelectFishIfNoNonFishAvailable(FishingBobEntity fbe)
    {
        return null;
    }

    //no access to bobber entity
    public List<ItemStack> addToTreasureWeightedList()
    {
        return List.of();
    }
}
