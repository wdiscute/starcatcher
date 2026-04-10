package com.wdiscute.starcatcher.registry.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModifyBasePointerSpeedModifier extends AbstractMinigameModifier
{
    public float baseSpeedRatio;

    public static final MapCodec<ModifyBasePointerSpeedModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("base_speed_ratio", 0f).forGetter(o -> o.baseSpeedRatio)
                    ).apply(instance, ModifyBasePointerSpeedModifier::new));

    public ModifyBasePointerSpeedModifier(float baseSpeed)
    {
        this.baseSpeedRatio = baseSpeed;
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        instance.pointerBaseSpeed = instance.pointerBaseSpeed * baseSpeedRatio;
        instance.pointerSpeed = instance.pointerBaseSpeed;
    }

    @Override
    public MapCodec<? extends AbstractMinigameModifier> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<Supplier<AbstractMinigameModifier>, Supplier<AbstractMinigameModifier>> getRegistryHolder()
    {
        return SCMinigameModifiers.SPAWN_SWEET_SPOTS;
    }

}
