package com.wdiscute.starcatcher.networkandstuff;

import com.wdiscute.starcatcher.Starcatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;


public class Payloads
{

    public record FishingPayload(FishProperties fp, ItemStack rod) implements CustomPacketPayload
    {

        public static final Type<FishingPayload> TYPE = new Type<>(Starcatcher.rl("fishing"));

        public static final StreamCodec<ByteBuf, FishingPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(FishProperties.RECORD_CODEC),
                FishingPayload::fp,
                ByteBufCodecs.fromCodec(ItemStack.CODEC),
                FishingPayload::rod,
                FishingPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }


    public record FishingCompletedPayload(int time) implements CustomPacketPayload
    {

        public static final Type<FishingCompletedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_completed"));

        public static final StreamCodec<ByteBuf, FishingCompletedPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                FishingCompletedPayload::time,
                FishingCompletedPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record FishCaughtPayload(ItemStack is) implements CustomPacketPayload
    {

        public static final Type<FishCaughtPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught"));

        public static final StreamCodec<ByteBuf, FishCaughtPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(ItemStack.CODEC),
                FishCaughtPayload::is,
                FishCaughtPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
