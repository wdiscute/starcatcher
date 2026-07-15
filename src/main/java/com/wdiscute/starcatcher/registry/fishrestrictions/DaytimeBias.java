package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DaytimeBias extends AbstractFishRestriction
{
    private final int bestDaytime;
    private final int range;
    private final int extraChance;

    public static final MapCodec<DaytimeBias> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("best_daytime").forGetter(o -> o.bestDaytime),
                    Codec.INT.fieldOf("range").forGetter(o -> o.range),
                    Codec.INT.fieldOf("extra_chance_at_best").forGetter(o -> o.extraChance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, DaytimeBias::new));

    public DaytimeBias()
    {
        super("");
        this.bestDaytime = 90;
        this.range = 10;
        this.extraChance = 0;
    }

    public DaytimeBias(int bestY, int range, int extraChance, String translationOverride)
    {
        super(translationOverride);
        this.bestDaytime = bestY;
        this.range = range;
        this.extraChance = extraChance;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.DAYTIME_BIAS;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //fishes in area for guidebook ignores this restriction
        if (context.equals(Context.GUIDE_FISHES_HOVER)) return 0;

        //returns the extra weight scaled linearly from 0 to extraChance, with extraChance at 100% at bestY
        int distance = Math.abs(entity.blockPosition().getY() - bestDaytime);

        int scaledChance = extraChance * (1 - distance / range);

        return Math.max(scaledChance, 0);
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.elevation");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of(
                Component.translatable("gui.guide.extra_chance", bestDaytime, extraChance),
                Component.translatable("gui.guide.range", bestDaytime - range + " - " + bestDaytime + range)
        );
    }
}
