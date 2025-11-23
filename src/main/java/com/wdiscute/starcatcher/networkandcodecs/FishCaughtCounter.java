package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

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

    public static final Codec<List<FishCaughtCounter>> LIST_CODEC = FishCaughtCounter.CODEC.listOf();


    public static boolean AwardFishCaughtCounter(FishProperties fpCaught, Player player, int ticks, int size, int weight)
    {
        List<FishCaughtCounter> listFishCaughtCounter = DataAttachments.get(player).fishesCaught();
        List<FishCaughtCounter> newlist = new ArrayList<>();

        boolean newFish = true;

        for (FishCaughtCounter fcc : listFishCaughtCounter)
        {
            if (fpCaught.equals(fcc.fp))
            {
                int fastestToSave = Math.min(fcc.fastestTicks, ticks);
                float averageToSave = (fcc.averageTicks * fcc.count + ticks) / (fcc.count + 1);
                int countToSave = fcc.count;

                //if cheated in, fixes trackers
                if(fcc.fastestTicks == 0) fastestToSave = ticks;
                if(fcc.averageTicks == 0) averageToSave = ticks;
                if(fcc.count == 999999) countToSave = 0;

                int sizeToSave = Math.max(size, fcc.size);
                int weightToSave = Math.max(weight, fcc.weight);

                newlist.add(new FishCaughtCounter(fpCaught, countToSave + 1, fastestToSave, averageToSave, sizeToSave, weightToSave, false));


                newFish = false;
            }
            else
            {
                newlist.add(fcc);
            }
        }

        if (newFish) newlist.add(new FishCaughtCounter(fpCaught, 1, ticks, ticks, size, weight, false));

        //display message above exp bar
        Payloads.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> ((ServerPlayer) player)),
                new Payloads.FishCaughtPayload(fpCaught, newFish, size, weight)
        );

        DataAttachments.get(player).setFishesCaught(newlist);
        return newFish;
    }

}
