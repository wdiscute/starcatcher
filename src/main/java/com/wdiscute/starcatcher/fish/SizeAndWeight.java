package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record SizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage, float weightDeviation, float goldenChance)
{
    public float getSizeForPercentile(float percentile)
    {
        return (int) Math.round(sizeAverage + sizeDeviation * (1.0 - (percentile / 50.0)));
    }

    public float getWeightForPercentile(float percentile)
    {
        return (int) Math.round(weightAverage + weightDeviation * (1.0 - (percentile / 50.0)));
    }


    public enum Units
    {
        METRIC("gui.guide.units.metric", 1f, 1f),
        IMPERIAL("gui.guide.units.imperial", 0.3937f, 0.0352739619495804f),
        CHEESEBURGER("gui.guide.units.cheeseburger", 0.09f, 0.0087f),
        DEVELOPER("gui.guide.units.developer", 0.00592f, 0.0000140845f),
        BANANA("gui.guide.units.banana", 0.05f, 0.00833f),
        DUCK("gui.guide.units.duck", 0.02f, 0.0006667f),
        SPACE_WHALE("gui.guide.units.space_whale", 1f, 1f),
        SCIENTIFIC("gui.guide.units.scientific", 1f, 1f),
        ;

        private static final Units[] vals = values();
        private final String translationKey;
        private final float multiplierSize;
        private final float multiplierWeight;

        Units(String translationKey, float multiplierSize, float multiplierWeight)
        {
            this.translationKey = translationKey;
            this.multiplierSize = multiplierSize;
            this.multiplierWeight = multiplierWeight;
        }

        public String getTranslationKey()
        {
            return this.translationKey;
        }

        public float getMultiplierSize()
        {
            return this.multiplierSize;
        }

        public float getMultiplierWeight()
        {
            return this.multiplierWeight;
        }

        public Units next()
        {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Units previous()
        {
            if (this.ordinal() == 0) return vals[vals.length - 1];
            return vals[(this.ordinal() - 1) % vals.length];
        }

        public String getSizeAsString(float sizeInCm)
        {
            //space whale is always infinite
            if (this.equals(Units.SPACE_WHALE)) return "∞ space whales";
            if (this.equals(Units.SCIENTIFIC)) return "0 AU";

            float size = sizeInCm * this.getMultiplierSize();
            String sizeString = ((float) (int) (size * 100)) / 100 + " " + I18n.get(this.getTranslationKey() + ".size");

            if (this.equals(Units.METRIC))
            {
                sizeString = ((int) size) + "cm";
                if (size > 100) sizeString = (float) ((int) (size / 100 * 100)) / 100 + "m";
            }

            if (this.equals(Units.IMPERIAL))
            {
                sizeString = ((int) size) + "''";
                if (size > 12) sizeString = ((int) (size / 12)) + "'" + ((int) (size % 12)) + "''";
            }

            return sizeString;
        }

        public String getWeightAsString(float weightInGrams)
        {
            //space whale is always infinite
            if (this.equals(Units.SPACE_WHALE)) return "∞ space whales";
            if (this.equals(Units.SCIENTIFIC)) return "0 R136a1's";

            float weight = weightInGrams * this.getMultiplierWeight();
            String weightString = ((float) (int) (weight * 100)) / 100 + " " + I18n.get(this.getTranslationKey() + ".weight");

            if (this.equals(Units.METRIC))
            {
                if (weight <= 1000) weightString = ((int) weight) + "g";
                if (weight > 1000) weightString = (float) ((int) (weight / 1000 * 100)) / 100 + "kg";
            }

            if (this.equals(Units.IMPERIAL))
            {
                weightString = ((int) weight) + "oz";
                if (weight > 12) weightString = ((int) (weight / 16)) + " lb " + ((int) (weight % 16)) + " oz";
            }

            return weightString;
        }

    }

    public SizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage, float weightDeviation)
    {
        this(sizeAverage, sizeDeviation, weightAverage, weightDeviation, 0.02f);
    }

    public static final SizeAndWeight DEFAULT = new SizeAndWeight(41f, 21f, 2001f, 701f, 0.02f);
    public static final SizeAndWeight NONE = new SizeAndWeight(0, 0, 0, 0, 0);

    public static final Codec<SizeAndWeight> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("average_size_cm").forGetter(SizeAndWeight::sizeAverage),
                    Codec.FLOAT.fieldOf("deviation_size_cm").forGetter(SizeAndWeight::sizeDeviation),
                    Codec.FLOAT.fieldOf("average_weight_grams").forGetter(SizeAndWeight::weightAverage),
                    Codec.FLOAT.fieldOf("deviation_weight_grams").forGetter(SizeAndWeight::weightDeviation),
                    Codec.FLOAT.fieldOf("golden_chance").forGetter(SizeAndWeight::goldenChance)
            ).apply(instance, SizeAndWeight::new));

    public static final StreamCodec<ByteBuf, SizeAndWeight> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SizeAndWeight::sizeAverage,
            ByteBufCodecs.FLOAT, SizeAndWeight::sizeDeviation,
            ByteBufCodecs.FLOAT, SizeAndWeight::weightAverage,
            ByteBufCodecs.FLOAT, SizeAndWeight::weightDeviation,
            ByteBufCodecs.FLOAT, SizeAndWeight::goldenChance,
            SizeAndWeight::new
    );

    public static int getRandomSize(FishProperties fp, float percentile)
    {
        percentile = Mth.clamp(percentile, 0.01f, 99.999f);
        percentile = 100 - percentile;
        percentile = percentile / 100;
        float dev = fp.sizeWeight().sizeDeviation() * 2;
        float average = fp.sizeWeight().sizeAverage();

        return (int) (average + percentile * dev - dev / 2);
    }

    public static int getRandomWeight(FishProperties fp, float percentile)
    {
        percentile = Mth.clamp(percentile, 0.01f, 99.999f);
        percentile = 100 - percentile;
        percentile = percentile / 100;
        float dev = fp.sizeWeight().weightDeviation() * 2;
        float average = fp.sizeWeight().weightAverage();

        return (int) (average + percentile * dev - dev / 2);
    }
}
