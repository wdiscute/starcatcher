package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.List;

public class AdjustDecayRateModifier extends AbstractMinigameModifier
{
    float multiplier;

    public static final MapCodec<AdjustDecayRateModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(mod -> mod.multiplier),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AdjustDecayRateModifier::new));


    public AdjustDecayRateModifier(float multiplier, String translationOverride)
    {
        super(translationOverride);
        this.multiplier = multiplier;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        instance.decay = instance.decay * multiplier;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if (shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_decay_rate.shift",
                    new DecimalFormat("#.##").format(multiplier * 100)));
        else
        {
            if (multiplier > 1)
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_decay_rate.increase"));
            else
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_decay_rate.decrease"));
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("adjust_decay_rate");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[AdjustDecayRateModifier@" + Integer.toHexString(hashCode()) + "](multiplier: " + multiplier + ")";
    }
}
