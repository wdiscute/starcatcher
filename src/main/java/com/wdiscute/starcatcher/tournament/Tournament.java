package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ExtraComposites;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.StringRepresentable;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.io.utils.NeoForgeStreamCodecs;

import java.util.*;

public class Tournament
{
    public UUID tournamentUUID;
    public String name;
    public Status status;
    public UUID owner;
    public List<TournamentPlayerScore> playerScores;
    public TournamentSettings settings;
    public List<SingleStackContainer> lootPool;
    public long lastsUntilEpoch;

    public static final Tournament DEFAULT = new Tournament(
            UUID.randomUUID(),
            "missingno",
            Tournament.Status.SETUP,
            UUID.randomUUID(),
            new ArrayList<>(),
            TournamentSettings.DEFAULT,
            new ArrayList<>(),
            0);

    public static final Codec<Tournament> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("tournament_uuid").forGetter(t -> t.tournamentUUID),
                    Codec.STRING.optionalFieldOf("name", "Unnamed Tournament").forGetter(t -> t.name),
                    Status.CODEC.fieldOf("status").forGetter(t -> t.status),
                    UUIDUtil.CODEC.fieldOf("owner").forGetter(t -> t.owner),
                    TournamentPlayerScore.CODEC.listOf().fieldOf("player_scores").forGetter(t -> t.playerScores),
                    TournamentSettings.CODEC.fieldOf("settings").forGetter(t -> t.settings),
                    SingleStackContainer.LIST_CODEC.optionalFieldOf("loot_pool", SingleStackContainer.EMPTY_LIST).forGetter(t -> t.lootPool),
                    Codec.LONG.fieldOf("lastsUntil").forGetter(t -> t.lastsUntilEpoch)
            ).apply(instance, Tournament::new)
    );

    public static final StreamCodec<Tournament> STREAM_CODEC = ExtraComposites.composite(
            ByteBufCodecs.UUID, t -> t.tournamentUUID,
            ByteBufCodecs.STRING, t -> t.name,
            Status.STREAM_CODEC, t -> t.status,
            ByteBufCodecs.UUID, t -> t.owner,
            TournamentPlayerScore.STREAM_CODEC.apply(ByteBufCodecs.list()), t -> t.playerScores,
            TournamentSettings.STREAM_CODEC, t -> t.settings,
            SingleStackContainer.STREAM_CODEC_LIST, t -> t.lootPool,
            ByteBufCodecs.VAR_LONG, t -> t.lastsUntilEpoch,
            Tournament::new
    );

    public Tournament(UUID tournamentUUID,
                      String name,
                      Status status,
                      UUID owner,
                      List<TournamentPlayerScore> playerScore,
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
        this.lastsUntilEpoch = lastsUntil;
    }

    public static Tournament empty(UUID uuid)
    {
        return new Tournament(
                uuid,
                "Unnamed Tournament",
                Tournament.Status.SETUP,
                null,
                new ArrayList<>(),
                new TournamentSettings(
                        TournamentSettings.Scoring.SIMPLE,
                        48000,
                        0,
                        0,
                        SingleStackContainer.EMPTY_LIST),
                SingleStackContainer.EMPTY_LIST,
                200
        );
    }

    public Tournament setOwner(UUID owner)
    {
        this.owner = owner;
        playerScores.add(TournamentPlayerScore.empty(owner));

        return this;
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
        public static final StreamCodec<Status> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Status.class);
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }

        public boolean isDone()
        {
            return this == FINISHED || this == CANCELLED;
        }
    }

}