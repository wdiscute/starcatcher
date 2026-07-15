package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.*;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

//      <><|    <- fish
public record FishProperties(
        CatchInfo catchInfo,
        int baseChance,
        SizeAndWeight sizeWeight,
        Rarity rarity,
        List<AbstractFishRestriction> restrictions,
        Difficulty dif,
        boolean skipMinigame,
        boolean hasGuideEntry,
        ResourceLocation textures
)
{
    public static final ResourceLocation SURFACE = Starcatcher.rl("textures/gui/minigame/surface.png");
    public static final ResourceLocation SKY = Starcatcher.rl("textures/gui/minigame/sky.png");
    public static final ResourceLocation LAVA_OVERWORLD = Starcatcher.rl("textures/gui/minigame/lava_overworld.png");
    public static final ResourceLocation NETHER = Starcatcher.rl("textures/gui/minigame/nether.png");
    public static final ResourceLocation CAVE = Starcatcher.rl("textures/gui/minigame/cave.png");
    public static final ResourceLocation ICY = Starcatcher.rl("textures/gui/minigame/icy.png");
    public static final ResourceLocation DEEP_DARK = Starcatcher.rl("textures/gui/minigame/deep_dark.png");
    public static final ResourceLocation END = Starcatcher.rl("textures/gui/minigame/end.png");
    public static final ResourceLocation END_VOID = Starcatcher.rl("textures/gui/minigame/end_void.png");

    public static final Codec<FishProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CatchInfo.CODEC.fieldOf("catch_info").forGetter(FishProperties::catchInfo),
                    Codec.INT.fieldOf("base_chance").forGetter(FishProperties::baseChance),
                    SizeAndWeight.CODEC.fieldOf("size_and_weight").forGetter(FishProperties::sizeWeight),
                    Rarity.CODEC.fieldOf("rarity").forGetter(FishProperties::rarity),
                    AbstractFishRestriction.ABSTRACT_PROCESSOR_CODEC.listOf().fieldOf("restrictions").forGetter(FishProperties::restrictions),
                    Difficulty.CODEC.fieldOf("difficulty").forGetter(FishProperties::dif),
                    Codec.BOOL.fieldOf("skips_minigame").forGetter(FishProperties::skipMinigame),
                    Codec.BOOL.fieldOf("has_guide_entry").forGetter(FishProperties::hasGuideEntry),
                    ResourceLocation.CODEC.fieldOf("textures").forGetter(FishProperties::textures)

            ).apply(instance, FishProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishProperties> STREAM_CODEC = ExtraComposites.composite(
            CatchInfo.STREAM_CODEC, FishProperties::catchInfo,
            ByteBufCodecs.VAR_INT, FishProperties::baseChance,
            SizeAndWeight.STREAM_CODEC, FishProperties::sizeWeight,
            Rarity.STREAM_CODEC, FishProperties::rarity,
            ByteBufCodecs.fromCodec(AbstractFishRestriction.ABSTRACT_PROCESSOR_CODEC.listOf()), FishProperties::restrictions,
            Difficulty.STREAM_CODEC, FishProperties::dif,
            ByteBufCodecs.BOOL, FishProperties::skipMinigame,
            ByteBufCodecs.BOOL, FishProperties::hasGuideEntry,
            ResourceLocation.STREAM_CODEC, FishProperties::textures,
            FishProperties::new
    );

    @Override
    public @NotNull String toString()
    {
        return "[FishProperties] " + getDisplayName().getString();
    }

    public Component getDisplayName()
    {
        if (catchInfo.alwaysSpawnEntity())
            return Component.translatable("entity." + catchInfo.entityToSpawn().getRegisteredName().replace(":", "."));
        else
            return Component.translatable(catchInfo.fish().toStack().getDescriptionId());
    }

    public ResourceLocation toLoc(Level level)
    {
        return level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(this);
    }

    public static FishProperties empty()
    {
        return new FishProperties(
                CatchInfo.DEFAULT,
                5,
                SizeAndWeight.DEFAULT,
                Rarity.COMMON,
                new ArrayList<>(),
                Difficulty.EASY,
                false,
                true,
                SURFACE
        );
    }

    //rod constructors
    public FishProperties withCatchInfo(CatchInfo catchInfo)
    {
        return new FishProperties(catchInfo, this.baseChance, this.sizeWeight, this.rarity,
                this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withBaseChance(int baseChance)
    {
        return new FishProperties(this.catchInfo, baseChance, this.sizeWeight, this.rarity,
                this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withSizeAndWeight(SizeAndWeight sizeAndWeight)
    {
        return new FishProperties(this.catchInfo, this.baseChance, sizeAndWeight, this.rarity,
                this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withSizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage,
                                            float weightDeviation)
    {
        return new FishProperties(this.catchInfo, this.baseChance,
                new SizeAndWeight(sizeAverage, sizeDeviation, weightAverage, weightDeviation), this.rarity,
                this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withRarity(Rarity rarity)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, rarity,
                this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withTextures(ResourceLocation textures)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity,
                this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, textures);
    }

    public FishProperties withHasGuideEntry(boolean hasGuideEntry)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity,
                this.restrictions, this.dif, this.skipMinigame, hasGuideEntry, this.textures);
    }

    public FishProperties withRestrictions(List<AbstractFishRestriction> restrictions)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity,
                restrictions, this.dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withDifficulty(Difficulty dif)
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity,
                this.restrictions, dif, this.skipMinigame, this.hasGuideEntry, this.textures);
    }

    public FishProperties withSkipsMinigame()
    {
        return new FishProperties(this.catchInfo, this.baseChance, this.sizeWeight, this.rarity,
                this.restrictions, this.dif, true, this.hasGuideEntry, this.textures);
    }

    public FishProperties addRestriction(AbstractFishRestriction restriction)
    {
        this.restrictions.add(restriction);
        return this;
    }

    public FishProperties addRestrictions(AbstractFishRestriction... restriction)
    {
        this.restrictions.addAll(List.of(restriction));
        return this;
    }

    public FishProperties withEntityToSpawn(Holder<EntityType<?>> entityTypeHolder)
    {
        return withCatchInfo(catchInfo.withEntityToSpawn(entityTypeHolder));
    }

    public FishProperties withEntityToSpawn(String namespace, String path)
    {
        return withCatchInfo(catchInfo.withEntityToSpawn(Utils.holderEntity(namespace, path)));
    }

    public FishProperties withPercentageChance(float chance)
    {
        restrictions.add(new ChancePercentageRestriction(chance));
        return this;
    }

    public FishProperties withItemToOverrideWith(MaybeStack override)
    {
        return withCatchInfo(catchInfo.withItemToOverrideWith(override));
    }

    public FishProperties withMaxLimit(int max)
    {
        restrictions.add(new CaughtLimitRestriction(max, ""));
        return this;
    }

    public FishProperties trophy()
    {
        return withCatchInfo(catchInfo.withFishType(CatchInfo.FishEntryType.TROPHY));
    }

    public FishProperties message()
    {
        return withCatchInfo(catchInfo.withFishType(CatchInfo.FishEntryType.MESSAGE));
    }

    public FishProperties extra()
    {
        return withCatchInfo(catchInfo.withFishType(CatchInfo.FishEntryType.EXTRA));
    }

    public FishProperties withFish(MaybeStack fish)
    {
        return withCatchInfo(catchInfo.withFish(fish));
    }

    public FishProperties withFish(ItemStack stack)
    {
        return withCatchInfo(catchInfo.withFish(new MaybeStack(stack)));
    }

    public FishProperties withFish(DeferredBlock<Block> fish)
    {
        return withCatchInfo(catchInfo.withFish(new MaybeStack(fish)));
    }

    public FishProperties withFish(Item fish)
    {
        return withCatchInfo(catchInfo.withFish(new MaybeStack(fish)));
    }

    public FishProperties withFish(String namespace, String path)
    {
        return withCatchInfo(catchInfo.withFish(new MaybeStack(namespace, path)));
    }

    public FishProperties withFish(DeferredItem<Item> fish)
    {
        return withCatchInfo(catchInfo.withFish(new MaybeStack(fish)));
    }

    public FishProperties withBucketedFish(MaybeStack bucket)
    {
        return withCatchInfo(catchInfo.withBucket(bucket));
    }

    public FishProperties withBucketedFish(String namespace, String path)
    {
        return withCatchInfo(catchInfo.withBucket(new MaybeStack(namespace, path)));
    }

    public FishProperties addRarityRestriction(RarityCountRestriction.RarityCount... rarityCount)
    {
        restrictions.add(new RarityCountRestriction(rarityCount));
        return this;
    }

    public FishProperties withAlwaysSpawnEntity()
    {
        return withCatchInfo(catchInfo.withAlwaysSpawnEntity());
    }

    public FishProperties addBait(BaitRestriction bait)
    {
        Map<ResourceLocation, Integer> map = new HashMap<>();
        AtomicReference<String> override = new AtomicReference<>();
        override.set("");

        //put baits already in the fp
        this.restrictions.forEach(o ->
        {
            if (o instanceof BaitRestriction be)
            {
                map.putAll(be.baits);
                override.set(be.translationOverride);
            }
        });

        //put baits from method param
        map.putAll(bait.baits);
        override.set(bait.translationOverride);

        List<AbstractFishRestriction> list = new ArrayList<>(this.restrictions);

        list.removeIf(o -> o instanceof BaitRestriction);
        list.add(new BaitRestriction(map, override.get()));

        return withRestrictions(list);
    }

    public FishProperties withWeather(WeatherRestriction rain)
    {
        this.restrictions.add(rain);
        return this;
    }

    public FishProperties withDaytimeRestriction(DaytimeRestriction midnight)
    {
        this.restrictions.add(midnight);
        return this;
    }

    public int calculateChance(Entity entity, Level level, ItemStack rod, AbstractFishRestriction.Context context)
    {
        return FishApi.calculateChance(this, entity, level, rod, context);
    }
}
