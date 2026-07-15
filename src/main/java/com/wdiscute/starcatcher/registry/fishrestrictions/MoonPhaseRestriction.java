package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoonPhaseRestriction extends AbstractFishRestriction
{
    public enum Phase implements StringRepresentable
    {
        //shamelessly stolen from tide's code
        FULL_MOON("full_moon", 0),
        WANING_GIBBOUS("waning_gibbous", 1),
        THIRD_QUARTER("third_quarter", 2),
        WANING_CRESCENT("waning_crescent", 3),
        NEW_MOON("new_moon", 4),
        WAXING_CRESCENT("waxing_crescent", 5),
        FIRST_QUARTER("first_quarter", 6),
        WAXING_GIBBOUS("waxing_gibbous", 7),
        ;

        public static final Codec<Phase> CODEC = StringRepresentable.fromEnum(Phase::values);
        public static final StreamCodec<FriendlyByteBuf, Phase> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Phase.class);
        final String key;
        final int phase;

        Phase(String key, int moonPhaseAsAnIntBecauseMinecraftThoughtThatWasAGoodIdeaWhat)
        {
            this.key = key;
            this.phase = moonPhaseAsAnIntBecauseMinecraftThoughtThatWasAGoodIdeaWhat;
        }

        public String toTranslationKey()
        {
            return "gui.guide." + key;
        }

        @Override
        public String getSerializedName()
        {
            return this.key;
        }
    }

    private final List<Phase> allowedPhases;

    public static final MapCodec<MoonPhaseRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Phase.CODEC.listOf().fieldOf("allowed_phases").forGetter(o -> o.allowedPhases),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, MoonPhaseRestriction::new));

    public MoonPhaseRestriction(List<Phase> allowedPhases, String translationOverride)
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

        if (allowedPhases.stream().anyMatch(o -> o.phase == level.getMoonPhase()))
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
        return allowedPhases.stream().map(o -> (Component) Component.translatable(o.toTranslationKey())).toList();
    }

    public static final MoonPhaseRestriction NEW_MOON = new MoonPhaseRestriction(List.of(Phase.NEW_MOON), "");
    public static final MoonPhaseRestriction CRESCENT_PHASES = new MoonPhaseRestriction(List.of(Phase.WANING_CRESCENT, Phase.WAXING_CRESCENT), "");
    public static final MoonPhaseRestriction FULL_MOON = new MoonPhaseRestriction(List.of(Phase.FULL_MOON), "");
    public static final MoonPhaseRestriction FIRST_QUARTER = new MoonPhaseRestriction(List.of(Phase.FIRST_QUARTER), "");
    public static final MoonPhaseRestriction WANING_PHASES = new MoonPhaseRestriction(List.of(Phase.WANING_CRESCENT, Phase.WANING_GIBBOUS), "");
    public static final MoonPhaseRestriction THIRD_QUARTER = new MoonPhaseRestriction(List.of(Phase.THIRD_QUARTER), "");
}
