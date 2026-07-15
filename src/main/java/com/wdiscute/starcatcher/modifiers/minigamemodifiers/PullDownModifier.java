package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class PullDownModifier extends AbstractMinigameModifier
{

    public static final MapCodec<PullDownModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, PullDownModifier::new));

    public PullDownModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean flipRodAndProgressDisplay(FishingMinigameScreen instance)
    {
        return true;
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        return super.onHit(instance, ass);
    }
    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(instance, guiGraphics, partialTick, width, height);
        //todo render clouds
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("pull_down");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
