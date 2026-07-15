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

public class AdjustMovingModifier extends AbstractMinigameModifier
{
    float multiplier;

    public static final MapCodec<AdjustMovingModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(mod -> mod.multiplier),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AdjustMovingModifier::new));


    public AdjustMovingModifier(float multiplier, String translationOverride)
    {
        super(translationOverride);
        this.multiplier = multiplier;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if (shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_moving_sweetspots.shift",
                    new DecimalFormat("#.##").format(multiplier * 100)));
        else
        {
            if (multiplier > 1)
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_moving_sweetspots.increase"));
            else
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_moving_sweetspots.decrease"));
        }
    }

    @Override
    public ActiveSweetSpot onSpotAdded(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onSpotAdded(instance, ass);
        ass.movingRate = ass.movingRate * multiplier;
        return ass;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("adjust_moving_sweetspots");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
