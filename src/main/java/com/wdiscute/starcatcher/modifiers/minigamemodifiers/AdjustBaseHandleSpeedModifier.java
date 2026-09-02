package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

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
        if(shift)
        {
            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed.shift",
                    Starcatcher.FORMAT.format(multiplier * 100)));
        }
        else
        {
            if(multiplier >= 1.5f)
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed.big_increase"));

            if(multiplier >= 1f)
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed.increase"));

            if(multiplier <= 0.5f)
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed.big_decrease"));

            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_handle_speed.decrease"));
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
    public Identifier getIdentifier()
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
