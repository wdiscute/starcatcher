package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SpawnTreasureOnHitX extends AbstractMinigameModifier
{
    public static final MapCodec<SpawnTreasureOnHitX> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("hits_to_spawn_treasure").forGetter(o -> o.hitsToSpawn),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SpawnTreasureOnHitX::new));

    private final int hitsToSpawn;

    public SpawnTreasureOnHitX(int hits, String translationOverride)
    {
        super(translationOverride);
        this.hitsToSpawn = hits;
    }

    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        return List.of(Component.translatable("tooltip.modifier.starcatcher.spawn_treasure_on_hit_x", hitsToSpawn));
    }

    @Override
    public void onMiss(FishingMinigameScreen instance)
    {
        super.onMiss(instance);
        instance.modifierData.put(getIdentifier(), 0);
    }

    @Override
    public boolean onHit(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        int hits = (int) instance.modifierData.computeIfAbsent(getIdentifier(), o -> 0) + 1;
        instance.modifierData.put(getIdentifier(), hits);

        if (hits == 3 && !instance.treasureActive && instance.treasureProgress == 0)
        {
            instance.addSweetSpot(new ActiveSweetSpot(instance, Difficulty.SweetSpot.TREASURE));
            instance.addSweetSpot(new ActiveSweetSpot(instance, Difficulty.SweetSpot.TREASURE));
            removed = true;
        }

        return super.onHit(instance, ass);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("spawn_treasure_on_hit_x");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
