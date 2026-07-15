package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.DeepslateSweetSpotBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.List;

public class AdjustVanishingRate extends AbstractMinigameModifier
{
    float multiplier;

    public static final MapCodec<AdjustVanishingRate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(mod -> mod.multiplier),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AdjustVanishingRate::new));

    public AdjustVanishingRate(float multiplier, String translationOverride)
    {
        super(translationOverride);
        this.multiplier = multiplier;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        MutableComponent end = Component.translatable("tooltip.modifier.starcatcher.adjust_vanishing_rate");

        if (shift)
        {
            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_vanishing_rate.shift",
                    new DecimalFormat("#.##").format(multiplier * 100)));
        }
        else
        {
            if (multiplier >= 1.5f)
                return List.of(Component.translatable("tooltip.modifier.keyword.big_increase").append(end));

            if (multiplier >= 1f)
                return List.of(Component.translatable("tooltip.modifier.keyword.increase").append(end));

            if (multiplier <= 0.5f)
                return List.of(Component.translatable("tooltip.modifier.keyword.big_decrease").append(end));

            return List.of(Component.translatable("tooltip.modifier.keyword.decrease").append(end));
        }
    }

    @Override
    public ActiveSweetSpot onSpotAdded(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onSpotAdded(instance, ass);
        if (ass.behaviour instanceof DeepslateSweetSpotBehaviour)
            ass.vanishingRate =  ((ass.vanishingRate * multiplier) + ass.vanishingRate) / 2;
        else
            ass.vanishingRate = ass.vanishingRate * multiplier;
        return ass;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("adjust_vanishing_rate");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
