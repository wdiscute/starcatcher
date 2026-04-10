package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.dries007.tfc.common.entities.ai.prey.PestAi;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RarityCountRestriction extends AbstractFishRestriction
{
    private final List<RarityCount> rarityCount;
    private final String translationOverride;

    public static final MapCodec<RarityCountRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    RarityCount.CODEC.listOf().fieldOf("rarities").forGetter(RarityCountRestriction::getRarityCount),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(RarityCountRestriction::getTranslationOverride)
            ).apply(instance, RarityCountRestriction::new));


    public record RarityCount(FishProperties.Rarity rarity, int count, CountType countType)
    {

        public static final Codec<RarityCount> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        //golden here is used for "all rarities" as no fish is ever stored with gold rarity.
                        //I don't wanna hear anything from kapiten about this
                        FishProperties.Rarity.CODEC.optionalFieldOf("rarity", FishProperties.Rarity.NONE).forGetter(RarityCount::rarity),
                        Codec.INT.optionalFieldOf("count", 0).forGetter(RarityCount::count),
                        CountType.CODEC.fieldOf("count_type").forGetter(RarityCount::countType)
                ).apply(instance, RarityCount::new));

        public enum CountType implements StringRepresentable
        {
            ALL("all"),
            UNIQUE("unique"),
            TOTAL("total");

            final String name;

            CountType(String name)
            {
                this.name = name;
            }

            public static final Codec<CountType> CODEC = StringRepresentable.fromEnum(CountType::values);

            @Override
            public String toString()
            {
                return name;
            }

            @Override
            public String getSerializedName()
            {
                return name;
            }
        }
    }


    public RarityCountRestriction()
    {
        this.rarityCount = List.of();
        this.translationOverride = "";
    }

    public RarityCountRestriction(RarityCount... rarityCount)
    {
        this.rarityCount = List.of(rarityCount);
        this.translationOverride = "";
    }

    public RarityCountRestriction(List<RarityCount> rarityCount, String translationOverride)
    {
        this.rarityCount = rarityCount;
        this.translationOverride = translationOverride;
    }


    public List<RarityCount> getRarityCount()
    {
        return rarityCount;
    }

    public String getTranslationOverride()
    {
        return translationOverride;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.RARITY_COUNT_RESTRICTION;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties trophyFp, @NotNull Entity entity1, ItemStack rod, Context context)
    {
        Entity entity = entity1 instanceof FishingBobEntity fbe ? fbe.player : entity1;

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = SCDataAttachments.get(entity, SCDataAttachments.FISHING_GUIDE).fishesCaught;

        for (RarityCount rarityCount : rarityCount)
        {
            if (!isRarityCountRequirementMet(rarityCount, entity, fishesCaught)) return -9999;
        }

        return 0;
    }

    static Map<FishProperties.Rarity, Pair<Integer, Integer>> getFishesCaughtCountMap(RarityCount.CountType type, Entity entity)
    {
        Level level = entity.level();

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = SCDataAttachments.get(entity, SCDataAttachments.FISHING_GUIDE).fishesCaught;
        var registry = FishProperties.getRegistry(level);
        List<FishProperties> allFishes = FishProperties.getAllFPs(level);
        Map<FishProperties.Rarity, Pair<Integer, Integer>> map = new HashMap<>();

        //populate default map with all rarities and [0, 0]
        Arrays.stream(FishProperties.Rarity.values()).forEach(o -> map.put(o, new Pair<>(0, 0)));

        for (FishProperties fp : allFishes)
        {
            int playerCount;
            boolean golden = false;

            if (fishesCaught.containsKey(registry.getKey(fp)))
            {
                if (type == RarityCount.CountType.ALL || type == RarityCount.CountType.UNIQUE)
                    playerCount = 1;
                else
                    playerCount = fishesCaught.get(registry.getKey(fp)).count();
                golden = fishesCaught.get(registry.getKey(fp)).caughtGolden();
            }
            else
            {
                playerCount = 0;
            }

            map.put(FishProperties.Rarity.NONE, Pair.of(map.get(FishProperties.Rarity.NONE).getFirst() + playerCount, map.get(FishProperties.Rarity.NONE).getSecond() + 1));
            map.put(FishProperties.Rarity.GOLDEN, Pair.of(map.get(FishProperties.Rarity.GOLDEN).getFirst() + (golden ? 1 : 0), map.get(FishProperties.Rarity.GOLDEN).getSecond() + 1));

            map.compute(fp.rarity(), (k, currentRarityPlayerCount) ->
                    Pair.of(currentRarityPlayerCount.getFirst() + playerCount, currentRarityPlayerCount.getSecond() + 1));

        }
        return map;
    }

    public boolean isRarityCountRequirementMet(RarityCount rarityCount, Entity entity, Map<ResourceLocation, FishCaughtCounter> fishesCaught)
    {
        Level level = entity.level();

        if (rarityCount.countType == RarityCount.CountType.ALL)
        {
            Registry<FishProperties> registry = FishProperties.getRegistry(level);
            List<FishProperties> fps = FishProperties.getFishes(level);

            if (rarityCount.rarity.equals(FishProperties.Rarity.NONE))
            {
                //is every fp in fishes caught?
                return fps.stream().allMatch(fp -> fishesCaught.containsKey(registry.getKey(fp)));
            }
            else
            {
                //all golden check
                if (rarityCount.rarity == FishProperties.Rarity.GOLDEN)
                {
                    boolean obtainedEveryFish = fps.stream().allMatch(fp -> fishesCaught.containsKey(registry.getKey(fp)));

                    if (obtainedEveryFish)
                        //if obtained every fish and every fp has corresponding fishesCaught entry with caughtGolden
                        return fps.stream().allMatch(o -> fishesCaught.get(FishProperties.getKey(level, o)).caughtGolden());

                    return false;
                }

                //filter fps by rarity, do they all have an entry in fishes caught?
                return fps.stream()
                        .filter(fp -> fp.rarity().equals(rarityCount.rarity))
                        .allMatch(fp -> fishesCaught.containsKey(registry.getKey(fp)));
            }
        }
        else
        {

            Map<FishProperties.Rarity, Pair<Integer, Integer>> raritiesCaught = getFishesCaughtCountMap(rarityCount.countType, entity);

            //if rarity not selected
            if (rarityCount.rarity.equals(FishProperties.Rarity.NONE))
            {
                AtomicInteger totalCount = new AtomicInteger();
                raritiesCaught.forEach((r, i) -> totalCount.addAndGet(i.getFirst()));
                return rarityCount.count <= totalCount.get();
            }
            else
            {
                //if countRequired is more than count player has
                return rarityCount.count <= raritiesCaught.get(rarityCount.rarity).getFirst();
            }
        }
    }


    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught;
        rarityCount.forEach(o -> hover.add(getSingleEntryShortDescription(o, player)
                .withColor(isRarityCountRequirementMet(o, player, fishesCaught) ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED)));

        return hover;
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int color = getFishChance(0, level, fp, player, ItemStack.EMPTY, context) >= 0 ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;

        if (!translationOverride.isEmpty())
            return Component.translatable(translationOverride).withStyle(Style.EMPTY.withColor(color));


        Map<ResourceLocation, FishCaughtCounter> fishesCaught = SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught;

        if (rarityCount.size() == 1)
        {
            RarityCount rc = rarityCount.get(0);
            return getSingleEntryShortDescription(rc, player)
                    .withStyle(Style.EMPTY.withColor(isRarityCountRequirementMet(rarityCount.get(0), player, fishesCaught) ?
                            SCColors.GUIDE_GREEN : SCColors.GUIDE_RED));
        }
        else
        {
            return Component.translatable("gui.guide.hover").withStyle(Style.EMPTY.withColor(color));
        }
    }

    MutableComponent getSingleEntryShortDescription(RarityCount rc, @NotNull Entity entity)
    {
        var map = getFishesCaughtCountMap(rc.countType, entity);

        if (rc.countType.equals(RarityCount.CountType.ALL) && rc.rarity.equals(FishProperties.Rarity.NONE))
            return Component.translatable("gui.guide.rarity_count.all", map.get(rc.rarity).getFirst() + "/" + map.get(rc.rarity).getSecond());

        if (rc.countType.equals(RarityCount.CountType.ALL))
            return Component.translatable("gui.guide.rarity_count.all", map.get(rc.rarity).getFirst() + "/" + map.get(rc.rarity).getSecond() + " " + rc.rarity.getSerializedName());

        if (rc.rarity.equals(FishProperties.Rarity.NONE))
            return Component.translatable("gui.guide.rarity_count.single", map.get(rc.rarity).getFirst() + "/" + rc.count + " " + rc.countType);

        return Component.translatable("gui.guide.rarity_count.single", map.get(rc.rarity).getFirst() + "/" + rc.count + " " + rc.countType + " " + rc.rarity.getSerializedName());
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (rarityCount.size() == 1) return List.of();
        List<Component> hover = new ArrayList<>();

        rarityCount.forEach(o -> hover.add(getSingleEntryShortDescription(o, player)));

        return hover;
    }
}
