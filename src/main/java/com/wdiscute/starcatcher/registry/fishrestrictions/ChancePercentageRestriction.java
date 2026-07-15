package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class ChancePercentageRestriction extends AbstractFishRestriction
{
    private final float chance;

    public static final MapCodec<ChancePercentageRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ChancePercentageRestriction::new));

    public ChancePercentageRestriction()
    {
        super("");
        this.chance = 0.5f;
    }

    public ChancePercentageRestriction(float chance)
    {
        super("");
        this.chance = chance;
    }

    public ChancePercentageRestriction(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
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
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (context.equals(Context.COMMAND)) return Utils.r.nextFloat() > chance ? -9999 : 0;
        if (context.equals(Context.FISHING)) return Utils.r.nextFloat() > chance ? -9999 : 0;
        if (context.equals(Context.GUIDE_ENTRY)) return 0;
        if (context.equals(Context.GUIDE_FISHES_IN_AREA)) return 0;
        if (context.equals(Context.GUIDE_FISHES_HOVER)) return 0;
        if (context.equals(Context.FISH_ENTITY)) return Utils.r.nextFloat() > chance ? -9999 : 0;
        if (context.equals(Context.JEMI)) return 0;
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
    public int getColor(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return SCColors.GUIDE_TEXT;
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.translatable("gui.guide.chance", new DecimalFormat("#.##").format(chance * 100) + "%");
    }
}
