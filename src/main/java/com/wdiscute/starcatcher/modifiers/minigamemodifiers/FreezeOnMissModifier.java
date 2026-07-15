package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class FreezeOnMissModifier extends AbstractMinigameModifier
{
    public static final ResourceLocation OVERLAY = Starcatcher.rl("textures/gui/minigame/modifiers/freeze.png");

    private final int length;
    private final int rampTime;

    public static final MapCodec<FreezeOnMissModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("length").forGetter(o -> o.length),
                    Codec.INT.optionalFieldOf("rampTime", -1).forGetter(o -> o.rampTime),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, FreezeOnMissModifier::new));

    public FreezeOnMissModifier(int length, int rampTime, String translationOverride)
    {
        super(translationOverride);
        this.rampTime = rampTime;
        this.length = length;
    }

    @Override
    public String toString()
    {
        return "[FreezeOnMissModifier@" + Integer.toHexString(hashCode()) + "]";
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        for (AbstractMinigameModifier modifier : instance.getModifiers())
        {
            if(modifier instanceof FreezeHandleModifier fpwam)
            {
                fpwam.tickCount = 0;
                return;
            }
        }

        instance.addModifier(new FreezeHandleModifier(40, 10));
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
        return Starcatcher.rl("freeze_on_miss");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
