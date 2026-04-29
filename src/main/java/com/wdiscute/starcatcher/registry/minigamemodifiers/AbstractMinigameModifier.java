package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractMinigameModifier
{
    public static final Codec<AbstractMinigameModifier> MINIGAME_MODIFIER_CODEC = ResourceLocation.CODEC
            .dispatch(mod -> mod.getRegistryHolderOrThrow().getId(), loc -> Starcatcher.MINIGAME_MODIFIERS_REGISTRY.get(loc).get().getCodecOrThrow());

    public static final Codec<List<Supplier<Supplier<AbstractMinigameModifier>>>> DOUBLE_SUP_LIST_CODEC =
            AbstractMinigameModifier.MINIGAME_MODIFIER_CODEC.xmap(AbstractMinigameModifier::toDoubleSup, dSup -> dSup.get().get()).listOf();

    public boolean removed = false;
    public int tickCount = 0;
    protected FishingMinigameScreen instance;

    public abstract MapCodec<? extends AbstractMinigameModifier> codec();

    public abstract DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder();

    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolderOrThrow()
    {
        var holder = getRegistryHolder();
        if (holder == null)
        {
            throw new IllegalStateException("Modifier " + this + " does not have a registry holder!");
        }

        return holder;
    }

    public MapCodec<? extends AbstractMinigameModifier> getCodecOrThrow()
    {
        var codec = codec();
        if (codec == null)
        {
            throw new IllegalStateException("Modifier " + this + " does not have a codec!");
        }
        return codec;
    }

    public Supplier<AbstractMinigameModifier> toSupplier()
    {
        return () -> this;
    }

    public Supplier<Supplier<AbstractMinigameModifier>> toDoubleSup()
    {
        return this::toSupplier;
    }

    public void onAdd(FishingMinigameScreen instance)
    {
        this.instance = instance;
    }

    /**
     * Runs when removed or the minigame ends
     */
    public void onRemove()
    {
    }

    /**
     * Transforms an ActiveSweetSpot before it gets added.
     * Setting spot removed to true cancels it
     */
    public ActiveSweetSpot onSpotAdded(ActiveSweetSpot spot)
    {
        return spot;
    }

    /**
     * Runs right before the sweetSpotBehaviour hit
     *
     * @return whether the hit should be cancelled
     */
    public boolean onHit(ActiveSweetSpot ass)
    {
        return false;
    }

    ;

    public void onMiss()
    {
    }

    public void tick()
    {
        tickCount++;
    }

    public void onKeyPress(int key, int scanCode, int keyModifiers)
    {
    }

    public void onKeyReleased(int key, int scanCode, int keyModifiers)
    {
    }

    public void renderBackground(GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
    }

    ;

    public void renderForeground(GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
    }

    ;

    /**
     * Disables rendering the included pointer
     * <p>
     * Still renders {@link #renderOnPointer(GuiGraphics, PoseStack, float)}
     */
    public boolean disablePointerRendering()
    {
        return false;
    }

    /**
     * Has the correctly rotated poseStack already
     */
    public void renderOnPointer(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick)
    {
    }

    ;

    public boolean disableSweetSpotRendering(ActiveSweetSpot spot)
    {
        return false;
    }

    public void renderOnSweetSpot(GuiGraphics guiGraphics, PoseStack poseStack, ActiveSweetSpot spot, float partialTick)
    {
    }

    ;

    public boolean forceAwardTreasure()
    {
        return false;
    }

    public boolean skipRenderingKimbeMarker()
    {
        return false;
    }

    public boolean skipHitParticles()
    {
        return false;
    }

    public boolean skipMissSound()
    {
        return false;
    }

    public boolean skipHitSound()
    {
        return false;
    }
}
