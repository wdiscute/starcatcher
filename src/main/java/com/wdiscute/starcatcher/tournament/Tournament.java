package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.networkandcodecs.ExtraComposites;
import com.wdiscute.starcatcher.networkandcodecs.FishProperties;
import com.wdiscute.starcatcher.networkandcodecs.SingleStackContainer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;
import java.util.UUID;

public record Tournament
        (
                String name,
                Status status,
                UUID owner,
                List<TournamentPlayerScore> players,
                TournamentSettings settings,
                List<SingleStackContainer> pool,
                long lastsUntil
        )
{

    public static final Codec<Tournament> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("name", "Unnamed Tournament").forGetter(Tournament::name),
                    Status.CODEC.fieldOf("status").forGetter(Tournament::status),
                    UUIDUtil.CODEC.fieldOf("owner").forGetter(Tournament::owner),
                    TournamentPlayerScore.LIST_CODEC.fieldOf("players").forGetter(Tournament::players),
                    TournamentSettings.CODEC.fieldOf("settings").forGetter(Tournament::settings),
                    SingleStackContainer.LIST_CODEC.optionalFieldOf("legendary", SingleStackContainer.EMPTY_LIST).forGetter(Tournament::pool),
                    Codec.LONG.fieldOf("lastsUntil").forGetter(Tournament::lastsUntil)
            ).apply(instance, Tournament::new)
    );


    public static final StreamCodec<RegistryFriendlyByteBuf, Tournament> STREAM_CODEC = ExtraComposites.composite(
            ByteBufCodecs.STRING_UTF8, Tournament::name,
            Status.STREAM_CODEC, Tournament::status,
            UUIDUtil.STREAM_CODEC, Tournament::owner,
            TournamentPlayerScore.STREAM_CODEC_LIST, Tournament::players,
            TournamentSettings.STREAM_CODEC, Tournament::settings,
            SingleStackContainer.STREAM_CODEC_LIST, Tournament::pool,
            ByteBufCodecs.VAR_LONG, Tournament::lastsUntil,
            Tournament::new
    );


    public enum Status implements StringRepresentable
    {
        SETUP("setup"), 
        ACTIVE("active"), 
        FINISHED("finished");

        Status(String name)
        {
            this.key = name;
        }

        public String toString()
        {
            return this.key;
        }

        public static final Codec<Status> CODEC = StringRepresentable.fromEnum(Status::values);
        public static final Codec<List<Status>> LIST_CODEC = Status.CODEC.listOf();
        public static final StreamCodec<RegistryFriendlyByteBuf, Status> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Status.class);
        public static final StreamCodec<RegistryFriendlyByteBuf, List<Status>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }
    }

}