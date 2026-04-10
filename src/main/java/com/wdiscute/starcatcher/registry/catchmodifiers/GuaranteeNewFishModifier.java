package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class GuaranteeNewFishModifier extends AbstractCatchModifier
{
    float chance;

    public GuaranteeNewFishModifier(float changeOutOf100)
    {
        chance = changeOutOf100;
    }

    @Override
    public void afterChoosingTheCatch(List<FishProperties> immutableAvailable)
    {
        super.afterChoosingTheCatch(immutableAvailable);

        if (U.r.nextFloat(100) > chance) return;

        Level level = instance.level();

        List<FishProperties> caughtList = FishingGuideAttachment.getFishesCaught(instance.player).keySet().stream().map(loc -> U.getFpFromRl(level, loc)).toList();

        Optional<FishProperties> notCaughtFish = immutableAvailable.stream().filter(properties -> !caughtList.contains(properties) && properties.hasGuideEntry()).findAny();

        notCaughtFish.ifPresent(fish ->
        {
            instance.fpToFish = fish;
            instance.rlToFish = level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fish);
        });
    }
}
