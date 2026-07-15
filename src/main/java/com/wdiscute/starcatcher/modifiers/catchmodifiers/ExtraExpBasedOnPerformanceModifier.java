package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ExtraExpBasedOnPerformanceModifier extends AbstractCatchModifier
{
    public static final MapCodec<ExtraExpBasedOnPerformanceModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ExtraExpBasedOnPerformanceModifier::new));

    public ExtraExpBasedOnPerformanceModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public void onSuccessfulMinigameCompletion(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        super.onSuccessfulMinigameCompletion(fbe, time, completedTreasure, perfectCatch, hits);
        int hitsNonCheated = Math.min(hits, 20);
        fbe.player.giveExperiencePoints(fbe.fpToFish.rarity().getXp() * (hitsNonCheated / 3) + 1);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("extra_exp_based_on_performance");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}

