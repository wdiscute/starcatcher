package com.wdiscute.starcatcher.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class FishingGuideAttachment
{
    public Map<ResourceLocation, FishCaughtCounter> fishesCaught;
    public boolean receivedGuide;
    public boolean fishedRod;

    public FishingGuideAttachment(Map<ResourceLocation, FishCaughtCounter> fishesCaught, boolean receivedGuide, boolean fishedRod)
    {
        this.fishesCaught = new HashMap<>(fishesCaught); //guarantees the map is mutable
        this.receivedGuide = receivedGuide;
    }

    public static FishingGuideAttachment createDefault()
    {
        return new FishingGuideAttachment(
                new HashMap<>(),
                false, false);
    }

    public static final Codec<FishingGuideAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(ResourceLocation.CODEC, FishCaughtCounter.CODEC).fieldOf("fishes_caught").forGetter(data -> data.fishesCaught),
                    Codec.BOOL.lenientOptionalFieldOf("received_guide", false).forGetter(data -> data.receivedGuide),
                    Codec.BOOL.lenientOptionalFieldOf("fished_rod", false).forGetter(data -> data.fishedRod)
            ).apply(instance, FishingGuideAttachment::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishingGuideAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, FishCaughtCounter.STREAM_CODEC), data -> data.fishesCaught,
            ByteBufCodecs.BOOL, data -> data.receivedGuide,
            ByteBufCodecs.BOOL, data -> data.fishedRod,
            FishingGuideAttachment::new
    );

    public static Map<ResourceLocation, FishCaughtCounter> getFishesCaught(Player player)
    {
        return get(player).fishesCaught;
    }

    public static void setFishesCaught(Player player, Map<ResourceLocation, FishCaughtCounter> fishesCaught)
    {
        get(player).fishesCaught = fishesCaught;
        sync(player);
    }

    public static boolean getFishedRod(Player player)
    {
        return get(player).fishedRod;
    }

    public static void setFishedRod(Player player, boolean fishedRod)
    {
        get(player).fishedRod = fishedRod;
        sync(player);
    }

    public static boolean getReceivedGuide(Player player)
    {
        return get(player).receivedGuide;
    }

    public static void setReceivedGuide(Player player, boolean receivedGuide)
    {
        get(player).receivedGuide = receivedGuide;
        sync(player);
    }

    public static FishingGuideAttachment get(Entity holder)
    {
        return holder.getData(SCDataAttachments.FISHING_GUIDE);
    }

    public static void sync(Player player)
    {
        player.syncData(SCDataAttachments.FISHING_GUIDE);
    }

}
