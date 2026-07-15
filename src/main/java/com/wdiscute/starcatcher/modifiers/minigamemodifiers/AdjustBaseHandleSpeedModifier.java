package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.List;

public class AdjustBaseHandleSpeedModifier extends AbstractMinigameModifier
{
    public float multiplier;

    public static final MapCodec<AdjustBaseHandleSpeedModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("multiplier", 0f).forGetter(o -> o.multiplier),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
                    ).apply(instance, AdjustBaseHandleSpeedModifier::new));

    public AdjustBaseHandleSpeedModifier(float multiplier, String translationOverride)
    {
        super(translationOverride);
        this.multiplier = multiplier;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        MutableComponent end = Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed");

        if(shift)
        {
            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed.shift",
                    new DecimalFormat("#.##").format(multiplier * 100)));
        }
        else
        {
            if(multiplier >= 1.5f)
                return List.of(Component.translatable("tooltip.modifier.keyword.big_increase").append(end));

            if(multiplier >= 1f)
                return List.of(Component.translatable("tooltip.modifier.keyword.increase").append(end));

            if(multiplier <= 0.5f)
                return List.of(Component.translatable("tooltip.modifier.keyword.big_decrease").append(end));

            return List.of(Component.translatable("tooltip.modifier.keyword.decrease").append(end));
        }
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        instance.handleBaseSpeed = instance.handleBaseSpeed * multiplier;
        instance.handleSpeed = instance.handleBaseSpeed;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("adjust_handle_speed");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[AdjustBaseHandleSpeedModifier@" + Integer.toHexString(hashCode()) + "](multiplier: " + multiplier + ")";
    }
}
