package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ExtraComposites;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.*;

public class Tournament
{
    public UUID tournamentUUID;
    public String name;
    public Status status;
    public UUID owner;
    public Map<UUID, TournamentPlayerScore> playerScores;
    public TournamentSettings settings;
    public List<SingleStackContainer> lootPool;
    public long lastsUntil;

    public static final Codec<Tournament> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("tournament_uuid").forGetter(Tournament::getOwner),
                    Codec.STRING.optionalFieldOf("name", "Unnamed Tournament").forGetter(Tournament::getName),
                    Status.CODEC.fieldOf("status").forGetter(Tournament::getStatus),
                    UUIDUtil.CODEC.fieldOf("owner").forGetter(Tournament::getOwner),
                    Codec.unboundedMap(UUIDUtil.CODEC, TournamentPlayerScore.CODEC).fieldOf("player_scores").forGetter(Tournament::getPlayerScores),
                    TournamentSettings.CODEC.fieldOf("settings").forGetter(Tournament::getSettings),
                    SingleStackContainer.LIST_CODEC.optionalFieldOf("loot_pool", SingleStackContainer.EMPTY_LIST).forGetter(Tournament::getLootPool),
                    Codec.LONG.fieldOf("lastsUntil").forGetter(Tournament::getLastsUntil)
            ).apply(instance, Tournament::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Tournament> STREAM_CODEC = ExtraComposites.composite(
            UUIDUtil.STREAM_CODEC, Tournament::getTournamentUUID,
            ByteBufCodecs.STRING_UTF8, Tournament::getName,
            Status.STREAM_CODEC, Tournament::getStatus,
            UUIDUtil.STREAM_CODEC, Tournament::getOwner,
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, UUIDUtil.STREAM_CODEC, TournamentPlayerScore.STREAM_CODEC), Tournament::getPlayerScores,
            TournamentSettings.STREAM_CODEC, Tournament::getSettings,
            SingleStackContainer.STREAM_CODEC_LIST, Tournament::getLootPool,
            ByteBufCodecs.VAR_LONG, Tournament::getLastsUntil,
            Tournament::new
    );

    public Tournament(UUID tournamentUUID,
                      String name,
                      Status status,
                      UUID owner,
                      Map<UUID, TournamentPlayerScore> playerScore,
                      TournamentSettings settings,
                      List<SingleStackContainer> pool,
                      long lastsUntil
    )
    {
        this.tournamentUUID = tournamentUUID;
        this.name = name;
        this.status = status;
        this.owner = owner;
        this.playerScores = playerScore;
        this.settings = settings;
        this.lootPool = pool;
        this.lastsUntil = lastsUntil;
    }

    public UUID getTournamentUUID()
    {
        return tournamentUUID;
    }

    public String getName()
    {
        return name;
    }

    public List<SingleStackContainer> getLootPool()
    {
        return lootPool;
    }

    public Map<UUID, TournamentPlayerScore> getPlayerScores()
    {
        return playerScores;
    }

    public long getLastsUntil()
    {
        return lastsUntil;
    }

    public Status getStatus()
    {
        return status;
    }

    public TournamentSettings getSettings()
    {
        return settings;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public enum Status implements StringRepresentable
    {
        SETUP("gui.starcatcher.tournament.status.setup"),
        ACTIVE("gui.starcatcher.tournament.status.active"),
        CANCELLED("gui.starcatcher.tournament.status.cancelled"),
        FINISHED("gui.starcatcher.tournament.status.finished");

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