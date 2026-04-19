package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FPsSeenPayload(List<Identifier> locs) implements CustomPacketPayload {

    public static final Type<FPsSeenPayload> TYPE = new Type<>(Starcatcher.rl("fps_seen"));

    public static final StreamCodec<ByteBuf, FPsSeenPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FPsSeenPayload::locs,
            FPsSeenPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Map<Identifier, FishCaughtCounter> map = new HashMap<>(FishingGuideAttachment.getFishesCaught(context.player()));

            locs.forEach(loc -> {
                FishCaughtCounter fishCaughtCounter = map.get(loc);

                if (fishCaughtCounter != null)
                    map.replace(loc,  fishCaughtCounter.removeNotification());
            });

            FishingGuideAttachment.setFishesCaught(context.player(), map);
        });
    }
}
