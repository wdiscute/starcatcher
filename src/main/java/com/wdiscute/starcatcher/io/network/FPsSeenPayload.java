package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record FPsSeenPayload(List<FishProperties> fps) implements CustomPacketPayload {

    public static final Type<FPsSeenPayload> TYPE = new Type<>(Starcatcher.rl("fps_seen"));

    public static final StreamCodec<ByteBuf, FPsSeenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(FishProperties.LIST_CODEC),
            FPsSeenPayload::fps,
            FPsSeenPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        List<FishProperties> list = U.getFpsFromRls(context.player().level(), context.player().getData(ModDataAttachments.FISHES_NOTIFICATION));
        List<FishProperties> newList = new ArrayList<>();

        for (FishProperties fp : list) {
            if (!fps().contains(fp))
                newList.add(fp);
        }

        context.player().setData(ModDataAttachments.FISHES_NOTIFICATION, U.getRlsFromFps(context.player().level(), newList));
    }
}
