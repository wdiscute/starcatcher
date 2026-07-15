package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class NewCatchIncreaseModifier extends AbstractCatchModifier
{
    int increase;

    public static final MapCodec<NewCatchIncreaseModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("increase").forGetter(o -> o.increase),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, NewCatchIncreaseModifier::new));

    public NewCatchIncreaseModifier(int increase, String translationOverride)
    {
        super(translationOverride);
        this.increase = increase;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if (shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.new_catch_increase.shift", increase));

        return List.of(Component.translatable("tooltip.modifier.starcatcher.new_catch_increase"));
    }


    @Override
    public List<FishProperties> modifyAvailablePool(FishingBobEntity fbe, List<FishProperties> available)
    {
        List<FishProperties> list = new ArrayList<>(available);

        //keep a unique set of fps so it doesn't add for each instance of the fp already in the list,
        // otherwise FPs with high base chances would disproportionally get increased odds
        Set<FishProperties> set = new HashSet<>();

        Map<ResourceLocation, FishCaughtCounter> fishesCaught = fbe.player.getData(SCDataAttachments.FISHING_GUIDE).fishesCaught;

        for (FishProperties fp : available)
        {
            if(set.contains(fp)) continue;
            ResourceLocation key = fbe.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fp);

            if (key == null) continue;

            if (!fishesCaught.containsKey(key))
            {
                set.add(fp);
                for (int i = 0; i < increase; i++)
                {
                    list.add(fp);
                }
            }
        }

        return list;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("new_catch_increase");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
