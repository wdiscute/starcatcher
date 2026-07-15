package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SBFPsSeenPayload(List<ResourceLocation> locs) implements CustomPacketPayload {

    public static final Type<SBFPsSeenPayload> TYPE = new Type<>(Starcatcher.rl("fps_seen"));

    public static final StreamCodec<ByteBuf, SBFPsSeenPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SBFPsSeenPayload::locs,
            SBFPsSeenPayload::new
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
