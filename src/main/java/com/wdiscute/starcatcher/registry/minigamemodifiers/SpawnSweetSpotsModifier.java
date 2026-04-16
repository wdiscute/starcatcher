package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.Random;
import java.util.function.Supplier;

public class SpawnSweetSpotsModifier extends AbstractTimedModifier{
    private final Random r = new Random();
    public int cooldown = 2;
    public float chance = 0.10f;
    public FishProperties.SweetSpot sweetSpot;
    public boolean sudokuVanish = true;

    public static final MapCodec<SpawnSweetSpotsModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("length", -1).forGetter(AbstractTimedModifier::getLength),
                    Codec.INT.fieldOf("cooldown").forGetter(mod -> mod.cooldown),
                    Codec.FLOAT.fieldOf("chance").forGetter(mod -> mod.chance),
                    FishProperties.SweetSpot.CODEC.fieldOf("sweet_spot").forGetter(mod -> mod.sweetSpot),
                    Codec.BOOL.fieldOf("sudoku_vanish").forGetter(mod -> mod.sudokuVanish)
            ).apply(instance, SpawnSweetSpotsModifier::new));

    public SpawnSweetSpotsModifier(int length, int cooldown, float chance, FishProperties.SweetSpot sweetSpot, boolean sudokuVanish) {
        super(length);
        this.cooldown = cooldown;
        this.chance = chance;
        this.sweetSpot = sweetSpot;
    }

    public SpawnSweetSpotsModifier(FishProperties.SweetSpot sweetSpot) {
        super();
        this.sweetSpot = sweetSpot;
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec() {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder() {
        return SCMinigameModifiers.SPAWN_SWEET_SPOTS;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (tickCount % cooldown == 0 && r.nextFloat() < chance)
        {
            ActiveSweetSpot activeSweetSpot = new ActiveSweetSpot(instance, sweetSpot);
            instance.addSweetSpot(activeSweetSpot);
        }
    }

    public static FishProperties.SweetSpot legacyFreeze(){
        FishProperties.SweetSpot original = FishProperties.SweetSpot.FREEZE;
        return new FishProperties.SweetSpot(original.sweetSpotType(), original.texturePath(), original.size(), original.reward(), original.isFlip(), 0.02f, original.movingRate(), original.particleColor());
    }

}
