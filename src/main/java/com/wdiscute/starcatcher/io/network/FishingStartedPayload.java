package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

public record FishingStartedPayload(FishProperties fp, ItemStack rod) implements CustomPacketPayload {

    public static final Type<FishingStartedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_started"), FishingStartedPayload.class);

    public static final StreamCodec< FishingStartedPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, FishingStartedPayload::fp,
            ByteBufCodecs.ITEM_STACK, FishingStartedPayload::rod,
            FishingStartedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public void handle(IPayloadContext context) {
        context.enqueueWork(()-> client(this, context));
    }

    @OnlyIn(Dist.CLIENT)
    public static void client(FishingStartedPayload data, IPayloadContext context) {
        Minecraft.getInstance().setScreen(new FishingMinigameScreen(data.fp(), data.rod()));
    }
}
