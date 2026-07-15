package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.text.DecimalFormat;
import java.util.List;

public class ExtraGoldenChanceModifier extends AbstractCatchModifier
{
    final float risk;
    final boolean onlyForPerfectCatch;

    public static final MapCodec<ExtraGoldenChanceModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("weight").forGetter(o -> o.risk),
                    Codec.BOOL.optionalFieldOf("only_for_perfect_catch", false).forGetter(o -> o.onlyForPerfectCatch),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ExtraGoldenChanceModifier::new));

    public ExtraGoldenChanceModifier(float chance, boolean onlyForPerfectCatch, String translationOverride)
    {
        super(translationOverride);
        this.risk = chance;
        this.onlyForPerfectCatch = onlyForPerfectCatch;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        String chanceS = new DecimalFormat("#.##").format(risk * 100);

        if (onlyForPerfectCatch)
        {
            if (shift)
                return List.of(Component.translatableEscape("tooltip.modifier.starcatcher.extra_golden_chance.perfect.shift", chanceS));
            else
                return List.of(Component.translatableEscape("tooltip.modifier.starcatcher.extra_golden_chance.perfect", chanceS));
        }
        else
        {
            if (shift)
                return List.of(Component.translatableEscape("tooltip.modifier.starcatcher.extra_golden_chance.base.shift", chanceS));
            else
                return List.of(Component.translatableEscape("tooltip.modifier.starcatcher.extra_golden_chance.base", chanceS));
        }
    }

    @Override
    public boolean shouldBeGolden(FishingBobEntity fbe, int time, boolean treasure, boolean perfect, int hits)
    {
        //if dont need perfect catch, do normal math
        if (!onlyForPerfectCatch)
            return fbe.level().getRandom().nextFloat() < risk;

        //otherwise only run if it's perfect
        if (perfect)
            return fbe.level().getRandom().nextFloat() < risk;

        return false;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("extra_golden_chance");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
