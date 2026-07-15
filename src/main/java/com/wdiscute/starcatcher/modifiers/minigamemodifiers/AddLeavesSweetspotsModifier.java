package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.LeafSweetSpotBehaviour;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class AddLeavesSweetspotsModifier extends AbstractMinigameModifier
{
    public float chancePerTick;

    public static final MapCodec<AddLeavesSweetspotsModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("chance_per_tick", 0.025f).forGetter(o -> o.chancePerTick),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
                    ).apply(instance, AddLeavesSweetspotsModifier::new));

    public AddLeavesSweetspotsModifier(float chancePerTick, String overrideOverride)
    {
        super(overrideOverride);
        this.chancePerTick = chancePerTick;
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        super.tick(instance);

        if(Utils.r.nextFloat() < chancePerTick)
        {
            if(instance.getActiveSweetSpots().stream().anyMatch(o -> o.behaviour instanceof LeafSweetSpotBehaviour)) return;
            ActiveSweetSpot activeSweetSpot = new ActiveSweetSpot(instance, Difficulty.SweetSpot.LEAF);
            Minecraft.getInstance().player.playSound(SoundEvents.BIG_DRIPLEAF_PLACE, 1f, 1f);
            instance.addSweetSpot(activeSweetSpot);
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("add_leaves_spots");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[AddLeavesSweetspotsModifier@" + Integer.toHexString(hashCode()) + "](chance: " + chancePerTick + ")";
    }
}
