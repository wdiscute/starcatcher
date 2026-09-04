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
import net.nikdo53.neobackports.registry.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaitRestriction extends AbstractFishRestriction
{
    public final List<Utils.Duo<ResourceLocation, Integer>> baits;

    public static final MapCodec<BaitRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Utils.Duo.codec(ResourceLocation.CODEC, "id", Codec.INT, "extra_chance").listOf().fieldOf("baits").forGetter(o -> o.baits),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BaitRestriction::new));

    public BaitRestriction(List<Utils.Duo<ResourceLocation, Integer>> baits, String translationOverride)
    {
        super(translationOverride);
        this.baits = baits;
    }

    public BaitRestriction(Utils.Duo<ResourceLocation, Integer> baits, String translationOverride)
    {
        super(translationOverride);
        this.baits = List.of(baits);
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

        return baits.stream()
                .filter(o -> o.first().equals(BuiltInRegistries.ITEM.getKey(bait)))
                .findAny().map(Utils.Duo::second).orElse(0);
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
            return MutableComponent.create(BuiltInRegistries.ITEM.get(baits.get(0).first()).getDescription().getContents());
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

        baits.forEach((duo) ->
        {
            Optional<Item> optional = BuiltInRegistries.ITEM.getOptional(duo.first());
            optional.ifPresent(o -> hover.add(Component.literal(duo.second() + " - ")
                    .append(Component.translatable(o.getDescriptionId()))));
        });
        return hover;
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return fp.baseChance() == 0 ? List.of(Component.translatable("gui.guide.bait_required")) : List.of();
    }

    public static final BaitRestriction CHERRY_BAIT = new BaitRestriction(new Utils.Duo<>(SCItems.CHERRY_BAIT.getId(), 50), "");
    public static final BaitRestriction LUSH_BAIT = new BaitRestriction(new Utils.Duo<>(SCItems.LUSH_BAIT.getId(), 50), "");
    public static final BaitRestriction SCULK_BAIT = new BaitRestriction(new Utils.Duo<>(SCItems.SCULK_BAIT.getId(), 50), "");
    public static final BaitRestriction DRIPSTONE_BAIT = new BaitRestriction(new Utils.Duo<>(SCItems.DRIPSTONE_BAIT.getId(), 50), "");
    public static final BaitRestriction MURKWATER_BAIT = new BaitRestriction(new Utils.Duo<>(SCItems.MURKWATER_BAIT.getId(), 50), "");
    public static final BaitRestriction LEGENDARY_BAIT = new BaitRestriction(new Utils.Duo<>(SCItems.LEGENDARY_BAIT.getId(), 50), "");

    public static final BaitRestriction WITHER_SKELETON_SKULL = new BaitRestriction(new Utils.Duo<>(Utils.rl("wither_skeleton_skull"), 50), "");

    public static final BaitRestriction FISH_OF_THIEVES = new BaitRestriction(
            List.of(
                    new Utils.Duo<>(Utils.rl("fishofthieves", "earthworms"), 50),
                    new Utils.Duo<>(Utils.rl("fishofthieves", "grubs"), 50),
                    new Utils.Duo<>(Utils.rl("fishofthieves", "leeches"), 50)),
            "");

    public static final BaitRestriction ALMIGHTY_WORM = new BaitRestriction(new Utils.Duo<>(SCItems.ALMIGHTY_WORM.getId(), 5), "");

}
