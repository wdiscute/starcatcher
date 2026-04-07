package com.wdiscute.starcatcher.registry;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.compat.QualityFoodCompat;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.registry.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.starcatcher.registry.minigamemodifiers.*;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.SCSweetSpotsBehaviour;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

//      <><|    <- fish
public record FishProperties(
        CatchInfo catchInfo,
        Star star,
        int baseChance,
        SizeAndWeight sizeWeight,
        Rarity rarity,
        List<AbstractFishRestriction> restrictions,
        Difficulty dif,
        boolean skipMinigame,
        boolean hasGuideEntry
)
{
    public static final Codec<FishProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CatchInfo.CODEC.fieldOf("catch_info").forGetter(FishProperties::catchInfo),
                    Star.CODEC.optionalFieldOf("star", Star.DEFAULT).forGetter(FishProperties::star),
                    Codec.INT.fieldOf("base_chance").forGetter(FishProperties::baseChance),
                    SizeAndWeight.CODEC.fieldOf("size_and_weight").forGetter(FishProperties::sizeWeight),
                    Rarity.CODEC.fieldOf("rarity").forGetter(FishProperties::rarity),
                    AbstractFishRestriction.ABSTRACT_PROCESSOR_CODEC.listOf().fieldOf("restrictions").forGetter(FishProperties::restrictions),
                    Difficulty.CODEC.fieldOf("difficulty").forGetter(FishProperties::dif),
                    Codec.BOOL.fieldOf("skips_minigame").forGetter(FishProperties::skipMinigame),
                    Codec.BOOL.fieldOf("has_guide_entry").forGetter(FishProperties::hasGuideEntry)

            ).apply(instance, FishProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishProperties> STREAM_CODEC = ExtraComposites.composite(
            CatchInfo.STREAM_CODEC, FishProperties::catchInfo,
            Star.STREAM_CODEC, FishProperties::star,
            ByteBufCodecs.VAR_INT, FishProperties::baseChance,
            SizeAndWeight.STREAM_CODEC, FishProperties::sizeWeight,
            Rarity.STREAM_CODEC, FishProperties::rarity,
            ByteBufCodecs.fromCodec(AbstractFishRestriction.ABSTRACT_PROCESSOR_CODEC.listOf()), FishProperties::restrictions,
            Difficulty.STREAM_CODEC, FishProperties::dif,
            ByteBufCodecs.BOOL, FishProperties::skipMinigame,
            ByteBufCodecs.BOOL, FishProperties::hasGuideEntry,
            FishProperties::new
    );

    @Override
    public @NotNull String toString()
    {
        if (catchInfo.alwaysSpawnEntity)
            return "[FishProperties] " + catchInfo.entityToSpawn;
        else
            return "[FishProperties] " + catchInfo.fish;
    }

    public Component getDisplayName()
    {
        if (catchInfo.alwaysSpawnEntity)
            return Component.translatable("entity." + catchInfo.entityToSpawn.getRegisteredName().replace(":", "."));
        else
            return Component.translatable(catchInfo.fish.value().getDescriptionId());
    }

    public FishProperties loadTreasure(ServerPlayer player)
    {
        //if treasure itemstack already exists, dont do anything
        if(!catchInfo.treasureIs.isEmpty()) return this;

        //otherwise create itemstack from loot pool
        LootParams lootparams = new LootParams.Builder((ServerLevel) player.level())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withLuck(player.getLuck())
                .create(LootContextParamSets.FISHING);

        LootTable table = player.level().getServer().reloadableRegistries().getLootTable(
                ResourceKey.create(Registries.LOOT_TABLE, catchInfo.treasure)
        );

        ObjectArrayList<ItemStack> randomItems = table.getRandomItems(lootparams);

        System.out.println(randomItems);

        return new FishProperties(
                new CatchInfo(catchInfo.fish, catchInfo.bucketedFish, catchInfo.entityToSpawn, catchInfo.alwaysSpawnEntity,
                        catchInfo.overrideMinigameWith, catchInfo.treasure, randomItems.get(0), catchInfo.fishEntryType),
                star,
                baseChance,
                sizeWeight,
                rarity,
                restrictions,
                dif,
                skipMinigame,
                hasGuideEntry
        );
    }

    public ResourceLocation toLoc(Level level)
    {
        return U.getRlFromFp(level, this);
    }

    /**
     * @deprecated use Builder instead
     */
    @Deprecated(forRemoval = true)
    public static final FishProperties DEFAULT = new FishProperties(
            CatchInfo.DEFAULT,
            Star.DEFAULT,
            5,
            SizeAndWeight.DEFAULT,
            Rarity.COMMON,
            new ArrayList<>(),
            Difficulty.EASY,
            false,
            true
    );

    public static final FishProperties VANILLA_FISH = new FishProperties(
            CatchInfo.VANILLA,
            Star.DEFAULT,
            -348,
            SizeAndWeight.DEFAULT,
            Rarity.COMMON,
            new ArrayList<>(),
            Difficulty.EASY,
            false,
            false
    );

    public FishProperties withHideCatch()
    {
        return new FishProperties(this.catchInfo.withItemToOverrideWith(SCItems.UNKNOWN_FISH), this.star, this.baseChance, this.sizeWeight, this.rarity, this.restrictions, this.dif, this.skipMinigame, this.hasGuideEntry);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private CatchInfo.Builder catchInfo = new CatchInfo.Builder();
        private Star star = Star.DEFAULT;
        private int baseChance = 5;

        private SizeAndWeight sw = SizeAndWeight.DEFAULT;
        private Rarity rarity = Rarity.COMMON;
        private List<AbstractFishRestriction> restrictions = new ArrayList<>();
        private Difficulty dif = Difficulty.EASY;
        private boolean skipMinigame = false;
        private boolean hasGuideEntry = true;

        public Builder withCatchInfo(CatchInfo.Builder builder)
        {
            this.catchInfo = builder;
            return this;
        }

        public Builder withFish(Holder<Item> fish)
        {
            this.catchInfo.withFish(fish);
            return this;
        }

        public Builder withTreasure(ResourceLocation treasure)
        {
            this.catchInfo.treasure = treasure;
            return this;
        }

        public Builder withTreasure(ItemStack itemStack)
        {
            this.catchInfo.treasureIs = itemStack;
            return this;
        }

        public Builder withBucketedFish(Holder<Item> bucketedFish)
        {
            this.catchInfo.withBucketedFish(bucketedFish);
            return this;
        }

        public Builder withEntityToSpawn(Holder<EntityType<?>> entity)
        {
            this.catchInfo.withEntityToSpawn(entity);
            return this;
        }

        public Builder withSeasons(SeasonRestriction seasonRestriction)
        {
            addRestrictions(seasonRestriction);
            return this;
        }

        public Builder withAlwaysSpawnEntity()
        {
            this.catchInfo.withAlwaysSpawnEntity(true);
            return this;
        }

        public Builder withAlwaysSpawnEntity(boolean alwaysSpawnEntity)
        {
            this.catchInfo.withAlwaysSpawnEntity(alwaysSpawnEntity);
            return this;
        }

        public Builder withItemToOverrideWith(Holder<Item> itemToOverrideWith)
        {
            this.catchInfo.withOverrideMinigameWith(itemToOverrideWith);
            return this;
        }

        public Builder withStar(Star star)
        {
            this.star = star;
            return this;
        }

        public Builder withBaseChance(int baseChance)
        {
            this.baseChance = baseChance;
            return this;
        }

        public Builder withSizeAndWeight(SizeAndWeight sizeAndWeight)
        {
            this.sw = sizeAndWeight;
            return this;
        }

        public Builder withRarity(Rarity rarity)
        {
            this.rarity = rarity;
            return this;
        }

        public Builder addRestrictions(AbstractFishRestriction... restriction)
        {
            this.restrictions.addAll(Arrays.stream(restriction).toList());
            return this;
        }

        public Builder addRestrictions(List<AbstractFishRestriction> restriction)
        {
            this.restrictions.addAll(restriction);
            return this;
        }

        public Builder withDifficulty(Difficulty newDif)
        {
            List<Supplier<Supplier<AbstractMinigameModifier>>> old = List.copyOf(this.dif.modifiers);
            this.dif = newDif.addModifiers(old);
            return this;
        }

        public Builder addModifier(List<Supplier<Supplier<AbstractMinigameModifier>>> modifiers)
        {
            this.dif = dif.addModifiers(modifiers);
            return this;
        }

        public Builder addModifier(Supplier<Supplier<AbstractMinigameModifier>> modifier)
        {
            this.dif = dif.addModifiers(List.of(modifier));
            return this;
        }

        public Builder withDaytimeRestriction(DaytimeRestriction daytime)
        {
            addRestrictions(daytime);
            return this;
        }

        public Builder withWeather(WeatherRestriction weather)
        {
            addRestrictions(weather);
            return this;
        }

        public Builder withSkipMinigame(boolean skipMinigame)
        {
            this.skipMinigame = skipMinigame;
            return this;
        }

        public Builder withHasGuideEntry(boolean hasGuideEntry)
        {
            this.hasGuideEntry = hasGuideEntry;
            return this;
        }

        public FishProperties build()
        {
            return new FishProperties(
                    catchInfo.build(),
                    star,
                    baseChance,
                    sw,
                    rarity,
                    restrictions,
                    dif,
                    skipMinigame,
                    hasGuideEntry
            );
        }
    }

    //region CatchInfo
    public record CatchInfo(
            Holder<Item> fish,
            Holder<Item> bucketedFish,
            Holder<EntityType<?>> entityToSpawn,
            boolean alwaysSpawnEntity,
            Holder<Item> overrideMinigameWith,
            ResourceLocation treasure,
            ItemStack treasureIs,
            FishEntryType fishEntryType
    )
    {
        public enum FishEntryType implements StringRepresentable
        {
            FISH("fish"),
            TROPHY("trophy"),
            SECRET("secret"),
            OTHER("secret");

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
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(CatchInfo::fish),
                        BuiltInRegistries.ITEM.holderByNameCodec().optionalFieldOf("fish_bucket", SCItems.MISSINGNO).forGetter(CatchInfo::bucketedFish),
                        BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().optionalFieldOf("entity", SCEntities.FISH).forGetter(CatchInfo::entityToSpawn),
                        Codec.BOOL.optionalFieldOf("always_spawn_entity", false).forGetter(CatchInfo::alwaysSpawnEntity),
                        BuiltInRegistries.ITEM.holderByNameCodec().optionalFieldOf("override_minigame_item", SCItems.MISSINGNO).forGetter(CatchInfo::overrideMinigameWith),
                        ResourceLocation.CODEC.optionalFieldOf("treasure_loot_table", U.rl("gameplay/fishing/treasure")).forGetter(CatchInfo::treasure),
                        ItemStack.OPTIONAL_CODEC.optionalFieldOf("treasure_item", ItemStack.EMPTY).forGetter(CatchInfo::treasureIs),
                        FishEntryType.CODEC.optionalFieldOf("type", FishEntryType.FISH).forGetter(CatchInfo::fishEntryType)
                ).apply(instance, CatchInfo::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CatchInfo> STREAM_CODEC = ExtraComposites.composite(
                ByteBufCodecs.holderRegistry(Registries.ITEM), CatchInfo::fish,
                ByteBufCodecs.holderRegistry(Registries.ITEM), CatchInfo::bucketedFish,
                ByteBufCodecs.holderRegistry(Registries.ENTITY_TYPE), CatchInfo::entityToSpawn,
                ByteBufCodecs.BOOL, CatchInfo::alwaysSpawnEntity,
                ByteBufCodecs.holderRegistry(Registries.ITEM), CatchInfo::overrideMinigameWith,
                ResourceLocation.STREAM_CODEC, CatchInfo::treasure,
                ItemStack.OPTIONAL_STREAM_CODEC, CatchInfo::treasureIs,
                FishEntryType.STREAM_CODEC, CatchInfo::fishEntryType,
                CatchInfo::new
        );

        public static final CatchInfo DEFAULT = new CatchInfo(
                SCItems.MISSINGNO,
                SCItems.MISSINGNO,
                //cant use entity reference as its not registered for the psf
                U.holderEntity("starcatcher", "fish"),
                false,
                SCItems.MISSINGNO,
                U.rl("gameplay/fishing/treasure"),
                ItemStack.EMPTY,
                FishEntryType.FISH
        );

        public static final CatchInfo VANILLA = new CatchInfo(
                SCItems.MISSINGNO,
                SCItems.MISSINGNO,
                U.holderEntity("starcatcher", "fish"),
                false,
                SCItems.UNKNOWN_FISH,
                U.rl("gameplay/fishing/treasure"),
                ItemStack.EMPTY,
                FishEntryType.FISH
        );

        public CatchInfo withItemToOverrideWith(Holder<Item> itemToOverrideWith)
        {
            return new CatchInfo(this.fish, this.bucketedFish, this.entityToSpawn, alwaysSpawnEntity, itemToOverrideWith, this.treasure, this.treasureIs, this.fishEntryType);
        }

        public static class Builder
        {
            private Holder<Item> fish = SCItems.MISSINGNO;
            private Holder<Item> bucketedFish = SCItems.MISSINGNO;
            private Holder<EntityType<?>> entityToSpawn = U.holderEntity("starcatcher", "fish");
            private boolean alwaysSpawnEntity = false;
            private Holder<Item> itemToOverrideWith = SCItems.MISSINGNO;
            private ResourceLocation treasure = U.rl("gameplay/fishing/treasure");
            private ItemStack treasureIs = ItemStack.EMPTY;
            private FishEntryType fishEntryType = FishEntryType.FISH;

            public Builder withFish(Holder<Item> fish)
            {
                this.fish = fish;
                return this;
            }

            public Builder withBucketedFish(Holder<Item> bucketedFish)
            {
                this.bucketedFish = bucketedFish;
                return this;
            }

            public Builder withEntityToSpawn(Holder<EntityType<?>> entityToSpawn)
            {
                this.entityToSpawn = entityToSpawn;
                return this;
            }

            public Builder withAlwaysSpawnEntity(boolean alwaysSpawnEntity)
            {
                this.alwaysSpawnEntity = alwaysSpawnEntity;
                return this;
            }

            public Builder withOverrideMinigameWith(Holder<Item> itemToOverrideWith)
            {
                this.itemToOverrideWith = itemToOverrideWith;
                return this;
            }

            public Builder withFishEntryType(FishEntryType fishEntryType)
            {
                this.fishEntryType = fishEntryType;
                return this;
            }

            public Builder withTreasure(ResourceLocation rl)
            {
                treasure = rl;
                return this;
            }

            public Builder withTreasure(ItemStack is)
            {
                treasureIs = is;
                return this;
            }

            public CatchInfo build()
            {
                return new CatchInfo(fish, bucketedFish, entityToSpawn, alwaysSpawnEntity, itemToOverrideWith, treasure, treasureIs, fishEntryType);
            }
        }
    }

    //endregion CatchInfo

    //region Star

    public record Star(
            String name,
            int x,
            int y,
            List<String> connections,
            int debugColor
    )
    {
        public static Star fromRaAndDec(String name, int degrees, float dec, int color, String... connections)
        {
            double angleX = Math.cos(Math.toRadians(degrees));
            double angleY = Math.sin(Math.toRadians(degrees));

            int offsetX = dec > 0 ? 2700 : 900;
            int offsetY = 900;

            dec = transform(dec);

            int x = (int) (offsetX + angleX * Math.abs(dec) * 10);
            int y = (int) (offsetY + angleY * Math.abs(dec) * 10);

            return new Star(name, x, y, Arrays.stream(connections).filter(o -> !o.isEmpty()).toList(), color);
        }

        public static float transform(float v)
        {
            if (v == 0f) return 0f;

            float abs = Math.abs(v);

            if (abs > 90f)
            {
                throw new IllegalArgumentException("Value must be between -90 and 90");
            }

            return Math.copySign(90f - abs, v);
        }

        public static Star fromRaAndDec(String name, int hours, int minutes, double seconds, float dec, int color, String... connections)
        {
            double decimalHours = hours + minutes / 60.0 + seconds / 3600.0;
            int deg = ((int) (decimalHours * 15.0));

            return fromRaAndDec(name, deg, dec, color, connections);
        }

        public static final Codec<Star> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(Star::name),
                        ExtraCodecs.intRange(0, 3600).fieldOf("x").forGetter(Star::x),
                        ExtraCodecs.intRange(0, 1800).fieldOf("y").forGetter(Star::y),
                        Codec.STRING.listOf().fieldOf("connections").forGetter(Star::connections),
                        Codec.INT.fieldOf("debug_color").forGetter(Star::debugColor)
                ).apply(instance, Star::new));

        public static final StreamCodec<ByteBuf, Star> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Star::name,
                ByteBufCodecs.INT, Star::x,
                ByteBufCodecs.INT, Star::y,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), Star::connections,
                ByteBufCodecs.INT, Star::debugColor,
                Star::new
        );

        public static final Star DEFAULT = new Star("", 0, 0, List.of(), 0xffffffff);
    }

    //endregion Star


    public static class WorldRestrictions
    {
        public static final List<AbstractFishRestriction> OVERWORLD =
                List.of(
                        DimensionRestriction.OVERWORLD
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LUSH_CAVES =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.LUSH_CAVES,
                        ElevationRestriction.BELOW_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LUSH_CAVES_AND_JUNGLES =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.LUSH_CAVES_AND_JUNGLES,
                        ElevationRestriction.BELOW_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_BAMBOO_JUNGLE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.BAMBOO_JUNGLE,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_STONE_CAVES =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.ZERO_TO_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_DEEPSLATE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.BELOW_ZERO
                );

        public static final List<AbstractFishRestriction> OVERWORLD_DRIPSTONE_CAVES =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.DRIPSTONE_CAVES,
                        ElevationRestriction.BELOW_ZERO
                );

        public static final List<AbstractFishRestriction> OVERWORLD_DEEP_DARK =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.DEEP_DARK,
                        ElevationRestriction.BELOW_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_RIVER =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.RIVERS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_ALL_OCEANS =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.ALL_OCEANS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_OCEAN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.NORMAL_OCEANS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LUKEWARM_OCEAN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.LUKEWARM_OCEAN,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_COLD_AND_LUKEWARM_OCEAN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.COLD_AND_LUKEWARM_OCEAN,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_WARM_OCEAN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.WARM_OCEANS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_DEEP_OCEAN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.DEEP_OCEANS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_MOUNTAIN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.LAKES,
                        ElevationRestriction.ABOVE_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LAKE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.LAKES,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_WARM_LAKE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.WARM_LAKES,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_SAVANNA =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.SAVANNAS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_COLD_RIVER =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.COLD_RIVERS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_COLD_OCEAN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.COLD_OCEANS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_COLD_LAKE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.COLD_LAKES,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_COLD_MOUNTAIN =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.COLD_LAKES,
                        ElevationRestriction.ABOVE_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_BEACH =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.BEACHES,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_MUSHROOM_FIELDS =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.MUSHROOM_FIELDS,
                        ElevationRestriction.FIFTY_TO_HUNDRED
                );

        public static final List<AbstractFishRestriction> OVERWORLD_JUNGLE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.JUNGLES,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_TAIGA =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.TAIGAS,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_CHERRY_GROVE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.CHERRY_GROVES,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_JUNGLES_AND_SWAMPS =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.JUNGLES_AND_SWAMPS,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_SWAMPS =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.SWAMPS,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_SWAMP_ONLY =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.SWAMP_ONLY,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_MANGROVE_SWAMP =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.MANGROVE_SWAMP,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_DARK_FOREST =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.DARK_FOREST,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_FOREST =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        BiomeRestriction.FOREST,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_SURFACE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.ABOVE_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LAVA_SURFACE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.ABOVE_FIFTY,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LAVA_UNDERGROUND =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.BELOW_FIFTY,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> OVERWORLD_UNDERGROUND =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.BELOW_FIFTY
                );

        public static final List<AbstractFishRestriction> OVERWORLD_LAVA_DEEPSLATE =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.BELOW_ZERO,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> NETHER_LAVA =
                List.of(
                        DimensionRestriction.NETHER,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> NETHER_LAVA_CRIMSON_FOREST =
                List.of(
                        DimensionRestriction.NETHER,
                        BiomeRestriction.CRIMSON_FOREST,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> NETHER_LAVA_WARPED_FOREST =
                List.of(
                        DimensionRestriction.NETHER,
                        BiomeRestriction.WARPED_FOREST,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> NETHER_LAVA_SOUL_SAND_VALLEY =
                List.of(
                        DimensionRestriction.NETHER,
                        BiomeRestriction.SOUL_SAND_VALLEY,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> NETHER_LAVA_BASALT_DELTAS =
                List.of(
                        DimensionRestriction.NETHER,
                        BiomeRestriction.BASALT_DELTAS,
                        FluidRestriction.LAVA
                );

        public static final List<AbstractFishRestriction> END =
                List.of(
                        DimensionRestriction.END
                );

        public static final List<AbstractFishRestriction> END_OUTER_ISLANDS =
                List.of(
                        DimensionRestriction.END,
                        BiomeRestriction.OUTER_ISLANDS
                );

        public static final List<AbstractFishRestriction> OVERWORLD_VOID =
                List.of(
                        DimensionRestriction.OVERWORLD,
                        ElevationRestriction.BELOW_MINUS_SIXTY_FOUR,
                        FluidRestriction.VOID
                );

        public static final List<AbstractFishRestriction> NETHER_VOID =
                List.of(
                        DimensionRestriction.NETHER,
                        ElevationRestriction.BELOW_ZERO,
                        FluidRestriction.VOID
                );

        public static final List<AbstractFishRestriction> END_VOID =
                List.of(
                        DimensionRestriction.END,
                        FluidRestriction.VOID
                );

    }

    //region dif
    public record Difficulty(
            int speed,
            int penalty,
            float decay,
            List<Supplier<Supplier<AbstractMinigameModifier>>> modifiers,
            List<SweetSpot> sweetSpots
    )
    {

        public Difficulty(int speed, int penalty, float decay, List<Supplier<Supplier<AbstractMinigameModifier>>> modifiers, SweetSpot... sweetSpots)
        {
            this(speed, penalty, decay, modifiers, Arrays.stream(sweetSpots).toList());
        }

        public Difficulty addModifiers(List<Supplier<Supplier<AbstractMinigameModifier>>> newModifier)
        {
            List<Supplier<Supplier<AbstractMinigameModifier>>> list = new ArrayList<>();
            list.addAll(newModifier);
            list.addAll(this.modifiers);
            return new Difficulty(this.speed, this.penalty, this.decay, list, this.sweetSpots);
        }

        public Difficulty vanishing(float vanishingRate)
        {
            List<SweetSpot> sss = new ArrayList<>();
            sweetSpots.forEach(s -> sss.add(s.vanishing(vanishingRate)));
            return new Difficulty(speed, penalty, decay, modifiers, sss);
        }

        public Difficulty vanishing()
        {
            List<SweetSpot> sss = new ArrayList<>();
            sweetSpots.forEach(s -> sss.add(s.vanishing(0.1f)));
            return new Difficulty(speed, penalty, decay, modifiers, sss);
        }

        public Difficulty moving(float movingRate)
        {
            List<SweetSpot> sss = new ArrayList<>();
            sweetSpots.forEach(s -> sss.add(s.moving(movingRate)));
            return new Difficulty(speed, penalty, decay, modifiers, sss);
        }

        public Difficulty moving()
        {
            List<SweetSpot> sss = new ArrayList<>();
            sweetSpots.forEach(s -> sss.add(s.moving(1)));
            return new Difficulty(speed, penalty, decay, modifiers, sss);
        }

        public Difficulty flip()
        {
            List<SweetSpot> sss = new ArrayList<>();
            sweetSpots.forEach(s -> sss.add(s.flip()));
            return new Difficulty(speed, penalty, decay, modifiers, sss);
        }


        //region preset difficulties

        public static Difficulty TRASH = new Difficulty(
                10, 0, 0,
                List.of(),
                SweetSpot.TRASH, SweetSpot.TRASH
        );

        public static Difficulty EASY = new Difficulty(
                9, 5, 1,
                List.of(),
                SweetSpot.NORMAL, SweetSpot.NORMAL
        );
        public static Difficulty EASY_VANISHING = EASY.vanishing();
        public static Difficulty EASY_MOVING = EASY.moving();

        public static Difficulty MEDIUM = new Difficulty(
                10, 20, 1,
                List.of(),
                SweetSpot.NORMAL, SweetSpot.THIN
        );
        public static Difficulty MEDIUM_VANISHING = MEDIUM.vanishing();
        public static Difficulty MEDIUM_MOVING = MEDIUM.moving();
        public static Difficulty MEDIUM_VANISHING_MOVING = MEDIUM.moving().vanishing();

        public static Difficulty HARD = new Difficulty(
                11, 10, 1,
                List.of(),
                SweetSpot.THIN, SweetSpot.THIN);
        public static Difficulty HARD_VANISHING = HARD.vanishing();
        public static Difficulty HARD_MOVING = HARD.moving();

        public static Difficulty THIN_NO_DECAY_NOT_FORGIVING = new Difficulty(
                11, 40, 0,
                List.of(),
                SweetSpot.THIN, SweetSpot.THIN
        );

        public static Difficulty HEAVY_FIVE_NORMAL = new Difficulty(
                5, 40, 0,
                List.of(),
                SweetSpot.NORMAL_HEAVY, SweetSpot.NORMAL_HEAVY, SweetSpot.NORMAL_HEAVY, SweetSpot.NORMAL_HEAVY, SweetSpot.NORMAL_HEAVY
        );

        public static Difficulty FOUR_BIG = new Difficulty(
                9, 20, 0,
                List.of(),
                SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL
        );
        public static Difficulty FOUR_BIG_VANISHING = FOUR_BIG.vanishing();
        public static Difficulty FOUR_BIG_MOVING = FOUR_BIG.moving();

        public static Difficulty HEAVY_EIGHT_AQUA = new Difficulty(
                12, 20, 0,
                List.of(),
                SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1
        );

        public static Difficulty HEAVY_EIGHT_AQUA_MOVING = new Difficulty(
                12, 20, 0,
                List.of(),
                SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1
        ).moving();

        public static Difficulty TWO_AQUA_ONE_THIN = new Difficulty(
                9, 20, 0,
                List.of(),
                SweetSpot.AQUA, SweetSpot.AQUA, SweetSpot.THIN
        );
        public static Difficulty TWO_AQUA_ONE_THIN_VANISHING = TWO_AQUA_ONE_THIN.vanishing();

        public static Difficulty FOUR_THIN = new Difficulty(
                9, 20, 0,
                List.of(),
                SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN
        );

        public static Difficulty FOUR_THIN_VANISHING = FOUR_THIN.vanishing();
        public static Difficulty FOUR_THIN_MOVING = FOUR_THIN.moving();

        public static Difficulty EIGHT_THIN = new Difficulty(
                9, 20, 0,
                List.of(),
                SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN
        );
        public static Difficulty EIGHT_THIN_VANISHING = EIGHT_THIN.vanishing();
        public static Difficulty EIGHT_THIN_MOVING = EIGHT_THIN.moving();
        public static Difficulty EIGHT_THIN_MOVING_VANISHING = EIGHT_THIN.vanishing();

        public static Difficulty THREE_BIG_TWO_THIN = new Difficulty(
                9, 20, 0,
                List.of(),
                SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.THIN, SweetSpot.THIN
        ).vanishing();
        public static Difficulty THREE_BIG_TWO_THIN_VANISHING = THREE_BIG_TWO_THIN.vanishing();

        public static Difficulty EIGHT_STONE_SPOTS = new Difficulty(
                12, 20, 0,
                List.of(),
                SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE
        );

        public static Difficulty TWO_STONE_SPOTS_EASY = new Difficulty(
                12, 20, 0,
                List.of(),
                SweetSpot.STONE_5, SweetSpot.STONE_5, SweetSpot.STONE_5, SweetSpot.STONE_5
        );

        public static Difficulty FOUR_STONE_SPOTS = new Difficulty(
                12, 20, 0,
                List.of(),
                SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE
        );

        public static Difficulty EASY_FAST_FISH = new Difficulty(
                15, 20, 1,
                List.of(),
                SweetSpot.NORMAL, SweetSpot.NORMAL
        );

        public static Difficulty SINGLE_AQUA = new Difficulty(
                13, 5, 1,
                List.of(),
                SweetSpot.AQUA
        );

        public static Difficulty SINGLE_AQUA_MOVING = new Difficulty(
                13, 5, 1,
                List.of(),
                SweetSpot.AQUA
        ).moving();

        public static Difficulty SINGLE_BIG_FAST = new Difficulty(
                13, 30, 1,
                List.of(),
                SweetSpot.NORMAL_STEADY
        );
        public static Difficulty SINGLE_BIG_FAST_MOVING = SINGLE_BIG_FAST.moving();
        public static Difficulty SINGLE_BIG_FAST_VANISHING = SINGLE_BIG_FAST.vanishing();

        public static Difficulty TWO_AQUA = new Difficulty(
                10, 20, 1,
                List.of(),
                SweetSpot.AQUA, SweetSpot.AQUA
        );

        public static Difficulty FOUR_AQUA = new Difficulty(
                10, 20, 1,
                List.of(),
                SweetSpot.AQUA, SweetSpot.AQUA, SweetSpot.AQUA, SweetSpot.AQUA
        );

        public static Difficulty SINGLE_THIN_FAST = new Difficulty(
                14, 10, 1,
                List.of(),
                SweetSpot.THIN
        );

        public static Difficulty TWO_THIN = new Difficulty(
                11, 15, 1,
                List.of(),
                SweetSpot.THIN, SweetSpot.THIN
        );

        public static Difficulty TWO_THIN_NO_DECAY = new Difficulty(
                10, 20, 0,
                List.of(),
                SweetSpot.THIN, SweetSpot.THIN
        );

        public static Difficulty NON_STOP_ACTION_THREE_BIG = new Difficulty(
                14, 20, 1,
                List.of(),
                SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL
        );

        public static Difficulty NON_STOP_ACTION_AQUA = new Difficulty(
                14, 2, 0.2f,
                List.of(),
                SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1, SweetSpot.AQUA_1
        );

        public static Difficulty WITHER = new Difficulty(
                10, 30, 1,
                List.of(),
                SweetSpot.WITHER_BIG, SweetSpot.WITHER, SweetSpot.WITHER
        );

        public static Difficulty CREEPER = new Difficulty(
                10, 20, 1,
                List.of(new SpawnSweetSpotsModifier(-1, 5, 0.25f, SweetSpot.TNT, true).toDoubleSup()),
                SweetSpot.CREEPER, SweetSpot.CREEPER
        );


        public static Difficulty DEEPSLATE_CRAB = new Difficulty(
                14, 10, 1,
                List.of(),
                SweetSpot.DEEPSLATE_CRAB_CLAW, SweetSpot.DEEPSLATE_CRAB_CLAW,
                SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG,
                SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG);

        public static Difficulty OBSIDIAN_CRAB = new Difficulty(
                14, 10, 1,
                List.of(),
                SweetSpot.OBSIDIAN_CRAB_CLAW, SweetSpot.OBSIDIAN_CRAB_CLAW,
                SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG,
                SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG);

        public static Difficulty NETHER_CRAB = new Difficulty(
                14, 10, 1,
                List.of(),
                SweetSpot.NETHER_CRAB_CLAW, SweetSpot.NETHER_CRAB_CLAW,
                SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG,
                SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG);

        public static Difficulty END_CRAB = new Difficulty(
                14, 10, 1,
                List.of(),
                SweetSpot.END_CRAB_CLAW, SweetSpot.END_CRAB_CLAW,
                SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG,
                SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG);

        public static Difficulty VOIDBITER = new Difficulty(
                14, 10, 1,
                List.of(),
                SweetSpot.AQUA, SweetSpot.THIN, SweetSpot.THIN);

        public static Difficulty JOEL = new Difficulty(
                14, 5, 1,
                List.of(),
                SweetSpot.AQUA_1, SweetSpot.AQUA_1
        );

        public static Difficulty CERBERAY = new Difficulty(
                16, 10, 1.5f,
                List.of(new SpawnSweetSpotsModifier(-1, 4, 0.50f, SweetSpot.TNT, true).toDoubleSup(), SCMinigameModifiers.NIKDO53_MODIFIER),
                SweetSpot.THIN, SweetSpot.THIN, SweetSpot.THIN
        );

        //endregion preset difficulties


        public static final Codec<Difficulty> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("speed").forGetter(Difficulty::speed),
                        Codec.INT.fieldOf("missPenalty").forGetter(Difficulty::penalty),
                        Codec.FLOAT.fieldOf("decay").forGetter(Difficulty::decay),
                        AbstractMinigameModifier.DOUBLE_SUP_LIST_CODEC.fieldOf("modifiers").forGetter(Difficulty::modifiers),
                        SweetSpot.LIST_CODEC.fieldOf("sweetspots").forGetter(Difficulty::sweetSpots)
                ).apply(instance, Difficulty::new));


        public static final StreamCodec<FriendlyByteBuf, Difficulty> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Difficulty::speed,
                ByteBufCodecs.INT, Difficulty::penalty,
                ByteBufCodecs.FLOAT, Difficulty::decay,
                ByteBufCodecs.fromCodec(AbstractMinigameModifier.DOUBLE_SUP_LIST_CODEC), Difficulty::modifiers,
                SweetSpot.LIST_STREAM_CODEC, Difficulty::sweetSpots,
                Difficulty::new
        );
    }

    public record SweetSpot(
            ResourceLocation sweetSpotType,
            ResourceLocation texturePath,
            int size,
            int reward,
            boolean isFlip,
            float vanishingRate,
            float movingRate,
            int particleColor,
            List<Supplier<Supplier<AbstractMinigameModifier>>> onHitModifiers
    )
    {
        public SweetSpot(ResourceLocation sweetSpotType, ResourceLocation texturePath, int size, int reward, int particleColor, List<Supplier<Supplier<AbstractMinigameModifier>>> onHitModifiers)
        {
            this(sweetSpotType, texturePath, size, reward, false, 0, 0, particleColor, onHitModifiers);
        }

        public SweetSpot(ResourceLocation sweetSpotType, ResourceLocation texturePath, int size, int reward, int particleColor)
        {
            this(sweetSpotType, texturePath, size, reward, false, 0, 0, particleColor, List.of());
        }

        public SweetSpot(ResourceLocation sweetSpotType, ResourceLocation texturePath, int size, int reward, boolean isFlip, float vanishingRate, float movingRate, int particleColor)
        {
            this(sweetSpotType, texturePath, size, reward, isFlip, vanishingRate, movingRate, particleColor, List.of());
        }


        private static final ResourceLocation RL_NORMAL = Starcatcher.rl("textures/gui/minigame/spots/normal.png");
        private static final ResourceLocation RL_NORMAL_STEADY = Starcatcher.rl("textures/gui/minigame/spots/normal_steady.png");
        private static final ResourceLocation RL_THIN = Starcatcher.rl("textures/gui/minigame/spots/thin.png");
        private static final ResourceLocation RL_THIN_STEADY = Starcatcher.rl("textures/gui/minigame/spots/thin_steady.png");
        private static final ResourceLocation RL_FREEZE = Starcatcher.rl("textures/gui/minigame/spots/frozen.png");
        private static final ResourceLocation RL_TREASURE = Starcatcher.rl("textures/gui/minigame/spots/treasure.png");
        private static final ResourceLocation RL_WITHER = Starcatcher.rl("textures/gui/minigame/spots/wither.png");
        private static final ResourceLocation RL_WITHER_BIG = Starcatcher.rl("textures/gui/minigame/spots/wither_big.png");
        private static final ResourceLocation RL_CREEPER = Starcatcher.rl("textures/gui/minigame/spots/creeper.png");
        private static final ResourceLocation RL_TNT = Starcatcher.rl("textures/gui/minigame/spots/tnt.png");
        private static final ResourceLocation RL_STONE = Starcatcher.rl("textures/gui/minigame/spots/stone.png");
        private static final ResourceLocation RL_AQUA = Starcatcher.rl("textures/gui/minigame/spots/aqua.png");

        private static final ResourceLocation RL_NETHER_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/nether_crab_claw.png");
        private static final ResourceLocation RL_NETHER_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/nether_crab_leg.png");

        private static final ResourceLocation RL_END_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/end_crab_leg.png");
        private static final ResourceLocation RL_END_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/end_crab_claw.png");

        private static final ResourceLocation RL_DEEPSLATE_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/deepslate_crab_leg.png");
        private static final ResourceLocation RL_DEEPSLATE_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/deepslate_crab_claw.png");

        private static final ResourceLocation RL_OBSIDIAN_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/obsidian_crab_leg.png");
        private static final ResourceLocation RL_OBSIDIAN_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/obsidian_crab_claw.png");

        private static final ResourceLocation RL_THIN_STEADY_MOSSY = Starcatcher.rl("textures/gui/minigame/spots/thin_mossy.png");


        public SweetSpot flip()
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, true, this.vanishingRate, this.movingRate, this.particleColor, this.onHitModifiers);
        }

        public SweetSpot vanishing(float vanishingRate)
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, this.isFlip, vanishingRate, this.movingRate, this.particleColor, this.onHitModifiers);
        }

        public SweetSpot moving(float movingRate)
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, this.isFlip, this.vanishingRate, movingRate, this.particleColor, this.onHitModifiers);
        }

        @SafeVarargs
        public final SweetSpot withModifiers(Supplier<Supplier<AbstractMinigameModifier>>... modifiers)
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, this.isFlip, this.vanishingRate, this.movingRate, this.particleColor, Arrays.stream(modifiers).toList());
        }


        public static SweetSpot TRASH = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL,
                22,
                30,
                0x00ff00
        );

        public static SweetSpot NORMAL_STEADY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL_STEADY,
                33,
                15,
                0x00ff00
        );

        public static SweetSpot NORMAL = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL,
                22,
                15,
                0x00ff00
        );

        public static SweetSpot THIN_STEADY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_THIN_STEADY,
                20,
                20,
                0x00ff00
        );

        public static SweetSpot THIN_STEADY_MOSSY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_THIN_STEADY_MOSSY,
                20,
                10,
                true, 0.01f, 1,
                0x00ff00
        );

        public static SweetSpot THIN = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_THIN,
                15,
                20,
                0x00ff00
        );

        public static SweetSpot FREEZE = new SweetSpot(
                SCSweetSpotsBehaviour.FROZEN,
                RL_FREEZE,
                33,
                15,
                0x095f92
        );

        public static SweetSpot NORMAL_HEAVY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL,
                22,
                1,
                0x00ff00
        );

        public static SweetSpot TREASURE = new SweetSpot(
                SCSweetSpotsBehaviour.TREASURE,
                RL_TREASURE,
                20,
                15,
                0xFFD700
        );

        public static SweetSpot WITHER = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_WITHER,
                22,
                15,
                false,
                0,
                3,
                0x1f1f1f
        );

        public static SweetSpot WITHER_BIG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_WITHER_BIG,
                33,
                15,
                0x1f1f1f
        );

        public static SweetSpot CREEPER = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_CREEPER,
                22,
                15,
                0x515353
        );

        public static SweetSpot TNT = new SweetSpot(
                SCSweetSpotsBehaviour.TNT,
                RL_TNT,
                33,
                30,
                0xff0000
        );

        public static SweetSpot STONE_5 = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_STONE,
                33,
                5,
                0x494949
        );


        public static SweetSpot STONE = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_STONE,
                33,
                1,
                0x494949
        );

        public static SweetSpot AQUA = new SweetSpot(
                SCSweetSpotsBehaviour.AQUA,
                RL_AQUA,
                22,
                8,
                0x387982
        );

        public static SweetSpot AQUA_1 = new SweetSpot(
                SCSweetSpotsBehaviour.AQUA,
                RL_AQUA, 22, 1, 0x387982
        );

        public static SweetSpot AQUA_5 = new SweetSpot(
                SCSweetSpotsBehaviour.AQUA,
                RL_AQUA, 22, 1, 0x387982
        );

        public static SweetSpot AQUA_10 = new SweetSpot(
                SCSweetSpotsBehaviour.AQUA,
                RL_AQUA, 22, 10, 0x387982
        );

        public static SweetSpot DEEPSLATE_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_DEEPSLATE_CRAB_CLAW, 24, 10, 0xff8400
        );

        public static SweetSpot DEEPSLATE_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_DEEPSLATE_CRAB_LEG, 15, 1, 0xff8400
        );

        public static SweetSpot OBSIDIAN_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_OBSIDIAN_CRAB_CLAW, 24, 10, 0x3b2754
        );

        public static SweetSpot OBSIDIAN_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_OBSIDIAN_CRAB_LEG, 15, 1, 0x3b2754
        );

        public static SweetSpot NETHER_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NETHER_CRAB_CLAW, 24, 10, 0xcd4545
        );

        public static SweetSpot NETHER_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NETHER_CRAB_LEG, 15, 1, 0xcd4545
        );

        public static SweetSpot END_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_END_CRAB_CLAW, 24, 10, 0xc67ed9
        );

        public static SweetSpot END_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_END_CRAB_LEG, 15, 1, 0xc67ed9
        );


        public static final Codec<SweetSpot> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("sweet_spot_type").forGetter(SweetSpot::sweetSpotType),
                        ResourceLocation.CODEC.fieldOf("texture_path").forGetter(SweetSpot::texturePath),
                        Codec.INT.fieldOf("hitbox_size_in_pixels").forGetter(SweetSpot::size),
                        Codec.INT.fieldOf("reward").forGetter(SweetSpot::reward),
                        Codec.BOOL.fieldOf("is_flip").forGetter(SweetSpot::isFlip),
                        Codec.FLOAT.fieldOf("vanishing_rate").forGetter(SweetSpot::vanishingRate),
                        Codec.FLOAT.fieldOf("moving_rate").forGetter(SweetSpot::movingRate),
                        Codec.INT.fieldOf("color_as_int").forGetter(SweetSpot::particleColor),
                        AbstractMinigameModifier.DOUBLE_SUP_LIST_CODEC.optionalFieldOf("add_modifiers_on_hit", List.of()).forGetter(SweetSpot::onHitModifiers)
                ).apply(instance, SweetSpot::new));

        public static final Codec<List<SweetSpot>> LIST_CODEC = CODEC.listOf();

        public static final StreamCodec<FriendlyByteBuf, SweetSpot> STREAM_CODEC = ExtraComposites.composite(
                ResourceLocation.STREAM_CODEC, SweetSpot::sweetSpotType,
                ResourceLocation.STREAM_CODEC, SweetSpot::texturePath,
                ByteBufCodecs.INT, SweetSpot::size,
                ByteBufCodecs.INT, SweetSpot::reward,
                ByteBufCodecs.BOOL, SweetSpot::isFlip,
                ByteBufCodecs.FLOAT, SweetSpot::vanishingRate,
                ByteBufCodecs.FLOAT, SweetSpot::movingRate,
                ByteBufCodecs.INT, SweetSpot::particleColor,
                ByteBufCodecs.fromCodec(AbstractMinigameModifier.DOUBLE_SUP_LIST_CODEC), SweetSpot::onHitModifiers,
                SweetSpot::new
        );

        public static final StreamCodec<FriendlyByteBuf, List<SweetSpot>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
    }

    //endregion dif


    public record SizeAndWeight(float sizeAverage, float sizeDeviation, float weightAverage, float weightDeviation,
                                float goldenChance)
    {
        public enum Units
        {
            METRIC("gui.guide.units.metric", 1f, 1f),
            IMPERIAL("gui.guide.units.imperial", 0.3937f, 0.0352739619495804f),
            CHEESEBURGER("gui.guide.units.cheeseburger", 0.09f, 0.0087f),
            FOOTBALL("gui.guide.units.football", 0.04545f, 0.00233f),
            DEVELOPER_HEIGHT("gui.guide.units.developer", 0.00592f, 0.0000140845f),
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

            public String getSizeAsString(int sizeInCm)
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

            public String getWeightAsString(int weightInGrams)
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


    public enum Rarity implements StringRepresentable
    {
        TRASH("trash", 0, Style.EMPTY.applyFormat(ChatFormatting.WHITE), 99),
        COMMON("common", 4, Style.EMPTY.applyFormat(ChatFormatting.WHITE), 40),
        UNCOMMON("uncommon", 8, Style.EMPTY.applyFormat(ChatFormatting.GREEN), 40),
        RARE("rare", 12, Style.EMPTY.applyFormat(ChatFormatting.BLUE), 30),
        EPIC("epic", 20, Style.EMPTY.applyFormat(ChatFormatting.LIGHT_PURPLE), 10),
        LEGENDARY("legendary", 35, Style.EMPTY.applyFormat(ChatFormatting.GOLD), 10),
        GOLDEN("golden", 0, Style.EMPTY.applyFormat(ChatFormatting.GOLD), 0);

        public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
        public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);
        private final String key;
        private final int xp;
        private final Style style;
        private final int stoneHookGraceTicks;

        Rarity(String key, int xp, Style style, int stoneHookGraceTicks)
        {
            this.key = key;
            this.xp = xp;
            this.style = style;
            this.stoneHookGraceTicks = stoneHookGraceTicks;
        }

        public Component wrapWithRarityMarkdown(String s)
        {
            return Component.literal("<sc" + getSerializedName() + ">" + s + "</sc" + getSerializedName() + ">");
        }

        public String wrapWithRarityMarkdownAsString(String s)
        {
            return "<sc" + getSerializedName() + ">" + s + "</sc" + getSerializedName() + ">";
        }

        public String getSerializedName()
        {
            return this.key;
        }

        public int getStoneHookGraceTicks()
        {
            return stoneHookGraceTicks;
        }

        public int getId()
        {
            return this.ordinal();
        }

        public int getXp()
        {
            return xp;
        }

        public Style getStyle()
        {
            return style;
        }

        public static boolean isGolden(ItemStack stack)
        {
            if (stack.has(SCDataComponents.CAUGHT_FISH_INFO))
            {
                CaughtFishInfo caughtFishInfo = stack.get(SCDataComponents.CAUGHT_FISH_INFO);
                return caughtFishInfo != null && caughtFishInfo.golden();
            }
            return false;
        }
    }

    public static List<ResourceLocation> getBiomesAsListFromTags(List<ResourceLocation> biomes, List<ResourceLocation> tags, Level level)
    {
        level.registryAccess().registry(Registries.BIOME);

        List<ResourceLocation> rls = new ArrayList<>();

        for (ResourceLocation rl : tags)
        {
            TagKey<Biome> biomeBeingChecked = TagKey.create(Registries.BIOME, rl);

            Optional<HolderSet.Named<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(biomeBeingChecked);

            if (optional.isPresent())
            {
                for (Holder<Biome> biomeHolder : optional.get())
                {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for (ResourceLocation rl : biomes)
        {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if (optional.isPresent()) if (!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }

    public static List<ResourceLocation> getBiomesBlacklistAsList(List<ResourceLocation> biomesBlacklist, List<ResourceLocation> biomesBlacklistTags, Level level)
    {
        level.registryAccess().registry(Registries.BIOME);

        List<ResourceLocation> rls = new ArrayList<>();

        for (ResourceLocation rl : biomesBlacklistTags)
        {
            TagKey<Biome> biomeBeingChecked = TagKey.create(Registries.BIOME, rl);

            Optional<HolderSet.Named<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(biomeBeingChecked);

            if (optional.isPresent())
            {
                for (Holder<Biome> biomeHolder : optional.get())
                {
                    String biomeString = biomeHolder.getRegisteredName();

                    rls.add(ResourceLocation.parse(biomeString));
                }
            }
        }

        for (ResourceLocation rl : biomesBlacklist)
        {
            Optional<Holder.Reference<Biome>> optional = level.registryAccess().lookupOrThrow(Registries.BIOME).get(ResourceKey.create(Registries.BIOME, rl));
            if (optional.isPresent()) if (!rls.contains(rl)) rls.add(rl);
        }

        return rls;
    }

    public static List<FishProperties> getFPs(Level level)
    {
        return getFPs(level.registryAccess());
    }

    public static List<FishProperties> getFPs(RegistryAccess registryAccess)
    {
        return registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY).stream().toList();
    }

    public int calculateChance(Entity entity, Level level, ItemStack rod, AbstractFishRestriction.Context context)
    {
        //if dev worm return base chance
        if (SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, new SingleStackContainer(ItemStack.EMPTY)).stack().is(SCItems.DEV_WORM))
            return 1;

        int chance = baseChance;

        for (var restriction : restrictions)
        {
            chance += restriction.getFishChance(chance, level, this, entity, rod, context);
        }

        return chance;
    }

    public static Fluid getSource(Fluid fluid1)
    {
        if (fluid1 instanceof FlowingFluid fluid)
        {
            return fluid.getSource();
        }

        return fluid1;
    }

    public static SizeAndWeight sizeWeight(float sizeAvg, float sizeDev, float weightAvg, float weightDev)
    {
        return new SizeAndWeight(sizeAvg, sizeDev, weightAvg, weightDev);
    }

    public static ResourceLocation rl(String ns, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }

    public static ItemStack makeItemStack(ItemStack rod, FishProperties fp, int size, int weight, float percentile, boolean golden, Player player, boolean perfectCatch)
    {
        ItemStack bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, SingleStackContainer.empty()).stack();
        boolean isStarcaught = fp.catchInfo().bucketedFish().is(SCItems.STARCAUGHT_BUCKET.getKey()) && bait.is(Items.BUCKET);
        boolean isBucketed = !fp.catchInfo().bucketedFish().is(SCItems.MISSINGNO.getKey()) && !isStarcaught && bait.is(Items.BUCKET);


        //starcaught bucketed fish
        if (isStarcaught)
        {
            ItemStack fish = new ItemStack(fp.catchInfo().fish());
            //quality food compat
            if (ModList.get().isLoaded("quality_food"))
                QualityFoodCompat.addQuality(fish, player, player.level(), golden, perfectCatch, percentile);
            ItemStack bucket = new ItemStack(SCItems.STARCAUGHT_BUCKET.get());
            SCDataComponents.set(bucket, SCDataComponents.BUCKETED_FISH, new SingleStackContainer(fish));
            return bucket;
        }

        //bucketed fish - non starcaught
        if (isBucketed)
            return new ItemStack(fp.catchInfo().bucketedFish());

        //normal itemstack
        ItemStack fish = new ItemStack(fp.catchInfo().fish());

        //store caught fish info data component
        if (fp.hasGuideEntry() && SCConfig.SAVE_DATA_TO_ITEMS.get())
            SCDataComponents.set(fish, SCDataComponents.CAUGHT_FISH_INFO, new CaughtFishInfo(size, weight, percentile, fp.rarity(), golden));

        //quality food compat
        if (ModList.get().isLoaded("quality_food"))
            QualityFoodCompat.addQuality(fish, player, player.level(), golden, perfectCatch, percentile);

        return fish;
    }

    public static void spawnFishFromPlayerFishing(ServerPlayer player, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        ServerLevel level = ((ServerLevel) player.level());

        if (SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).isEmpty()) return;

        Entity levelEntity = level.getEntity(SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).getUuid());
        if (levelEntity instanceof FishingBobEntity fbe)
        {
            if (time != -1)
            {
                FishProperties fp = fbe.fpToFish;

                SCCriterionTriggers.MINIGAME_COMPLETED.get().trigger(player, hits, perfectCatch, completedTreasure, time, fp.catchInfo().fish());

                //trigger modifiers
                fbe.modifiers.forEach(m -> m.onSuccessfulMinigameCompletion(player, time, completedTreasure, perfectCatch, hits));

                //play sound
                SCTackleSkins.get(level, fbe.rod).onSuccessfulMinigame(player);

                //if should cancel because of modifier, return
                if (fbe.modifiers.stream().anyMatch(m -> m.shouldCancelAfterSuccessfulMinigameCompletion(
                        player, time, completedTreasure, perfectCatch, hits))) return;

                //pick size, weight and golden
                float percentile = U.r.nextFloat(100);
                int size = SizeAndWeight.getRandomSize(fp, percentile);
                int weight = SizeAndWeight.getRandomWeight(fp, percentile);

                //golden if got lucky & hasn't caught golden yet
                boolean golden = U.r.nextFloat() < fp.sizeWeight().goldenChance() && FishCaughtCounter.canCatchGolden(fp, player);
                //golden if previous, or any modifier overrides it to be golden
                golden = golden || fbe.modifiers.stream().anyMatch(AbstractCatchModifier::shouldBeGolden);

                if (fbe.modifiers.stream().anyMatch(AbstractCatchModifier::cancelGolden)) golden = false;

                //award fish counter
                FishCaughtCounter.awardFishCaughtCounter(fp, player, time, size, weight, percentile, perfectCatch, true, golden);

                //add score to tournaments
                TournamentHandler.addScore(player, fp, perfectCatch, size, weight, percentile);

                //award exp
                int exp = fp.rarity().getXp();
                player.giveExperiencePoints(exp);

                List<ItemStack> items = new ArrayList<>();

                //if should spawn entity
                if (fp.catchInfo().alwaysSpawnEntity() ||
                        ModList.get().isLoaded("fishingreal") ||
                        fbe.modifiers.stream().anyMatch(AbstractCatchModifier::forceSpawnEntity))
                {
                    Vec3 objPos = player.position().subtract(fbe.position());

                    double x = objPos.x / 25;
                    double y = objPos.y / 20;
                    double z = objPos.z / 25;

                    x = Math.clamp(x, -1, 1);
                    y = Math.clamp(y, -1, 1);
                    z = Math.clamp(z, -1, 1);

                    x *= 2.5;
                    y *= 2;
                    z *= 2.5;

                    Entity entity = fp.catchInfo().entityToSpawn().value().create(level);

                    if (entity == null)
                    {
                        LogUtils.getLogger().warn("starcatcher doesnt like when the flag or whatever is not enabled");
                        return;
                    }

                    //set fish item if it's a starcatcher fish entity
                    if (entity instanceof FishEntity fe)
                        fe.setFish(getFishedItemStackFromFPForStarcatcherFishEntitySpecifically(fp, size, weight, percentile, golden));

                    entity.setPos(fbe.position().add(0, 1.2f, 0));

                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    entity.setDeltaMovement(vec3);
                    level.addFreshEntity(entity);
                }
                //if not entity then add base item stack
                else
                {
                    ItemStack is = makeItemStack(fbe.rod, fbe.fpToFish, size, weight, percentile, golden, player, perfectCatch);
                    items.add(is);

                    //modify base itemstack from modifiers
                    for (AbstractCatchModifier acm : fbe.modifiers) acm.modifyBaseItemStack(is);
                }

                //add items to list from modifiers
                for (AbstractCatchModifier acm : fbe.modifiers)
                    items.addAll(acm.addToFishedItems(time, perfectCatch, hits, completedTreasure, player));

                //add treasure
                if (completedTreasure || fbe.modifiers.stream().anyMatch(acm -> acm.forceAwardTreasure(fbe, time, completedTreasure, perfectCatch, hits)))
                {
                    items.add(fp.loadTreasure(player).catchInfo.treasureIs);
                }

                //spawn items from list
                for (ItemStack itemStackToSpawn : items)
                {
                    //make ItemEntities for fish item stack
                    ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, itemStackToSpawn);

                    //assign delta movement so fish flies towards player
                    double x = Math.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                    double y = Math.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                    double z = Math.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                    Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                    itemFished.setDeltaMovement(vec3);

                    //add item entity to level
                    level.addFreshEntity(itemFished);
                }

            }
            else
            {
                //if fish minigame failed/canceled
                fbe.modifiers.forEach(AbstractCatchModifier::onFailedMinigame);

                //play sound from tackle skin
                SCTackleSkins.get(level, fbe.rod).onFailedMinigame(player);
            }

            //consume bait
            ItemStack bait = SCDataComponents.getOrDefault(fbe.rod, SCDataComponents.BAIT, SingleStackContainer.empty()).stack();
            if (!bait.is(Items.BUCKET))
            {
                bait.shrink(1);
                SCDataComponents.set(fbe.rod, SCDataComponents.BAIT, new SingleStackContainer(bait));
            }

            if (bait.is(Items.BUCKET) && !fbe.fpToFish.catchInfo().bucketedFish().is(SCItems.MISSINGNO.getKey()) && time != -1)
            {
                bait.shrink(1);
                SCDataComponents.set(fbe.rod, SCDataComponents.BAIT, new SingleStackContainer(bait));
            }

            fbe.kill();
        }

        SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB.get());
    }

    private static ItemStack getFishedItemStackFromFPForStarcatcherFishEntitySpecifically(FishProperties fp, int size, int weight, float percentile, boolean golden)
    {
        ItemStack is = new ItemStack(fp.catchInfo().fish());
        if (fp.hasGuideEntry() && SCConfig.SAVE_DATA_TO_ITEMS.get())
            SCDataComponents.set(is, SCDataComponents.CAUGHT_FISH_INFO, new CaughtFishInfo(size, weight, percentile, fp.rarity(), golden));
        return is;
    }

}
