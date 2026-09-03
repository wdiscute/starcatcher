package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoonPhaseRestriction extends AbstractFishRestriction
{
    private final List<MoonPhase> allowedPhases;

    public static final MapCodec<MoonPhaseRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    MoonPhase.CODEC.listOf().fieldOf("allowed_phases").forGetter(o -> o.allowedPhases),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, MoonPhaseRestriction::new));

    public MoonPhaseRestriction(List<MoonPhase> allowedPhases, String translationOverride)
    {
        super(translationOverride);
        this.allowedPhases = allowedPhases;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.MOON_PHASE;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (context.equals(Context.RADAR)) return 0;
        if (context.equals(Context.GUIDE_FISHES_IN_AREA)) return 0;

        MoonPhase value = level.environmentAttributes()
                .getValue(EnvironmentAttributes.MOON_PHASE, entity.position(), null);

        if (allowedPhases.stream().anyMatch(o -> o.equals(value)))
            return 0;

        return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.moon_phase.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.moon_phase.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.moon_phase");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return allowedPhases.stream().map(o -> (Component) Component.translatable("gui.guide." + o.name())).toList();
    }

    public static final MoonPhaseRestriction NEW_MOON = new MoonPhaseRestriction(List.of(MoonPhase.NEW_MOON), "");
    public static final MoonPhaseRestriction CRESCENT_PHASES = new MoonPhaseRestriction(List.of(MoonPhase.WANING_CRESCENT, MoonPhase.WAXING_CRESCENT), "");
    public static final MoonPhaseRestriction FULL_MOON = new MoonPhaseRestriction(List.of(MoonPhase.FULL_MOON), "");
    public static final MoonPhaseRestriction FIRST_QUARTER = new MoonPhaseRestriction(List.of(MoonPhase.FIRST_QUARTER), "");
    public static final MoonPhaseRestriction WANING_PHASES = new MoonPhaseRestriction(List.of(MoonPhase.WANING_CRESCENT, MoonPhase.WANING_GIBBOUS), "");
    public static final MoonPhaseRestriction THIRD_QUARTER = new MoonPhaseRestriction(List.of(MoonPhase.THIRD_QUARTER), "");
}
