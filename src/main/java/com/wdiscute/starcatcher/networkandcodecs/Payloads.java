package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentPlayerScore;
import com.wdiscute.starcatcher.tournament.TournamentSettings;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;


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

    public record FishCaughtPayload(FishProperties fp, boolean newFish, int size, int weight) implements CustomPacketPayload
    {

        public static final Type<FishCaughtPayload> TYPE = new Type<>(Starcatcher.rl("fish_caught"));

        public static final StreamCodec<ByteBuf, FishCaughtPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(FishProperties.CODEC),
                FishCaughtPayload::fp,
                ByteBufCodecs.BOOL,
                FishCaughtPayload::newFish,
                ByteBufCodecs.INT,
                FishCaughtPayload::size,
                ByteBufCodecs.INT,
                FishCaughtPayload::weight,
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

    public record TournamentDataToClient(List<GameProfile> listSignups, Tournament tour) implements CustomPacketPayload
    {

        public static final StreamCodec<ByteBuf, GameProfile> GAME_PROFILE_STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, GameProfile::getId,
                ByteBufCodecs.STRING_UTF8, GameProfile::getName,
                GameProfile::new
        );

        public static final StreamCodec<ByteBuf, List<GameProfile>> GAME_PROFILE_STREAM_CODEC_LIST = GAME_PROFILE_STREAM_CODEC.apply(ByteBufCodecs.list());


        public static final Type<TournamentDataToClient> TYPE = new Type<>(Starcatcher.rl("tour"));

        public static final StreamCodec<RegistryFriendlyByteBuf, TournamentDataToClient> STREAM_CODEC = StreamCodec.composite(
                GAME_PROFILE_STREAM_CODEC_LIST, TournamentDataToClient::listSignups,
                Tournament.STREAM_CODEC, TournamentDataToClient::tour,
                TournamentDataToClient::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
