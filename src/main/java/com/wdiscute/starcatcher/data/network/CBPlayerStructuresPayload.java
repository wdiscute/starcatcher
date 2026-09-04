package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.registry.fishrestrictions.StructureRestriction;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.List;

public record CBPlayerStructuresPayload(List<ResourceLocation> structures) implements CustomPacketPayload
{
    public static final Type<CBPlayerStructuresPayload> TYPE = new Type<>(Starcatcher.rl("player_in_structures"), CBPlayerStructuresPayload.class);

    public static final StreamCodec<CBPlayerStructuresPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), CBPlayerStructuresPayload::structures,
            CBPlayerStructuresPayload::new
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
            StructureRestriction.playerInStructures = structures;
            FishingGuideScreen.onStructuresReceived();
        });
    }
}
