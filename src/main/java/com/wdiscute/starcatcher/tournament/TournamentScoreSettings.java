package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.data.ExtraComposites;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class TournamentScoreSettings
{
    float trashScore;
    float commonScore;
    float uncommonScore;
    float rareScore;
    float epicScore;
    float legendaryScore;
    float percentileMultiplier;
    float perfectCatchMultiplier;

    public TournamentScoreSettings(float trashScore, float commonScore, float uncommonScore, float rareScore,
                                   float epicScore, float legendaryScore, float percentileMultiplier, float perfectCatchMultiplier)
    {
        this.trashScore = trashScore;
        this.commonScore = commonScore;
        this.uncommonScore = uncommonScore;
        this.rareScore = rareScore;
        this.epicScore = epicScore;
        this.legendaryScore = legendaryScore;
        this.percentileMultiplier = percentileMultiplier;
        this.perfectCatchMultiplier = perfectCatchMultiplier;
    }

    public static final Codec<TournamentScoreSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("trash_score").forGetter(t -> t.trashScore),
                    Codec.FLOAT.fieldOf("common_score").forGetter(t -> t.commonScore),
                    Codec.FLOAT.fieldOf("uncommon_score").forGetter(t -> t.uncommonScore),
                    Codec.FLOAT.fieldOf("rare_score").forGetter(t -> t.rareScore),
                    Codec.FLOAT.fieldOf("epic_score").forGetter(t -> t.epicScore),
                    Codec.FLOAT.fieldOf("legendary_score").forGetter(t -> t.legendaryScore),
                    Codec.FLOAT.fieldOf("percentile_multiplier").forGetter(t -> t.percentileMultiplier),
                    Codec.FLOAT.fieldOf("perfect_catch_multiplier").forGetter(t -> t.perfectCatchMultiplier)
            ).apply(instance, TournamentScoreSettings::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TournamentScoreSettings> STREAM_CODEC = ExtraComposites.composite(
            ByteBufCodecs.FLOAT, t -> t.trashScore,
            ByteBufCodecs.FLOAT, t -> t.commonScore,
            ByteBufCodecs.FLOAT, t -> t.uncommonScore,
            ByteBufCodecs.FLOAT, t -> t.rareScore,
            ByteBufCodecs.FLOAT, t -> t.epicScore,
            ByteBufCodecs.FLOAT, t -> t.legendaryScore,
            ByteBufCodecs.FLOAT, t -> t.percentileMultiplier,
            ByteBufCodecs.FLOAT, t -> t.perfectCatchMultiplier,
            TournamentScoreSettings::new
    );

    public static TournamentScoreSettings empty()
    {
        return new TournamentScoreSettings(0, 1, 2, 3, 4, 5, 1, 1);
    }
}