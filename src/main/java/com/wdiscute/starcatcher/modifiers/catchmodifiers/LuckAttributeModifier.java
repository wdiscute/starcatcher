package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;

public class LuckAttributeModifier extends AbstractCatchModifier
{
    final Map<Rarity, Integer> map;

    public static final MapCodec<LuckAttributeModifier> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.unboundedMap(Rarity.CODEC, Codec.INT).fieldOf("rarity_count_increase_times_luck")
                            .forGetter(o -> o.map),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, LuckAttributeModifier::new));

    public LuckAttributeModifier(Map<Rarity, Integer> map, String translationOverride)
    {
        super(translationOverride);
        this.map = map;
    }

    @Override
    public List<FishProperties> modifyAvailablePool(FishingBobEntity fbe, List<FishProperties> available)
    {
        if(!fbe.player.getAttributes().hasAttribute(Attributes.LUCK)) return available;
        AttributeInstance attribute = fbe.player.getAttribute(Attributes.LUCK);
        if(attribute == null) return available;
        double luck = attribute.getValue();
        if(luck == 0d) return available;

        //list to return
        List<FishProperties> list = new ArrayList<>();

        //set to make sure we only run the logic once for each fp
        Set<FishProperties> set = new HashSet<>();

        for (FishProperties fp : available)
        {
            //add the fp being checked to keep the list at least the exact same
            list.add(fp);

            //skip if extra chance logic already ran for fp, skip
            if(set.contains(fp)) continue;
            set.add(fp);

            //add X amount of extra fp based on map and luck level
            int countToAdd = (int) (map.getOrDefault(fp.rarity(), 0) * luck);
            for (int i = 0; i < countToAdd; i++)
                list.add(fp);
        }

        return list;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("luck_attribute");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
