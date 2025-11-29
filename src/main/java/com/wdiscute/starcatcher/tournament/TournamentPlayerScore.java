package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ExtraComposites;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class TournamentPlayerScore
{
    public int score;
    public int misses;
    public int common;
    public int uncommon;
    public int rare;
    public int epic;
    public int legendary;

    public static final Codec<TournamentPlayerScore> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("score", 0).forGetter(TournamentPlayerScore::getScore),
                    Codec.INT.optionalFieldOf("misses", 0).forGetter(TournamentPlayerScore::getMisses),
                    Codec.INT.optionalFieldOf("common", 0).forGetter(TournamentPlayerScore::getCommon),
                    Codec.INT.optionalFieldOf("uncommon", 0).forGetter(TournamentPlayerScore::getUncommon),
                    Codec.INT.optionalFieldOf("rare", 0).forGetter(TournamentPlayerScore::getRare),
                    Codec.INT.optionalFieldOf("epic", 0).forGetter(TournamentPlayerScore::getRare),
                    Codec.INT.optionalFieldOf("legendary", 0).forGetter(TournamentPlayerScore::getLegendary)
            ).apply(instance, TournamentPlayerScore::new)
    );

    public static final StreamCodec<ByteBuf, TournamentPlayerScore> STREAM_CODEC = ExtraComposites.composite(
            ByteBufCodecs.INT, TournamentPlayerScore::getScore,
            ByteBufCodecs.INT, TournamentPlayerScore::getMisses,
            ByteBufCodecs.INT, TournamentPlayerScore::getCommon,
            ByteBufCodecs.INT, TournamentPlayerScore::getUncommon,
            ByteBufCodecs.INT, TournamentPlayerScore::getRare,
            ByteBufCodecs.INT, TournamentPlayerScore::getEpic,
            ByteBufCodecs.INT, TournamentPlayerScore::getLegendary,
            TournamentPlayerScore::new
    );

    public static TournamentPlayerScore empty()
    {
        return new TournamentPlayerScore(0, 0, 0, 0, 0, 0, 0);
    }

    public void addScore(int score)
    {
        this.score += score;
    }

    public TournamentPlayerScore(int score, int misses, int common, int uncommon, int rare, int epic, int legendary)
    {
        this.score = score;
        this.misses = misses;
        this.common = common;
        this.uncommon = uncommon;
        this.rare = rare;
        this.epic = epic;
        this.legendary = legendary;
    }

    public static final Codec<List<TournamentPlayerScore>> LIST_CODEC = TournamentPlayerScore.CODEC.listOf();

    public int getCommon()
    {
        return common;
    }

    public int getEpic()
    {
        return epic;
    }

    public int getLegendary()
    {
        return legendary;
    }

    public int getMisses()
    {
        return misses;
    }

    public int getRare()
    {
        return rare;
    }

    public int getScore()
    {
        return score;
    }

    public int getUncommon()
    {
        return uncommon;
    }
}
