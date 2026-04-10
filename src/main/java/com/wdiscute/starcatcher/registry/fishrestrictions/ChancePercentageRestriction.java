package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.FishProperties;
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

public class ChancePercentageRestriction extends AbstractFishRestriction
{
    private final float chance;
    private final String translationOverride;

    public static final MapCodec<ChancePercentageRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(ChancePercentageRestriction::getTranslationOverride)
            ).apply(instance, ChancePercentageRestriction::new));

    public ChancePercentageRestriction()
    {
        this.chance = 0.5f;
        this.translationOverride = "";
    }

    public ChancePercentageRestriction(float chance)
    {
        this.chance = chance;
        this.translationOverride = "";
    }

    public ChancePercentageRestriction(float chance, String translationOverride)
    {
        this.chance = chance;
        this.translationOverride = translationOverride;
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
        return SCFishRestrictions.CHANCE_RESTRICTION;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (context.equals(Context.COMMAND)) return U.r.nextFloat() > chance ? -9999 : 0;
        if (context.equals(Context.FISHING)) return U.r.nextFloat() > chance ? -9999 : 0;
        if (context.equals(Context.GUIDE_ENTRY)) return 0;
        if (context.equals(Context.GUIDE_FISHES_IN_AREA)) return 0;
        if (context.equals(Context.GUIDE_FISHES_HOVER)) return 0;
        if (context.equals(Context.FISH_ENTITY)) return U.r.nextFloat() > chance ? -9999 : 0;
        if (context.equals(Context.EMI)) return 0;
        return 0;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of(Component.translatable("gui.guide.hover.elevation.correct", chance + "%").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of();
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if(!translationOverride.isEmpty()) return Component.translatable(translationOverride);

        return Component.translatable("gui.guide.chance", chance);
    }
}
