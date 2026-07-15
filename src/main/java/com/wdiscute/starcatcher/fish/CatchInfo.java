package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.data.ExtraComposites;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record CatchInfo(
        MaybeStack fish,
        MaybeStack bucketedFish,
        Holder<EntityType<?>> entityToSpawn,
        boolean alwaysSpawnEntity,
        MaybeStack overrideMinigameWith,
        FishEntryType fishEntryType
)
{

    public enum FishEntryType implements StringRepresentable
    {
        FISH("fish"),
        TROPHY("trophy"),
        MESSAGE("message"),
        EXTRA("extra");

        public static final Codec<FishEntryType> CODEC = StringRepresentable.fromEnum(FishEntryType::values);
        public static final StreamCodec<RegistryFriendlyByteBuf, FishEntryType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(FishEntryType.class);
        private final String name;

        FishEntryType(String key)
        {
            this.name = key;
        }

        public String getSerializedName()
        {
            return this.name;
        }
    }

    public static final Codec<CatchInfo> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    MaybeStack.CODEC.fieldOf("item").forGetter(CatchInfo::fish),
                    MaybeStack.CODEC.optionalFieldOf("fish_bucket", MaybeStack.EMPTY).forGetter(CatchInfo::bucketedFish),
                    BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().optionalFieldOf("entity", SCEntities.FISH).forGetter(CatchInfo::entityToSpawn),
                    Codec.BOOL.optionalFieldOf("always_spawn_entity", false).forGetter(CatchInfo::alwaysSpawnEntity),
                    MaybeStack.CODEC.optionalFieldOf("override_minigame_item", MaybeStack.EMPTY).forGetter(CatchInfo::overrideMinigameWith),
                    FishEntryType.CODEC.optionalFieldOf("type", FishEntryType.FISH).forGetter(CatchInfo::fishEntryType)
            ).apply(instance, CatchInfo::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CatchInfo> STREAM_CODEC = ExtraComposites.composite(
            MaybeStack.STREAM_CODEC, CatchInfo::fish,
            MaybeStack.STREAM_CODEC, CatchInfo::bucketedFish,
            ByteBufCodecs.holderRegistry(Registries.ENTITY_TYPE), CatchInfo::entityToSpawn,
            ByteBufCodecs.BOOL, CatchInfo::alwaysSpawnEntity,
            MaybeStack.STREAM_CODEC, CatchInfo::overrideMinigameWith,
            FishEntryType.STREAM_CODEC, CatchInfo::fishEntryType,
            CatchInfo::new
    );

    public static final CatchInfo DEFAULT = new CatchInfo(
            MaybeStack.EMPTY,
            MaybeStack.EMPTY,
            SCEntities.FISH,
            false,
            MaybeStack.EMPTY,
            FishEntryType.FISH
    );

    public CatchInfo withFish(MaybeStack fish)
    {
        return new CatchInfo(fish, this.bucketedFish, this.entityToSpawn, alwaysSpawnEntity,
                this.overrideMinigameWith, this.fishEntryType);
    }

    public CatchInfo withBucket(MaybeStack bucket)
    {
        return new CatchInfo(this.fish, bucket, this.entityToSpawn, alwaysSpawnEntity,
                this.overrideMinigameWith, this.fishEntryType);
    }

    public CatchInfo withEntityToSpawn(Holder<EntityType<?>> entityToSpawn)
    {
        return new CatchInfo(this.fish, this.bucketedFish, entityToSpawn, alwaysSpawnEntity,
                this.overrideMinigameWith, this.fishEntryType);
    }

    public CatchInfo withAlwaysSpawnEntity()
    {
        return new CatchInfo(this.fish, this.bucketedFish, this.entityToSpawn, true,
                this.overrideMinigameWith, this.fishEntryType);
    }

    public CatchInfo withFishType(FishEntryType type)
    {
        return new CatchInfo(this.fish, this.bucketedFish, this.entityToSpawn, this.alwaysSpawnEntity,
                this.overrideMinigameWith, type);
    }

    public CatchInfo withItemToOverrideWith(MaybeStack itemToOverrideWith)
    {
        return new CatchInfo(this.fish, this.bucketedFish, this.entityToSpawn, alwaysSpawnEntity,
                itemToOverrideWith, this.fishEntryType);
    }
}
