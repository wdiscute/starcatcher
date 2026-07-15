package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class AddBasicSweetSpotModifier extends AbstractMinigameModifier
{
    Difficulty.SweetSpot ss;

    public static final MapCodec<AddBasicSweetSpotModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Difficulty.SweetSpot.CODEC.fieldOf("sweetspot_to_add").forGetter(o -> o.ss),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AddBasicSweetSpotModifier::new));

    public AddBasicSweetSpotModifier(Difficulty.SweetSpot ss, String translationOverride)
    {
        super(translationOverride);
        this.ss = ss;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);

        instance.addSweetSpot(new ActiveSweetSpot(instance, ss));
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("add_basic_sweetspot");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
