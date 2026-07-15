package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.List;

public class AwardTreasureOnPerfectCatch extends AbstractCatchModifier
{
    float chance;

    public static final MapCodec<AwardTreasureOnPerfectCatch> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AwardTreasureOnPerfectCatch::new));

    public AwardTreasureOnPerfectCatch(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if(shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.award_treasure_on_perfect_catch.shift", new DecimalFormat("#.##").format(chance * 100)));
        else if(chance >= 1)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.award_treasure_on_perfect_catch.always"));
        else
            return List.of(Component.translatable("tooltip.modifier.starcatcher.award_treasure_on_perfect_catch"));

    }

    @Override
    public boolean forceAwardTreasure(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        return perfectCatch && fbe.level().getRandom().nextFloat() < chance;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("award_treasure_on_perfect_catch");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
