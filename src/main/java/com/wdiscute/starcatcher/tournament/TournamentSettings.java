package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.io.utils.NeoForgeStreamCodecs;

import java.util.List;

public class TournamentSettings
{
    public Scoring scoring;
    public long durationInTicks;
    public float perfectCatchMultiplier;
    public int missPenalty;
    public List<SingleStackContainer> entryCost;

    public static final TournamentSettings DEFAULT = new TournamentSettings(
            TournamentSettings.Scoring.SIMPLE,
            0,
            0,
            0,
            List.of());

    public boolean canSignUp(Player player)
    {
        boolean canSignup = true;
        if (!entryCost.isEmpty())
        {
            for (SingleStackContainer ssc : entryCost)
            {
                if (!player.getInventory().hasAnyMatching(is -> is.is(ssc.stack().getItem()) && is.getCount() >= ssc.stack().getCount()))
                    canSignup = false;
            }
        }
        return canSignup;
    }

    public float getPerfectCatchMultiplier()
    {
        return perfectCatchMultiplier;
    }

    public int getMissPenalty()
    {
        return missPenalty;
    }

    public Scoring getScoring()
    {
        return scoring;
    }

    public List<SingleStackContainer> getEntryCost()
    {
        return entryCost;
    }

    public long getDurationInTicks()
    {
        return durationInTicks;
    }

    public enum Scoring implements StringRepresentable
    {
        SIMPLE("gui.starcatcher.tournament.scoring.simple"),
        WEIGHT("gui.starcatcher.tournament.scoring.weight"),
        RARITY("gui.starcatcher.tournament.scoring.rarity"),
        GOLDEN("gui.starcatcher.tournament.scoring.golden"),
        ADVANCED("gui.starcatcher.tournament.scoring.advanced");

        Scoring(String name)
        {
            this.key = name;
        }

        public String toString()
        {
            return this.key;
        }

        public static final Codec<Scoring> CODEC = StringRepresentable.fromEnum(Scoring::values);
        public static final StreamCodec<Scoring> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Scoring.class);
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }

        private static final Scoring[] vals = values();

        public Scoring previous()
        {
            if (this.ordinal() == 0) return vals[vals.length - 1];
            return vals[(this.ordinal() - 1) % vals.length];
        }

        public Scoring next()
        {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    public TournamentSettings(Scoring type, long duration, float perfectCatchMultiplier, int missPenalty, List<SingleStackContainer> entryCost)
    {
        this.scoring = type;
        this.durationInTicks = duration;
        this.perfectCatchMultiplier = perfectCatchMultiplier;
        this.missPenalty = missPenalty;
        this.entryCost = entryCost;
    }

    public static final Codec<TournamentSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Scoring.CODEC.optionalFieldOf("type", Scoring.SIMPLE).forGetter(TournamentSettings::getScoring),
                    Codec.LONG.optionalFieldOf("duration", 0L).forGetter(TournamentSettings::getDurationInTicks),
                    Codec.FLOAT.optionalFieldOf("perfect_catch_multiplier", 0.0f).forGetter(TournamentSettings::getPerfectCatchMultiplier),
                    Codec.INT.optionalFieldOf("miss_penalty", 0).forGetter(TournamentSettings::getMissPenalty),
                    SingleStackContainer.LIST_CODEC.optionalFieldOf("entry_cost", List.of()).forGetter(TournamentSettings::getEntryCost)
            ).apply(instance, TournamentSettings::new)
    );

    public static final StreamCodec<TournamentSettings> STREAM_CODEC = StreamCodec.composite(
            Scoring.STREAM_CODEC, TournamentSettings::getScoring,
            ByteBufCodecs.VAR_LONG, TournamentSettings::getDurationInTicks,
            ByteBufCodecs.FLOAT, TournamentSettings::getPerfectCatchMultiplier,
            ByteBufCodecs.VAR_INT, TournamentSettings::getMissPenalty,
            SingleStackContainer.STREAM_CODEC_LIST, TournamentSettings::getEntryCost,
            TournamentSettings::new
    );
}
