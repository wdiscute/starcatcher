package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.HashMap;
import java.util.Map;

public class FishingGuideAttachment {
    public Map<ResourceLocation, FishCaughtCounter> fishesCaught;
    private boolean receivedGuide;

    public FishingGuideAttachment(Map<ResourceLocation, FishCaughtCounter> fishesCaught, boolean receivedGuide ) {
        this.fishesCaught = new HashMap<>(fishesCaught); //guarantees the map is mutable
        this.receivedGuide = receivedGuide;
    }

    public static FishingGuideAttachment createDefault() {
        return new FishingGuideAttachment(
                new HashMap<>(),
                false);
    }

    public static final Codec<FishingGuideAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(ResourceLocation.CODEC, FishCaughtCounter.CODEC).fieldOf("fishes_caught").forGetter(data -> data.fishesCaught),
                    Codec.BOOL.optionalFieldOf("received_guide", false).forGetter(data -> data.receivedGuide)
            ).apply(instance, FishingGuideAttachment::new)
    );

    public static final StreamCodec<FishingGuideAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.RESOURCE_LOCATION, FishCaughtCounter.STREAM_CODEC), data -> data.fishesCaught,
            ByteBufCodecs.BOOL, data -> data.receivedGuide,

            FishingGuideAttachment::new
    );


    public static Map<ResourceLocation, FishCaughtCounter> getFishesCaught(Player player) {
        return get(player).fishesCaught;
    }

    public static void setFishesCaught(Player player, Map<ResourceLocation, FishCaughtCounter> fishesCaught) {
        FishingGuideAttachment fishingGuideAttachment = get(player);
        fishingGuideAttachment.fishesCaught = fishesCaught;
        player.setData(SCDataAttachments.FISHING_GUIDE, fishingGuideAttachment);
    }

    public static boolean getReceivedGuide(Player player) {
        return get(player).receivedGuide;
    }

    public void setReceivedGuide(Player player, boolean receivedGuide) {
        this.receivedGuide = receivedGuide;
        player.setData(SCDataAttachments.FISHING_GUIDE, this);;
    }

    public static FishingGuideAttachment get(Entity holder){
        return holder.getData(SCDataAttachments.FISHING_GUIDE);
    }

    public static void sync(Player player){
        player.syncData(SCDataAttachments.FISHING_GUIDE);
    }

}
