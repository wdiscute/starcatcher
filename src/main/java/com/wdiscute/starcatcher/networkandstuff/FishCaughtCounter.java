package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
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
        int count
)
{

    public static final Codec<FishCaughtCounter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.fieldOf("fps").forGetter(FishCaughtCounter::fp),
                    Codec.INT.optionalFieldOf("count", 0).forGetter(FishCaughtCounter::count)
            ).apply(instance, FishCaughtCounter::new)
    );


    public static final StreamCodec<ByteBuf, List<FishCaughtCounter>> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, FishCaughtCounter::fp,
            ByteBufCodecs.VAR_INT, FishCaughtCounter::count,
            FishCaughtCounter::new
    ).apply(ByteBufCodecs.list());


    public static final Codec<List<FishCaughtCounter>> LIST_CODEC = FishCaughtCounter.CODEC.listOf();


    public static boolean AwardFishCaughtCounter(FishProperties fp, Player player)
    {
        List<FishCaughtCounter> listFishCaughtCounter = player.getData(ModDataAttachments.FISHES_CAUGHT);

        List<FishCaughtCounter> newlist = new ArrayList<>();


        for (FishCaughtCounter f : listFishCaughtCounter)
        {

            if (fp.equals(f.fp))
            {
                newlist.add(new FishCaughtCounter(fp, f.count + 1));
                return false;
            }

            newlist.add(f);
        }

        newlist.add(new FishCaughtCounter(fp, 1));

        player.setData(ModDataAttachments.FISHES_CAUGHT, newlist);
        return true;
    }

}
