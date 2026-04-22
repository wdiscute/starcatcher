package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class BurnOnMissModifier extends AbstractTimedModifier
{
    public static final Identifier OVERLAY = Starcatcher.rl("textures/gui/minigame/modifiers/burn.png");

    public static final MapCodec<BurnOnMissModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength)
            ).apply(instance, BurnOnMissModifier::new));

    public BurnOnMissModifier(int length) {
        super(length);
    }

    public BurnOnMissModifier() {
        super();
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.BURN_ON_MISS;
    }

    @Override
    public void onMiss()
    {
        super.onMiss();
        instance.addUniqueModifier(new BurnPointerWhileActiveModifier(40, 5, 16));
    }

    @Override
    public void renderBackground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(guiGraphics, partialTick, width, height);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                OVERLAY, width / 2 - 48, height / 2 - 48,
                96, 96, 0, 0, 96, 96, 96, 96);
    }
}
