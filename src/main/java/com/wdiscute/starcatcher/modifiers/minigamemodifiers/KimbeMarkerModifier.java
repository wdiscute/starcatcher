package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.Identifier;

public class KimbeMarkerModifier extends AbstractMinigameModifier
{
    public static final MapCodec<KimbeMarkerModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, KimbeMarkerModifier::new));

    public KimbeMarkerModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        super.tick(instance);

        //decrease kimbe markers alpha
        int color = instance.kimbeMarkerColor;

        int alpha = (color >>> 24) & 0xff;


        alpha = Math.max(0, alpha - 20);

        color = (color & 0x00ffffff) | (alpha << 24);

        instance.kimbeMarkerColor = color;
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        instance.kimbeMarkerColor = 0xffff6767;
        instance.kimbeMarkerPos = instance.getHandlePosPrecise();
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        instance.kimbeMarkerColor = 0xff2ce17d;
        instance.kimbeMarkerPos = instance.getHandlePosPrecise();
        return super.onHit(instance, ass);
    }

    @Override
    public Identifier getIdentifier()
    {
        return Starcatcher.rl("kimbe_marker");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    @Override
    public String toString()
    {
        return "[KimbeMarkerModifier@" + Integer.toHexString(hashCode()) + "]";
    }
}
