package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.GlowingSweetSpotBehaviour;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.SculkSweetSpotBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;

public class DeepDarkModifier extends AbstractMinigameModifier
{
    public static final MapCodec<DeepDarkModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, DeepDarkModifier::new));

    public DeepDarkModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        if(ass.behaviour instanceof GlowingSweetSpotBehaviour) return false;

        if(ass.behaviour instanceof SculkSweetSpotBehaviour)
        {
            Object data = instance.modifierData.computeIfAbsent(getIdentifier(), o -> 0);

            if (data instanceof Integer i)
                instance.modifierData.put(getIdentifier(), i + 40);

            Minecraft.getInstance().player.playSound(SoundEvents.SCULK_BLOCK_HIT, 1f, 1f);
        }

        return super.onHit(instance, ass);
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        Object data = instance.modifierData.computeIfAbsent(getIdentifier(), o -> 0);

        if (data instanceof Integer i)
            instance.modifierData.put(getIdentifier(), Math.clamp(i - 10, 0, 255));
    }

    @Override
    public void renderForeground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderForeground(instance, guiGraphics, partialTick, width, height);

        Object data = instance.modifierData.computeIfAbsent(getIdentifier(), o -> 0);

        if (data instanceof Integer i)
        {
            Color c = new Color(0x00000000, true);

            int newAlpha = Math.clamp(c.getAlpha() + i, 0, 255);

            Color newColor = new Color(
                    c.getRed(),
                    c.getGreen(),
                    c.getBlue(),
                    newAlpha
            );

            guiGraphics.fill(0, 0, instance.width, instance.height, newColor.getRGB());
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("deep_dark");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[DeepDarkModifier@" + Integer.toHexString(hashCode()) + "]";
    }
}
