package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class BurnOnMissModifier extends AbstractMinigameModifier
{
    int length;
    int rampTime;
    int extraSpeed;

    public static final ResourceLocation OVERLAY = Starcatcher.rl("textures/gui/minigame/modifiers/burn.png");

    public static final MapCodec<BurnOnMissModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("length").forGetter(o -> o.length),
                    Codec.INT.fieldOf("ramp_time").forGetter(o -> o.rampTime),
                    Codec.INT.fieldOf("extra_speed").forGetter(o -> o.extraSpeed),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BurnOnMissModifier::new));

    public BurnOnMissModifier(int length, int rampTime, int extraSpeed, String translationOverride)
    {
        super(translationOverride);
        this.length = length;
        this.rampTime = rampTime;
        this.extraSpeed = extraSpeed;
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        instance.addUniqueModifier(new BurnPointerWhileActiveModifier(length, rampTime, extraSpeed));
    }

    @Override
    public void renderBackground(FishingMinigameScreen instance, GuiGraphics guiGraphics, float partialTick, int width, int height)
    {
        super.renderBackground(instance, guiGraphics, partialTick, width, height);

        int layers = (int) instance.modifierData.getOrDefault(Starcatcher.rl("multi_layer"), 0);

        float increase = layers * 0.22f + 1;

        int posX = (int) ((float) width / 2 - 48 * increase);
        int posY = (int) ((float) height / 2 - 48 * increase);

        guiGraphics.blit(OVERLAY,
                posX, posY,
                (int) (96 * increase), (int) (96 * increase),
                0, 0,
                96, 96,
                96, 96
        );
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("burn_on_miss");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
