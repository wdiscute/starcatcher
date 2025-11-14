package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record FishCaughtCounter(
        FishProperties fp,
        int count,
        int fastestTicks,
        float averageTicks,
        int size,
        int weight,
        boolean caughtGolden
)
{

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.fieldOf("fps").forGetter(FishCaughtCounter::fp),
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::count),
                    Codec.INT.optionalFieldOf("fastest_ticks", 0).forGetter(FishCaughtCounter::fastestTicks),
                    Codec.FLOAT.optionalFieldOf("average_ticks", 0.0f).forGetter(FishCaughtCounter::averageTicks),
                    Codec.INT.optionalFieldOf("best_size", 0).forGetter(FishCaughtCounter::size),
                    Codec.INT.optionalFieldOf("best_weight", 0).forGetter(FishCaughtCounter::weight),
                    Codec.BOOL.optionalFieldOf("caught_golden", false).forGetter(FishCaughtCounter::caughtGolden)
            ).apply(instance, FishCaughtCounter::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishCaughtCounter> STREAM_CODEC = ExtraComposites.composite(
            FishProperties.STREAM_CODEC, FishCaughtCounter::fp,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::count,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::fastestTicks,
            ByteBufCodecs.FLOAT, FishCaughtCounter::averageTicks,
            ByteBufCodecs.INT, FishCaughtCounter::size,
            ByteBufCodecs.INT, FishCaughtCounter::weight,
            ByteBufCodecs.BOOL, FishCaughtCounter::caughtGolden,
            FishCaughtCounter::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<FishCaughtCounter>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());


    public static final Codec<List<FishCaughtCounter>> LIST_CODEC = FishCaughtCounter.CODEC.listOf();


    public static boolean AwardFishCaughtCounter(FishProperties fpCaught, Player player, int ticks)
    {
        List<FishCaughtCounter> listFishCaughtCounter = player.getData(ModDataAttachments.FISHES_CAUGHT);
        List<FishCaughtCounter> newlist = new ArrayList<>();

        boolean newFish = true;

        int size = ((int) Starcatcher.truncatedNormal(fpCaught.sw().sizeAverage(), fpCaught.sw().sizeDeviation()));
        int weight = ((int) Starcatcher.truncatedNormal(fpCaught.sw().weightAverage(), fpCaught.sw().weightDeviation()));

        for (FishCaughtCounter fcc : listFishCaughtCounter)
        {
            if (fpCaught.equals(fcc.fp))
            {
                int fastestToSave = Math.min(fcc.fastestTicks, ticks);

                float averageToSave = (fcc.averageTicks * fcc.count + ticks) / (fcc.count + 1);

                if(fcc.fastestTicks == 0) fastestToSave = ticks;
                if(fcc.averageTicks == 0) averageToSave = ticks;

                int sizeToSave = Math.max(size, fcc.size);
                int weightToSave = Math.max(weight, fcc.weight);

                newlist.add(new FishCaughtCounter(fpCaught, fcc.count + 1, fastestToSave, averageToSave, sizeToSave, weightToSave, false));
                newFish = false;

            }
            else
            {
                newlist.add(fcc);
            }
        }

        if (newFish) newlist.add(new FishCaughtCounter(fpCaught, 1, ticks, ticks, size, weight, false));

        player.setData(ModDataAttachments.FISHES_CAUGHT, newlist);
        return newFish;
    }

}
