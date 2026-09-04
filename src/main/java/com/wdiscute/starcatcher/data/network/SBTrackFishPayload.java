package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record SBTrackFishPayload(ResourceLocation fp) implements CustomPacketPayload
{
    public static final Type<SBTrackFishPayload> TYPE = new Type<>(Starcatcher.rl("track_fish"), SBTrackFishPayload.class);

    public static final StreamCodec<SBTrackFishPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.RESOURCE_LOCATION,
            SBTrackFishPayload::fp,
            SBTrackFishPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            if (context.player().getData(SCDataAttachments.TRACKED_FISH).equals(fp))
                context.player().setData(SCDataAttachments.TRACKED_FISH, Starcatcher.MISSINGNO);
            else
                context.player().setData(SCDataAttachments.TRACKED_FISH, fp);
        });
    }
}
