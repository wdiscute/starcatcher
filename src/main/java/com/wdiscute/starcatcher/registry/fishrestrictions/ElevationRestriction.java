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

public class ElevationRestriction extends AbstractFishRestriction
{
    private final int minY;
    private final int maxY;

    public static final MapCodec<ElevationRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("min_y").forGetter(o -> o.minY),
                    Codec.INT.fieldOf("max_y").forGetter(o -> o.maxY),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ElevationRestriction::new));

    public ElevationRestriction()
    {
        super("");
        this.minY = Integer.MIN_VALUE;
        this.maxY = Integer.MAX_VALUE;
    }

    public ElevationRestriction(int minY, int maxY, String translationOverride)
    {
        super(translationOverride);
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.ELEVATION_RESTRICTION;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (entity.getY() > minY && entity.getY() < maxY)
            return 0;
        else
            return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.elevation.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.elevation.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (minY != Integer.MIN_VALUE && maxY != Integer.MAX_VALUE)
            return List.of(Component.translatable("gui.guide.between", minY, maxY));

        if (minY != Integer.MIN_VALUE)
            return List.of(Component.translatable("gui.guide.above", minY));

        return List.of(Component.translatable("gui.guide.below", maxY));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.elevation");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (minY != Integer.MIN_VALUE && maxY != Integer.MAX_VALUE)
            return Component.literal("> " + minY + ", < " + maxY);

        if (minY != Integer.MIN_VALUE && maxY == Integer.MAX_VALUE)
            return Component.translatable("gui.guide.above", minY);

        if (minY == Integer.MIN_VALUE && maxY != Integer.MAX_VALUE)
            return Component.translatable("gui.guide.below", maxY);
        return Component.empty();
    }

    public static final ElevationRestriction ABOVE_FIFTY = new ElevationRestriction(50, Integer.MAX_VALUE, "gui.guide.elevation.surface");
    public static final ElevationRestriction FIFTY_TO_HUNDRED = new ElevationRestriction(50, 100, "gui.guide.elevation.surface");
    public static final ElevationRestriction ABOVE_HUNDRED = new ElevationRestriction(100, Integer.MAX_VALUE, "gui.guide.elevation.mountains");
    public static final ElevationRestriction ABOVE_TWO_HUNDRED = new ElevationRestriction(200, Integer.MAX_VALUE, "gui.guide.elevation.skies");
    public static final ElevationRestriction BELOW_FIFTY = new ElevationRestriction(Integer.MIN_VALUE, 50, "gui.guide.elevation.underground");
    public static final ElevationRestriction END_VOID = new ElevationRestriction(Integer.MIN_VALUE, 50, "");
    public static final ElevationRestriction BELOW_ZERO = new ElevationRestriction(Integer.MIN_VALUE, 0, "gui.guide.elevation.deepslate");
    public static final ElevationRestriction BELOW_MINUS_SIXTY_FOUR = new ElevationRestriction(Integer.MIN_VALUE, -64, "gui.guide.elevation.void");
    public static final ElevationRestriction ZERO_TO_FIFTY = new ElevationRestriction(0, 50, "gui.guide.elevation.stone_caves");
}
