package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class SpawnSweetSpotsModifier extends AbstractMinigameModifier
{
    private final Random r = new Random();
    public int cooldown;
    public int tickCooldownToWaitUntilCanAddAnother = 0;
    public float chance;
    public Difficulty.SweetSpot sweetSpot;
    public boolean sudokuVanish;

    public static final MapCodec<SpawnSweetSpotsModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(mod -> mod.cooldown),
                    Codec.FLOAT.fieldOf("chance").forGetter(mod -> mod.chance),
                    Difficulty.SweetSpot.CODEC.fieldOf("sweetspot").forGetter(mod -> mod.sweetSpot),
                    Codec.BOOL.fieldOf("sudoku_vanish").forGetter(mod -> mod.sudokuVanish),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SpawnSweetSpotsModifier::new));

    public SpawnSweetSpotsModifier(int cooldown, float chance, Difficulty.SweetSpot sweetSpot, boolean sudokuVanish, String translationOverride)
    {
        super(translationOverride);
        this.cooldown = cooldown;
        this.chance = chance;
        this.sweetSpot = sweetSpot;
        this.sudokuVanish = sudokuVanish;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
    }

    @Override
    public String toString()
    {
        return "[SpawnSweetSpotModifier@" + Integer.toHexString(hashCode()) + "] (cd: " + cooldown + " / %: " + chance + " / wait until " + tickCooldownToWaitUntilCanAddAnother + ")";
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        super.tick(instance);

        if (instance.tickCount >= tickCooldownToWaitUntilCanAddAnother  && r.nextFloat() < chance)
        {
            tickCooldownToWaitUntilCanAddAnother = instance.tickCount + cooldown;
            ActiveSweetSpot activeSweetSpot = new ActiveSweetSpot(instance, sweetSpot);
            instance.addSweetSpot(activeSweetSpot);
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("spawn_sweetspots");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
