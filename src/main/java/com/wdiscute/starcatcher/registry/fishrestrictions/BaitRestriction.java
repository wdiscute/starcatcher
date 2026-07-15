package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaitRestriction extends AbstractFishRestriction
{
    public final Map<ResourceLocation, Integer> baits;

    public static final MapCodec<BaitRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.strictUnboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("baits").forGetter(o -> o.baits),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BaitRestriction::new));

    public BaitRestriction(Map<ResourceLocation, Integer> baits, String translationOverride)
    {
        super(translationOverride);
        this.baits = baits;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.BAIT;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //if (context.equals(Context.GUIDE_FISHES_HOVER)) return fp.baseChance() == 0 ? -9999 : 0;

        Item bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack().getItem();

        if (baits.containsKey(BuiltInRegistries.ITEM.getKey(bait)))
            return baits.get(BuiltInRegistries.ITEM.getKey(bait));

        return 0;
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.bait");
    }

    @Override
    public int getColor(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return SCColors.GUIDE_TEXT_DARK;
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        //bait name / [hover]
        if (baits.size() == 1)
            return MutableComponent.create(BuiltInRegistries.ITEM.get(baits.keySet().stream().findFirst().get()).getDescription().getContents());
        else
            return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();
        //hover - Chance added by bait:
        hover.add(Component.translatable("gui.guide.bait_chance_added").withStyle(Style.EMPTY.withBold(true)));
        hover.add(Component.empty());

        baits.forEach((item, value) ->
        {
            Optional<Item> optional = BuiltInRegistries.ITEM.getOptional(item);
            optional.ifPresent(o -> hover.add(Component.literal(value + " - ")
                    .append(Component.translatable(o.getDescriptionId()))));
        });
        return hover;
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return fp.baseChance() == 0 ? List.of(Component.translatable("gui.guide.bait_required")) : List.of();
    }

    public static final BaitRestriction CHERRY_BAIT = new BaitRestriction(Map.of(SCItems.CHERRY_BAIT.getId(), 50), "");
    public static final BaitRestriction LUSH_BAIT = new BaitRestriction(Map.of(SCItems.LUSH_BAIT.getId(), 50), "");
    public static final BaitRestriction SCULK_BAIT = new BaitRestriction(Map.of(SCItems.SCULK_BAIT.getId(), 50), "");
    public static final BaitRestriction DRIPSTONE_BAIT = new BaitRestriction(Map.of(SCItems.DRIPSTONE_BAIT.getId(), 50), "");
    public static final BaitRestriction MURKWATER_BAIT = new BaitRestriction(Map.of(SCItems.MURKWATER_BAIT.getId(), 50), "");
    public static final BaitRestriction LEGENDARY_BAIT = new BaitRestriction(Map.of(SCItems.LEGENDARY_BAIT.getId(), 50), "");

    public static final BaitRestriction WITHER_SKELETON_SKULL = new BaitRestriction(Map.of(Utils.rl("wither_skeleton_skull"), 50), "");

    public static final BaitRestriction FISH_OF_THIEVES = new BaitRestriction(
            Map.of(
                    Utils.rl("fishofthieves", "earthworms"), 50,
                    Utils.rl("fishofthieves", "grubs"), 50,
                    Utils.rl("fishofthieves", "leeches"), 50),
            "");

    public static final BaitRestriction ALMIGHTY_WORM = new BaitRestriction(Map.of(SCItems.ALMIGHTY_WORM.getId(), 5), "");

}
