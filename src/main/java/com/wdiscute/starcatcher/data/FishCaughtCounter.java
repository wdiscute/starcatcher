package com.wdiscute.starcatcher.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.FTBTeamsCompat;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.data.network.CBFishCaughtNotifs;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.registry.SCStats;
import com.wdiscute.utils.Utils;
import net.minecraft.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public record FishCaughtCounter(
        int count,
        int fastestTicks,
        float averageTicks,
        float percentile,
        long firstCatch,
        boolean caughtGolden,
        boolean perfectCatch,
        boolean hasGuideNotification
)
{
    public static FishCaughtCounter get(Player player, ResourceLocation loc)
    {
        return FishingGuideAttachment.getFishesCaught(player).get(loc);
    }

    public static boolean canCatchGolden(FishProperties fp, Player player)
    {
        //can't catch golden if it's always entity
        if(fp.catchInfo().alwaysSpawnEntity()) return false;

        //can't catch golden if it has no guide entry
        if(fp.hasGuideEntry()) return false;

        //returns false if player has already caught the golden fish of that fp
        Map<ResourceLocation, FishCaughtCounter> fishesCaught = FishingGuideAttachment.getFishesCaught(player);
        ResourceLocation loc = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKeyOrNull(fp);
        if (!fishesCaught.containsKey(loc)) return true;
        return !fishesCaught.get(loc).caughtGolden;
    }

    public FishCaughtCounter removeNotification()
    {
        return new FishCaughtCounter(this.count, this.fastestTicks, this.averageTicks, this.percentile, this.firstCatch, this.caughtGolden, perfectCatch, false);
    }

    @Nonnull
    public static FishCaughtCounter create(int ticks, float percentile, boolean perfectCatch, boolean golden, boolean hasGuideNotification)
    {
        return new FishCaughtCounter(1, ticks, (float) ticks, percentile, Util.getEpochMillis() / 1000, golden, perfectCatch, hasGuideNotification);
    }

    public FishCaughtCounter getUpdated(int ticks, float percentile, boolean perfectCatch, boolean goldenCatch, boolean hasGuideNotification)
    {
        int fastestToSave = Math.min(this.fastestTicks, ticks);
        float averageToSave = (this.averageTicks * this.count + ticks) / (this.count + 1);
        int countToSave = this.count;
        boolean perfect = perfectCatch || this.perfectCatch;
        boolean golden = goldenCatch || this.caughtGolden;

        //if cheated in, fixes trackers
        if (this.fastestTicks == 0) fastestToSave = ticks;
        if (this.averageTicks == 0) averageToSave = ticks;
        if (this.count == 999999) countToSave = 0;

        float percentileToSave = Math.min(percentile, this.percentile);

        return new FishCaughtCounter(
                countToSave + 1,
                fastestToSave,
                averageToSave,
                percentileToSave,
                this.firstCatch,
                golden,
                perfect,
                hasGuideNotification);
    }

    public static void awardFishCaughtCounter(FishProperties fpCaught, @Nullable ResourceLocation rl, Player player,
                                              int ticks, float percentile,
                                              boolean perfectCatch, boolean awardToTeam, boolean golden, boolean displayToast)
    {
        //award stat
        boolean isFish = fpCaught.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH);
        if(isFish)
        {
            player.awardStat(Stats.FISH_CAUGHT);
            player.awardStat(SCStats.STARCAUGHT_FISH.get());

            switch (fpCaught.rarity())
            {
                case TRASH -> player.awardStat(SCStats.TRASH_CAUGHT.get());
                case COMMON -> player.awardStat(SCStats.COMMON_CAUGHT.get());
                case UNCOMMON -> player.awardStat(SCStats.UNCOMMON_CAUGHT.get());
                case RARE -> player.awardStat(SCStats.RARE_CAUGHT.get());
                case EPIC -> player.awardStat(SCStats.EPIC_CAUGHT.get());
                case LEGENDARY -> player.awardStat(SCStats.LEGENDARY_CAUGHT.get());
            }

            if(golden)
                player.awardStat(SCStats.GOLDEN_CATCHES.get());

            if(perfectCatch)
                player.awardStat(SCStats.PERFECT_CATCHES.get());

            //update stats on client
            if(player instanceof ServerPlayer sp)
                sp.getStats().sendStats(sp);
        }

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = FishingGuideAttachment.getFishesCaught(player);

        //if rl param is null, get it from fp from registry
        ResourceLocation loc = rl == null ? player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKeyOrNull(fpCaught) : rl;
        //if fp/rl is valid
        if (loc != null)
        {
            FishCaughtCounter fishCaughtCounter = fishesCaught.get(loc);
            boolean newFish = fishCaughtCounter == null;

            //ftb teams compat to share fishes caught to team, does not share size and weight
            if (ModList.get().isLoaded("ftbteams") && awardToTeam && SCConfig.ENABLE_FTB_TEAM_SHARING.get())
                FTBTeamsCompat.awardToTeam(player, fpCaught, rl, ticks);

            if (newFish)
                fishCaughtCounter = FishCaughtCounter.create(ticks, percentile, perfectCatch, golden, isFish && fpCaught.hasGuideEntry());
            else
                fishCaughtCounter = fishCaughtCounter.getUpdated(ticks, percentile, perfectCatch, golden, isFish && fpCaught.hasGuideEntry());

            fishesCaught.put(loc, fishCaughtCounter);

            //send packet to client to display message above exp bar and fish caught toast, unless it alwaysSpawnEntity() (where sw and caught doesn't make sense)
            if (!fpCaught.catchInfo().alwaysSpawnEntity() && fpCaught.hasGuideEntry())
                PacketDistributor.sendToPlayer(((ServerPlayer) player), new CBFishCaughtNotifs(fpCaught, displayToast && newFish, percentile, golden));

            FishingGuideAttachment.setFishesCaught(player, fishesCaught);
        }
    }

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::count),
                    Codec.INT.optionalFieldOf("fastest_ticks", 0).forGetter(FishCaughtCounter::fastestTicks),
                    Codec.FLOAT.optionalFieldOf("average_ticks", 0.0f).forGetter(FishCaughtCounter::averageTicks),
                    Codec.FLOAT.optionalFieldOf("best_percentile", 0f).forGetter(FishCaughtCounter::percentile),
                    Codec.LONG.optionalFieldOf("first_catch", 0L).forGetter(FishCaughtCounter::firstCatch),
                    Codec.BOOL.optionalFieldOf("caught_golden", false).forGetter(FishCaughtCounter::caughtGolden),
                    Codec.BOOL.optionalFieldOf("perfect_catch", false).forGetter(FishCaughtCounter::perfectCatch),
                    Codec.BOOL.optionalFieldOf("has_guide_notification", false).forGetter(FishCaughtCounter::hasGuideNotification)

            ).apply(instance, FishCaughtCounter::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishCaughtCounter> STREAM_CODEC = ExtraComposites.composite(
            ByteBufCodecs.VAR_INT, FishCaughtCounter::count,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::fastestTicks,
            ByteBufCodecs.FLOAT, FishCaughtCounter::averageTicks,
            ByteBufCodecs.FLOAT, FishCaughtCounter::percentile,
            ByteBufCodecs.VAR_LONG, FishCaughtCounter::firstCatch,
            ByteBufCodecs.BOOL, FishCaughtCounter::caughtGolden,
            ByteBufCodecs.BOOL, FishCaughtCounter::perfectCatch,
            ByteBufCodecs.BOOL, FishCaughtCounter::hasGuideNotification,

            FishCaughtCounter::new
    );
}
