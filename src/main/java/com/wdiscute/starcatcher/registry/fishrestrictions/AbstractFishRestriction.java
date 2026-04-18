package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractFishRestriction
{
    public static final Codec<AbstractFishRestriction> ABSTRACT_PROCESSOR_CODEC = Identifier.CODEC
            .dispatch(processor -> processor.getRegistryHolderOrThrow().getId(),
                    loc ->
                    {
                        AbstractFishRestriction fr = Starcatcher.FISH_RESTRICTIONS_REGISTRY.getValue(loc);
                        if (fr == null)
                        {
                            LogUtils.getLogger().error("Fish Restriction {} is not registered! " +
                                    "Make sure it's not dependent on another mod, and that you spelt the name correctly. " +
                                    "Using empty restriction instead.", loc);
                            return EmptyRestriction.CODEC;
                        }
                        return fr.getCodecOrThrow();
                    });

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

    public abstract int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context);

    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.empty();
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

    public void render(GuiGraphicsExtractor guiGraphics, int topLeftX, int topLeftY, int mouseX, int mouseY, Context context)
    {
    }

    public enum Context
    {
        COMMAND,
        FISHING,
        GUIDE_ENTRY,
        GUIDE_FISHES_IN_AREA,
        GUIDE_FISHES_HOVER,
        FISH_ENTITY,
        EMI,
        OTHER;

        public boolean isGuide()
        {
            return this == GUIDE_ENTRY || this == GUIDE_FISHES_HOVER || this == GUIDE_FISHES_IN_AREA;
        }
    }
}
