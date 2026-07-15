package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.compat.ReliquifiedArtifactsCompat;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.List;

public class AnglersHatModifier extends AbstractCatchModifier
{
    public static final MapCodec<AnglersHatModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, AnglersHatModifier::new));

    public AnglersHatModifier(String translationOverrider)
    {
        super(translationOverrider);
    }

    @Override
    public boolean forceAwardTreasure(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        if (completedTreasure) return false;
        if (ModList.get().isLoaded("reliquified_artifacts"))
        {
            return ReliquifiedArtifactsCompat.shouldAwardBonusTreasure(fbe.player);
        }
        return false;
    }

    @Override
    public List<ItemStack> addToFishedItems(FishingBobEntity fbe, FishProperties fp, int time, boolean perfectCatch, int hits, boolean completedTreasure)
    {
        if (fbe.fpToFish.catchInfo().alwaysSpawnEntity() ||
            fbe.modifiers.stream().anyMatch(o -> o.forceSpawnEntity(fbe)) ||
                !fbe.fpToFish.hasGuideEntry() || fbe.fpToFish.catchInfo().fishEntryType()
                .equals(CatchInfo.FishEntryType.FISH)) return List.of();

        if (ModList.get().isLoaded("reliquified_artifacts"))
        {
            return ReliquifiedArtifactsCompat.getBonusCatchItems(fbe.player, fbe);
        }
        return List.of();
    }

    @Override
    public void onSuccessfulMinigameCompletion(FishingBobEntity fbe, int time, boolean completedTreasure, boolean perfectCatch, int hits)
    {
        if (ModList.get().isLoaded("reliquified_artifacts"))
        {
            ReliquifiedArtifactsCompat.awardRelicXP(fbe.player, completedTreasure);
        }
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("anglers_hat");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
