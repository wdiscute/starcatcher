package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.registry.fishrestrictions.StructureRestriction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record CBPlayerStructuresPayload(List<Identifier> structures) implements CustomPacketPayload
{
    public static final Type<CBPlayerStructuresPayload> TYPE = new Type<>(Starcatcher.rl("player_in_structures"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBPlayerStructuresPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), CBPlayerStructuresPayload::structures,
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
