package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public record FishCaughtCounter(
        FishProperties fp,
        int count,
        int fastestTicks,
        float averageTicks
)
{

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.fieldOf("fps").forGetter(FishCaughtCounter::fp),
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::count),
                    Codec.INT.optionalFieldOf("fastest_ticks", 0).forGetter(FishCaughtCounter::fastestTicks),
                    Codec.FLOAT.optionalFieldOf("average_ticks", 0.0f).forGetter(FishCaughtCounter::averageTicks)
            ).apply(instance, FishCaughtCounter::new)
    );


    public static final StreamCodec<RegistryFriendlyByteBuf, List<FishCaughtCounter>> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, FishCaughtCounter::fp,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::count,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::fastestTicks,
            ByteBufCodecs.FLOAT, FishCaughtCounter::averageTicks,
            FishCaughtCounter::new
    ).apply(ByteBufCodecs.list());


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

                newlist.add(new FishCaughtCounter(fpCaught, fcc.count + 1, newFastestTicks, newAverageTicks));
                newFish = false;

            }
            else
            {
                newlist.add(fcc);
            }
        }

        if (newFish) newlist.add(new FishCaughtCounter(fpCaught, 1, ticks, ticks));

        player.setData(ModDataAttachments.FISHES_CAUGHT, newlist);
        return newFish;
    }

}
