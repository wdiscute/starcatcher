package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.data.ExtraComposites;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.*;

public class Tournament
{
    public UUID tournamentUUID;
    public String name;
    public Status status;
    public UUID owner;
    public String ownerName;
    public List<PlayerScore> playerScores;
    public TournamentScoreSettings scoreSettings;
    public long startTimeEpoch;
    public long durationInTicks;

    public static final StreamCodec<RegistryFriendlyByteBuf, Tournament> STREAM_CODEC = ExtraComposites.composite(
            UUIDUtil.STREAM_CODEC, t -> t.tournamentUUID,
            ByteBufCodecs.STRING_UTF8, t -> t.name,
            Status.STREAM_CODEC, t -> t.status,
            UUIDUtil.STREAM_CODEC, t -> t.owner,
            ByteBufCodecs.STRING_UTF8, t -> t.ownerName,
            PlayerScore.LIST_STREAM_CODEC, t -> t.playerScores,
            TournamentScoreSettings.STREAM_CODEC, t -> t.scoreSettings,
            ByteBufCodecs.VAR_LONG, t -> t.startTimeEpoch,
            ByteBufCodecs.VAR_LONG, t -> t.durationInTicks,
            Tournament::new
    );

    public static final Codec<Tournament> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("tournament_uuid").forGetter(t -> t.tournamentUUID),
                    Codec.STRING.optionalFieldOf("name", "Unnamed Tournament").forGetter(t -> t.name),
                    Status.CODEC.fieldOf("status").forGetter(t -> t.status),
                    UUIDUtil.CODEC.fieldOf("owner").forGetter(t -> t.owner),
                    Codec.STRING.optionalFieldOf("owner_name", "Unknown Player").forGetter(t -> t.ownerName),
                    PlayerScore.CODEC.listOf().fieldOf("scores").forGetter(t -> t.playerScores),
                    TournamentScoreSettings.CODEC.fieldOf("score_settings").forGetter(t -> t.scoreSettings),
                    Codec.LONG.fieldOf("start_time").forGetter(t -> t.startTimeEpoch),
                    Codec.LONG.fieldOf("duration").forGetter(t -> t.durationInTicks)
            ).apply(instance, Tournament::new)
    );


    public Tournament(UUID tournamentUUID,
                      String name,
                      Status status,
                      UUID owner,
                      String ownerName,
                      List<PlayerScore> playerScore,
                      TournamentScoreSettings scoreSettings,
                      long startTime,
                      long duration
    )
    {
        this.tournamentUUID = tournamentUUID;
        this.name = name;
        this.status = status;
        this.owner = owner;
        this.ownerName = ownerName;
        this.playerScores = playerScore;
        this.scoreSettings = scoreSettings;
        this.startTimeEpoch = startTime;
        this.durationInTicks = duration;
    }

    public static Tournament empty(UUID tournamentUUID, Player owner)
    {
        return new Tournament(
                tournamentUUID,
                "Unnamed Tournament",
                Status.PREPARING,
                owner.getUUID(),
                owner.getName().getString(),
                new ArrayList<>(){{add(new PlayerScore(owner.getUUID(), owner.getName().getString(), 0));}},
                TournamentScoreSettings.empty(),
                0L,
                36000L
        );
    }

    public static Tournament empty(UUID tournamentUUID, UUID owner, String ownerName)
    {
        return new Tournament(
                tournamentUUID,
                "Unnamed Tournament",
                Status.PREPARING,
                owner,
                ownerName,
                new ArrayList<>(),
                TournamentScoreSettings.empty(),
                0L,
                36000L
        );
    }

    public boolean isPlayerSignedUp(Player player)
    {
        return playerScores.stream().anyMatch(o -> o.uuid.equals(player.getUUID()));
    }

    public enum Status implements StringRepresentable
    {
        PREPARING("gui.starcatcher.tournament.status.preparing"),
        ACTIVE("gui.starcatcher.tournament.status.active"),
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
        public static final StreamCodec<RegistryFriendlyByteBuf, Status> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Status.class);
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }

        public boolean isDone()
        {
            return this == FINISHED;
        }
    }

    public static class PlayerScore
    {
        public UUID uuid;
        public String name;
        public float score;

        public PlayerScore(UUID uuid, String name, float score)
        {
            this.uuid = uuid;
            this.name = name;
            this.score = score;
        }

        public static final Codec<PlayerScore> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        UUIDUtil.CODEC.fieldOf("uuid").forGetter(ps -> ps.uuid),
                        Codec.STRING.fieldOf("name").forGetter(ps -> ps.name),
                        Codec.FLOAT.fieldOf("score").forGetter(ps -> ps.score)
                ).apply(instance, PlayerScore::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PlayerScore> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, ps -> ps.uuid,
                ByteBufCodecs.STRING_UTF8, ps -> ps.name,
                ByteBufCodecs.FLOAT, ps -> ps.score,
                PlayerScore::new
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, List<PlayerScore>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
    }
}