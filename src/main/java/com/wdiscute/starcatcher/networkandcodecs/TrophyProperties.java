package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.ModItems;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record TrophyProperties
        (
                FishProperties fp,
                TrophyType trophyType,
                String customName,
                RarityProgress all,
                RarityProgress common,
                RarityProgress uncommon,
                RarityProgress rare,
                RarityProgress epic,
                RarityProgress legendary,
                int chanceToCatch
        )
{

    public static final TrophyProperties DEFAULT = new TrophyProperties(
            FishProperties.DEFAULT,
            TrophyType.EXTRA,
            "Missingno Trophy",
            RarityProgress.DEFAULT,
            RarityProgress.DEFAULT,
            RarityProgress.DEFAULT,
            RarityProgress.DEFAULT,
            RarityProgress.DEFAULT,
            RarityProgress.DEFAULT,
            100
            );

    public static final Codec<TrophyProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishProperties.CODEC.optionalFieldOf("fish_properties", DEFAULT.fp).forGetter(TrophyProperties::fp),
                    TrophyType.CODEC.optionalFieldOf("trophy_type", DEFAULT.trophyType).forGetter(TrophyProperties::trophyType),
                    Codec.STRING.optionalFieldOf("custom_name", DEFAULT.customName).forGetter(TrophyProperties::customName),
                    RarityProgress.CODEC.optionalFieldOf("all", DEFAULT.common).forGetter(TrophyProperties::all),
                    RarityProgress.CODEC.optionalFieldOf("common", DEFAULT.common).forGetter(TrophyProperties::common),
                    RarityProgress.CODEC.optionalFieldOf("uncommon", DEFAULT.common).forGetter(TrophyProperties::uncommon),
                    RarityProgress.CODEC.optionalFieldOf("rare", DEFAULT.common).forGetter(TrophyProperties::rare),
                    RarityProgress.CODEC.optionalFieldOf("epic", DEFAULT.common).forGetter(TrophyProperties::epic),
                    RarityProgress.CODEC.optionalFieldOf("legendary", DEFAULT.common).forGetter(TrophyProperties::legendary),
                    Codec.INT.optionalFieldOf("chance_to_catch", DEFAULT.chanceToCatch).forGetter(TrophyProperties::chanceToCatch)
            ).apply(instance, TrophyProperties::new)
    );

    public static final Codec<List<TrophyProperties>> LIST_CODEC = TrophyProperties.CODEC.listOf();

    public record RarityProgress(int total, int unique)
    {
        public static final Codec<RarityProgress> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                                Codec.INT.optionalFieldOf("total", 0).forGetter(RarityProgress::total),
                                Codec.INT.optionalFieldOf("unique", 0).forGetter(RarityProgress::unique)
                        ).apply(instance, RarityProgress::new));

        public static final RarityProgress DEFAULT = new RarityProgress(0, 0);
    }

    public enum TrophyType implements StringRepresentable
    {
        TROPHY("trophy"),
        SECRET("secret"),
        EXTRA("extra");

        public static final Codec<TrophyType> CODEC = StringRepresentable.fromEnum(TrophyType::values);
        private final String key;

        TrophyType(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }
    }

}
