package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SkipMinigameModifier extends AbstractCatchModifier
{
    public static final MapCodec<SkipMinigameModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, SkipMinigameModifier::new));

    public SkipMinigameModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public List<ItemStack> addToFishedItems(FishingBobEntity fbe, FishProperties fp, int time, boolean perfectCatch, int hits, boolean completedTreasure)
    {
        if (fbe.level().getRandom().nextFloat() < 0.1f)
            return List.of(fbe.treasure);
        return super.addToFishedItems(fbe, fp, time, perfectCatch, hits, completedTreasure);
    }

    @Override
    public boolean forceSkipMinigame(FishingBobEntity fbe)
    {
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("skip_minigame");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
