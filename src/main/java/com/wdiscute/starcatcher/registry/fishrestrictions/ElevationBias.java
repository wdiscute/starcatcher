package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.network.chat.Component;
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
    private final int extraChance;
    private final String translationOverride;

    public static final MapCodec<ElevationBias> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("best_y").forGetter(ElevationBias::getBestY),
                    Codec.INT.fieldOf("range").forGetter(ElevationBias::getRange),
                    Codec.INT.fieldOf("extra_chance_at_best").forGetter(ElevationBias::getExtraChance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(ElevationBias::getTranslationOverride)
            ).apply(instance, ElevationBias::new));

    public ElevationBias()
    {
        this.bestY = 90;
        this.range = 10;
        this.extraChance = 0;
        this.translationOverride = "";
    }

    public ElevationBias(int bestY, int range, int extraChance, String translationOverride)
    {
        this.bestY = bestY;
        this.range = range;
        this.extraChance = extraChance;
        this.translationOverride = translationOverride;
    }

    public int getBestY()
    {
        return bestY;
    }

    public int getRange()
    {
        return range;
    }

    public int getExtraChance()
    {
        return extraChance;
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
        return SCFishRestrictions.ELEVATION_BIAS;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //returns the extra chance scaled linearly from 0 to extraChance, with extraChance at 100% at bestY
        int distance = Math.abs(entity.blockPosition().getY() - bestY);

        int scaledChance = extraChance * (1 - distance / range);

        return Math.max(scaledChance, 0);
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.translatable("gui.guide.elevation").copy().append(
                translationOverride.isEmpty() ? Component.translatable("gui.guide.hover") : Component.translatable(translationOverride)
        );
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of(
                Component.translatable("gui.guide.extra_chance", bestY, extraChance),
                Component.translatable("gui.guide.range", bestY - range + " - " + bestY + range)
        );
    }
}
