package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.fishrestrictions.DaytimeRestriction;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.text.DecimalFormat;
import java.util.List;

public class IgnoreDaytimeRestrictionsModifier extends AbstractCatchModifier implements DaytimeRestriction.SkipsDaytimeRestriction
{
    float chance;

    public static final MapCodec<IgnoreDaytimeRestrictionsModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("weight").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, IgnoreDaytimeRestrictionsModifier::new));

    public IgnoreDaytimeRestrictionsModifier(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if(shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.ignore_daytime_restrictions.shift", new DecimalFormat("#.##").format(chance * 100)));

        if(chance >= 1)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.ignore_daytime_restrictions"));
        else
            return List.of(Component.translatable("tooltip.modifier.starcatcher.ignore_daytime_restrictions.maybe"));
    }

    @Override
    public boolean shouldSkipDaytime(Level level)
    {
        return level.getRandom().nextFloat() < chance;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("ignore_daytime_restrictions");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
