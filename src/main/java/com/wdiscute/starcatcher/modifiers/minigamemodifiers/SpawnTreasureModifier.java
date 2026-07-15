package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.Utils;
import net.minecraft.resources.ResourceLocation;

public class SpawnTreasureModifier extends AbstractMinigameModifier
{
    float chance;

    public static final MapCodec<SpawnTreasureModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SpawnTreasureModifier::new));

    public SpawnTreasureModifier(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        //add treasure
        if (chance > Utils.r.nextFloat() && !instance.treasureActive && instance.treasureProgress == 0)
            instance.addSweetSpot(new ActiveSweetSpot(instance, Difficulty.SweetSpot.TREASURE));
        return super.onHit(instance, ass);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("spawn_treasure");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[SpawnTreasure@" + Integer.toHexString(hashCode()) + "](chance: " + chance + ")";
    }
}
