package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TrophyProperties
        (
                TrophyType type,
                ResourceLocation rl,
                int UniqueFishCount,
                int TotalCaughtCount
        )
{


    public static final Codec<TrophyProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TrophyType.CODEC.fieldOf("trophy_type").forGetter(TrophyProperties::type),
                    ResourceLocation.CODEC.fieldOf("resource_location").forGetter(TrophyProperties::rl),
                    Codec.INT.optionalFieldOf("unique_fishes", Integer.MIN_VALUE).forGetter(TrophyProperties::UniqueFishCount),
                    Codec.INT.optionalFieldOf("total_fishes", Integer.MIN_VALUE).forGetter(TrophyProperties::TotalCaughtCount)
            ).apply(instance, TrophyProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TrophyProperties> STREAM_CODEC = StreamCodec.composite(
            TrophyType.STREAM_CODEC, TrophyProperties::type,
            ResourceLocation.STREAM_CODEC, TrophyProperties::rl,
            ByteBufCodecs.VAR_INT, TrophyProperties::UniqueFishCount,
            ByteBufCodecs.VAR_INT, TrophyProperties::TotalCaughtCount,
            TrophyProperties::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<TrophyProperties>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    public static final Codec<List<TrophyProperties>> LIST_CODEC = TrophyProperties.CODEC.listOf();


    public static final TrophyProperties DEFAULT = new TrophyProperties(TrophyType.ITEM, Starcatcher.rl("missingno"), Integer.MAX_VALUE, Integer.MAX_VALUE);

    public enum TrophyType implements StringRepresentable
    {
        ITEM("item"),
        ENTITY("entity"),
        ADVANCEMENT("advancement");

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
