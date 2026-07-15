package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FluidRestriction extends AbstractFishRestriction
{
    private final List<ResourceLocation> fluids;

    public static final MapCodec<FluidRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("fluids").forGetter(o -> o.fluids),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, FluidRestriction::new));

    public FluidRestriction()
    {
        super("");
        this.fluids = List.of();
    }

    public FluidRestriction(List<ResourceLocation> fluids, String translationOverride)
    {
        super(translationOverride);
        this.fluids = fluids;
    }

    public FluidRestriction(ResourceLocation fluids, String translationOverride)
    {
        super(translationOverride);
        this.fluids = List.of(fluids);
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
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (context.isGuide() || context.equals(Context.RADAR) || context.equals(Context.TRACKER))
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
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if(fluids.size() == 1 && !fluids.getFirst().getPath().equals("water"))
        {
            if(!translationOverride.isEmpty())
                return List.of(Component.translatable(translationOverride).withStyle(Style.EMPTY.withColor(SCColors.GUIDE_TEXT_DARK)));
            else
                return List.of(Component.translatable("block." + fluids.getFirst().toLanguageKey()).withStyle(Style.EMPTY.withColor(SCColors.GUIDE_TEXT_DARK)));
        }
        return super.getIndexHover(level, fp, player, context);
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.fluid");
    }

    @Override
    public int getSortPriority()
    {
        return 99;
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if(fluids.size() == 1 && fluids.getFirst().equals(ResourceLocation.withDefaultNamespace("water"))) return Component.empty();
        return super.getDescription(level, fp, player, context);
    }

    @Override
    public int getColor(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return SCColors.GUIDE_TEXT_DARK;
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        //Fluid name / [hover]
        if (fluids.size() == 1)
            return Component.translatable("block." + fluids.getFirst().toLanguageKey());
        else
            return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if(fluids.size() == 1) return List.of();
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

    public static final FluidRestriction LAVA = new FluidRestriction(ResourceLocation.withDefaultNamespace("lava"), "fluid.starcatcher.lava");
    public static final FluidRestriction WATER = new FluidRestriction(ResourceLocation.withDefaultNamespace("water"), "fluid.starcatcher.water");
    public static final FluidRestriction VOID = new FluidRestriction(ResourceLocation.withDefaultNamespace("empty"), "fluid.starcatcher.void");
    public static final FluidRestriction AIR = new FluidRestriction(ResourceLocation.withDefaultNamespace("empty"), "fluid.starcatcher.air");
    public static final FluidRestriction ACID = new FluidRestriction(Utils.rl("alexscaves", "acid"), "fluid.starcatcher.acid");
    public static final FluidRestriction PURPLE_SODA = new FluidRestriction(Utils.rl("alexscaves", "purple_soda"), "fluid.starcatcher.purple_soda");
}
