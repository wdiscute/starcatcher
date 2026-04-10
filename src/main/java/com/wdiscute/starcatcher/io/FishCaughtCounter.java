package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.compat.FTBTeamsCompat;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.FishCaughtPayload;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.Map;

public record FishCaughtCounter(
        int count,
        int fastestTicks,
        float averageTicks,
        int size,
        int weight,
        float percentile,
        long firstCatch,
        boolean caughtGolden,
        boolean perfectCatch,
        boolean hasGuideNotification
)
{

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::count),
                    Codec.INT.optionalFieldOf("fastest_ticks", 0).forGetter(FishCaughtCounter::fastestTicks),
                    Codec.FLOAT.optionalFieldOf("average_ticks", 0.0f).forGetter(FishCaughtCounter::averageTicks),
                    Codec.INT.optionalFieldOf("best_size", 0).forGetter(FishCaughtCounter::size),
                    Codec.INT.optionalFieldOf("best_weight", 0).forGetter(FishCaughtCounter::weight),
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
            ByteBufCodecs.INT, FishCaughtCounter::size,
            ByteBufCodecs.INT, FishCaughtCounter::weight,
            ByteBufCodecs.FLOAT, FishCaughtCounter::percentile,
            ByteBufCodecs.VAR_LONG, FishCaughtCounter::firstCatch,
            ByteBufCodecs.BOOL, FishCaughtCounter::caughtGolden,
            ByteBufCodecs.BOOL, FishCaughtCounter::perfectCatch,
            ByteBufCodecs.BOOL, FishCaughtCounter::hasGuideNotification,

            FishCaughtCounter::new
    );

    public static FishCaughtCounter get(Player player, FishProperties loc)
    {
        return get(player, U.getRlFromFp(player.level(), loc));
    }

    public static FishCaughtCounter get(Player player, ResourceLocation loc)
    {
        return FishingGuideAttachment.getFishesCaught(player).get(loc);
    }

    public static FishCaughtCounter createHacked()
    {
        return new FishCaughtCounter(999999, 0, 0, 0, 0, 0, 0, false, false, true);
    }

    public static boolean canCatchGolden(FishProperties fp, ServerPlayer player)
    {
        //returns false if player has already caught the golden fish of that fp
        Map<ResourceLocation, FishCaughtCounter> fishesCaught = FishingGuideAttachment.getFishesCaught(player);
        ResourceLocation loc = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKeyOrNull(fp);
        if (!fishesCaught.containsKey(loc)) return true;
        return !fishesCaught.get(loc).caughtGolden;
    }

    public FishCaughtCounter removeNotification()
    {
        return new FishCaughtCounter(this.count, this.fastestTicks, this.averageTicks, this.size, this.weight, this.percentile, this.firstCatch, this.caughtGolden, perfectCatch, false);
    }

    @Nonnull
    public static FishCaughtCounter create(int ticks, int size, int weight, float percentile, boolean perfectCatch, boolean golden, boolean hasGuideNotification)
    {
        return new FishCaughtCounter(1, ticks, (float) ticks, size, weight, percentile, U.getTime(), golden, perfectCatch, hasGuideNotification);
    }

    public FishCaughtCounter getUpdated(int ticks, int size, int weight, float percentile, boolean perfectCatch, boolean goldenCatch, boolean hasGuideNotification)
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

        int sizeToSave = Math.max(size, this.size);
        int weightToSave = Math.max(weight, this.weight);
        float percentileToSave = Math.max(percentile, this.percentile);

        return new FishCaughtCounter(
                countToSave + 1,
                fastestToSave,
                averageToSave,
                sizeToSave,
                weightToSave,
                percentileToSave,
                this.firstCatch,
                golden,
                perfect,
                hasGuideNotification);
    }

    public static void awardFishCaughtCounter(FishProperties fpCaught, Player player, int ticks, int size, int weight,
                                              float percentile, boolean perfectCatch, boolean awardToTeam, boolean golden)
    {
        awardFishCaughtCounter(fpCaught, null, player, ticks, size, weight, percentile, perfectCatch, awardToTeam, golden);
    }

    public static void awardFishCaughtCounter(FishProperties fpCaught, ResourceLocation rl, Player player, int ticks, int size, int weight, float percentile, boolean perfectCatch, boolean awardToTeam, boolean golden)
    {
        //ftb teams compat to share fishes caught to team, does not share size and weight
        if (ModList.get().isLoaded("ftbteams") && awardToTeam && SCConfig.ENABLE_FTB_TEAM_SHARING.get())
        {
            FTBTeamsCompat.awardToTeam(player, fpCaught);
            return;
        }

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = FishingGuideAttachment.getFishesCaught(player);

        ResourceLocation loc = rl == null ? player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKeyOrNull(fpCaught) : rl;
        if (loc != null)
        {
            FishCaughtCounter fishCaughtCounter = fishesCaught.get(loc);
            boolean newFish = fishCaughtCounter == null;

            if (newFish)
                fishCaughtCounter = FishCaughtCounter.create(ticks, size, weight, percentile, perfectCatch, golden, fpCaught.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH));
            else
                fishCaughtCounter = fishCaughtCounter.getUpdated(ticks, size, weight, percentile, perfectCatch, golden, fpCaught.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH));

            fishesCaught.put(loc, fishCaughtCounter);

            //send packet to client to display message above exp bar and fish caught toast, unless it alwaysSpawnEntity() (where sw and caught doesn't make sense)
            if (!fpCaught.catchInfo().alwaysSpawnEntity() && fpCaught.hasGuideEntry())
                PacketDistributor.sendToPlayer(((ServerPlayer) player), new FishCaughtPayload(fpCaught, newFish, size, weight, percentile));

            FishingGuideAttachment.setFishesCaught(player, fishesCaught);
        }


    }

}
