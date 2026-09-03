package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CBFishingStartedPayload(FishProperties fp, MaybeStack treasure,
                                      MaybeStack rod) implements CustomPacketPayload
{

    public static final Type<CBFishingStartedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_started"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBFishingStartedPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, CBFishingStartedPayload::fp,
            MaybeStack.STREAM_CODEC, CBFishingStartedPayload::treasure,
            MaybeStack.STREAM_CODEC, CBFishingStartedPayload::rod,
            CBFishingStartedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> FishingMinigameScreen.fromPayload(this, context));
    }
}
