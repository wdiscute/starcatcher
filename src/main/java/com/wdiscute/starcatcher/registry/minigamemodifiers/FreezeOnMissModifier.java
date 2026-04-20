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

public class FreezeOnMissModifier extends AbstractTimedModifier
{
    public static final Identifier OVERLAY = Starcatcher.rl("textures/gui/minigame/modifiers/freeze.png");

    public static final MapCodec<FreezeOnMissModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength)
            ).apply(instance, FreezeOnMissModifier::new));

    public FreezeOnMissModifier(int length) {
        super(length);
    }

    public FreezeOnMissModifier() {
        super();
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.FREEZE_ON_MISS;
    }

    @Override
    public void onMiss()
    {
        super.onMiss();
        instance.addUniqueModifier(new FrozenPointerWhileActiveModifier(40, 10));
    }

    @Override
    public void renderBackground(GuiGraphicsExtractor guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(guiGraphics, partialTick, width, height);
        guiGraphics.blit(RenderPipelines.GUI,
                OVERLAY, width / 2 - 48, height / 2 - 48,
                96, 96, 0, 0, 96, 96, 96, 96);
    }
}
