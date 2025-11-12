package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public record FishCaughtCounter(
        FishProperties fp,
        int count,
        int fastestTicks,
        float averageTicks,
        float weight,
        float size,
        boolean caughtGolden
)
{

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.fieldOf("fps").forGetter(FishCaughtCounter::fp),
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::count),
                    Codec.INT.optionalFieldOf("fastest_ticks", 0).forGetter(FishCaughtCounter::fastestTicks),
                    Codec.FLOAT.optionalFieldOf("average_ticks", 0.0f).forGetter(FishCaughtCounter::averageTicks),
                    Codec.FLOAT.optionalFieldOf("best_size", 0.0f).forGetter(FishCaughtCounter::size),
                    Codec.FLOAT.optionalFieldOf("best_weight", 0.0f).forGetter(FishCaughtCounter::weight),
                    Codec.BOOL.optionalFieldOf("caught_golden", false).forGetter(FishCaughtCounter::caughtGolden)
            ).apply(instance, FishCaughtCounter::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishCaughtCounter> STREAM_CODEC = ExtraComposites.composite(
            FishProperties.STREAM_CODEC, FishCaughtCounter::fp,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::count,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::fastestTicks,
            ByteBufCodecs.FLOAT, FishCaughtCounter::averageTicks,
            ByteBufCodecs.FLOAT, FishCaughtCounter::size,
            ByteBufCodecs.FLOAT, FishCaughtCounter::weight,
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

        for (FishCaughtCounter fcc : listFishCaughtCounter)
        {
            if (fpCaught.equals(fcc.fp))
            {
                int newFastestTicks = Math.min(fcc.fastestTicks, ticks);

                float newAverageTicks = (fcc.averageTicks * fcc.count + ticks) / (fcc.count + 1);

                //todo add weight and stuff
                newlist.add(new FishCaughtCounter(fpCaught, fcc.count + 1, newFastestTicks, newAverageTicks, 0, 0, false));
                newFish = false;

            }
            else
            {
                newlist.add(fcc);
            }
        }

        if (newFish) newlist.add(new FishCaughtCounter(fpCaught, 1, ticks, ticks, 0, 0, false));

        player.setData(ModDataAttachments.FISHES_CAUGHT, newlist);
        return newFish;
    }

}
