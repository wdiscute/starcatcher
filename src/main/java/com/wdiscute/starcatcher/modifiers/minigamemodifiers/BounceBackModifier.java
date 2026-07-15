package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class BounceBackModifier extends AbstractMinigameModifier
{
    public static final MapCodec<BounceBackModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, BounceBackModifier::new));

    private int bouncedTicks = 0;
    private boolean bounced = false;

    public BounceBackModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public void onAdd(FishingMinigameScreen instance)
    {
        super.onAdd(instance);
        bounced = false;
        bouncedTicks = 0;
    }

    @Override
    public String toString()
    {
        return "[BouncebackModifier@" + Integer.toHexString(hashCode()) + "] (bounced: " + bounced + " / bouncedTicks: " + bouncedTicks + ")";
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        super.tick(instance);
        if(bouncedTicks > 0)
        {
            instance.progress += instance.hp / 50f;
            bouncedTicks--;
        }
    }

    @Override
    public boolean preventLosingMinigame(FishingMinigameScreen instance)
    {
        //if hasn't triggered
        if (!bounced)
        {
            //bounce ticks for smooth animation
            bouncedTicks = 10;

            //set progress to 10 to not lose instantly again
            instance.progress = 10;
            Minecraft.getInstance().player.playSound(SoundEvents.SLIME_HURT);
            removed = true;
            return true;
        }

        //return true to prevent if bounced ticks is not 0
        return bouncedTicks > 0;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("bounce_back");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
