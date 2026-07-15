package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElevationBias extends AbstractFishRestriction
{
    private final int bestY;
    private final int range;

    public static final MapCodec<ElevationBias> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("best_y").forGetter(o -> o.bestY),
                    Codec.INT.fieldOf("range").forGetter(o -> o.range),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ElevationBias::new));

    public ElevationBias()
    {
        super("");
        this.bestY = 90;
        this.range = 10;
    }

    public ElevationBias(int bestY, int range, String translationOverride)
    {
        super(translationOverride);
        this.bestY = bestY;
        this.range = range;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.ELEVATION_BIAS;
    }

    @Override
    public int getColor(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int chance = adjustChance(10, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER);
        if (chance == -9999)
            return SCColors.GUIDE_RED;

        if (chance > -4)
            return SCColors.GUIDE_GREEN;

        return SCColors.GUIDE_YELLOW;
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of(Component.translatable("gui.guide.elevation_bias.hover", bestY - range, bestY + range, bestY));
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //returns the extra weight scaled linearly from 0 to extraChance, with extraChance at 100% at bestY
        int distance = Math.abs(entity.blockPosition().getY() - bestY);

        if (distance > range) return -9999;

        int chanceToRemove = (int) (currentChance * ((float) distance / (float) range));
        return chanceToRemove >= currentChance ? -9999 : -chanceToRemove;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int chance = adjustChance(10, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER);
        if (chance == -9999)
            return List.of(Component.translatable("gui.guide.hover.elevation.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));

        if (chance > -4)
            return List.of(Component.translatable("gui.guide.hover.elevation.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));

        return List.of(Component.translatable("gui.guide.hover.elevation.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_YELLOW)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.elevation");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.translatable("gui.guide.elevation_bias", bestY);
    }

    public static final ElevationBias MOUNTAIN = new ElevationBias(100, 30, "");
    public static final ElevationBias AMETHYSTBACK = new ElevationBias(20, 10, "");
}
