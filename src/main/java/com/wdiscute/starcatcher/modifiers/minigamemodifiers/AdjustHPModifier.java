package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.List;

public class AdjustHPModifier extends AbstractMinigameModifier
{
    float multiplier;

    public static final MapCodec<AdjustHPModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(mod -> mod.multiplier),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AdjustHPModifier::new));


    public AdjustHPModifier(float multiplier, String translationOverride)
    {
        super(translationOverride);
        this.multiplier = multiplier;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        instance.hp = (int) (instance.hp * multiplier);
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if (shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_hp.shift",
                    new DecimalFormat("#.##").format(multiplier * 100)));
        else
        {
            if (multiplier > 1)
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_hp.increase"));
            else
                return List.of(Component.translatable("tooltip.modifier.starcatcher.adjust_hp.decrease"));
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("adjust_hp");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[AdjustHPModifier@" + Integer.toHexString(hashCode()) + "](multiplier: " + multiplier + ")";
    }
}
