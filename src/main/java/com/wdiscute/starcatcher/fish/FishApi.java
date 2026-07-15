package com.wdiscute.starcatcher.fish;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.compat.QualityFoodCompat;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.items.StarcaughtBucket;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import java.util.ArrayList;
import java.util.List;

public class FishApi
{
    /**
     * Runs all the logic including modifiers to select which FishProperties to catch.
     * The ResourceLocation might be MISSINGNO if the FishProperties was created by a modifier, such as player-written Message-in-a-Bottle
     */
    public static Pair<FishProperties, ResourceLocation> getFP(FishingBobEntity fbe, Player player, List<AbstractCatchModifier> modifiers, ItemStack rod)
    {
        //force select fish from modifiers
        for (AbstractCatchModifier modifier : modifiers)
        {
            Pair<FishProperties, ResourceLocation> force = modifier.forceSelectFish(fbe);
            if (force != null) return force;
        }

        Level level = fbe.level();

        FishProperties fpToFish = null;
        ResourceLocation rlToAwardUponFishingComplete = null;

        List<FishProperties> available = new ArrayList<>();

        modifiers.forEach(o -> o.onReelStart(fbe));

        //if any non-fish (trophies, secrets, extras) is available, select it
        for (FishProperties fp : FishApi.getNonFishes(level))
        {
            int chance = fp.calculateChance(fbe, level, rod, AbstractFishRestriction.Context.FISHING);

            if (chance > 0)
            {
                fpToFish = fp;
                rlToAwardUponFishingComplete = FishApi.getKey(level, fp);
                break;
            }
        }

        //run force select fish modifiers if no non-fish is available (vanilla bobber, creeper hat)
        if (fpToFish == null)
            for (AbstractCatchModifier modifier : modifiers)
            {
                Pair<FishProperties, ResourceLocation> force = modifier.forceSelectFishIfNoNonFishAvailable(fbe);
                if (force != null) return force;
            }

        //add available fish to list if no trophy/secret/extra was available
        for (FishProperties fp : FishApi.getFishes(level))
        {
            int chance = fp.calculateChance(fbe, level, rod, AbstractFishRestriction.Context.FISHING);
            for (int i = 0; i < chance; i++) available.add(fp);
        }

        //trigger modifiers to modify available pool
        for (AbstractCatchModifier acm : modifiers) available = acm.modifyAvailablePool(fbe, available);

        //if no fish is available and no non-fish was selected, reset player fishing data and award nothing
        if (available.isEmpty() && fpToFish == null && player != null)
        {
            player.displayClientMessage(Component.translatable("gui.starcatcher.reel_no_fish"), true);
            return null;
        }

        //get random fish from available pool, if no trophy/secret is selected
        if (fpToFish == null)
        {
            fpToFish = available.get(fbe.getRandom().nextInt(available.size()));
            rlToAwardUponFishingComplete = FishApi.getKey(level, fpToFish);
        }

        return Pair.of(fpToFish, rlToAwardUponFishingComplete);
    }

    /**
     * Calculates the chance of catching the specific FP.
     * Each restriction may add, remove or modify from the base chance.
     * If a restriction returns -9999 that is considered an impossible catch and all remaining restriction checks are ignored.
     * None of the parameters should be passed as null as different restrictions might check for data in those parameters.
     */
    public static int calculateChance(FishProperties fp, Entity entity, Level level, ItemStack rod, AbstractFishRestriction.Context context)
    {
        int chance = fp.baseChance();

//        if(fp.catchInfo().fish().rl().equals(SCItems.OASIS_STURGEON.getId()))
//        {
//            System.out.println("adwa");
//        }

        for (var restriction : fp.restrictions())
        {
            int chanceToAdd = restriction.adjustChance(chance, level, fp, entity, rod, context);
            chance += chanceToAdd;
            //if restriction doesn't allow for fish, skip remaining conditions
            if (chanceToAdd == -9999) break;
        }

        for (var restriction : fp.restrictions())
        {
            int chanceToAdd = restriction.adjustChance(chance, level, fp, entity, rod, context);
            chance += chanceToAdd;
            //if restriction doesn't allow for fish, skip remaining conditions
            if (chanceToAdd == -9999) break;
        }

        return chance;
    }
 
    /**
     * Spawns the fished (item)entity using the FishingBobEntity linked in the player DataAttachment.
     */
    public static void spawnFishFromPlayerFishing(ServerPlayer player, boolean completed, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        ServerLevel level = ((ServerLevel) player.level());

        if (SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).isEmpty()) return;

        Entity levelEntity = level.getEntity(SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).getUuid());
        if (levelEntity instanceof FishingBobEntity fbe)
        {
            //if won minigame
            if (completed)
            {
                FishProperties fp = fbe.fpToFish;

                //todo change this to be a neoforge event

                //trigger criterion
                SCCriterionTriggers.FISH.get().trigger(player, fbe.rlToAwardUponFishingComplete == null ? Starcatcher.MISSINGNO : fbe.rlToAwardUponFishingComplete, fp.rarity(), time, perfectCatch);

                //trigger modifiers
                fbe.modifiers.forEach(m -> m.onSuccessfulMinigameCompletion(fbe, time, completedTreasure, perfectCatch, hits));

                //play sound
                fbe.tackleSkin.onSuccessfulMinigame(player);

                //pick rod percentile
                float percentile = fbe.getRandom().nextFloat() * 100;

                //modify percentile from modifiers
                for (AbstractCatchModifier modifier : fbe.modifiers)
                    percentile = modifier.modifyPercentile(fbe, percentile);

                //golden if any modifier wants (base chance is from default modifiers, 1% + 1% for perfect catch
                boolean golden = FishCaughtCounter.canCatchGolden(fp, player) &&
                                 fbe.modifiers.stream().anyMatch(o ->
                                         o.shouldBeGolden(fbe, time, completedTreasure, perfectCatch, hits));

                //if any modifier cancels golden, then not golden
                if (fbe.modifiers.stream().anyMatch(o -> o.cancelGolden(fbe))) golden = false;

                //award fish counter entry to guide book
                FishCaughtCounter.awardFishCaughtCounter(fbe.fpToFish, fbe.rlToAwardUponFishingComplete,
                        player, time, percentile, perfectCatch, true, golden, !SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught.containsKey(fbe.rlToAwardUponFishingComplete));

                //add score to tournaments
                TournamentHandler.addScore(player, fp, perfectCatch, percentile);

                //award exp
                int exp = fp.rarity().getXp();
                player.giveExperiencePoints(exp);

                //create list of items to fish up
                List<ItemStack> items = new ArrayList<>();

                //check if it can spawn entity
                boolean canSpawnEntity;
                ResourceLocation location = fp.catchInfo().entityToSpawn().getKey().location();
                if (location.getNamespace().equals("starcatcher"))
                    //if entity is from starcatcher, can only spawn if it's a bucketable fish (aka has a model)
                    canSpawnEntity = SCItems.BUCKETABLE_FISHES_REGISTRY.getEntries().stream().map(
                            o -> BuiltInRegistries.ITEM.getKey(o.getDelegate().value())
                    ).anyMatch(rl -> rl.equals(fp.catchInfo().fish().rl()));
                else
                    //if entity is not from starcatcher, then it can spawn if not golden
                    //because the default is starcatcher:fish, meaning if its any other entity, then it can spawn
                    canSpawnEntity = !golden;

                //check if should spawn entity, can catch && anything wants to spawn it
                if (canSpawnEntity &&
                    (
                            fp.catchInfo().alwaysSpawnEntity()
                            || fbe.modifiers.stream().anyMatch(o -> o.forceSpawnEntity(fbe))
                            || ModList.get().isLoaded("fishingreal")
                    )
                )
                {
                    Vec3 pPos = player.position().subtract(fbe.position());

                    double x = pPos.x / 25;
                    double y = pPos.y / 20;
                    double z = pPos.z / 25;

                    x = Math.clamp(x, -1, 1);
                    y = Math.clamp(y, -1, 1);
                    z = Math.clamp(z, -1, 1);

                    x *= 2.5;
                    y *= 2;
                    z *= 2.5;

                    Entity entity = fp.catchInfo().entityToSpawn().value().create(level);

                    if (entity == null)
                    {
                        LogUtils.getLogger().warn("starcatcher doesn't like when the flag or whatever is not enabled");
                        return;
                    }

                    //set fish item if it's a starcatcher fish entity
                    if (entity instanceof FishEntity fe)
                        fe.setFish(makeItemStackNonBucket(fp, percentile, golden, player, perfectCatch));

                    entity.setPos(fbe.position().add(0, 1.2f, 0));

                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    entity.setDeltaMovement(vec3);
                    level.addFreshEntity(entity);

                    //consume bait if not bucket
                    ItemStack bait = SCDataComponents.getOrDefault(fbe.rod, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();
                    if(!bait.is(Tags.Items.BUCKETS_EMPTY))
                    {
                        bait.shrink(1);
                        player.awardStat(SCStats.BAIT_USED.get(), 1);
                        SCDataComponents.set(fbe.rod, SCDataComponents.BAIT, new MaybeStack(bait));
                    }
                }
                //if not entity then add rod item resourceLocation
                else
                {
                    ItemStack is = makeItemStack(fbe.rod, fbe.fpToFish, percentile, golden, player, perfectCatch);

                    if (fbe.modifiers.stream().noneMatch(acm -> acm.shouldSkipAddingBaseItem(fbe, is)))
                        items.add(is);
                }

                //damage rod
                if (SCConfig.ENABLE_ROD_DURABILITY.get())
                {
                    ItemStack rod = null;
                    if (player.getOffhandItem().is(SCTags.RODS)) rod = player.getOffhandItem();
                    if (player.getMainHandItem().is(SCTags.RODS)) rod = player.getMainHandItem();

                    //if rod is found (should never fail!)
                    if (rod != null)
                    {
                        ItemStack bobber = SCDataComponents.getOrDefault(rod, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack();
                        ItemStack bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();
                        ItemStack hook = SCDataComponents.getOrDefault(rod, SCDataComponents.HOOK, MaybeStack.EMPTY).toStack();

                        rod.hurtAndBreak(1, (ServerLevel) player.level(), player, Utils::nothing);

                        //if rod broke, award bobber, bait & hook
                        if (rod.isEmpty())
                        {
                            player.addItem(bobber);
                            player.addItem(bait);
                            player.addItem(hook);
                        }
                    }
                    else
                    {
                        LogUtils.getLogger().warn("Starcatcher couldn't find a rod to decrease durability! Report this to the devs on github or discord");
                    }
                }

                //add items to list from modifiers
                for (AbstractCatchModifier acm : fbe.modifiers)
                    items.addAll(acm.addToFishedItems(fbe, fp, time, perfectCatch, hits, completedTreasure));

                //add treasure
                if (completedTreasure || fbe.modifiers.stream().anyMatch(acm -> acm.forceAwardTreasure(fbe, time, completedTreasure, perfectCatch, hits)))
                {
                    player.awardStat(SCStats.STARCAUGHT_TREASURES.get());
                    items.add(fbe.treasure);
                }

                //fire ItemFishedEvent for mod compat (e.g. PMMO). Throwaway FishingHook only exists to satisfy the event constructor.
                if (!items.isEmpty())
                {
                    FishingHook fakeHook = new FishingHook(player, level, 0, 0);
                    fakeHook.setPos(fbe.position());
                    ItemFishedEvent event = new ItemFishedEvent(items, 0, fakeHook);
                    NeoForge.EVENT_BUS.post(event);
                    if (event.isCanceled()) items.clear();
                    fakeHook.discard();
                }

                //spawn items from list
                for (ItemStack itemStackToSpawn : items)
                {
                    //make ItemEntities for fish item resourceLocation
                    ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, itemStackToSpawn);

                    //assign delta movement so fish flies towards player
                    double x = Math.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                    double y = Math.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                    double z = Math.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    itemFished.setDeltaMovement(vec3);

                    //add item entity to level
                    level.addFreshEntity(itemFished);
                }

            }
            else
            {
                //award stat of fish missed
                player.awardStat(SCStats.STARCAUGHT_FISH_MISSED.get());

                //if fish minigame failed/canceled
                fbe.modifiers.forEach(o -> o.onFailedMinigame(fbe));

                //play sound from tackle skin
                fbe.tackleSkin.onFailedMinigame(player);

                //always consume bait if not bucket (fish don't eat buckets!)
                ItemStack bait = SCDataComponents.getOrDefault(fbe.rod, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();
                if(!bait.is(Tags.Items.BUCKETS_EMPTY))
                {
                    bait.shrink(1);
                    player.awardStat(SCStats.BAIT_USED.get(), 1);
                    SCDataComponents.set(fbe.rod, SCDataComponents.BAIT, new MaybeStack(bait));
                }
            }

            //sync stats to player for guide book
            if(player instanceof ServerPlayer sp)
                sp.getStats().sendStats(sp);

            //kill bobber entity
            fbe.kill();
        }

        SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB.get());
    }


    public static ItemStack makeItemStackNonBucket(FishProperties fp, float percentile,
                                                   boolean golden, Player player, boolean perfectCatch)
    {
        //normal itemstack
        ItemStack fish = fp.catchInfo().fish().toStack();

        //quality food compat
        if (ModList.get().isLoaded("quality_food"))
            QualityFoodCompat.addQuality(fish, player, golden, perfectCatch, percentile);

        //store caught fish info data component
        if (fp.hasGuideEntry() && SCConfig.SAVE_DATA_TO_ITEMS.get() && fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH))
            SCDataComponents.set(fish, SCDataComponents.CAUGHT_FISH_INFO, new CaughtFishInfo(fp.sizeWeight().getSizeForPercentile(percentile), fp.sizeWeight().getWeightForPercentile(percentile), percentile, golden ? Rarity.GOLDEN : fp.rarity()));

        return fish;
    }

    /**
     * Generates the itemstack for fishing taking into account bucketability
     */
    public static ItemStack makeItemStack(ItemStack rod, FishProperties fp, float percentile,
                                          boolean golden, Player player, boolean perfectCatch)
    {
        ItemStack bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();

        //can be bucketed if has bucketed fish in fp, bait has bucket, and is not golden
        boolean canBeBucketed = !fp.catchInfo().bucketedFish().toStack().isEmpty() && bait.is(Tags.Items.BUCKETS_EMPTY) && !golden;

        //always consume bait if not bait is not a bucket
        if(!bait.is(Tags.Items.BUCKETS_EMPTY))
        {
            bait.shrink(1);
            player.awardStat(SCStats.BAIT_USED.get(), 1);
            SCDataComponents.set(rod, SCDataComponents.BAIT, new MaybeStack(bait));
        }

        //if bucketed fish
        if (canBeBucketed)
        {
            //always consume bait (bucket) if bucketed fish
            bait.shrink(1);
            player.awardStat(SCStats.BAIT_USED.get(), 1);
            SCDataComponents.set(rod, SCDataComponents.BAIT, new MaybeStack(bait));

            ItemStack baseFish = fp.catchInfo().fish().toStack();

            CaughtFishInfo caughtFishInfo = new CaughtFishInfo(fp.sizeWeight().getSizeForPercentile(percentile), fp.sizeWeight().getWeightForPercentile(percentile), percentile, golden ? Rarity.GOLDEN : fp.rarity());

            //if starcatcher bucketed fish
            if (baseFish.is(SCTags.BUCKETABLE_FISHES))
            {
                ItemStack bucket = StarcaughtBucket.getBucketForStack(baseFish).getDefaultInstance();

                //quality food compat
                if (ModList.get().isLoaded("quality_food") && SCConfig.SAVE_DATA_TO_ITEMS.get())
                    QualityFoodCompat.addQuality(baseFish, player, golden, perfectCatch, percentile);

                //only save data on fish resourceLocation if config is enabled
                if (SCConfig.SAVE_DATA_TO_ITEMS.get())
                    SCDataComponents.set(baseFish, SCDataComponents.CAUGHT_FISH_INFO, caughtFishInfo);

                SCDataComponents.set(bucket, SCDataComponents.BUCKETED_FISH, new MaybeStack(baseFish));
                return bucket;
            }
            else
                //non starcatcher bucketed fish
                return fp.catchInfo().bucketedFish().toStack().copy();
        }

        //if non bucket fish, make normal itemstack
        return makeItemStackNonBucket(fp, percentile, golden, player, perfectCatch);
    }

    public static ItemStack getTreasure(ServerPlayer player, FishProperties fp, List<AbstractCatchModifier> modifiers)
    {
        Registry<FishProperties> fishProperties = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY);
        Treasure data = fishProperties.wrapAsHolder(fp).getData(SCDataMaps.TREASURE);

        if (data == null) return ItemStack.EMPTY;

        return data.unpack(player, modifiers);
    }

    public static ResourceLocation getKey(Level level, FishProperties fp)
    {
        return level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fp);
    }

    public static List<FishProperties> getFishes(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).stream()
                .filter(o -> o.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH)).toList();
    }

    public static List<FishProperties> getFishes(Level level)
    {
        return getFishes(level.registryAccess());
    }

    public static Registry<FishProperties> getRegistry(Level level)
    {
        return level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY);
    }

    public static FishProperties getFP(RegistryAccess registryAccess, ResourceLocation rl)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).get(rl);
    }

    public static FishProperties getFP(Level level, ResourceLocation rl)
    {
        return getFP(level.registryAccess(), rl);
    }

    public static List<FishProperties> getNonFishes(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).stream()
                .filter(o -> !o.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH)).toList();
    }

    public static List<FishProperties> getNonFishes(Level level)
    {
        return getNonFishes(level.registryAccess());
    }

    public static List<FishProperties> getAllFPs(Level level)
    {
        return getAllFPs(level.registryAccess());
    }

    public static List<FishProperties> getAllFPs(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).stream().toList();
    }

    public static List<FishProperties> getTrophies(Level level)
    {
        return getTrophies(level.registryAccess());
    }

    public static List<FishProperties> getTrophies(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).stream().filter(o -> o.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.TROPHY)).toList();
    }

    public static List<FishProperties> getMessages(Level level)
    {
        return getMessages(level.registryAccess());
    }

    public static List<FishProperties> getMessages(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).stream().filter(o -> o.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.MESSAGE)).toList();
    }
}
