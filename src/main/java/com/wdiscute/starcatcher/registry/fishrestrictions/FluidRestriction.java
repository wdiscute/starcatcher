package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FluidRestriction extends AbstractFishRestriction
{
    private final List<ResourceLocation> fluids;
    private final String translationOverride;

    public static final MapCodec<FluidRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("fluids").forGetter(FluidRestriction::getFluids),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(FluidRestriction::getTranslationOverride)
            ).apply(instance, FluidRestriction::new));

    public FluidRestriction()
    {
        this.fluids = List.of();
        this.translationOverride = "";
    }

    public FluidRestriction(List<ResourceLocation> fluids, String translationOverride)
    {
        this.fluids = fluids;
        this.translationOverride = translationOverride;
    }

    public FluidRestriction(ResourceLocation fluids, String translationOverride)
    {
        this.fluids = List.of(fluids);
        this.translationOverride = translationOverride;
    }

    public List<ResourceLocation> getFluids()
    {
        return fluids;
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
        return SCFishRestrictions.FLUID;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (context.equals(Context.GUIDE_FISHES_HOVER))
        {
            return 0;
        }
        else
        {
            BlockPos bp = entity.blockPosition();
            boolean fluid = fluids.contains(BuiltInRegistries.FLUID.getKey(getSource(level.getFluidState(bp).getType())));
            boolean fluidAbove = fluids.contains(BuiltInRegistries.FLUID.getKey(getSource(level.getFluidState(bp.above()).getType())));
            boolean fluidBelow = fluids.contains(BuiltInRegistries.FLUID.getKey(getSource(level.getFluidState(bp.below()).getType())));

            if (!fluid && !fluidAbove && !fluidBelow) return -9999;
            else return 0;
        }
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @Nullable Player player, Context context)
    {
        if (!translationOverride.isEmpty()) return Component.translatable(translationOverride);

        MutableComponent start = Component.translatable("gui.guide.fluid");

        //Fluid name / [hover]
        if (fluids.size() == 1)
            return start.append(Component.translatable("block." + fluids.getFirst().toLanguageKey()));
        else
            return start.append(Component.translatable("gui.guide.hover"));
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();

        fluids.forEach((rl) -> hover.add(Component.translatable("block." + rl.toLanguageKey())));

        return hover;
    }

    private static Fluid getSource(Fluid fluid)
    {
        if (fluid instanceof FlowingFluid flowingFluid)
        {
            return flowingFluid.getSource();
        }

        return fluid;
    }

    public static final FluidRestriction LAVA = new FluidRestriction(ResourceLocation.withDefaultNamespace("lava"), "");
    public static final FluidRestriction WATER = new FluidRestriction(ResourceLocation.withDefaultNamespace("water"), "");
    public static final FluidRestriction VOID = new FluidRestriction(ResourceLocation.withDefaultNamespace("empty"), "");
    public static final FluidRestriction ACID = new FluidRestriction(U.rl("alexscaves", "acid"), "");
    public static final FluidRestriction PURPLE_SODA = new FluidRestriction(U.rl("alexscaves", "purple_soda"), "");
}
