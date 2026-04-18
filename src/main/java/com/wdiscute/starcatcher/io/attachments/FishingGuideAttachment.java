package com.wdiscute.starcatcher.io.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class FishingGuideAttachment {
    public Map<Identifier, FishCaughtCounter> fishesCaught;
    public boolean receivedGuide;

    public FishingGuideAttachment(Map<Identifier, FishCaughtCounter> fishesCaught, boolean receivedGuide ) {
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
                    Codec.unboundedMap(Identifier.CODEC, FishCaughtCounter.CODEC).fieldOf("fishes_caught").forGetter(data -> data.fishesCaught),
                    Codec.BOOL.lenientOptionalFieldOf("received_guide", false).forGetter(data -> data.receivedGuide)
            ).apply(instance, FishingGuideAttachment::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishingGuideAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, FishCaughtCounter.STREAM_CODEC), data -> data.fishesCaught,
            ByteBufCodecs.BOOL, data -> data.receivedGuide,

            FishingGuideAttachment::new
    );


    public static Map<Identifier, FishCaughtCounter> getFishesCaught(Player player) {
        return get(player).fishesCaught;
    }

    public static void setFishesCaught(Player player, Map<Identifier, FishCaughtCounter> fishesCaught) {
        get(player).fishesCaught = fishesCaught;
        sync(player);
    }

    public static boolean getReceivedGuide(Player player) {
        return get(player).receivedGuide;
    }

    public static void setReceivedGuide(Player player, boolean receivedGuide) {
        get(player).receivedGuide = receivedGuide;
        sync(player);
    }

    public static FishingGuideAttachment get(Entity holder){
        return holder.getData(SCDataAttachments.FISHING_GUIDE);
    }

    public static void sync(Player player){
        player.syncData(SCDataAttachments.FISHING_GUIDE);
    }

}
