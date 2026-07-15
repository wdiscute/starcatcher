package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractFishRestriction implements Comparable<AbstractFishRestriction>
{
    public final String translationOverride;

    public static final Codec<AbstractFishRestriction> ABSTRACT_PROCESSOR_CODEC = ResourceLocation.CODEC
            .dispatch(processor -> processor.getRegistryHolderOrThrow().getId(),
                    loc ->
                    {
                        AbstractFishRestriction fr = Starcatcher.FISH_RESTRICTIONS_REGISTRY.get(loc);
                        if (fr == null)
                        {
                            LogUtils.getLogger().error("Fish Restriction {} is not registered! " +
                                                       "Make sure it's not dependent on another mod, and that you spelt the name correctly. " +
                                                       "Using empty restriction instead.", loc);
                            return EmptyRestriction.CODEC;
                        }
                        return fr.getCodecOrThrow();
                    });

    protected AbstractFishRestriction(String translationOverride)
    {
        this.translationOverride = translationOverride;
    }

    public abstract MapCodec<? extends AbstractFishRestriction> codec();

    public abstract DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder();

    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolderOrThrow()
    {
        if (getRegistryHolder() == null)
            throw new IllegalStateException("Fish Restriction " + this + " does not have a registry holder!");
        return getRegistryHolder();
    }

    public MapCodec<? extends AbstractFishRestriction> getCodecOrThrow()
    {
        if (codec() == null) throw new IllegalStateException("Fish Restriction " + this + " does not have a codec!");
        return codec();
    }

    public abstract int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context);


    public MutableComponent getDescriptionPrefix()
    {
        return Component.empty();
    }

    public int getColor(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return adjustChance(0, level, fp, player, ItemStack.EMPTY, context) >= 0 ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;
    }

    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (translationOverride.isEmpty() || translationOverride.equals("hide"))
            return getDescriptionPrefix()
                    .append(getNonOverriddenDescription(level, fp, player, context)
                            .withStyle(Style.EMPTY.withColor(getColor(level, fp, player, context))));

        return getDescriptionPrefix()
                .append(Component.translatable(translationOverride)
                        .withStyle(Style.EMPTY.withColor(getColor(level, fp, player, context))));
    }

    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.literal("?????");
    }

    public boolean isEnabled()
    {
        return true;
    }


    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of();
    }

    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of();
    }

    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of();
    }

    public void render(GuiGraphics guiGraphics, int topLeftX, int topLeftY, int mouseX, int mouseY, Context context)
    {
    }

    public int getSortPriority()
    {
        return 0;
    }

    @Override
    public int compareTo(AbstractFishRestriction other)
    {
        return Integer.compare(this.getSortPriority(), other.getSortPriority());
    }

    public enum Context
    {
        COMMAND,
        FISHING,
        TRACKER,
        RADAR,
        GUIDE_ENTRY,
        GUIDE_FISHES_IN_AREA,
        GUIDE_FISHES_HOVER,
        FISH_ENTITY,
        JEMI,
        OTHER;

        public boolean isGuide()
        {
            return this == GUIDE_ENTRY || this == GUIDE_FISHES_HOVER || this == GUIDE_FISHES_IN_AREA;
        }
    }
}
