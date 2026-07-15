package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class TriggersSkipMinigameModifier extends AbstractCatchModifier implements SkipMinigameIfTriggerFoundModifier.SkipsMinigame
{
    public static final MapCodec<TriggersSkipMinigameModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, TriggersSkipMinigameModifier::new));

    public TriggersSkipMinigameModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("trigger_skip_minigame");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
