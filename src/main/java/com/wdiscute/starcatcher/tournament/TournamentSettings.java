package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;

public class TournamentSettings
{
    public Type type;
    public int duration;
    public float perfectCatchMultiplier;
    public int missPenalty;
    public List<SingleStackContainer> entryCost;

    public boolean canSignUp(Player player)
    {
        boolean canSignup = true;
        if(!entryCost.isEmpty())
        {
            for (SingleStackContainer ssc : entryCost)
            {
                if(!player.getInventory().hasAnyMatching(is -> is.is(ssc.stack().getItem()) && is.getCount() >= ssc.stack().getCount()))
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

    public Type getType()
    {
        return type;
    }

    public List<SingleStackContainer> getEntryCost()
    {
        return entryCost;
    }

    public int getDuration()
    {
        return duration;
    }

    public enum Type implements StringRepresentable
    {
        SIMPLE("simple"),
        SCORE_BASED("score_based");

        Type(String name)
        {
            this.key = name;
        }

        public String toString()
        {
            return this.key;
        }

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        public static final StreamCodec<RegistryFriendlyByteBuf, Type> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Type.class);
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }
    }

    public TournamentSettings(Type type, int duration, float perfectCatchMultiplier, int missPenalty, List<SingleStackContainer> entryCost)
    {
        this.type = type;
        this.duration = duration;
        this.perfectCatchMultiplier = perfectCatchMultiplier;
        this.missPenalty = missPenalty;
        this.entryCost = entryCost;
    }

    public static final Codec<TournamentSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Type.CODEC.optionalFieldOf("type", Type.SIMPLE).forGetter(TournamentSettings::getType),
                    Codec.INT.optionalFieldOf("duration", 0).forGetter(TournamentSettings::getDuration),
                    Codec.FLOAT.optionalFieldOf("perfect_catch_multiplier", 0.0f).forGetter(TournamentSettings::getPerfectCatchMultiplier),
                    Codec.INT.optionalFieldOf("miss_penalty", 0).forGetter(TournamentSettings::getMissPenalty),
                    SingleStackContainer.LIST_CODEC.optionalFieldOf("", List.of()).forGetter(TournamentSettings::getEntryCost)
            ).apply(instance, TournamentSettings::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TournamentSettings> STREAM_CODEC = StreamCodec.composite(
            Type.STREAM_CODEC, TournamentSettings::getType,
            ByteBufCodecs.INT, TournamentSettings::getDuration,
            ByteBufCodecs.FLOAT, TournamentSettings::getPerfectCatchMultiplier,
            ByteBufCodecs.VAR_INT, TournamentSettings::getMissPenalty,
            SingleStackContainer.STREAM_CODEC_LIST, TournamentSettings::getEntryCost,
            TournamentSettings::new
    );
}
