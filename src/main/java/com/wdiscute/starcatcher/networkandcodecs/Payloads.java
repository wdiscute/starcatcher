package com.wdiscute.starcatcher.networkandcodecs;

import com.wdiscute.starcatcher.Starcatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import java.util.List;


public class Payloads
{

    public record FishingPayload(FishProperties fp, ItemStack rod) implements CustomPacketPayload
    {

        public static final Type<FishingPayload> TYPE = new Type<>(Starcatcher.rl("fishing"));

        public static final StreamCodec<ByteBuf, FishingPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(FishProperties.CODEC),
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


    public record FishingCompletedPayload(int time, boolean completedTreasure, boolean perfectCatch, int hits) implements CustomPacketPayload
    {

        public static final Type<FishingCompletedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_completed"));

        public static final StreamCodec<ByteBuf, FishingCompletedPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                FishingCompletedPayload::time,
                ByteBufCodecs.BOOL,
                FishingCompletedPayload::completedTreasure,
                ByteBufCodecs.BOOL,
                FishingCompletedPayload::perfectCatch,
                ByteBufCodecs.INT,
                FishingCompletedPayload::hits,
                FishingCompletedPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record FishCaughtPayload(FishProperties fp) implements CustomPacketPayload
    {

        public static final Type<FishCaughtPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught"));

        public static final StreamCodec<ByteBuf, FishCaughtPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(FishProperties.CODEC),
                FishCaughtPayload::fp,
                FishCaughtPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record FPsSeen(List<FishProperties> fps) implements CustomPacketPayload
    {

        public static final Type<FPsSeen> TYPE = new Type<>(Starcatcher.rl("fps_seen"));

        public static final StreamCodec<ByteBuf, FPsSeen> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(FishProperties.LIST_CODEC),
                FPsSeen::fps,
                FPsSeen::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
