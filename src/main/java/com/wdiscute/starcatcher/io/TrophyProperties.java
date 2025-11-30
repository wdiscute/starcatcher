package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TrophyProperties
        (
                FishProperties fp,
                boolean alwaysShow,
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
            FishProperties.DEFAULT.withFish(ModItems.MISSINGNO),
            false,
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
                    FishProperties.CODEC.fieldOf("fish_properties").forGetter(TrophyProperties::fp),
                    Codec.BOOL.fieldOf("always_show").forGetter(TrophyProperties::alwaysShow),
                    TrophyType.CODEC.fieldOf("trophy_type").forGetter(TrophyProperties::trophyType),
                    Codec.STRING.fieldOf("custom_name").forGetter(TrophyProperties::customName),
                    RarityProgress.CODEC.fieldOf("all").forGetter(TrophyProperties::all),
                    RarityProgress.CODEC.fieldOf("common").forGetter(TrophyProperties::common),
                    RarityProgress.CODEC.fieldOf("uncommon").forGetter(TrophyProperties::uncommon),
                    RarityProgress.CODEC.fieldOf("rare").forGetter(TrophyProperties::rare),
                    RarityProgress.CODEC.fieldOf("epic").forGetter(TrophyProperties::epic),
                    RarityProgress.CODEC.fieldOf("legendary").forGetter(TrophyProperties::legendary),
                    Codec.INT.fieldOf("chance_to_catch").forGetter(TrophyProperties::chanceToCatch)
            ).apply(instance, TrophyProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TrophyProperties> STREAM_CODEC = ExtraComposites.composite(
            FishProperties.STREAM_CODEC, TrophyProperties::fp,
            ByteBufCodecs.BOOL, TrophyProperties::alwaysShow,
            TrophyType.STREAM_CODEC, TrophyProperties::trophyType,
            ByteBufCodecs.STRING_UTF8, TrophyProperties::customName,
            RarityProgress.STREAM_CODEC, TrophyProperties::all,
            RarityProgress.STREAM_CODEC, TrophyProperties::common,
            RarityProgress.STREAM_CODEC, TrophyProperties::uncommon,
            RarityProgress.STREAM_CODEC, TrophyProperties::rare,
            RarityProgress.STREAM_CODEC, TrophyProperties::epic,
            RarityProgress.STREAM_CODEC, TrophyProperties::legendary,
            ByteBufCodecs.VAR_INT, TrophyProperties::chanceToCatch,
            TrophyProperties::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<TrophyProperties>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final Codec<List<TrophyProperties>> LIST_CODEC = TrophyProperties.CODEC.listOf();

    public record RarityProgress(int total, int unique)
    {
        public static final Codec<RarityProgress> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                                Codec.INT.fieldOf("total").forGetter(RarityProgress::total),
                                Codec.INT.fieldOf("unique").forGetter(RarityProgress::unique)
                        ).apply(instance, RarityProgress::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RarityProgress> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, RarityProgress::total,
                ByteBufCodecs.VAR_INT, RarityProgress::unique,
                RarityProgress::new
        );

        public static final RarityProgress DEFAULT = new RarityProgress(0, 0);
    }

    public enum TrophyType implements StringRepresentable
    {
        TROPHY("trophy"),
        SECRET("secret"),
        EXTRA("extra");

        public static final Codec<TrophyType> CODEC = StringRepresentable.fromEnum(TrophyType::values);
        public static final StreamCodec<FriendlyByteBuf, TrophyType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(TrophyType.class);
        private final String key;

        TrophyType(String key)
        {
            this.key = key;
        }

        public @NotNull String getSerializedName()
        {
            return this.key;
        }
    }

}
