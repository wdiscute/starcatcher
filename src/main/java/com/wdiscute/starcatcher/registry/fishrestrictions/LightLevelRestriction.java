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
import net.nikdo53.neobackports.registry.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightLevelRestriction extends AbstractFishRestriction
{
    private final int minLight;
    private final int maxLight;

    public static final MapCodec<LightLevelRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("min_light", Integer.MIN_VALUE).forGetter(o -> o.minLight),
                    Codec.INT.optionalFieldOf("max_light", Integer.MAX_VALUE).forGetter(o -> o.maxLight),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, LightLevelRestriction::new));

    public LightLevelRestriction()
    {
        super("");
        this.minLight = Integer.MIN_VALUE;
        this.maxLight = Integer.MAX_VALUE;
    }

    public LightLevelRestriction(int minLight, int maxLight, String translationOverride)
    {
        super(translationOverride);
        this.minLight = minLight;
        this.maxLight = maxLight;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.LIGHT_LEVEL_RESTRICTION;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        int lightLevel = level.getMaxLocalRawBrightness(entity.blockPosition());
        if (lightLevel > minLight && lightLevel < maxLight)
            return 0;
        else
            return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.light_level.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.light_level.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (minLight != Integer.MIN_VALUE && maxLight != Integer.MAX_VALUE)
            return List.of(Component.translatable("gui.guide.between", minLight, maxLight));

        if (minLight != Integer.MIN_VALUE)
            return List.of(Component.translatable("gui.guide.above", minLight));

        return List.of(Component.translatable("gui.guide.below", maxLight));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.light_level");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (minLight != Integer.MIN_VALUE && maxLight != Integer.MAX_VALUE)
            return Component.literal("> " + minLight + ", < " + maxLight);

        if (minLight != Integer.MIN_VALUE && maxLight == Integer.MAX_VALUE)
            return Component.translatable("gui.guide.above", minLight);

        if (minLight == Integer.MIN_VALUE && maxLight != Integer.MAX_VALUE)
            return Component.translatable("gui.guide.below", maxLight);
        return Component.empty();
    }

    public static final LightLevelRestriction DARKNESS = new LightLevelRestriction(Integer.MIN_VALUE, 1, "gui.guide.light_level.darkness");
    public static final LightLevelRestriction BRIGHT = new LightLevelRestriction(10, Integer.MAX_VALUE, "gui.guide.light_level.bright");
}
