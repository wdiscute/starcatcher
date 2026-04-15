package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FPsSeenPayload(List<ResourceLocation> locs) implements CustomPacketPayload {

    public static final Type<FPsSeenPayload> TYPE = new Type<>(Starcatcher.rl("fps_seen"), FPsSeenPayload.class);

    public static final StreamCodec<FPsSeenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.RESOURCE_LOCATION.apply(ByteBufCodecs.list()),
            FPsSeenPayload::locs,
            FPsSeenPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Map<ResourceLocation, FishCaughtCounter> map = new HashMap<>(FishingGuideAttachment.getFishesCaught(context.player()));

            locs.forEach(loc -> {
                FishCaughtCounter fishCaughtCounter = map.get(loc);

                if (fishCaughtCounter != null)
                    map.replace(loc,  fishCaughtCounter.removeNotification());
            });

            FishingGuideAttachment.setFishesCaught(context.player(), map);
        });
    }
}
